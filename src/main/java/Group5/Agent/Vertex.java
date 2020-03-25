package Group5.Agent;

import Group5.GameController.Area;
import Interop.Percept.Vision.ObjectPercept;

public class Vertex {
    private boolean visited;
    private ObjectPercept object;
    private Area area;

    public Vertex(boolean visited, ObjectPercept object) {
        this.visited = visited;
        this.object = object;
        this.area =null;
    }

    public Vertex(boolean visited, Area area) {
        this.visited = visited;
        this.area = area;
        this.object = null;

    }

    public ObjectPercept getObject() {
        return this.object;
    }

    public Area getArea() {
        return this.area;
    }


    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isAreaVertex() {
        return this.area != null;
    }

}

class ObjectPerceptVertex extends Vertex {
    public ObjectPerceptVertex(boolean visited, ObjectPercept object) {
        super(visited, object);

    }
}

class AreaVertex extends  Vertex {
    public AreaVertex(boolean visited, Area area) {
        super(visited, area);
    }
}
