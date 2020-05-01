package Group8.Controller.Utils;

import Group8.Controller.G8Launcher;
import Group8.Controller.LauncherGUI;
import Group8.Controller.Utils.Scenario;
import Group8.Controller.Utils.Vector2d;
import Interop.Geometry.Point;
import Interop.Percept.Vision.FieldOfView;
import javafx.scene.shape.Circle;



public abstract class AgentInfo {
    private static int counter =0;
    private Circle c;
    private Point actualPos;
    private Point drawPos;
    private Vector2d direction;
    private AgentType agentType;
    private String name;
    private FieldOfView visualField;
    private double visualRange;
    public enum AgentType{
        GUARD,INTRUDER
    }

    public AgentInfo(double x,double y,double r,AgentType agentType){
        c = new Circle(x * LauncherGUI.DRAW_CONSTANT,y * LauncherGUI.DRAW_CONSTANT,r * LauncherGUI.DRAW_CONSTANT);
        actualPos = new Point(x,y);
        drawPos = new Point(x * LauncherGUI.DRAW_CONSTANT,y * LauncherGUI.DRAW_CONSTANT);
        this.agentType = agentType;
        this.name = agentType.toString() + counter;
        counter++;

    }

    public void setVisualRange(double visualRange){this.visualRange = visualRange;}

    public Circle getC() {
        return c;
    }

    public Point getActualPos() {
        return actualPos;
    }

    public void setActualPos(double x, double y) {
        this.actualPos = new Point(x,y);
        this.drawPos = new Point(actualPos.getX() * LauncherGUI.DRAW_CONSTANT,actualPos.getY() * LauncherGUI.DRAW_CONSTANT);
    }
    public void setActualPos(Point p) {
        this.actualPos = p;
        this.drawPos = new Point(actualPos.getX() * LauncherGUI.DRAW_CONSTANT,actualPos.getY() * LauncherGUI.DRAW_CONSTANT);
    }

    public Point getDrawPos() {
        return drawPos;
    }

    public void setTranslateX(double dx){
        getC().setCenterX((actualPos.getX()+dx)*LauncherGUI.DRAW_CONSTANT);
        setActualPos(actualPos.getX()+dx,actualPos.getY());
    }
    public double getTranslateX(){
        return getC().getTranslateX();
    }

    public double geTranslateY(){
        return getC().getTranslateY();
    }

    public void setTranslateY(double dy){
        getC().setCenterY((actualPos.getY()+dy)*LauncherGUI.DRAW_CONSTANT);
        setActualPos(actualPos.getX(),actualPos.getY()+dy);
    }

    public Vector2d getDirection() {
        if(direction == null){
            direction = initDirection();
        }
        return direction;
    }

    public void setDirection(Vector2d direction) {
        this.direction = direction;
    }

    private Vector2d initDirection(){
        float visRange;
        if(agentType == AgentType.GUARD) {
            visRange = G8Launcher.Scenario.getViewRangeGuardNormal();
        }
        else if(agentType == AgentType.INTRUDER){
            visRange = G8Launcher.Scenario.getViewRangeIntruderNormal();
        }
        else{
            visRange = 1;
        }
        double angle = Math.random()*2*Math.PI;
        return new Vector2d(Math.cos(angle)*visRange,Math.sin(angle)*visRange);
    }

    public String getName(){ return this.name; }

    @Override
    public String toString() {
        return String.format("X: %f, Y: %f\n",drawPos.getX(),drawPos.getY());
    }
}