package Percept.Vision;

import Geometry.Point;

/**
 * Represents the field of view of an agent.
 * The field of view is relative to the agent.
 * The field of view always faces in the direction that the agent is facing.
 *
 * The field of view is a isosceles triangle, with:
 *  - the apex is centered on the perceiver
 *  - the left corner of the base representing the leftmost corner of the field of view
 *  - the right corner of the base representing the rightmost corner of the field of view
 *
 * The triangle is an approximation from the circle sector that would normally represent the filed of view.
 * The approximation simplifies computation of points of line segments intersecting the field of view.
 *
 * The agent is able to perceive only in this field of view.
 */
public class FieldOfView {

    private final Point perceiverCorner = new Point(0, 0);

    private Point leftCorner;
    private Point rightCorner;

    public FieldOfView(Point leftCorner, Point rightCorner) {
        this.leftCorner = leftCorner;
        this.rightCorner = rightCorner;
    }

    public Point getPerceiverCorner() {
        return perceiverCorner;
    }

    public Point getLeftCorner() {
        return leftCorner;
    }

    public Point getRightCorner() {
        return rightCorner;
    }

}
