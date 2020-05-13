package Group9.map.objects;

import Group9.agent.container.AgentContainer;
import Group9.map.area.ModifyLocationEffect;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;

public class TeleportArea extends MapObject {

    private TeleportArea connected;

    public TeleportArea(PointContainer area, TeleportArea connected) {
        super(area, new ArrayList<>(), ObjectPerceptType.Teleport);
        setConnected(connected);
    }

    public void setConnected(TeleportArea connected) {
        if(connected != null)
        {
            getEffects().clear();
            getEffects().add(
                    new ModifyLocationEffect(this, getArea()) {
                        @Override
                        public Vector2 get(AgentContainer<?> agentContainer) {
                            return TeleportArea.this.connected.getArea().getAsPolygon().generateRandomLocation();
                        }
                    }
            );
        }
        this.connected = connected;
    }

    public TeleportArea getConnected()
    {
        return this.connected;
    }

}
