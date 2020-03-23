package Group5.Agent;

import Group5.GameController.Area;

public class Vertex {
    final private String id;
    final private String name;
    private boolean visited;
    private Area containedObject;


    public Vertex(String id, String name, boolean visited, Area containedObject) {
        this.id = id;
        this.name = name;
        this.visited = visited;
        this.containedObject = containedObject;
    }

    public Vertex(String id, String name, Area containedObject) {
        this.id = id;
        this.name = name;
        this.visited = false;
        this.containedObject = containedObject;
    }

    public Area getContainedObject() {
        return containedObject;
    }

    public void setContainedObject(Area containedObject) {
        this.containedObject = containedObject;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }


    @Override
    public String toString() {
        return name;
    }

}
