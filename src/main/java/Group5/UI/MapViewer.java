package Group5.UI;

import javafx.scene.Group;
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
}
