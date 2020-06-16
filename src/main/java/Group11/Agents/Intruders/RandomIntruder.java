package Group11.Agents.Intruders;

import Group11.Agents.Agent;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.IntruderPercepts;

import java.util.Random;

public class RandomIntruder extends Intruder {
    private boolean once = true;
    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        if(once){
            super.setScenarioSettings(percepts);
            once = false;
        }

        if(percepts.wasLastActionExecuted()){
            double currentMaxSpeed = super.currentMaxSpeed(percepts.getAreaPercepts());
            return new Move(new Distance(currentMaxSpeed));
        }else {
            Random random = new Random();
            double maxRotationAngle = super.maxRotationAngle.getRadians();
            return new Rotate(Angle.fromRadians(maxRotationAngle*random.nextDouble()));
        }
    }
}
