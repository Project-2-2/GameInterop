package Group9.agent.deepspace;

import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.*;
import java.util.stream.Collectors;

public class DataContainer extends PointContainer.Circle {

    private DeepSpace explorerAgent;

    private List<GuardPercepts> percepts = new ArrayList<>();
    private Map<ObjectPerceptType, HashSet<Vector2>> objects = new HashMap<>();

    private boolean deadEnd;

    public DataContainer(DeepSpace explorerAgent, Vector2 position, double radius) {
        super(position.clone(), radius);
        this.explorerAgent = explorerAgent;
    }

    public Map<ObjectPerceptType, HashSet<Vector2>> getObjects() {
        return objects;
    }

    public boolean isDeadEnd() {
        return deadEnd;
    }

    public void setDeadEnd(boolean deadEnd) {
        this.deadEnd = deadEnd;
    }

    public void add(GuardPercepts percepts)
    {
        percepts.getVision().getObjects().getAll().forEach(e -> {
            Vector2 coordinate = Vector2.from(e.getPoint())
                    .rotated(-explorerAgent.getDirection().getClockDirection())
                    .add(explorerAgent.getPosition());
            if(this.objects.containsKey(e.getType()))
            {
                this.objects.get(e.getType()).add(coordinate);
            }
            else
            {
                this.objects.put(e.getType(), new HashSet<>() {{
                    this.add(coordinate);
                }});
            }
        });
    }

    public List<GuardPercepts> getPercepts() {
        return percepts;
    }
}
