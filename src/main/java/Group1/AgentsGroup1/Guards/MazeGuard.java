package Group1.AgentsGroup01.Guards;

import Group1.AgentsGroup01.Geometry.LineCut;
import Group1.AgentsGroup01.Geometry.Precision;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.*;
import java.util.stream.Collectors;

public class MazeGuard implements Interop.Agent.Guard {

    /**
     * AGENTS BRAIN
     */
    // increasing counter of current turn
    private int turnCount;
    // GuardState stores the current strategy state of the guard. It's actions as dependent upon it's state.
    private GuardStrategy guardStrategy;
    // schedules actions in advance
    private List<GuardAction> queueNextActions;
    // turn number, when to discover the map
    private int nextDiscoveryJourney;
    // Defines the hand which "touches" the wall while following it.
    private HAND hand;
    // Speed of agent depending upon area he is in
    private double speedModifier;
    // Last known location of intruder
    private Point lastKnownIntruderLocation;
    private Boolean justTurnedIntoHoleInWal;


    public MazeGuard() {
        turnCount = 1;
        guardStrategy = GuardStrategy.orientate;
        queueNextActions = new ArrayList<>();
        speedModifier = 1.0;
        lastKnownIntruderLocation = null;
        justTurnedIntoHoleInWal = false;
        // start later
        nextDiscoveryJourney = 150;
        if(Math.random() > 0.5) {
            hand = HAND.LEFT;
        }else{
            hand = HAND.RIGHT;
        }
    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        turnCount++;





        // Setting the stage
        if (percepts.getAreaPercepts().isInDoor()){
            speedModifier = percepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers().getInDoor();
        } else if (percepts.getAreaPercepts().isInWindow()){
            speedModifier = percepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers().getInWindow();
        } else if (percepts.getAreaPercepts().isInSentryTower()){
            speedModifier = percepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers().getInSentryTower();
        } else {
            speedModifier = 1.0;
        }

        if (!percepts.wasLastActionExecuted()) {
            queueNextActions.clear();
            return rescueAgent(percepts);
        }

        if (isIntruderDetected(percepts)){
            // forget scheduled actions to chase the intruder
            queueNextActions.clear();
            guardStrategy = GuardStrategy.chase;
        }

        if (queueNextActions.size() > 0){
            return executeScheduledAction();
        }


        // Let agent act
        if (guardStrategy == GuardStrategy.chase) {
            return nextOrientateActionChase(percepts);
        }


        if (guardStrategy == GuardStrategy.orientate) {
            return nextOrientateAction(percepts);
        }

        if (guardStrategy == GuardStrategy.explore) {
            // Agent should discover the map only while being in exploring state
            if (turnCount >= nextDiscoveryJourney) {
                nextDiscoveryScheduler();
                return discoverMap(percepts);
            }
            return nextExploreAction(percepts);
        }

        return new NoAction();
    }



    /**
     * Uniform distribution to schedule agent's random map discoveries.
     */
    private void nextDiscoveryScheduler(){
        nextDiscoveryJourney = turnCount + 80 + (int) (Math.random() * 60);
    }


