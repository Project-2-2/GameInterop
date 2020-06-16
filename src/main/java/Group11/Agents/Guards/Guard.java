package Group11.Agents.Guards;

import Group11.Agents.Agent;
import Group11.Point;
import Group9.math.Vector2;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Percept.AreaPercepts;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;

import java.util.ArrayList;

/**
 * This is our GuardClass.
 */

public abstract class Guard extends Agent implements Interop.Agent.Guard {
    protected double slowDownInDoor;
    protected double slowDownInSentry;
    protected double slowDownInWindow;
    protected Angle maxRotationAngle;
    protected double maxSpeed;

    public Guard(Point x, Angle angle){
        super(x,angle);
    }
    public Guard(){}

    protected void setScenarioSettings(GuardPercepts percepts){
        slowDownInDoor = percepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers().getInDoor();
        slowDownInSentry = percepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers().getInSentryTower();
        slowDownInWindow = percepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers().getInWindow();
        maxRotationAngle = percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle();
        maxSpeed = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();
    }
    protected double currentMaxSpeed(AreaPercepts areaPercepts){
        if(areaPercepts.isInDoor()){
            return maxSpeed*slowDownInDoor;
        }
        if(areaPercepts.isInSentryTower()){
            return maxSpeed*slowDownInSentry;
        }
        if(areaPercepts.isInWindow()){
            return maxSpeed*slowDownInWindow;
        }
        return maxSpeed;
    }
    protected ArrayList<GuardAction> moveTo(AreaPercepts areaPercepts, Point point){
        ArrayList<GuardAction> actions = new ArrayList<>();
        //First generate Turn Actions
        // Angle goalAngle = Direction.fromClockAngle(point);
        // double radians = goalAngle.getRadians();
        double radians = new Vector2(0,1).angledSigned(Vector2.from(point.toPoint()));

        double maxRadians = maxRotationAngle.getRadians();
        if(Math.abs(radians) <= maxRadians){
            if(Math.abs(radians) > 0.1){
                actions.add(new Rotate(Angle.fromRadians(radians)));
            }
        }else{
            int movesRadians = (int) Math.floor(Math.abs(radians)/maxRadians);
            double restRadians = (radians%maxRadians);
            for(int i=0; i<movesRadians;i++){
                actions.add(new Rotate(Angle.fromRadians(maxRadians)));
            }
            actions.add(new Rotate(Angle.fromRadians(restRadians)));
        }
        // Then Move Actions
        double distance = point.getDistanceFromOrigin().getValue()-0.51;
        double currentMaxSpeed = currentMaxSpeed(areaPercepts);
        if(distance<=currentMaxSpeed){
            actions.add(new Move(new Distance(currentMaxSpeed)));
        }else{
            int moves = (int) Math.floor(distance/currentMaxSpeed);
            double rest = (distance%currentMaxSpeed)/currentMaxSpeed;
            for(int i=0; i<moves;i++){
                actions.add(new Move(new Distance(currentMaxSpeed)));
            }
            actions.add(new Move(new Distance(currentMaxSpeed*rest)));
        }
        return actions;
    }
}
