package Group3.DiscreteMap;

import java.util.ArrayList;

import Interop.Geometry.Point;

public class Vertice {
	
	ObjectType type;
	ArrayList<DirectedEdge> edges;
	Point center;
	double radius;
	Integer coordinate[];
	boolean marked = false;
	Vertice parent = null;

	public Vertice(ObjectType type, Point center, double radius, Integer[] coordinate) {
		this.center = center;
		this.radius = radius;
		this.type = type;
		this.edges = new ArrayList<DirectedEdge>();
		this.coordinate = coordinate;
	}
	
	public ObjectType getType() {
		return type;
	}
	public void setType(ObjectType type) {
		this.type = type;
	}
	
	public ArrayList<DirectedEdge> getEdges() {
		return edges;
	}

	public void addEdge(Vertice endVertice, int degrees) {
		DirectedEdge edge = new DirectedEdge(this, endVertice, degrees);
		this.edges.add(edge);
		if(degrees >= 180) {
			degrees = degrees - 180;
		}
		else {
			degrees = degrees + 180;
		}
		DirectedEdge reverseedge = new DirectedEdge(endVertice, this, degrees);
		endVertice.edges.add(reverseedge);
	}
	
	public boolean isInside(Point point) {
		double xmin = center.getX() - radius;
		double xmax = center.getX() + radius;
		double ymin = center.getY() - radius;
		double ymax = center.getY() + radius;
		double x = point.getX();
		double y = point.getY();
		if((xmin <= x) && (xmax >= x)) {
			if((ymin <= y) && (ymax >= y)) {
				return true;
			}
			else return false;
		}
		else return false;
	}

	public Point getCenter() {
		return center;
	}

	public Integer[] getCoordinate() {
		return coordinate;
	}

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	public Vertice getParent() {
		return parent;
	}

	public void setParent(Vertice parent) {
		this.parent = parent;
	}
	
	public String toString() {
		return "[" + this.coordinate[0] + "|" + this.coordinate[2] + "]";
	}
}
