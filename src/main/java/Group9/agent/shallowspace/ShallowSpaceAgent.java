package Group9.agent.shallowspace;
import Group9.Game;
import Group9.agent.deepspace.ActionContainer;
import Group9.math.Vector2;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.VisionPrecepts;

import java.util.*;

public class ShallowSpaceAgent implements Guard {


    private Point position;//location of guard
    private Angle rotation; //0 rotation -> positive Y, neutral X
    private boolean foundIntruder = false;
    private boolean foundTargetArea = false;
    private boolean IntruderCapture = false;

    private Queue<ActionContainer<GuardAction>> followIntruder = new LinkedList<>();


    public ShallowSpaceAgent() {
        position = new Point(0,0);
        rotation = Angle.fromRadians(0);
    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        Vector2 intruderPosition = canSeeIntruder(percepts);

        if(intruderPosition != null || !followIntruder.isEmpty())
        {
            if(intruderPosition != null && !followIntruder.isEmpty())
            {
                followIntruder.clear();
                followIntruder.addAll(
                        moveTowardsPoint(percepts, new Vector2(0, 1 ), new Vector2.Origin(), intruderPosition)
                );
            }

            if(!followIntruder.isEmpty())
            {
                return followIntruder.poll().getAction();
            }
        }
        else
        {
            VisionPrecepts vision = percepts.getVision();
            Set<ObjectPercept> objectPercepts = vision.getObjects().getAll();

            if(foundTargetArea){
                return doTargetAreaAction(percepts);
            } else {
                for(ObjectPercept object : objectPercepts){
                    if (object.getType().equals(ObjectPerceptType.TargetArea)){
                        foundTargetArea = true;
                        break;
                    }
                }
                if(foundIntruder){
                    return doIntruderAction(percepts);
                }else{
                    for(ObjectPercept object : objectPercepts) {
                        if (object.getType().equals(ObjectPerceptType.Intruder)) {
                            foundIntruder = true;
                            break;
                        }
                    }
                }
            }
        }

        if(!percepts.wasLastActionExecuted())
        {
            Angle newRotation = Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble());
            return new Rotate(newRotation);
        }
        else
        {
            Distance movingDistance = new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * 0.5);

            return new Move(movingDistance);
        }
    }

    protected Queue<ActionContainer<GuardAction>> moveTowardsPoint(GuardPercepts percepts, Vector2 direction, Vector2 source,
                                                                   Vector2 target)
    {
        Queue<ActionContainer<GuardAction>> retActionsQueue = new LinkedList<>();

        Vector2 desiredDirection = target.sub(source).normalise();
        double rotationDiff = desiredDirection.angle(direction);

        if(Math.abs(rotationDiff) > 1E-10)
        {
            retActionsQueue.addAll(this.planRotation(percepts, rotationDiff));
        }

        final double maxAllowedMove = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();
        final double distance = target.distance(source);
        final int fullMoves = (int) (distance / maxAllowedMove);
        final double remainder = distance % percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();

        ActionContainer.Input input = ActionContainer.Input.create()
                .i("direction", direction).i("source", source).i("target", target).i("maxAllowedMove", maxAllowedMove)
                .i("distance", distance).i("fullMoves", fullMoves).i("remainder", remainder);
        for(int i = 0; i < fullMoves; i++)
        {
            retActionsQueue.add(
                    ActionContainer.of(this, new Move(new Distance(maxAllowedMove)), input.clone().i("#fullMoves-i", i))
            );
        }
        if(remainder > 0)
        {
            retActionsQueue.add(
                    ActionContainer.of(this, new Move(new Distance(remainder)), input.clone().i("#remainder", remainder))
            );
        }

        return retActionsQueue;
    }

    protected Queue<ActionContainer<GuardAction>> planRotation(GuardPercepts percepts, double alpha)
    {
        Queue<ActionContainer<GuardAction>> retActionsQueue = new LinkedList<>();

        double maxRotation = percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians();
        int fullRotations = (int) (alpha / maxRotation);
        double restRotation = alpha % maxRotation;

        for (int i = 0; i < fullRotations; i++)  {
            retActionsQueue.offer(
                    ActionContainer.of(this, new Rotate(Angle.fromRadians(maxRotation)))
            );
        }

        if (restRotation > 0) {
            retActionsQueue.offer(
                    ActionContainer.of(this, new Rotate(Angle.fromRadians(restRotation)))
            );
        }

        return retActionsQueue;
    }

    public Vector2 canSeeIntruder(GuardPercepts percepts)
    {
        Set<ObjectPercept> intruders = percepts.getVision().getObjects()
                .filter(e -> e.getType() == ObjectPerceptType.Intruder)
                .getAll();

        if(!intruders.isEmpty())
        {
            return Vector2.from(((ObjectPercept) intruders.toArray()[0]).getPoint());
        }

        return null;
    }

    private GuardAction doIntruderAction(GuardPercepts percepts){
        Set<ObjectPercept> guardView = percepts.getVision().getObjects().getAll();
        Angle theta = percepts.getVision().getFieldOfView().getViewAngle();
        Distance range = percepts.getVision().getFieldOfView().getRange();
        Point intruder_Pos;
     /*  for (ObjectPercept object : guardView) {
            if (object.getType().equals(ObjectPerceptType.Intruder)) {
                foundIntruder = true;
                intruder_Pos = object.getPoint();
                Move move = new Move(new Distance(position,intruder_Pos));
                return move;
              }
        }
        if(!foundIntruder){
            return new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
        }*/
        return null;
    }

    private GuardAction doTargetAreaAction(GuardPercepts percepts) {

        Set<ObjectPercept> guardView = percepts.getVision().getObjects().getAll();
        for (ObjectPercept object : guardView) {
            if (object.getType().equals(ObjectPerceptType.TargetArea)) {
                Move move = new Move(new Distance(object.getPoint(), new Point(0, 0)));
                if (move.getDistance().getValue() >= percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()) {
                    move = new Move(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
                }
                double newY = position.getY() + Math.sin(rotation.getRadians())*move.getDistance().getValue();
                double newX = position.getX() + Math.cos(rotation.getRadians())*move.getDistance().getValue();
                position = new Point(newX, newY);
                return move;
            }
        }

        Angle newRotation = Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble());
        rotation = Angle.fromRadians(rotation.getRadians()+newRotation.getRadians());
        if(rotation.getDegrees() > 360){
            rotation = Angle.fromDegrees(rotation.getDegrees() - 360);
        } else if(rotation.getDegrees() < 0){
            rotation = Angle.fromDegrees(rotation.getDegrees() + 360);
        }
        return new Rotate(newRotation);
    }



    private boolean checkGuardWin(GuardPercepts percepts){
        Set<ObjectPercept> guardView = percepts.getVision().getObjects().getAll();
        Distance captureDistance = percepts.getScenarioGuardPercepts().getScenarioPercepts().getCaptureDistance();
                for(ObjectPercept object : guardView){
            if(object.getType().equals(ObjectPerceptType.Intruder)){
                foundIntruder = true;
                Distance dis = new Distance(position,object.getPoint());
                if(dis.equals(captureDistance)){
                    IntruderCapture = true;
                }

            }
        }
        if(foundIntruder && IntruderCapture){
            return true;
        }
        return false;
    }

}