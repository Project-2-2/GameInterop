package Group9.map.dynamic;

import Group9.agent.AgentContainer;
import Group9.math.Vector2;
import Interop.Percept.Smell.SmellPerceptType;

public class Pheromone extends DynamicObject<AgentContainer<?>> {

    private SmellPerceptType type;

    public Pheromone(SmellPerceptType type, AgentContainer<?> source, Vector2 center, double radius, int lifetime) {
        super(source, center, radius, lifetime);
        this.type = type;
    }

    public SmellPerceptType getType() {
        return type;
    }
}
