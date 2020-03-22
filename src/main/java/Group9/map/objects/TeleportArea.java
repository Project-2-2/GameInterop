package Group9.map.objects;

import Group9.agent.container.AgentContainer;
import Group9.map.area.ModifyLocationEffect;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

public class TeleportArea extends MapObject {

    public TeleportArea(PointContainer area) {
        super(area, Arrays.asList(
                new ModifyLocationEffect(area) {
                    @Override
                    public Vector2 get(AgentContainer<?> agentContainer) {
                        return area.getAsPolygon().generateRandomLocation();
                    }
                }
        ), ObjectPerceptType.Teleport);
    }
}
