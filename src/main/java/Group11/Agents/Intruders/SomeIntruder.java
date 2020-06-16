package Group11.Agents.Intruders;

import Group11.Agents.Agent;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Utils.Utils;

import java.util.Random;

public class SomeIntruder extends Intruder{
    private boolean once = true;
    private int turnN = 0;
    private boolean spottedGuard = false;
    private double buffer = 0.001;
    // create an object of an intruder
    //constructor position of the intruder at the beginning point
    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        Random random = new Random();

        if(once){
            super.setScenarioSettings(percepts);
            once = false;
        }
        if (spottedGuard){
            spottedGuard = false;
            return new Move(new Distance(maxSprintSpeed));
        }

        ObjectPercept[] viewArray = percepts.getVision().getObjects().getAll().toArray(new ObjectPercept[0]);
        for (ObjectPercept objectPercept : viewArray) {
            if (objectPercept.getType().isAgent()) {
                spottedGuard = true;
                return new Rotate(Angle.fromRadians(maxRotationAngle.getRadians()));
            }
        }
        double target = percepts.getTargetDirection().getRadians();
        double maxRotationAngle = super.maxRotationAngle.getRadians();

        if(percepts.wasLastActionExecuted()){
            if(turnN>80 && turnN<83){
                if(target<(Math.PI/4)){
                    turnN = 0;
                    return new Rotate(Angle.fromRadians(target + buffer));
                    // Added buffer to prevent a NaN rotation error
                }
                else{
                    turnN++;
                    return new Rotate(Angle.fromRadians(maxRotationAngle));
                }
            }
            double currentMaxSpeed = super.currentMaxSpeed(percepts.getAreaPercepts());
            turnN++;
            return new Move(new Distance(currentMaxSpeed));
        }else {
            turnN++;
            return new Rotate(Angle.fromRadians(maxRotationAngle*random.nextDouble()));
        }
    }
    /*
    public void explore() {
    //run away method if we r caught by a guard
    }


    public void runAway(){

//IF the gurad is 4 blocks away then run opposite direction(180 degress)
// ELSE

        //make 4 variables for possible distances for each rotation

        //for loop for each rotation calculates the distance until the obstacle is reached

        //sort an array with the distances

        //if guard is not coming from max distance then choose it, otherwise choose the next distance
    }*/

}
