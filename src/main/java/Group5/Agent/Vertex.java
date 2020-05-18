package Group5.Agent;

import Group5.GameController.Area;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;
import java.util.Arrays;

public class Vertex {
    private ObjectPercept object;
    private Area area;

    private ArrayList<ObjectPerceptVertex> insideVertices;


    public Vertex(ObjectPercept object) {
        this.object = object;
        this.area =null;
    }

    public Vertex(Area area) {
        this.area = area;
        this.object = null;

    }

    public ObjectPercept getObject() {
        return this.object;
    }

    public Area getArea() {
        return this.area;
    }


    public boolean isAreaVertex() {
        return this.area != null;
    }

    /**
     * Return the distance between two verticies
     * @param v
     * @return
     */
    public double getDistance(Vertex v) {

        return Math.sqrt(Math.pow(this.getObject().getPoint().getX() - v.getObject().getPoint().getX(), 2) +
                Math.pow(this.getObject().getPoint().getY() - v.getObject().getPoint().getY(), 2));
    }

    public void generateInsideVertex() {
        final double DISTANCE_BETWEEN_INSIDE_VERTEX = 5;
        Area area = this.getArea();
        ObjectPerceptType type = area.getObjectsPerceptType();
        ArrayList<Point> areaCorners = area.getPositions();

        ArrayList<Point> set1 = new ArrayList<>(Arrays.asList(areaCorners.get(0), areaCorners.get(3)));
        ArrayList<Point> set2 = new ArrayList<>(Arrays.asList(areaCorners.get(1), areaCorners.get(2)));

        int var;
        for (Point p : set1) {
            int index = 0;
            for (Point q : set2) {
                if (index == 0) {
                    if (p.getX() > q.getX())
                        var = -1;
                    else
                        var = 1;
                    for (double x = p.getX(); x < q.getX(); x += var * DISTANCE_BETWEEN_INSIDE_VERTEX) {
                        this.insideVertices.add(new ObjectPerceptVertex(new ObjectPercept(type, new Point(x, p.getY()))));

                    }
                } else {
                    if (p.getY() > q.getY())
                        var = -1;
                    else
                        var = 1;
                    for (double y = p.getY(); y < q.getY(); y += var * DISTANCE_BETWEEN_INSIDE_VERTEX) {
                        this.insideVertices.add(new ObjectPerceptVertex(new ObjectPercept(type, new Point(p.getX(), y))));
                    }
                }
                index++;
            }
        }
    }

    public ArrayList<ObjectPerceptVertex> getInsideVertices() {
        return this.insideVertices;
    }

    public void setInsideVertex(ArrayList<ObjectPerceptVertex> newInsideVertices) {
        this.insideVertices = newInsideVertices;
    }
}

class ObjectPerceptVertex extends Vertex {
    public ObjectPerceptVertex(ObjectPercept object) {
        super(object);

    }
}

class AreaVertex extends Vertex {

    public AreaVertex(Area area) {
        super(area);
        this.generateInsideVertex();
    }
}