    /**
     * Uses vision to orientate on the map.
     * Main goal:
     *  - Go straight to find a wall.
     *  - Align agent perpendicular to the wall.
     * @param percepts
     * @return
     */
    private GuardAction nextOrientateAction(GuardPercepts percepts){
        ObjectPercepts visionObjects = percepts.getVision().getObjects();
        ObjectPercepts walls = visionObjects.filter(percept -> percept.getType().equals(ObjectPerceptType.Wall));

        // quickly find a wall
        // need to have at least 2 wall points to interpolate line and another point to verify if it's on the line
        if (walls.getAll().size() < 3) {
            Distance feasibleDistance = null;
            try {
                feasibleDistance = calculateFeasibleDistance(
                        walls,
                        new Distance(percepts.getVision().getFieldOfView().getRange().getValue() * 0.5),
                        percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
            } catch (Exception e) {
                return rescueAgent(percepts);
            }

            return new Move(feasibleDistance);
        }

        // orientate on wall, if possible
        LineCut wallLine;
        try {
            wallLine = findLine(walls);
        } catch (Exception e) {
            return rescueAgent(percepts);
        }

        // calculate angle and turn accordingly
        Angle angleToXAxis = calculateAngleToXAxis(wallLine);
        Angle maxRotationAngle = percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle();

        // unless the agent is already standing perpendicular to the wall
        if (Math.abs(angleToXAxis.getRadians()) < Precision.threshold) {
            guardStrategy = GuardStrategy.explore;
            return new NoAction();
        }

        double angleToRotate = angleToXAxis.getRadians() * (1);
        if (Math.abs(angleToRotate) >= maxRotationAngle.getRadians()){
            double rotate = ((int) Math.signum(angleToRotate) * maxRotationAngle.getRadians());
            return new Rotate(Angle.fromRadians(rotate));
        } else {
            return new Rotate(Angle.fromRadians(angleToRotate));
        }
    }


    /**
     * Given vision percepts, this function returns either a modified distance to the closed object,
     * or the maximum allowed distance, if the closed object is too far away.
     * @param objectPercepts
     * @param distanceFromWall
     * @param maxMove
     * @return
     */
    private Distance calculateFeasibleDistance(ObjectPercepts objectPercepts, Distance distanceFromWall, Distance maxMove) throws Exception {
        OptionalDouble minDistance = objectPercepts.getAll().stream().mapToDouble(p -> p.getPoint().getDistanceFromOrigin().getValue()).min();
        double feasibleDistance = minDistance.orElse(distanceFromWall.getValue() * 2) - distanceFromWall.getValue();

        if (maxMove.getValue() * speedModifier <= feasibleDistance){
            return new Distance(maxMove.getValue() * speedModifier);
        }

        if (feasibleDistance < 0) {
            throw new Exception("feasible distance is below zero.");
        }

        return new Distance(feasibleDistance);
    }


    /**
     * Given certain vision percepts, this functions tries to find a line that goes through these points.
     * @param objects
     * @return
     * @throws Exception
     */
    private LineCut findLine(ObjectPercepts objects) throws Exception {
        Point firstPoint;
        Point lastPoint = null;
        List<Point> points = new ArrayList<Point>();
        for (ObjectPercept percept: objects.getAll()){
            points.add(percept.getPoint());
        }
        points.sort(Comparator.comparing(Point::getX));


        for (int i = 0; i < points.size() - 2; i++) {
            firstPoint = points.get(i);
            LineCut lineCut = new LineCut(firstPoint, points.get(i+1));

            for (Point point: points.subList(i+2,points.size())){
                if (lineCut.isPointOnExtendedLine(point)) {
                    lastPoint = point;
                }
            }
            if (lastPoint != null) {
                return new  LineCut(firstPoint, lastPoint);
            }
        }

        throw new Exception("Implement rescue strategy!");
    }


    /**
     * Calculating the angle between a line and the x-axis.
     * @param lineCut
     * @return
     */
    private Angle calculateAngleToXAxis(LineCut lineCut) {
        double x = lineCut.getEnd().getX() - lineCut.getStart().getX();
        double y = lineCut.getEnd().getY() - lineCut.getStart().getY();
        double theta = Math.atan2(y, x);
        return Angle.fromRadians(theta);
    }


    /**
     * Rescues the agent if stuck.
     * @param percepts
     * @return
     */
    private GuardAction rescueAgent(GuardPercepts percepts) {
        double randomRotate = (int) (180 * Math.random());

        if (randomRotate < 90) {
            scheduleRotate(Angle.fromDegrees(90 + randomRotate), percepts);
        }else{
            scheduleRotate(Angle.fromDegrees(-randomRotate), percepts);
        }
        scheduleMove(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard(), percepts);

        guardStrategy = GuardStrategy.orientate;
        return executeScheduledAction();
    }

    /**
     * Rescues the agent if stuck.
     * @param percepts
     * @return
     */
    private GuardAction discoverMap(GuardPercepts percepts) {
        double randomRotate = 90 + (int) (45 * Math.random());

        scheduleRotate(Angle.fromDegrees(randomRotate * hand.modifier), percepts);
        scheduleMove(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard(), percepts);

        guardStrategy = GuardStrategy.orientate;
        return executeScheduledAction();
    }


    /**
     * Returns wall points which are directly in front of the agent.
     * @param walls
     * @return
     */
    private Point getWallStraightAhead(ObjectPercepts walls){
        List<ObjectPercept> sortedPoints = walls
                .getAll()
                .stream()
                .filter(p -> p.getPoint().getClockDirection().getDistance(Angle.fromDegrees(0)).getRadians() < 0.2)
                .sorted(Comparator.comparing(p -> p.getPoint().getClockDirection().getDistance(Angle.fromDegrees(0)).getRadians()))
                .collect(Collectors.toList());
        try{
            return sortedPoints.get(0).getPoint();
        } catch (Exception e){
            return null;
        }
    }


    /**
     * Using the Pledge Algorithm to explore the map.
     * @param percepts
     * @return
     */
    private GuardAction nextExploreAction(GuardPercepts percepts) {

        ObjectPercepts visionObjects = percepts.getVision().getObjects();
        ObjectPercepts walls = visionObjects.filter(percept -> percept.getType().equals(ObjectPerceptType.Wall));

        if (walls.getAll().size() == 0) {

            // get to viewRange
            if (justTurnedIntoHoleInWal){
                scheduleMove(new Distance(percepts.getVision().getFieldOfView().getRange().getValue() * 0.6 * 1), percepts);
            } else{
                scheduleMove(new Distance(percepts.getVision().getFieldOfView().getRange().getValue() * 1), percepts);
            }

            // turn around
            scheduleRotate(Angle.fromDegrees(90 * hand.modifier * -1), percepts);
            justTurnedIntoHoleInWal = true;
            return executeScheduledAction();
        }
        justTurnedIntoHoleInWal = false;

        Point wallPointStraightAhead = getWallStraightAhead(walls);
        if (wallPointStraightAhead == null) {
            /*ObjectPercept mostLeftWall = getWallMostLeft(walls);
            if (mostLeftWall.getPoint().getClockDirection().getRadians() < 0.3){

            }*/
            return new Move(
                    new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * speedModifier)
            );
        }

        double distanceToWall = wallPointStraightAhead.getDistanceFromOrigin().getValue();
        double minDistance = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();
        if (distanceToWall > minDistance) {
            return new Move(
                    new Distance(
                            Math.min(
                                    (distanceToWall - minDistance) * speedModifier,
                                    percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * speedModifier
                            )
                    )
            );
        }

        scheduleRotate(Angle.fromDegrees(90 * hand.modifier), percepts);
        return executeScheduledAction();
    }

