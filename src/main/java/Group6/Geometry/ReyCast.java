package Group6.Geometry;
import Group6.WorldState.AgentState;
import Interop.Percept.Vision.FieldOfView;
import java.lang.Math;

import java.util.Set;

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

    public ReyCast(AgentState agentState, FieldOfView fieldofView){
        reyLenght = fieldofView.getRange().getValue();
        reys = generateReys(agentState.getLocation(), agentState.getDirection(),reyNum, fieldofView);


    }

    private Set<LineSegment> generateReys(Point agentLocation, Direction agentDirection, int reyNum, FieldOfView fieldofView) {
        double x_a = agentLocation.getX();
        double y_a = agentLocation.getY();

        double x_b = 0;
        double y_b = 0;


        //angle between rays
        Angle shiftRate = new Angle(fieldofView.getViewAngle().getRadians()/reyNum);
        //half of field of view angle
        Angle initialShift = new Angle(fieldofView.getViewAngle().getDegrees()/2);

        Direction reyDirection = Direction.fromDegrees(agentDirection.getDegrees()-initialShift.getDegrees());
        Direction currDirection = reyDirection;

        double currDirectionDegree = currDirection.getDegrees();
        double currAngleDegree = initialShift.getDegrees();

        for(int i=0; i<reyNum; i++){
            //the diraction is = to a, b, c, d or e
            if(currDirectionDegree==a.getDegrees()){
                x_b = x_a-deltaX(currAngleDegree);
                y_b = y_a;
            }
            else if(currDirectionDegree==b.getDegrees()){
                x_b = x_a;
                y_b = y_a-deltaY(currAngleDegree);
            }
            else if(currDirectionDegree==c.getDegrees()){
                x_b = x_a+deltaX(currAngleDegree);
                y_b = y_a;
            }
            else if(currDirectionDegree==d.getDegrees() || currDirectionDegree==e.getDegrees()){
                x_b = x_a;
                y_b = y_a+deltaY(currAngleDegree);
            }


            else if(currDirectionDegree>a.getDegrees() && currDirectionDegree<b.getDegrees()){
                x_b = x_a-deltaX(currAngleDegree);
                y_b = y_a-deltaY(currAngleDegree);
            }
            else if(currDirectionDegree>b.getDegrees() && currDirectionDegree<c.getDegrees()){
                x_b = x_a+deltaX(currAngleDegree);
                y_b = y_a-deltaY(currAngleDegree);
            }
            else if(currDirectionDegree>e.getDegrees() && currDirectionDegree<a.getDegrees()){
                x_b = x_a-deltaX(currAngleDegree);
                y_b = y_a+deltaY(currAngleDegree);
            }
            else if(currDirectionDegree>c.getDegrees() && currDirectionDegree<d.getDegrees()){
                x_b = x_a+deltaX(currAngleDegree);
                y_b = y_a+deltaY(currAngleDegree);
            }

            points[i] = new Point(x_b, y_b);


        }
        return reys;
    }

    public double deltaX(double angle){
        return Math.sin(angle)*reyLenght;
    }
    public double deltaY(double angle){
        return Math.cos(angle)*reyLenght;
    }

    public Set<LineSegment> getReys() {
        return reys;
    }

    public void setReys(Set<LineSegment> reys) {
        reys = reys;
    }
}

