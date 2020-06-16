package Group1.AgentsGroup01.Intruders;

import Group9.Game;
import Interop.Action.DropPheromone;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Smell.SmellPerceptType;

public class RandomIntruder implements Intruder {

    public RandomIntruder() {}

    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {

        if(false && Math.random() < 0.25)
        {
            return new DropPheromone(SmellPerceptType.values()[(int) (Math.random() * SmellPerceptType.values().length)]);
        }

        if(!percepts.wasLastActionExecuted())
        {
            return new Rotate(Angle.fromDegrees(percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
        }
        else
        {
            return new Move(new Distance(0.1));
        }
    }

}
