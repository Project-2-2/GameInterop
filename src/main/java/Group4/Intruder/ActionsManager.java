package Group4.Intruder;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import Group4.OurInterop.*;
import Group9.map.objects.MapObject;
import Group9.math.Vector2;
import Interop.Action.*;
import Interop.*;
import Interop.Percept.*;
import Interop.Geometry.*;
import Interop.Agent.*;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Utils.*;
import Group9.map.*;
import Group9.map.area.*;
import Group9.map.dynamic.*;
import Group9.map.objects.*;
import Group9.tree.*;
import Group9.math.*;

public class ActionsManager {


//    private List<MapObject> portals;
//    private List<MapObject> mapObjects;
//    private List<MapObject> walls;
//
//    public ActionsManager(List<MapObject> mapObjects) {
//        this.mapObjects = mapObjects;
//        this.walls = new ArrayList<MapObject>();
//        this.portals = new ArrayList<>();
//    }
//
//
//    public ArrayList<AMove> getAllMoves(PointContainer.Circle circle){
//        ArrayList movesList = new ArrayList<>();
//        ArrayList MovesList = new ArrayList();
//        Vector2 circCenter = circle.getCenter();
//
//        //////////////////////AMove actions /////////////////////////////
//
//        ///--AMove along x--
//        for(int x = 0; x<100; x++){
//            Move m = new Move(new Distance(new Point(circCenter.getX(),circCenter.getY()),new Point(circCenter.getX()+x,circCenter.getY())));
//            MovesList.add(m);
//            AMove f_m = new AMove(circCenter.getX()+x,circCenter.getY());
//            movesList.add(f_m);
//        }
//        ///--AMove along x--
//        for(int y = 0; y<100; y++){
//            Move m = new Move(new Distance(new Point(circCenter.getX(),circCenter.getY()),new Point(circCenter.getX(),circCenter.getY()+y)));
//            MovesList.add(m);
//            AMove f_m = new AMove(circCenter.getX(),circCenter.getY()+y);
//            movesList.add(f_m);
//        }
//
//        /////////////////////Rotate and move action //////////////////////////
//
//        //0,0174533 radians is 1 degree
//        double r = 0.0174533;
//        double init = 0;
//
//        /*
//        calculate movement with angle
//        xx = x + (d * cos(alpha))
//        yy = y + (d * sin(alpha))
//         */
//
//        for(int x=0; x<=360; x++) {
//            init = init + r;
//            Angle a = new Angle(init);
//            for (int n = 0; n < 100; n++) {
//                Move m = new Move(new Distance(new Point(circCenter.getX(), circCenter.getY()), new Point(circCenter.getX() + (n * Math.cos(init)), circCenter.getY())));
//                MovesList.add(m);
//                AMove f_m = new AMove(circCenter.getX() + (n * Math.cos(init)), circCenter.getY());
//                movesList.add(f_m);
//            }
//        }
//
//
//        return movesList;
//    }
//
//    private ArrayList<AMove> aRadiusMoves(PointContainer.Circle circle){
//        double radius = circle.getRadius();
//        Vector2 circCenter = circle.getCenter();
//        ArrayList movesList = new ArrayList<>();
//        ArrayList MovesList = new ArrayList();
//        for(double x = circCenter.getX()-radius; x<circCenter.getX()+radius; x++){
//            for(double y = circCenter.getY()-radius; y<circCenter.getY()+radius; y++){
//                if(x>0 && y>0 && x<=120 && y<=80){
//                    Move m = new Move(new Distance(new Point(circCenter.getX(),circCenter.getY()),new Point(x,y)));
//                    if(!checkObstaclesHit(m)){
//                        MovesList.add(m);
//                        AMove f_m = new AMove(x,y);
//                        movesList.add(f_m);
//                    }
//                }
//            }
//        }
//        return movesList;
//    }
//
//    private boolean checkObstaclesHit(Move move) {
//        //System.out.println("checking collision");
//        boolean hit = false;
//
//        Point pointA = move.getDistance().getPointA();
//        Point pointB = move.getDistance().getPointB();
//
//        double m = (pointA.getY() - pointB.getY()) / (pointA.getX() - pointB.getX());
//        double c = (pointA.getY() - pointA.getX()) * m;
//
//        for (MapObject obj : mapObjects) {
//            if (obj.getType() == ObjectPerceptType.Wall) this.walls.add(obj);
//        }
//        //System.out.println(walls.get(0));
//        Line2D line = new Line2D.Double(pointA.getX(), pointA.getY(), pointB.getX(), pointB.getY());
//        //System.out.println(line.getBounds());
//        for (MapObject w : walls) {
//            PointContainer.Polygon wall = w.getContainer().getAsPolygon();
//            if (wall.getPoints().length == 4) {
//
//                Vector2[] corners = w.getContainer().getAsPolygon().getPoints();
//
//                double x1 = Math.min(Math.min(corners[0].getX(),corners[1].getX()),Math.min(corners[2].getX(),corners[3].getX()));
//                double x2 = Math.max(Math.max(corners[0].getX(),corners[1].getX()),Math.max(corners[2].getX(),corners[3].getX()));
//                double y1 = Math.min(Math.min(corners[0].getY(),corners[1].getY()),Math.min(corners[2].getY(),corners[3].getY()));
//                double y2 = Math.max(Math.max(corners[0].getY(),corners[1].getY()),Math.max(corners[2].getY(),corners[3].getY()));
//
//
//                Rectangle2D wallRect = new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
//                if (line.contains(wallRect.getBounds()) || wallRect.intersectsLine(line) || wallRect.contains(line.getBounds())) {
//                    //System.out.println("hit");
//                    return true;
//                }
//            }else{
//                System.out.println("This is not a rectangle, please refrain from such curveballs");
//            }
//        }
//
//        /*
//        //System.out.println(hit);
//        scenario.getWalls().get(0);
//        for(int x=0; x<20; x++){
//            for(int y=0; y<20;y++){
//                System.out.println(x + " " + y +" " +scenario.inWall(x,y));
//            }
//        }
//
//         */
//        return hit;
//
//    }
//
////    public static boolean checkTeleports(OurIntruder a){
////        System.out.println("checking portals");
////        double x = a.getCurrentLocation().getX();
////        double y = a.getCurrentLocation().getY();
////
////        String mapD = System.getProperty("user.dir")+System.getProperty("file.separator")+"src"+System.getProperty("file.separator")+"GameControllerSample"+System.getProperty("file.separator")+"testmap.txt";
////        Scenario scenario = new Scenario(mapD);
////
////        ArrayList<TelePortal> portals = scenario.getTeleportals();
////
////        System.out.println(scenario.getTeleportals().size());
////        for(TelePortal p: portals){
////            if(p.contains(x,y)){
////                System.out.println("in portal");
////                a.setCurrentLocation(p.getNewLocation()[0],p.getNewLocation()[1]);
////                OurIntruder.moveExplorer(p.getNewLocation()[0],p.getNewLocation()[1],a);
////                return true;
////            }
////        }
////
////        return false;
////    }
}
