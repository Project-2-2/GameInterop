package Group5.GameController;

import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPerceptType;


public class Pheromone {
    private SmellPerceptType type;
    private int turnsLeft;
    private Point location;
//    private Ellipse shape;
    private double radius;

    public Pheromone(SmellPerceptType type, Point location, int turnsLeft, double radius) {
        this.type = type;
        this.turnsLeft = turnsLeft;
        this.location = location;
        this.radius = radius;

//        this.shape = new Ellipse(this.radius, this.radius);
//        this.shape.setCenterX(location.getX());
//        this.shape.setCenterY(location.getY());
//        switch (type) {
//            case Pheromone1:
//                this.shape.setFill(Color.rgb(20, 120, 200, 0.8));
//                break;
//            case Pheromone2:
//                this.shape.setFill(Color.rgb(80, 70, 100, 0.8));
//                break;
//            case Pheromone3:
//                this.shape.setFill(Color.rgb(10, 150, 130, 0.8));
//                break;
//            case Pheromone4:
//                this.shape.setFill(Color.rgb(120, 50, 180, 0.8));
//                break;
//            case Pheromone5:
//                this.shape.setFill(Color.rgb(250, 50, 150, 0.8));
//                break;
//        }

    }


    public int getTurnsLeft() {
        return turnsLeft;
    }

    public double getRadius(){ return radius; }

    public SmellPerceptType getType() {
        return type;
    }

    public void setTurnsLeft(int turnsLeft) {
        this.turnsLeft = turnsLeft;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
//        this.shape.setCenterX(this.location.getX());
//        this.shape.setCenterY(this.location.getY());
    }

    public void updateShape() {
        this.radius = this.radius - (this.radius/turnsLeft);
//        this.shape.setRadiusX(this.radius);
//        this.shape.setRadiusY(this.radius);
    }
}
