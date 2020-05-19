package Group8.Agents.Intruder;

import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Action.Sprint;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class IntruderUtils {


    public static final double EPS = 1e-6;
    public static double THRESHOLD;

    private static boolean init = false;

    /**
     * Creates an ordered list of all actions required to make a rotation of the specified amount
     * @param percepts Description of the environment as observed by the corresponding entity
     * @param rot The required rotation represented as an Angle object
     * @see Angle
     * @return Ordered list of IntrudersActions corresponding to rotation specified by the angle
     */
    public static List<IntruderAction> generateRotationSequence(IntruderPercepts percepts, Angle rot){
        final Angle MAX_ROTATION = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();
        List<IntruderAction> actionList = new ArrayList<>();
        double radiansLeft = rot.getRadians();
        if(radiansLeft > 0) {
            while (radiansLeft != 0) {
                if (radiansLeft > MAX_ROTATION.getRadians()) {
                    actionList.add(new Rotate(MAX_ROTATION));
                    radiansLeft -= MAX_ROTATION.getRadians();
                } else {
                    actionList.add(new Rotate(Angle.fromRadians(radiansLeft - EPS)));
                    radiansLeft = 0;
                }
            }
        }
        else if(radiansLeft < 0){
            while (radiansLeft != 0) {
                if (Math.abs(radiansLeft) > MAX_ROTATION.getRadians()) {
                    actionList.add(new Rotate(Angle.fromRadians(-MAX_ROTATION.getRadians())));
                    radiansLeft += MAX_ROTATION.getRadians();
                } else {
                    actionList.add(new Rotate(Angle.fromRadians(-radiansLeft + EPS)));
                    radiansLeft = 0;
                }
            }
        }
        return actionList;
    }

    public static final boolean predictCollision(IntruderPercepts percepts){
        if(!init){
            init = true;
            THRESHOLD = percepts.getVision().getFieldOfView().getRange().getValue();
        }
        //System.out.println("Check");
        List<ObjectPercept> objectPercepts = (List<ObjectPercept>) setToList(percepts.getVision().getObjects().getAll());
        List<ObjectPercept> colliders = new ArrayList<>();
        for (ObjectPercept obj:
             objectPercepts) {
           if(obj.getType() != ObjectPerceptType.EmptySpace && !obj.getType().isAgent()){
               colliders.add(obj);
           }
        }
        if (!colliders.isEmpty()) {
            for (ObjectPercept o :
                    colliders) {
                Distance d = new Distance(new Point(0, 0), o.getPoint());
                System.out.println(String.format("Distance to object: %f of type: %s",d.getValue(),o.getType()));
                if (d.getValue() <= THRESHOLD*0.5) {
                    return true;
                }
            }
        }
        return false;
    }

    // Utility function stolen from g9 code
    public static double getSpeedModifier(IntruderPercepts intruderPercepts)
    {
        SlowDownModifiers slowDownModifiers = intruderPercepts.getScenarioIntruderPercepts().getScenarioPercepts().getSlowDownModifiers();
        if(intruderPercepts.getAreaPercepts().isInWindow())
        {
            return slowDownModifiers.getInWindow();
        }
        else if(intruderPercepts.getAreaPercepts().isInSentryTower())
        {
            return slowDownModifiers.getInSentryTower();
        }
        else if(intruderPercepts.getAreaPercepts().isInDoor())
        {
            return slowDownModifiers.getInDoor();
        }

        return 1;
    }

    public static Sprint generateMaxSprint(IntruderPercepts percepts){
        return new Sprint(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
    }

    public static Move generateMaxMove(IntruderPercepts percepts){
        return new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
    }

    public static List<?> setToList(Set<?> set){
        List list = new ArrayList<>();
        set.forEach((a) -> list.add(a));
        return list;
    }
}
