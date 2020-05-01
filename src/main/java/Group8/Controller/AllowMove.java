package Group8.Controller;

import Interop.Geometry.Point;

/**
 * This class checks if a move is legal by using vector cross products
 */

public class AllowMove {

    //private double maxDistanceGuard = new Scenario() //Should get the maxDistance from the .txt file
    private double maxDistanceGuard = 1.4;

    public boolean isLegal(Point p1, Point p2, Point q1, Point q2){
        //Things are looking like a mess, but that's because I'm working on it

        //  p1 = new Point(p1.getX()/LauncherGUI.DRAW_CONSTANT, p1.getY()/LauncherGUI.DRAW_CONSTANT);
        //  p2 = new Point(p2.getX()/LauncherGUI.DRAW_CONSTANT, p2.getY()/ LauncherGUI.DRAW_CONSTANT);


       // Point r = remakePoint(q1);
      //  Point r2 = remakePoint(q2);
        Point r = q1;
        Point r2 = q2;




        // r = new Point(r.getX()*10, r.getY()*10);
        //   r2 = new Point(r2.getX()*10, r2.getY()*10);


        // if(new Distance(p1,p2).getValue()>this.maxDistanceGuard){return false;}

        if(p1.getX()==p2.getX() && r.getX()==r2.getX()){
            if(p1.getX()!=r.getX()){//System.out.println("A");
                return true;}  //both vertical but don't cross
            else{
                //check if (y1,y2) and (y3,y4) overlap
                if (Math.min(p1.getY(),p2.getY()) < Math.min(r.getY(),r2.getY())) {//System.out.println("B");
                    return Math.max(p1.getY(),p2.getY()) < Math.min(r.getY(),r2.getY());
                } else {//System.out.println("C");
                    return Math.max(r.getY(),r2.getY()) < Math.min(p1.getY(),p2.getY());
                }


            }

            /*  //min(x1, x2) < x0 < max(x1, x2) and min(x3, x4) < x0 < max(x3, x4)
        if( Math.min(p1.getX(), p2.getX()) < x0 && x0 < Math.max(p1.getX(), p2.getX()) &&
                Math.min(r.getX(), r2.getX()) < x0 && x0 < Math.max(r.getX(), r2.getX())
        ){
            System.out.println("E " + p1.getX() + " " + p2.getX() + " " + p1.getY() + " " + p2.getY());

            return false; //intersect
        }

        System.out.println("F");*/

        } else if(p1.getX()==p2.getX()){System.out.println("D");
            double a34 = (r2.getY()-r.getY())/(r2.getX()-r.getX());
            double b34 = r.getY() - a34*r.getX();
            double y = a34 * p1.getX() + b34;
            return !(y >= Math.min(p1.getY(),p2.getY()) && y <= Math.max(p1.getY(),p2.getY()) && p1.getX() >= Math.min(r.getX(),r2.getX()) && p1.getX() <= Math.max(r.getX(),r2.getX()));
        } else if(r.getX()==r2.getX()) {
            double a12 = (p2.getY()-p1.getY())/(p2.getX()-p1.getX());
            double b12 = p1.getY() - a12*p1.getX();
            double y = a12 * r.getX() + b12;
           // System.out.println("E + " + !(y >= Math.min(r.getY(),r2.getY()) && y <= Math.max(r.getY(),r2.getY()) && r.getX() >= Math.min(p1.getX(),p2.getX()) && r.getX() <= Math.max(p1.getX(),p2.getX())));
            return !(y >= Math.min(r.getY(),r2.getY()) && y <= Math.max(r.getY(),r2.getY()) && r.getX() >= Math.min(p1.getX(),p2.getX()) && r.getX() <= Math.max(p1.getX(),p2.getX()));
        }


        double a1 = (p2.getY()-p1.getY()) / (p2.getX()-p1.getX());
        double b1 =  p1.getY() - a1*p1.getX();
        double a2 =  (r2.getY()-r.getY()) / (r2.getX() - r.getX());
        double b2 =  r.getY() - a2 * r.getX();

        if (zeroTest(a1 - a2)) {//System.out.println("F");
            // Parallel lines
            return !zeroTest(b1 - b2);
        }



        if(a1 == a2 && b1 == b2){//System.out.println("G");
            //overlap
        }

        double x0 = -(b1-b2)/(a1-a2);
       // System.out.println("H");
        return !(x0 >= Math.min(p1.getX(),p2.getX()) && x0 <= Math.max(p1.getX(),p2.getX()) && x0 >= Math.min(r.getX(),r2.getX()) && x0 <= Math.max(r.getX(),r2.getX()));




    }

    public boolean zeroTest(double v) {
        return Math.abs(v) <= 0.0001;
        //ensures that the proper boolean conditions are satisfied
    }

    public Point remakePoint(Point p){
        //Takes into account the constant of the GUI
        if(p.getX()>1){
            if(p.getY()>1){
                p = new Point(p.getX()*LauncherGUI.DRAW_CONSTANT, p.getY()*LauncherGUI.DRAW_CONSTANT);
            } else{
                p = new Point(p.getX()*LauncherGUI.DRAW_CONSTANT, p.getY());
            }
        }else{
            if(p.getY()>1){
                p = new Point(p.getX(), p.getY()*LauncherGUI.DRAW_CONSTANT);
            } else{
                p = new Point(p.getX(), p.getY());
            }
        }
        return p;
    }

    public Point pointIntersection(Point A, Point B, Point C, Point D)
    {
        double x1 = B.getY() - A.getY();
        double f1 = A.getX() - B.getX();
        double g1 = x1*(A.getX()) + f1*(A.getY());

        double x2 = D.getY() - C.getY();
        double f2 = C.getX() - D.getX();
        double g2 = x2*(C.getX())+ f2*(C.getY());

        double diff = x1*f2 - x2*f1;

        double x = Math.abs((f2*g1 - f1*g2)/diff);
        double y = Math.abs((x1*g2 - x2*g1)/diff);

        return new Point(x, y);

    }

    public static void main(String[] args){

        //Test class
        Point p1 = new Point(1,1);
        Point p2 = new Point(1,2);
        Point r = new Point (0,1);
        Point r2 = new Point(5,1.5);

        if(new AllowMove().isLegal(p1,p2,r,r2)){
            System.out.println("The move is fully allowed, radius of the agent does not cross boundaries");
        } else{
            System.out.println("The move is not allowed, radius of the agent cross boundaries");
        }

    }

}