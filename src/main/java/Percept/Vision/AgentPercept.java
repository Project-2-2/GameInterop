package Percept.Vision;

import Geometry.Point;

/**
 * Representation of the perception of an agent.
 *
 * Agents are circles, therefore they are not perceived as other objects.
 * The perceiver is aware of the perceived agent as soon as the other agent enters the perceiver field of view.
 * In other words, if any point of an agent circle is in the field of view then the agent is perceived.
 */
public class AgentPercept {

    private AgentPerceptType type;
    private Point location;
    private double radius;

    /**
     * @param type The type of the agent percept.
     * @param location The location of the perceived agent relative to the perceiving agent.
     * @param radius The radius of the agent. Should be the same for all agents.
     */
    public AgentPercept(AgentPerceptType type, Point location, double radius) {
        this.type = type;
        this.location = location;
        this.radius = radius;
    }

    public AgentPerceptType getType() {
        return type;
    }

    /**
     * @return The location of the perceived agent relative to the perceiving agent.
     */
    public Point getLocation() {
        return location;
    }

    /**
     * @return The radius of the agent.
     */
    public double getRadius() {
        return radius;
    }

}
