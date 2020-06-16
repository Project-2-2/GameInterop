package Group10.Competition;

import Group10.Competition.Bias;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;
import Group10.Competition.Line;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class ToTargetAgent implements Intruder {

    private State state;
    private ArrayList<IntruderAction> actionsQueue;

    private ORIENTATION ORIENTATION;

    public ToTargetAgent() {

        state = State.totarget;
        actionsQueue = new ArrayList<>();
        if(Math.random() > 0.5) {
            ORIENTATION = ORIENTATION.LEFT;
        }else{
            ORIENTATION = ORIENTATION.RIGHT;
        }
    }

    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {

        if (actionsQueue.size() > 0){
            return queueActions();
        }

        if (!percepts.wasLastActionExecuted()) {
            return ifStuck(percepts);
        }

        Angle orientation = percepts.getTargetDirection().getDistance(Angle.fromDegrees(new Point(0,1).getClockDirection().getDegrees()));


        if (orientation.getDegrees() < 90) {
            if (ORIENTATION ==  ORIENTATION.LEFT){
                ORIENTATION = ORIENTATION.RIGHT;
            }
        }
        else if (orientation.getDegrees() > 270 ){
            if (ORIENTATION == ORIENTATION.RIGHT)
            ORIENTATION = ORIENTATION.LEFT;
        }

        if (state == State.totarget) {
            return updateToTargetState(percepts);
        }

        if (state == State.navigate) {
            return updateNavigationState(percepts);
        }

        if (state == State.explore) {
            return updateExplorationState(percepts);
        }
        return new NoAction();
    }

    //Make an Agent Bounce of the Wall
    private IntruderAction updateNavigationState(IntruderPercepts percepts){
        ObjectPercepts visionObjects = percepts.getVision().getObjects();
        ObjectPercepts walls = visionObjects.filter(percept -> percept.getType().equals(ObjectPerceptType.Wall));

        if (walls.getAll().size() <= 2) {
            return new Move(calculateWallDistance(walls,
                    new Distance(percepts.getVision().getFieldOfView().getRange().getValue() * 0.5),
                    percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder()));
        }

        Line wall;
        try {
            wall = findLine(walls);
        } catch (Exception e) {
            return ifStuck(percepts);
        }
        state = State.totarget;



        Angle angle = calculateAngleToLine(wall);
        Angle rotation = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();


        if (Math.abs(angle.getRadians()) < Bias.bias) {
            state = State.explore;
            return new NoAction();
        }

        double angleToRotate = angle.getRadians() * (1);
        if (Math.abs(angleToRotate) >= rotation.getRadians()){
            double rotate = ((int) Math.signum(angleToRotate) * rotation.getRadians());
            return new Rotate(Angle.fromRadians(rotate));
        }
        else {
            return new NoAction();
        }

    }


    private Distance calculateWallDistance(ObjectPercepts objectPercepts, Distance distanceFromWall, Distance maxMove){
        OptionalDouble minDistance = objectPercepts.getAll().stream().mapToDouble(p -> p.getPoint().getDistanceFromOrigin().getValue()).min();
        double feasibleDistance = minDistance.orElse(distanceFromWall.getValue() * 2) - distanceFromWall.getValue();

        if (maxMove.getValue() <= feasibleDistance){
            return maxMove;
        }
        return new Distance(feasibleDistance);
    }

    private Line findLine(ObjectPercepts objects) throws Exception {
        Point firstPoint;
        Point lastPoint = null;
        List<Point> points = new ArrayList<Point>();
        for (ObjectPercept percept: objects.getAll()){
            points.add(percept.getPoint());
        }
        points.sort(Comparator.comparing(Point::getX));


        for (int i = 0; i < points.size() - 2; i++) {
            firstPoint = points.get(i);
            Line lineCut = new Line(firstPoint, points.get(i+1));

            for (Point point: points.subList(i+2,points.size())){
                if (lineCut.isPointOnExtendedLine(point)) {
                    lastPoint = point;
                }
            }
            if (lastPoint != null) {
                return new  Line(firstPoint, lastPoint);
            }
        }

        throw new Exception("error");
    }



    private Angle calculateAngleToLine(Line l) {
        double x = l.getLe().getX() - l.getLs().getX();
        double y = l.getLe().getY() - l.getLs().getY();
        double theta = Math.atan2(y, x);
        return Angle.fromRadians(theta);
    }



    private IntruderAction ifStuck(IntruderPercepts percepts) {

        double randomRotate = (int) (180 * Math.random());

        if (randomRotate < 90) {
            queueRotation(Angle.fromDegrees(90 + randomRotate), percepts);
        }
        else {
            queueRotation(Angle.fromDegrees(-randomRotate), percepts);
        }

        queueMovement(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder(), percepts);

        state = State.navigate;
        return queueActions();
    }


    private Point wallsInView(ObjectPercepts walls){
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

    private IntruderAction updateExplorationState(IntruderPercepts percepts) {

        ObjectPercepts visionObjects = percepts.getVision().getObjects();
        ObjectPercepts walls = visionObjects.filter(percept -> percept.getType().equals(ObjectPerceptType.Wall));

        if (walls.getAll().size() == 0) {

            queueMovement(new Distance(percepts.getVision().getFieldOfView().getRange().getValue() * 1), percepts);
            queueRotation(Angle.fromDegrees(90 * ORIENTATION.update * -1), percepts);

            return queueActions();
        }

        Point wallPointStraightAhead = wallsInView(walls);
        if (wallPointStraightAhead == null) {
            return new Move(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder());
        }

        double distanceToWall = wallPointStraightAhead.getDistanceFromOrigin().getValue();
        double minDistance = percepts.getVision().getFieldOfView().getRange().getValue() * 0.5;
        if (distanceToWall > minDistance) {
            return new Move(new Distance(Math.min(distanceToWall - minDistance,
                    percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue())));
        }
        Angle orientation = wallPointStraightAhead.getClockDirection().getDistance(percepts.getTargetDirection());

        if (orientation.getDegrees() < 90) {
            queueRotation(Angle.fromDegrees(90 * (-1)), percepts);
        }
        else if (orientation.getDegrees() > 270 ){
            queueRotation(Angle.fromDegrees(90 * (1)), percepts);
        }
        else queueRotation(Angle.fromDegrees(90 * ORIENTATION.update), percepts);

        return queueActions();
    }

    private IntruderAction updateToTargetState(IntruderPercepts percepts) {
        Direction direction = percepts.getTargetDirection();
        double movement = percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue();

        if (direction.getDegrees() > 1) {
            double maxRotation = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians();

            if (direction.getRadians() > maxRotation)
                return new Rotate(Angle.fromRadians(maxRotation));

            return new Rotate(Angle.fromRadians(direction.getRadians()));
        } else {
            return new Move(new Distance(movement));
        }
    }

    private IntruderAction updateToTeleportState(IntruderPercepts percepts) {
        // todo if the agent cannot get to target it should go to the teleport
        return null;
    }

    private void queueMovement(Distance distance, IntruderPercepts percepts) {
        double distanceValue = distance.getValue();
        double maxMove = percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue();

        while(distanceValue > 0) {
            actionsQueue.add(new Move(new Distance(Math.min(distanceValue, maxMove))));
            distanceValue = distanceValue - maxMove;
        }
    }


    private void queueRotation(Angle angle, IntruderPercepts percepts) {
        double remainingAngle = angle.getDegrees();
        int sign = (int) Math.signum(remainingAngle);
        double maxRotate = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees();
        double rotateStep;

        while(Math.abs(remainingAngle) > Bias.bias) {

            if (Math.abs(remainingAngle) < maxRotate){
                rotateStep = remainingAngle;
            } else {
                rotateStep = maxRotate * sign;
            }
            actionsQueue.add(new Rotate(Angle.fromDegrees(rotateStep)));
            remainingAngle = (Math.abs(remainingAngle) - Math.abs(rotateStep)) * sign;

        }
    }

    private IntruderAction queueActions(){
        IntruderAction nextAction = actionsQueue.get(0);
        actionsQueue.remove(0);
        return nextAction;
    }

    private enum State {
        navigate, explore, totarget, toteleport
    }

    private enum ORIENTATION {

        LEFT(1), RIGHT(-1);

        private final int update;

        ORIENTATION(final int newU){
            update = newU;
        }
    }

}
