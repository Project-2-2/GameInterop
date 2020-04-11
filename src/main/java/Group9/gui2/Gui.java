package Group9.gui2;

import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.map.dynamic.DynamicObject;
import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;

public class Gui extends Application {

    private MainController mainController = new MainController(this);
    private MainScene scene = new MainScene(new StackPane(), mainController.getGame().getGameMap());
    private Stage primary = new Stage();

    public static void Gui(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setHeight(GuiSettings.defaultHeight);
        primaryStage.setWidth(GuiSettings.defaultWidth);
        primaryStage.setTitle("Orwells Dream");
        primaryStage.setScene(scene);
        primary = primaryStage;
        primaryStage.show();
        scene.rescale();
        Thread thread = new Thread(mainController);
        thread.start();
    }

    public void drawMovables(List<GuardContainer> guards, List<IntruderContainer> intruders, List<DynamicObject> objects){
        scene.drawMovables(guards, intruders, objects);
    }

}
