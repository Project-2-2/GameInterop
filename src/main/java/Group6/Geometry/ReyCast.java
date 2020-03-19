package Group6.Geometry;
import Group6.WorldState.AgentState;
import Interop.Percept.Vision.FieldOfView;
import java.lang.Math;

import java.util.*;

/**
 *
 *
 * agent is middle/crossing point
 *          b |
 *            |
 *            |
 *            |
 *            |
 *  a---------|-----------c
 *            |
 *            |
 *            |
 *            |
 *           e|d
 *
 *
 *
 * a=90 degrees, b 180 and so on. e =0
 *
 */
public class ReyCast {

    //reys forming the reyCast
    private List<LineSegment> reys;

    //quadrants
    private Angle a = new Angle(Math.toRadians(90));
    private Angle b = new Angle(Math.toRadians(180));
    private Angle c = new Angle(Math.toRadians(270));
    private Angle d = new Angle(Math.toRadians(360));
    private Angle e = new Angle(Math.toRadians(0));

    //variables needed
    private Angle shiftRate;
    private Angle initialShift;
    private Direction reyDirection;
    private Direction currDirection;
    private int reyNum = 20; //set the number of reys here.
    private double reyLenght;


    public ReyCast(AgentState agentState, FieldOfView fieldofView) {
        reyLenght = fieldofView.getRange().getValue();
        reys = generateReys(agentState.getLocation(), agentState.getDirection(), reyNum, fieldofView);
    }


    private List<LineSegment> generateReys(Point agentLocation, Direction agentDirection, int reyNum, FieldOfView fieldofView) {
        //get agent location
        double x_a = agentLocation.getX();
        double y_a = agentLocation.getY();

        //initialize to 0
        double x_b = 0;
        double y_b = 0;

        //initialize variables needed
        init(fieldofView, agentDirection);

        //create B points of line segments
        Point[] points = createPoints(x_b, y_b, x_a, y_a);

        //create lineSegments
        for(int i=0; i<reyNum; i++){
            LineSegment lineSegment = new LineSegment(
                    agentLocation,
                    points[i]);
            reys.add(lineSegment);
        }

        return reys;
    }


    private Point[] createPoints(double x_b, double y_b, double x_a, double y_a){
        Point[] points = new Point[reyNum];
        for (int i = 0; i < reyNum; i++) {
            //the direction is = to a, b, c, d or e
            if (currDirection.getDegrees() == a.getDegrees()) {
                x_b = x_a - reyLenght;
                y_b = y_a;
            } else if (currDirection.getDegrees() == b.getDegrees()) {
                x_b = x_a;
                y_b = y_a - reyLenght;
            } else if (currDirection.getDegrees() == c.getDegrees()) {
                x_b = x_a + reyLenght;
                y_b = y_a;
            } else if (currDirection.getDegrees() == d.getDegrees() || currDirection.getDegrees() == e.getDegrees()) {
                x_b = x_a;
                y_b = y_a + reyLenght;
            }
            else if (currDirection.getDegrees() > e.getDegrees() && currDirection.getDegrees() < a.getDegrees()) {
                if (currDirection.getDegrees() <= 45) {
                    double tempAnlge = Math.abs(currDirection.getDegrees() - e.getDegrees());
                    x_b = x_a + deltaXS(tempAnlge);
                    y_b = y_a - deltaYC(tempAnlge);
                }
                else {
                    double tempAnlge = Math.abs(currDirection.getDegrees() - a.getDegrees());
                    x_b = x_a + deltaXC(tempAnlge);
                    y_b = y_a - deltaYS(tempAnlge);
                }
            }
            else if (currDirection.getDegrees() > a.getDegrees() && currDirection.getDegrees() < b.getDegrees()) {
                if (currDirection.getDegrees() <= 135) { // check in which side of the a-b area
                    double tempAnlge = Math.abs(currDirection.getDegrees() - a.getDegrees());
                    x_b = x_a - deltaXC(tempAnlge);
                    y_b = y_a - deltaYS(tempAnlge);
                }
                else {
                    double tempAnlge = Math.abs(currDirection.getDegrees() - b.getDegrees());
                    x_b = x_a - deltaXS(tempAnlge);
                    y_b = y_a - deltaYC(tempAnlge);

                }
            }
            else if (currDirection.getDegrees() > b.getDegrees() && currDirection.getDegrees() < c.getDegrees()) {
                if (currDirection.getDegrees() <= 225) {
                    double tempAnlge = Math.abs(currDirection.getDegrees() - b.getDegrees());
                    x_b = x_a + deltaXS(tempAnlge);
                    y_b = y_a - deltaYC(tempAnlge);
                }
                else {
                    double tempAnlge = Math.abs(currDirection.getDegrees() - c.getDegrees());
                    x_b = x_a + deltaXC(tempAnlge);
                    y_b = y_a - deltaYS(tempAnlge);
                }
            }
            else if (currDirection.getDegrees() > c.getDegrees() && currDirection.getDegrees() < d.getDegrees()) {
                if (currDirection.getDegrees() <= 315) {
                    double tempAnlge = Math.abs(currDirection.getDegrees() - c.getDegrees());
                    x_b = x_a + deltaXC(tempAnlge);
                    y_b = y_a + deltaYS(tempAnlge);
                }
                else {
                    double tempAnlge = Math.abs(currDirection.getDegrees() - d.getDegrees());
                    x_b = x_a + deltaXS(tempAnlge);
                    y_b = y_a + deltaYC(tempAnlge);
                }
            }



            points[i] = new Point(x_b, y_b);
            currDirection = currDirection.getChangedBy(shiftRate);
        }
        return points;
    }
    private void init(FieldOfView fieldofView, Direction agentDirection){
        //angle between rays
        shiftRate = Angle.fromDegrees(0 - (fieldofView.getViewAngle().getDegrees() / reyNum));
        //half of field of view angle
        initialShift =  Angle.fromDegrees(fieldofView.getViewAngle().getDegrees() / 2);

        reyDirection = Direction.fromDegrees(agentDirection.getDegrees()); //initial direction of the agent
        currDirection = reyDirection.getChangedBy(initialShift); //outer left ray
    }


        public double deltaXS ( double angle){
            return Math.sin(Math.toRadians(angle)) * reyLenght;
        }
        public double deltaYC ( double angle){
            return Math.cos(Math.toRadians(angle)) * reyLenght;
        }
        public double deltaXC ( double angle){
            return Math.cos(angle) * reyLenght;
        }
        public double deltaYS ( double angle){
            return Math.sin(angle) * reyLenght;
        }

        public List<LineSegment> getReys () {
            return reys;
        }

        public void setReys (Set < LineSegment > reys) {
            reys = reys;
        }

    @Override
    public String toString() {
        String reycast = "Line Segments: \n";
        for(int i=0; i<reyNum; i++){
           String string =  reys.get(i).toString();
            reycast = reycast.concat(i+": "+reys.get(i).toString()+"\n");
        }
        return reycast;
    }
}


