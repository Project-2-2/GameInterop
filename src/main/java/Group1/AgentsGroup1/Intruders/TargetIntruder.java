package Group1.AgentsGroup01.Intruders;

import Group9.map.objects.Spawn;
import Group9.map.objects.TargetArea;
import Group9.map.objects.Wall;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Direction;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import com.sun.jdi.event.MonitorContendedEnteredEvent;

import java.util.Set;

public class TargetIntruder implements Intruder {

    private boolean firstRound;
    private Direction intruderDirection;
    private boolean sprintingMode = false;


    public TargetIntruder() {
        System.out.println("TargetIntruder created!");
        firstRound = true;
    }

    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        if (firstRound) {
            Direction targetDirection = percepts.getTargetDirection();
            firstRound = false;
            intruderDirection = targetDirection;
            return new Rotate(targetDirection);
        } else {
            Set<ObjectPercept> vision = percepts.getVision().getObjects().getAll();
            if (vision.contains(ObjectPerceptType.TargetArea)) {
                if (intruderDirection == percepts.getTargetDirection()) {

                    return moveForward(percepts);
                } else {
                    intruderDirection = percepts.getTargetDirection();
                }
            }
            if (vision.contains(ObjectPerceptType.Wall)){

            }

        } return moveForward(percepts);
    }


    public IntruderAction moveForward(IntruderPercepts percepts){
        if (sprintingMode) return new Move(percepts.getScenarioIntruderPercepts().getMaxSprintDistanceIntruder());
        else return new Move(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder());
    }
}