    /**
     * Checks the percepts, if there is any sign of an intruder.
     * @param percepts
     * @return
     */
    private boolean isIntruderDetected(GuardPercepts percepts) {
        // Currently only vision is used to detect an intruder
        ObjectPercepts visionObjects = percepts.getVision().getObjects();
        ObjectPercepts intruders = visionObjects.filter(percept -> percept.getType().equals(ObjectPerceptType.Intruder));
        return intruders.getAll().size() > 0;
    }


    /**
     * Chasing
     * @param percepts
     * @return
     */
    private GuardAction nextOrientateActionChase(GuardPercepts percepts) {
        double slowDownModifier = 1;

        ObjectPercepts visionObjects = percepts.getVision().getObjects();
        List<ObjectPercept> intruders = visionObjects
                .getAll()
                .stream()
                .filter(percept -> percept.getType().equals(ObjectPerceptType.Intruder))
                .sorted(Comparator.comparing(i -> i.getPoint().getDistanceFromOrigin().getValue()))
                .collect(Collectors.toList());

        Point perceivedIntruder;
        try {
            /* approximate intruder by averaging all detected points
            Sometimes these look as follows:
            [ObjectPercept{type=Intruder, point=Point{x= -0.2592326445255806,	y=1.3774978994845741}},
             ObjectPercept{type=Intruder, point=Point{x= -0.1909924674666555,	y=1.3108917320733584}},
             ObjectPercept{type=Intruder, point=Point{x= -0.13169640364551893,	y=1.2698363074336003}},
             ObjectPercept{type=Intruder, point=Point{x= -0.0771069610484844,	y=1.2419639210631743}},
             ObjectPercept{type=Intruder, point=Point{x= -0.0252753209133218,	y=1.222724309312376}},
             ObjectPercept{type=Intruder, point=Point{x=  0.025011868859878866,	y=1.2099794966477901}},
             ObjectPercept{type=Intruder, point=Point{x=  0.07466757231259316,	y=1.2026726202745408}},
             ObjectPercept{type=Intruder, point=Point{x=  0.1244929397891226,	y=1.2003794377625423}},
             ObjectPercept{type=Intruder, point=Point{x=  0.1752973109511529,	y=1.203166798296422}},
             ObjectPercept{type=Intruder, point=Point{x=  0.22801739567808135,	y=1.21162781858483}},
             ObjectPercept{type=Intruder, point=Point{x=  0.28389855499588773,	y=1.2271280566881797}},
             ObjectPercept{type=Intruder, point=Point{x=  0.3448808857982343,	y=1.2525315511170372}},
             ObjectPercept{type=Intruder, point=Point{x=  0.41474822550085977,	y=1.2946490324079174}},
             ObjectPercept{type=Intruder, point=Point{x=  0.505448015005699,	y=1.3788463460195495}}]
             */
            int len = intruders.size();
            OptionalDouble x = intruders.stream().mapToDouble(i -> i.getPoint().getX()).average();
            OptionalDouble y = intruders.stream().mapToDouble(i -> i.getPoint().getY()).average();
            perceivedIntruder = new Point(x.getAsDouble(),y.getAsDouble());
        } catch (Exception e) {
            guardStrategy = GuardStrategy.orientate;
            return new NoAction();
        }


        // get intruder distance measures
        Distance distanceToIntruder = perceivedIntruder.getDistanceFromOrigin();
        Angle angleClockDirectionToIntruder = perceivedIntruder.getClockDirection();
        Angle angleToIntruder = angleClockDirectionToIntruder.getDistance(Angle.fromDegrees(0));

        // get previous intruder distance
        if (lastKnownIntruderLocation != null) {
            Distance prevDistanceToIntruder = lastKnownIntruderLocation.getDistanceFromOrigin();
            Angle prevAngleClockDirectionToIntruder = lastKnownIntruderLocation.getClockDirection();
            Angle prevAngleToIntruder = prevAngleClockDirectionToIntruder.getDistance(Angle.fromDegrees(0));

/*            // algorithm to catch according to red pac-man ghost
            if (distanceToIntruder.getValue() < prevDistanceToIntruder.getValue() &&
                    distanceToIntruder.getValue() > 0.7 * percepts.getVision().getFieldOfView().getRange().getValue()){
                slowDownModifier = 0.5;
            }*/
        }

        // Save intruder location for next run
        lastKnownIntruderLocation = perceivedIntruder;


        if (angleToIntruder.getDegrees() > 0.1 * percepts.getVision().getFieldOfView().getViewAngle().getDegrees()){
            if (angleClockDirectionToIntruder.getDegrees() > 180){
                // left side of the y-axis
                return new Rotate(Angle.fromDegrees(angleToIntruder.getDegrees() * -1));
            } else {
                // right side of the y-axis
                return new Rotate(Angle.fromDegrees(angleToIntruder.getDegrees() * 1));
            }
        }

        if (distanceToIntruder.getValue() < 0.99 * percepts.getScenarioGuardPercepts().getScenarioPercepts().getCaptureDistance().getValue()) {
            // Close enough to intruder. But his real position might differ from current average approximation, since
            // the guard perceives the intruder with many objectPercepts.
            // Hence, rotating the guard to the direction with the most points might solve this issue. This is done by:
            // 1. get the angle distance from 0 for every point (to the left is a negative angle, to the right a positive)
            // 2. Calculate the mean of these points.

            double[] leftPoints = intruders.stream()
                    .filter(p -> p.getPoint().getClockDirection().getDegrees() > 180)
                    .mapToDouble(p -> p.getPoint().getClockDirection().getDistance(Angle.fromDegrees(0)).getDegrees())
                    .map(i -> i * -1)
                    .toArray();

            double[] rightPoints = intruders.stream()
                    .filter(p -> p.getPoint().getClockDirection().getDegrees() < 180)
                    .mapToDouble(p -> p.getPoint().getClockDirection().getDistance(Angle.fromDegrees(0)).getDegrees())
                    .toArray();

            double[] allPoints = new double[leftPoints.length + rightPoints.length];
            int currentPosition = 0;
            for (double item : leftPoints){
                allPoints[currentPosition] = item;
                currentPosition++;
            }
            for (double item : rightPoints){
                allPoints[currentPosition] = item;
                currentPosition++;
            }


            /*
            System.out.println("intruder caught " + distanceToIntruder.getValue() + " - " + angleToIntruder.getDegrees());
            System.out.println("direction:");
            System.out.println(intruders.stream().mapToDouble(i -> i.getPoint().getClockDirection().getDistance(Angle.fromDegrees(0)).getDegrees()).min());
            System.out.println(intruders.stream().mapToDouble(i -> i.getPoint().getClockDirection().getDistance(Angle.fromDegrees(0)).getDegrees()).max());
            System.out.println("distance");
            System.out.println(intruders.stream().mapToDouble(i -> i.getPoint().getDistanceFromOrigin().getValue()).min());
            System.out.println(intruders.stream().mapToDouble(i -> i.getPoint().getDistanceFromOrigin().getValue()).max());
            System.out.println("details");

            System.out.println(Arrays.toString(leftPoints));
            System.out.println(Arrays.toString(rightPoints));
            System.out.println(Arrays.stream(allPoints).average().orElse(0));
            */

            return new Rotate(Angle.fromDegrees(Arrays.stream(allPoints).average().orElse(0)));
        }
        return new Move(new Distance(
                Math.min(
                        // don't get to close to the intruder, otherwise having him inside the viewrange is difficult.
                        distanceToIntruder.getValue() * slowDownModifier - 0.9 * percepts.getScenarioGuardPercepts().getScenarioPercepts().getCaptureDistance().getValue(),
                        //distanceToIntruder.getValue() * slowDownModifier * speedModifier,
                        percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * speedModifier * slowDownModifier
                )
        ));

    }


