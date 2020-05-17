package Group9.agent.Intruder;

import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Percept.Vision.VisionPrecepts;

public class ViewArea {
    private Distance range;
    private Angle alpha;
    private Direction direction;
    private Coordinate origin;
    private Coordinate left;
    private Coordinate right;
    public ViewArea(Distance range, Angle alpha, Direction direction, double originX, double originY)
    {
        this.range = range;
        this.alpha = alpha;
        this.direction = direction;
        this.origin = new Coordinate(originX, originY);
    }
    public void constructArea()
    {
        double leftX = Math.cos(direction.getRadians() - 0.5 * alpha.getRadians()) * range.getValue();
        double leftY = Math.sin(direction.getRadians() - 0.5 * alpha.getRadians()) * range.getValue();
        left = new Coordinate(leftX, leftY);
        double rightX = Math.cos(direction.getRadians() + 0.5 * alpha.getRadians()) * range.getValue();
        double rightY = Math.sin(direction.getRadians() + 0.5 * alpha.getRadians()) * range.getValue();
        right = new Coordinate(rightX, rightY);

    }
    public boolean completelyContains(Cell c)
    {
        // The four corner points of the cell
        Coordinate[] points = c.getPoints();
        Coordinate Left;
        Coordinate Right;
        Coordinate up;
        Coordinate down;


        return false;
    }


}
