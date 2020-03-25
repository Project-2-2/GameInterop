package Group9.map.dynamic;

import Group9.agent.container.AgentContainer;
import Group9.math.Vector2;
import Interop.Percept.Smell.SmellPerceptType;

public class Pheromone extends DynamicObject<AgentContainer<?>> {

    private SmellPerceptType type;
    private final double initialRadius;
    private final int initialLifetime;

    public Pheromone(SmellPerceptType type, AgentContainer<?> source, Vector2 center, double radius, int lifetime) {
        super(source, center, radius, lifetime);
        this.initialRadius = radius;
        this.initialLifetime = lifetime;
        this.type = type;
    }

    public double getInitialRadius()
    {
        return this.initialRadius;
    }

    public int getInitialLifetime() {
        return initialLifetime;
    }

    public SmellPerceptType getType() {
        return type;
    }

}
