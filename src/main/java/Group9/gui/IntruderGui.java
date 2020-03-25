package Group9.gui;

import Group9.math.Vector2;

import java.util.Set;

public class IntruderGui extends AgentGui {
    public IntruderGui(double x, double y, double r, Vector2 direction, double range, Set<Vector2[]> visionRays, double viewAngle)
    {
        super(x, y, r, direction, range, false, visionRays, viewAngle);
    }
}
