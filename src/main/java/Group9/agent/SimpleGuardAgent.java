package Group9.agent;
import Group9.Game;
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
import java.util.Set;
import java.util.ArrayList;

public class SimpleGuardAgent implements Guard {


    private Point position;//location of guard
    private Angle rotation; //0 rotation -> positive Y, neutral X
    private boolean foundIntruder = false;
    private boolean foundTargetArea = false;
    private boolean IntruderCapture = false;


   public SimpleGuardAgent() {
        position = new Point(0,0);
        rotation = Angle.fromRadians(0);
    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

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