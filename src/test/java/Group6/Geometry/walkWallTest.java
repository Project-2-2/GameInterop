package Group6.Geometry;

import Interop.Geometry.Angle;

import java.util.List;


public class walkWallTest {
    public static void main(String[] args) {
        Point wallPointA = new Point(2.0, 12.0);
        Point wallPointB = new Point(4.0, 12.0);
        LineSegment partWallTemp = new LineSegment(wallPointA, wallPointB);
        System.out.println(wallPointA + "this is point a");
        System.out.println(wallPointB + "this is point b");
        if (wallPointA.getY() == partWallTemp.getMaxY()) {
            Point wallPointtemp = wallPointB;
            wallPointB = wallPointA;
            wallPointA = wallPointtemp;
        }
        System.out.println(wallPointA + "this is point a");
        System.out.println(wallPointB + "this is point b");
        LineSegment partWallFinal = new LineSegment(wallPointA, wallPointB);
        double alignment = partWallFinal.angleOfPoints(wallPointA, wallPointB);
        double alignmentDegree = 0;
        System.out.println("alignment" + alignment);
        if (alignment < 0) {
            alignmentDegree = 270 - alignment;
        }
        else {
            alignmentDegree = 90 - alignment;
        }
        Angle alignmentAngle = Angle.fromDegrees(alignmentDegree);
        System.out.println(alignmentDegree + "this is degree");
    }
}
