package Group11;


public class Segment {
    private Point a;
    private Point b;

    public Segment(Point x1, Point x2){
        this.a = x1;
        this.b = x2;
    }
    public boolean intersect(Segment z){
        double orientationA = orientation(a, b, z.getA());
        double orientationB = orientation(a, b, z.getB());
        double orientationC = orientation(z.getA(), z.getB(), this.getA());
        double orientationD = orientation(z.getA(), z.getB(), this.getB());
        //System.out.println("a "+orientationA+" b "+orientationB+" c "+orientationC+" d "+orientationD);
        if(orientationA!=orientationB && orientationC!=orientationD){
            return true;
        }
        else{
            return false;
        }
    }

    public double orientation(Point a, Point b, Point c){
       /* double val1 = (b.getY()-a.getY())/(b.getX()-a.getX());
        double val2 = (c.getY()-b.getY())/(c.getX()-b.getX());*/
        double val3 = (b.getY()-a.getY())*(c.getX()-b.getX())-(b.getX()-a.getX())*(c.getY()-b.getY());
        if(val3<0){
            return 2;
        }
        else{
            return 1;
        }
    }
    public Point getA(){
        return this.a;
    }
    public Point getB(){
        return this.b;
    }
    public void setA(Point x1){
        this.a = x1;
    }
    public void setB(Point x2){
        this.b = x2;
    }
}