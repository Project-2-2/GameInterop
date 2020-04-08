package Group9.gui;

import Group9.Game;
import Group9.agent.container.AgentContainer;
import Group9.gui.GUIConverter;
import Group9.gui.InternalWallGui;
import Group9.map.GameMap;
import Group9.map.objects.MapObject;
import Group9.map.parser.Parser;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Function;

public class ShowMap extends Application implements Function<AgentContainer<?>, Void> {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        GameMap gameMap = Parser.parseFile("./src/main/java/Group9/map/maps/test.map");
        Game game = new Game(gameMap, true);

        Group root = new Group();
        primaryStage.setScene(new Scene(root, 1280, 720));
        List<MapObject> mapObjects = gameMap.getObjects();
        mapObjects.forEach(m -> root.getChildren().add(GUIConverter.convert(m)));
        System.out.println(root.getChildren().get(0).getClass());
        root.getChildren().forEach(c -> ((InternalWallGui) c).updateScale());
        primaryStage.show();
    }

    @Override
    public Void apply(AgentContainer<?> agentContainer) {
        return null;
    }
}
