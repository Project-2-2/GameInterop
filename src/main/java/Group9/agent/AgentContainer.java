package Group9.agent;

import Group9.PiMath;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Vector;

public class AgentContainer<T> {

    private T agent;
    private PointContainer.Circle shape;
    private Vector2 direction;

    public AgentContainer(T agent, Vector position, Vector direction)
    {
        this.agent = agent;
        this.shape = new PointContainer.Circle(Vector2.from(position), 0.5);
        this.direction = Vector2.from(direction);
    }

    public T getAgent()
    {
        return this.agent;
    }

    public PointContainer.Circle getShape()
    {
        return shape;
    }

    public Vector2 getPosition()
    {
        return this.shape.getCenter();
    }

    public Vector2 getDirection()
    {
        return direction;
    }

    public void move(double distance)
    {
        this.shape.translate(this.shape.getCenter().add(this.direction.mul(distance, distance)));
    }

    /**
     * Turns the agent by a certain amount of radians and returns the updated direction.
     * @param radians
     * @return
     */
    public Vector2 rotate(double radians)
    {
        final double theta = PiMath.getDistanceBetweenAngles(this.getDirection().getClockDirection(), radians);
        final double x = direction.getX();
        final double y = direction.getY();

        this.direction = new Vector2(
                x * Math.cos(theta) - y * Math.sin(theta),
                x * Math.sin(theta) + y * Math.cos(theta)
        );
        return this.direction;
    }

}
