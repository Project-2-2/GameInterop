package Group5.GameController;

import Interop.Geometry.Distance;
import Interop.Geometry.Point;

public class SentryTower extends Area {

    //everything belows this distance can't be seen inside the sentry
    Distance minDistance = new Distance(2);

    private Area insideAreaSentry;

    public SentryTower(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4,int ix1, int iy1, int ix2, int iy2, int ix3, int iy3, int ix4, int iy4){
        super(x1, y1, x2, y2,x3, y3, x4, y4);
        insideAreaSentry = new Area(ix1,iy1,ix2,iy2,ix3,iy3,ix4,iy4);
    }

    public boolean enterTower(Point p){
        if (insideAreaSentry.isHit(p)){
            return true;
        }
        return false;
    }

    /**
     * this is a different vission method since the vision inside a tower is different
     * possible to watch over walls
     * objects close by are not visible
     */
    public static void visionInTower(){

    }
}
