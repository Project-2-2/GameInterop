package Group5.GameController;

public class TelePortal extends Area {

    protected int yTarget;
    protected int xTarget;
    protected double outOrientation;

    public TelePortal(int x1, int y1, int x2, int y2, int targetX, int targetY){
        super(x1,y1,x2,y2);
        yTarget=targetY;
        xTarget=targetX;
        outOrientation = 0.0;
    }

    public TelePortal(int x1, int y1, int x2, int y2, int targetX, int targetY, double orient){
        super(x1,y1,x2,y2);
        yTarget=targetY;
        xTarget=targetX;
        outOrientation = orient;
    }

    public int[] getNewLocation(){
        int[] target = new int[] {yTarget,xTarget};
        return target;
    }

    public double getNewOrientation(){
        return outOrientation;
    }
}