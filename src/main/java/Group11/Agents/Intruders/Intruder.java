package Group11.Agents.Intruders;

import Group11.Agents.Agent;
import Interop.Geometry.Angle;
import Interop.Percept.AreaPercepts;
import Interop.Percept.IntruderPercepts;

public abstract class Intruder extends Agent implements Interop.Agent.Intruder {
    protected double slowDownInDoor;
    protected double slowDownInSentry;
    protected double slowDownInWindow;
    protected Angle maxRotationAngle;
    protected double maxSpeed;
    protected double maxSprintSpeed;

    protected void setScenarioSettings(IntruderPercepts percepts){
        slowDownInDoor = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getSlowDownModifiers().getInDoor();
        slowDownInSentry = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getSlowDownModifiers().getInSentryTower();
        slowDownInWindow = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getSlowDownModifiers().getInWindow();
        maxSpeed = percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue();
        maxSprintSpeed = percepts.getScenarioIntruderPercepts().getMaxSprintDistanceIntruder().getValue();
        maxRotationAngle = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();
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
}
