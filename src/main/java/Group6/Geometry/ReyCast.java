package Group6.Geometry;
import Group6.WorldState.AgentState;
import Interop.Percept.Vision.FieldOfView;
import java.lang.Math;

import java.util.Set;

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
    private Set<LineSegment> reys;
    private int reyNum = 45;
    private double reyLenght;
    //quadrants
    private Angle a = new Angle(90);
    private Angle b = new Angle(180);
    private Angle c = new Angle(270);
    private Angle d = new Angle(360);
    private Angle e = new Angle(0);

    private Point[] points = new Point[reyNum];


    private Angle reyAngle;

    public ReyCast(AgentState agentState, FieldOfView fieldofView) {
        reyLenght = fieldofView.getRange().getValue();
        reys = generateReys(agentState.getLocation(), agentState.getDirection(), reyNum, fieldofView);


    }

    private Set<LineSegment> generateReys(Point agentLocation, Direction agentDirection, int reyNum, FieldOfView fieldofView) {
        double x_a = agentLocation.getX();
        double y_a = agentLocation.getY();

        double tempAnlge;
        double newDegree;

        double x_b = 0;
        double y_b = 0;


        //angle between rays
        Angle shiftRate = new Angle(fieldofView.getViewAngle().getRadians() / reyNum);
        //half of field of view angle
        Angle initialShift = new Angle(fieldofView.getViewAngle().getDegrees() / 2);

        Direction reyDirection = Direction.fromDegrees(agentDirection.getDegrees()); //initial direction of the agent
        Direction currDirection = Direction.fromDegrees(reyDirection.getDegrees() - initialShift.getDegrees()); //outer left ray

        double currDirectionDegree = currDirection.getDegrees(); //amount of degrees of outer left ray
        if (currDirectionDegree < 0){ // check if new degree is negative, if so, make positive
            currDirectionDegree = currDirectionDegree + 360;
        }
        double currAngleDegree = initialShift.getDegrees(); //degree of initial shift

        for (int i = 0; i < reyNum; i++) {
            //the direction is = to a, b, c, d or e
            if (currDirectionDegree == a.getDegrees()) {
                x_b = x_a - reyLenght;
                y_b = y_a;
            } else if (currDirectionDegree == b.getDegrees()) {
                x_b = x_a;
                y_b = y_a - reyLenght;
            } else if (currDirectionDegree == c.getDegrees()) {
                x_b = x_a + reyLenght;
                y_b = y_a;
            } else if (currDirectionDegree == d.getDegrees() || currDirectionDegree == e.getDegrees()) {
                x_b = x_a;
                y_b = y_a + reyLenght;
            }
            else if (currDirectionDegree > e.getDegrees() && currDirectionDegree < a.getDegrees()) {
                if (currDirectionDegree <= 45) {
                    tempAnlge = currDirectionDegree - e.getDegrees();
                    x_b = x_a - deltaXS(tempAnlge);
                    y_b = y_a + deltaYC(tempAnlge);
                }
                else {
                    tempAnlge = currDirectionDegree - a.getDegrees();
                    x_b = x_a - deltaXC(tempAnlge);
                    y_b = y_a + deltaYS(tempAnlge);
                }
            }
            else if (currDirectionDegree > a.getDegrees() && currDirectionDegree < b.getDegrees()) {
                if (currDirectionDegree <= 135) { // check in which side of the a-b area
                    tempAnlge = currDirectionDegree - a.getDegrees();
                    x_b = x_a - deltaXC(tempAnlge);
                    y_b = y_a - deltaYS(tempAnlge);
                    }
                else {
                    tempAnlge = currDirectionDegree - b.getDegrees();
                    x_b = x_a - deltaXS(tempAnlge);
                    y_b = y_a - deltaYC(tempAnlge);

                    }
                }
            else if (currDirectionDegree > b.getDegrees() && currDirectionDegree < c.getDegrees()) {
                     if (currDirectionDegree <= 225) {
                         tempAnlge = currDirectionDegree - b.getDegrees();
                         x_b = x_a + deltaXS(tempAnlge);
                         y_b = y_a - deltaYC(tempAnlge);
                     }
                     else {
                         tempAnlge = currDirectionDegree - c.getDegrees();
                         x_b = x_a + deltaXC(tempAnlge);
                         y_b = y_a - deltaYS(tempAnlge);
                     }
                }
            else if (currDirectionDegree > c.getDegrees() && currDirectionDegree < d.getDegrees()) {
                if (currDirectionDegree <= 315) {
                    tempAnlge = currDirectionDegree - c.getDegrees();
                    x_b = x_a + deltaXC(tempAnlge);
                    y_b = y_a + deltaYS(tempAnlge);
                }
                else {
                    tempAnlge = currDirectionDegree - d.getDegrees();
                    x_b = x_a + deltaXS(tempAnlge);
                    y_b = y_a + deltaYC(tempAnlge);
                }
            }



                points[i] = new Point(x_b, y_b);
                newDegree= currDirectionDegree + currAngleDegree;
                if( newDegree> 360){ //check if we crossed the 360 bound, then we just substract 360.
                    newDegree = newDegree-360;
                }
                currDirectionDegree= newDegree;


            }
            return reys;
        }

        public double deltaXS ( double angle){
            return Math.sin(angle) * reyLenght;
        }
        public double deltaYC ( double angle){
            return Math.cos(angle) * reyLenght;
        }
        public double deltaXC ( double angle){
            return Math.cos(angle) * reyLenght;
        }
        public double deltaYS ( double angle){
            return Math.sin(angle) * reyLenght;
        }

        public Set<LineSegment> getReys () {
            return reys;
        }

        public void setReys (Set < LineSegment > reys) {
            reys = reys;
        }
}


