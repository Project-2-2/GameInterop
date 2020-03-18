package Group9.map.objects;

import Group9.agent.container.AgentContainer;
import Group9.gui.targetAreaGui;
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
    public targetAreaGui getGui()
    {
        Vector2[] points = getArea().getAsPolygon().getPoints();
        return new targetAreaGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(), points[2].getY(), points[3].getX(), points[3].getY());
    }

}
