package Group5.Agent;

import Interop.Percept.Vision.ObjectPercept;

public class Vertex {
    final private String id;
    private boolean visited;
    private ObjectPercept object;


    public Vertex(String id, boolean visited, ObjectPercept object) {
        this.id = id;
        this.visited = visited;
        this.object = object;
    }

    public Vertex(String id, String name) {
        this.id = id;
        this.visited = false;
    }

    public ObjectPercept getObject() {
        return this.object;
    }

    public String getId() {
        return id;
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


}
