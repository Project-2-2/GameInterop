package Group6.WorldState;

import Group6.Geometry.Distance;
import Group6.Geometry.Quadrilateral;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.awt.geom.AffineTransform;
import Group6.WorldState.Object.AgentState;

/**
 * @author Florène Feyzi
 */

public class Collision {
    private AgentState agent;
    private Distance dist;
    private Scenario scenario;


    public Collision(AgentState agent, Distance dist, Scenario scenario){
        this.agent=agent;
        this.dist=dist;
        this.scenario=scenario;
    }
/*
//Old intersect version
    public boolean intersects(Circle circle, Quadrilateral wall){
        double circleDistanceX= Math.abs(circle.getCenterX()-wall.getPointA().getX());
        double circleDistanceY=Math.abs(circle.getCenterY()-wall.getPointA().getY());

        if(circleDistanceX>(wall.getWidth()/2+circle.getRadius()) || circleDistanceY>(wall.getHeight()/2+circle.getRadius())) {
            return false;
        }
        else {
            return true;
        }
    }
    */
    private boolean intersects(Circle c1, Quadrilateral r1) {
        float closestX = clamp((float)c1.getCenterX(), (int)r1.getPointA().getX(), (int)r1.getPointA().getX() + (int)r1.getWidth());
        float closestY = clamp((float)c1.getCenterY(), (int)r1.getPointA().getY() - (int)r1.getHeight(), (int)r1.getPointA().getY());

        float distanceX = (float)c1.getCenterX() - closestX;
        float distanceY = (float)c1.getCenterY() - closestY;

        return Math.pow(distanceX, 2) + Math.pow(distanceY, 2) < Math.pow(c1.getRadius(), 2);
    }

    public static float clamp(float value, float min, float max) {
        float x = value;
        if (x < min) {
            x = min;
        } else if (x > max) {
            x = max;
        }
        return x;
    }

    public boolean checkCollision() {

        /*
        THIS CHECKS WHETHER THE RECTANGLE THAT AGENT MOVED THROUGH HAS INTERSECTIONS
        */

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(agent.getDirection().getRadians());
        int xCoord = (int) (agent.getLocation().getX() + 0.5 * Math.sin(agent.getDirection().getRadians()));
        int yCoord = (int) (agent.getLocation().getY() + 0.5 * Math.cos(agent.getDirection().getRadians()));
        int distance = (int) (dist.getValue());
        Rectangle rectangle = new Rectangle(xCoord, yCoord, 1, distance);
        System.out.println("Rectangle coordinates "+rectangle.getWidth()+" "+rectangle.getHeight());
        Shape newRect = affineTransform.createTransformedShape(rectangle);
        int nbOfIntersection = 0;
        boolean checkCollision = false;
        for (Quadrilateral wall : scenario.getWallsAL()) {


            //needs to be changed and create a rectangle for the walls
            if (newRect.intersects(wall.getPointA().getX(), wall.getPointA().getY(), wall.getWidth(), wall.getHeight())) {
                nbOfIntersection++;
                System.out.println("part1");
            }
        }


           /*
        THIS CHECKS WHETHER THE END STATE RESULTS IN INTERSECTIONS
         */

        double CircleXCoord=agent.getLocation().getX()+dist.getValue()*Math.cos(agent.getDirection().getRadians());
        double CircleYCoord=agent.getLocation().getY()+dist.getValue()*Math.sin(agent.getDirection().getRadians());
        Circle circ= new Circle(CircleXCoord,CircleYCoord,0.5);

        for(Quadrilateral wall: scenario.getWallsAL()) {
            if (intersects(circ, wall)) {
                nbOfIntersection++;
                System.out.println("part2");
            }
        }
        if (nbOfIntersection > 0) {
            System.out.println("There are (will be) "+ nbOfIntersection+ " many collisions");
            checkCollision = true;
        }

        return checkCollision;
    }

}