    /**
     * Scheduling a series of moves by taking care of the max move distance.
     * @param distance
     * @param percepts
     */
    private void scheduleMove(Distance distance, GuardPercepts percepts) {
        double remainingDistance = distance.getValue();
        double maxMove = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * speedModifier;

        while(remainingDistance > 0) {
            queueNextActions.add(new Move(new Distance(Math.min(remainingDistance, maxMove))));
            remainingDistance = remainingDistance - maxMove;
        }
    }


    /**
     * Scheduling a series of rotations by taking care of the max rotation angle.
     * @param angle
     * @param percepts
     */
    private void scheduleRotate(Angle angle, GuardPercepts percepts) {
        double remainingAngle = angle.getDegrees();
        int sign = (int) Math.signum(remainingAngle);
        double maxRotate = percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees();
        double rotateStep = 0;

        while(Math.abs(remainingAngle) > Precision.threshold) {

            if (Math.abs(remainingAngle) < maxRotate){
                rotateStep = remainingAngle;
            } else {
                rotateStep = maxRotate * sign;
            }
            queueNextActions.add(new Rotate(Angle.fromDegrees(rotateStep)));
            remainingAngle = (Math.abs(remainingAngle) - Math.abs(rotateStep)) * sign;

        }
    }


    /**
     * Executes the next scheduled action according to a FIFO queue.
     * @return
     */
    private GuardAction executeScheduledAction(){
        GuardAction nextAction = queueNextActions.get(0);
        queueNextActions.remove(0);
        return nextAction;
    }


    /**
     * GuardState stores the current strategy state of the guard. It's actions as dependent upon it's state.
     */
    private enum GuardStrategy {
        orientate, explore, chase
    }


    /**
     * Defines the hand which "touches" the wall while following it.
     * Hence, this enum defines which way the agent turns.
     */
    private enum HAND {

        LEFT(1),
        RIGHT(-1);

        private final int modifier;

        HAND(final int newModifier){
            modifier = newModifier;
        }
    }



}
