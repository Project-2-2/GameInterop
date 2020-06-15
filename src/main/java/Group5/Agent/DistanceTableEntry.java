package Group5.Agent;

import Interop.Geometry.Distance;

public class DistanceTableEntry{

    private Vertex vertex;
    private Distance distance;
    private DistanceTableEntry previous;

    public DistanceTableEntry(Vertex vertex){
        this.vertex = vertex;
        this.distance = new Distance(Double.MAX_VALUE);
    }

    public Vertex getVertex(){
        return this.vertex;
    }

    public Distance getDistance(){
        return this.distance;
    }

    public DistanceTableEntry getPrevious(){
        return this.previous;
    }

    public void setVertex(Vertex v){
        this.vertex = v;
    }

    public void setDistance(Distance d){ this.distance = d; }

    public void setPrevious(DistanceTableEntry d){ this.previous = d; }
}
