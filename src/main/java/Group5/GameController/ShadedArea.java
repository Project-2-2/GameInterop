package Group5.GameController;

import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

public class ShadedArea extends Area {


    public ShadedArea(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4){
        super(x1, y1, x2, y2,x3, y3, x4, y4, ObjectPerceptType.ShadedArea);
    }

    /**
     * vision inside shaded are limited to only what's inside the shaded area
     * @param p the point that will be checked if it is inside the area
     */
    public void visionInShadedArea(Point p){
        //if point is inside area
        if (isHit(p)){

        }

    }
}
