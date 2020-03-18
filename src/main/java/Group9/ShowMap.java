package Group9;

import Group9.gui.GameObject;
import Group9.gui.InternalWallGui;
import Group9.map.GameMap;
import Group9.map.objects.MapObject;
import Group9.map.parser.Parser;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

public class ShowMap extends Application {
    public static void main(String[] args)
    {
        launch();
    }
    @Override
    public void start(Stage primaryStage)
    {
        Group root = new Group();
        primaryStage.setScene(new Scene(root, 1000, 1000));
        GameMap gameMap = Parser.parseFile("./src/main/java/Group9/map/maps/test.map");
        List<MapObject> mapObjects = gameMap.getObjects();
        mapObjects.forEach(m -> root.getChildren().add(m.getGui()));
        System.out.println(root.getChildren().get(0).getClass());
        root.getChildren().forEach(c -> ((InternalWallGui)c).updateScale());
        primaryStage.show();
    }
}
