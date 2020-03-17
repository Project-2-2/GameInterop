package Group6.WorldState;

import Group6.Geometry.Point;
import Interop.Percept.Smell.SmellPerceptType;

public class Pheromone {

    public enum Team {
        Inviders, Guards
    }

    private Team team;
    private Point location;
    private int droppedAtTurn;
    private SmellPerceptType type;

    public Pheromone(Team team, Point location, int droppedAtTurn, SmellPerceptType type) {
        this.team = team;
        this.location = location;
        this.droppedAtTurn = droppedAtTurn;
        this.type = type;
    }

    public Team getTeam() {
        return team;
    }

    public Point getLocation() {
        return location;
    }

    public int getDroppedAtTurn() {
        return droppedAtTurn;
    }

    public SmellPerceptType getType() {
        return type;
    }

}
