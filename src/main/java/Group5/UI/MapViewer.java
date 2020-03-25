package Group5.UI;

import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class MapViewer extends Group {

    private int mapHeight = MapFileParser.getMapHeight();
    private int mapWidth = MapFileParser.getMapWidth();

    /**
     * Add all Shapes to the MapView and AnchorPane to be rendered
     */
    public MapViewer() {
        this.prefHeight(this.mapWidth);
        this.prefWidth(this.mapHeight);
        this.getChildren().addAll(MapFileParser.getDrawableObjects());
        this.getChildren().addAll(MapFileParser.getDrawableAgents());
    }

    /**
     * @param x        position x
     * @param y        position y
     */
    public void moveIntruder(double x, double y) {
        assert x >= 0 && x < MapFileParser.getMapWidth() && y >= 0 && y < MapFileParser.getMapHeight();
        for (Shape shape : MapFileParser.getDrawableAgents()) {
            if (shape.getClass().equals(DrawableIntruderAgent.class)) {
                ((DrawableIntruderAgent) shape).setCenterX(x);
                ((DrawableIntruderAgent) shape).setCenterY(y);
//                ((DrawableIntruderAgent) shape).setCenterX(((DrawableIntruderAgent) shape).getCenterX() + x);
//                ((DrawableIntruderAgent) shape).setCenterY(((DrawableIntruderAgent) shape).getCenterY() + y);
            }
        }
    }
    
    /**
     *
     * @param x position for the agent
     * @param y position for the agent
     * @param angle viewing angle for the agent in Radians
     * @param distance for the visual field
     */
    public void drawAgentVisionField(double x, double y, double angle, double distance) {
        double value1 = -45*Math.PI/180;
        double value2 = 45*Math.PI/180;
        double x2 = distance * Math.cos(angle + value1) + x;
        double y2 = distance * Math.sin(angle + value1) + y;
        double x3 = distance * Math.cos(angle + value2) + x;
        double y3 = distance * Math.sin(angle + value2) + y;
        for (Shape shape : MapFileParser.getDrawableObjects()) {
            if (shape.getFill() == Color.YELLOW) {
                ((DrawableObject) shape).changeCoordinates(x, y, x2, y2, x3, y3);
            }
        }
    }

    public void doorOpening(double x1, double y1, double x2, double y2,double x3, double y3,double x4, double y4){
        for(Shape shape : MapFileParser.getDrawableObjects()){
            DrawableObject object = (DrawableObject) shape;
            if(object.x1==x1 && object.y1==y1 && object.x2==x2 && object.y2==y2 && object.x3==x3 && object.y3==y3 && object.x4==x4 && object.y4==y4)
            {
                object.setFill(Color.TRANSPARENT);
            }
        }
    }
    public void windowOpening(double x1, double y1, double x2, double y2,double x3, double y3,double x4, double y4){
        for(Shape shape : MapFileParser.getDrawableObjects()){
            DrawableObject object = (DrawableObject) shape;
            if(object.x1==x1 && object.y1==y1 && object.x2==x2 && object.y2==y2 && object.x3==x3 && object.y3==y3 && object.x4==x4 && object.y4==y4)
            {
                object.setFill(Color.TRANSPARENT);
            }
        }
    }
}
