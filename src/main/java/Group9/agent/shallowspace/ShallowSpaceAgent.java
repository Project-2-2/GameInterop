package Group9.agent.shallowspace;
import Group9.Game;
import Group9.agent.deepspace.ActionContainer;
import Group9.math.Vector2;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.VisionPrecepts;

import java.util.*;

public class ShallowSpaceAgent implements Guard {

    private Angle rotation = Angle.fromRadians(0); //0 rotation -> positive Y, neutral X
    private boolean foundTargetArea = false;

    private Queue<ActionContainer<GuardAction>> followIntruder = new LinkedList<>();
    private Queue<ActionContainer<GuardAction>> targetAreaGuarding = new LinkedList<>();

    public ShallowSpaceAgent() { }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        Vector2 intruderPosition = canSeeIntruder(percepts);

        if(intruderPosition != null || !followIntruder.isEmpty())
        {
            if(intruderPosition != null)
            {
                targetAreaGuarding.clear();
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

            if(!foundTargetArea)
            {
                for(ObjectPercept object : objectPercepts){
                    if (object.getType().equals(ObjectPerceptType.TargetArea)){
                        foundTargetArea = true;
                        break;
                    }
                }
            }
            if(foundTargetArea){
                if(targetAreaGuarding.isEmpty())
                {
                    targetAreaGuarding.addAll(doTargetAreaAction(percepts));
                }

                return targetAreaGuarding.poll().getAction();
            }
        }

        if(!percepts.wasLastActionExecuted())
        {
            Angle newRotation = Angle.fromRadians(
                    (Game._RANDOM.nextBoolean() ? 1 : -1) * percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()
            );
            return new Rotate(newRotation);
        }
        else
        {
            Distance movingDistance = new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts));

            return new Move(movingDistance);
        }
    }

    private double getSpeedModifier(GuardPercepts guardPercepts)
    {
        SlowDownModifiers slowDownModifiers =  guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers();
        if(guardPercepts.getAreaPercepts().isInWindow())
        {
            return slowDownModifiers.getInWindow();
        }
        else if(guardPercepts.getAreaPercepts().isInSentryTower())
        {
            return slowDownModifiers.getInSentryTower();
        }
        else if(guardPercepts.getAreaPercepts().isInDoor())
        {
            return slowDownModifiers.getInDoor();
        }

        return 1;
    }

    protected Queue<ActionContainer<GuardAction>> moveTowardsPoint(GuardPercepts percepts, Vector2 direction, Vector2 source,
                                                                   Vector2 target)
    {
        Queue<ActionContainer<GuardAction>> retActionsQueue = new LinkedList<>();

        Vector2 desiredDirection = target.sub(source);
        double rotationDiff = direction.angledSigned(desiredDirection);
        if(Math.abs(rotationDiff) > 1E-1)
        {
            retActionsQueue.add(ActionContainer.of(this, new Rotate(Angle.fromRadians(rotationDiff))));
        }

        final double maxAllowedMove = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts);
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

    public Vector2 canSeeIntruder(GuardPercepts percepts)
    {
        Set<ObjectPercept> intruders = percepts.getVision().getObjects()
                .filter(e -> e.getType() == ObjectPerceptType.Intruder)
                .getAll();

        if(!intruders.isEmpty())
        {
            Vector2 centre = new Vector2.Origin();
            for(ObjectPercept e : intruders)
            {
                centre = centre.add(Vector2.from(e.getPoint()));
            }
            return centre.mul(1D/intruders.size());
        }

        return null;
    }

    private Queue<ActionContainer<GuardAction>> doTargetAreaAction(GuardPercepts percepts) {

        Set<ObjectPercept> guardView = percepts.getVision()
                .getObjects()
                .filter(e -> e.getType() == ObjectPerceptType.TargetArea)
                .getAll();

        Vector2 max = null;
        final Vector2 viewingDirection = new Vector2(0, 1);
        for (ObjectPercept object : guardView) {
            Vector2 tmp = Vector2.from(object.getPoint());
            if(max == null || max.length() < tmp.length() && viewingDirection.angle(max) > viewingDirection.angle(tmp))
            {
                max = tmp;
            }
            /*if (Vector2.from(object.getPoint()).length() >= 0.1) {
                Move move = new Move(new Distance(object.getPoint(), new Point(0, 0)));
                if (move.getDistance().getValue() >= percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()) {
                    move = new Move(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
                }
                double newY = position.getY() + Math.sin(rotation.getRadians())*move.getDistance().getValue();
                double newX = position.getX() + Math.cos(rotation.getRadians())*move.getDistance().getValue();
                position = new Point(newX, newY);
                return move;
            }*/
        }
        if(max != null && max.length() >= 0.1)
        {
            return moveTowardsPoint(percepts, new Vector2(0 ,1), new Vector2.Origin(), max);
        }

        Angle newRotation = Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble());
        rotation = Angle.fromRadians(rotation.getRadians()+newRotation.getRadians());
        if(rotation.getDegrees() > 360){
            rotation = Angle.fromDegrees(rotation.getDegrees() - 360);
        } else if(rotation.getDegrees() < 0){
            rotation = Angle.fromDegrees(rotation.getDegrees() + 360);
        }
        Queue<ActionContainer<GuardAction>> actions = new LinkedList<>();
        actions.add(ActionContainer.of(this, new Rotate(newRotation)));
        return actions;
    }

    /*private boolean checkGuardWin(GuardPercepts percepts){
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
    }*/

}