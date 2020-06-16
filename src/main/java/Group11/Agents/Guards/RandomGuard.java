package Group11.Agents.Guards;

import Group11.Agents.Agent;
import Group11.Agents.Square;
import Group11.Agents.SquareMap;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;

import java.util.Random;

public class RandomGuard extends Guard {
    /**
     * Very simple Random Agent
     */
    private boolean once = true;
    private Rotate rotatedAngle;
    private Move movedDistance;

    public RandomGuard() {
        // Initialize mental map
        // SquareMap.initializeMap();
    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        if(once){
            super.setScenarioSettings(percepts);
            once = false;
        }


        //SquareMap.updateMap(percepts, rotatedAngle, movedDistance);


        if(percepts.wasLastActionExecuted()){
            double currentMaxSpeed = super.currentMaxSpeed(percepts.getAreaPercepts());
            movedDistance = new Move(new Distance(currentMaxSpeed));
            //SquareMap.updateLocation(percepts, rotatedAngle, movedDistance);
            // SquareMap.updateMap(percepts, rotatedAngle, movedDistance);
            return movedDistance;
        }else {
            Random random = new Random();
            double maxRotationAngle = super.maxRotationAngle.getRadians();
            rotatedAngle = new Rotate(Angle.fromRadians(maxRotationAngle*random.nextDouble()));
            //SquareMap.updateLocation(percepts, rotatedAngle, movedDistance);
            // SquareMap.updateMap(percepts, rotatedAngle, movedDistance);
            return rotatedAngle;
        }
    }
}
