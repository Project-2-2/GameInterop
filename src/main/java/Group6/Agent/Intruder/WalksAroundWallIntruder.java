package Group6.Agent.Intruder;




import Group6.Agent.Behaviour.Behaviour;
import Group6.Agent.Behaviour.ExploreBehaviour;
import Group6.Agent.RandomAgent;
import Group6.Geometry.Collection.Points;
import Group6.Geometry.Direction;
import Group6.Geometry.Line;
import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;
import Group6.WorldState.Contract.Object;
import Interop.Geometry.Distance;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPercepts;
import Group6.Utils;
import Group6.Percept.*;
import Group9.agent.RandomIntruderAgent;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPerceptType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * todo check if inbetween in the target area
 */


public class WalksAroundWallIntruder implements Intruder {

    public IntruderAction getAction(IntruderPercepts percepts) {
        if ( !getWallPercepts(percepts).getAll().isEmpty()) {
            List<Point> pointsIntersection = getTwoWallPoints(percepts);
            Point wallPointA = pointsIntersection.get(0);
            Point wallPointB = pointsIntersection.get(1);

            LineSegment partWallTemp = new LineSegment(wallPointA,wallPointB);
            if(wallPointA.getY() == partWallTemp.getMaxY()){
                Point wallPointtemp= wallPointB;
                wallPointB = wallPointA;
                wallPointA = wallPointtemp;
            }
            LineSegment partWallFinal =new LineSegment(wallPointA,wallPointB);
            double alignment = partWallFinal.angleOfPoints(wallPointA,wallPointB);
            double alignmentDegree=0;
            if (alignment <0 ){
                alignmentDegree = 270 - alignment;
            }
            else {
                alignmentDegree = 90- alignment;
            }
            Angle alignmentAngle =  Angle.fromDegrees(alignmentDegree);
            return getMoveWalkAroundWall(percepts, alignmentAngle);
        }
        else{
            return (IntruderAction)new ExploreBehaviour().getAction(percepts);
        }
    }

    private IntruderAction getMoveWalkAroundWall(IntruderPercepts percepts, Angle shiftAngle) {

        double turnAngle = 90.0;
        int count =0;
        //turn to get aligned to the wall
        new Rotate(shiftAngle);
        while(count <= 4) {
            if (getWallPercepts(percepts).getAll().isEmpty()) {
                new Rotate(Angle.fromDegrees(turnAngle));
                count += 1;
            }
            new Move(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder());
        }

        return (IntruderAction)new ExploreBehaviour().getAction(percepts);
    }


    public static ObjectPercepts getWallPercepts(Percepts percepts) {
        return percepts
                .getVision()
                .getObjects()
                .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Wall);
    }

    public static List<Point> getTwoWallPoints(Percepts percepts) {
        Iterator<ObjectPercept> iterator = getWallPercepts(percepts).getAll().iterator();
        Interop.Geometry.Point pointA = iterator.next().getPoint();
        Interop.Geometry.Point pointB = iterator.next().getPoint();
        List<Point> listPoints = new ArrayList();
        listPoints.add(new Group6.Geometry.Point(pointA));
        listPoints.add(new Group6.Geometry.Point(pointB));

        return listPoints;
    }

}


