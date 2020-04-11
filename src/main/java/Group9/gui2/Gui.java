package Group9.gui2;
import Group11.Control.Helpers.Movables;
import Group11.Control.Helpers.MultiMap;
import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Gui extends Application {

    private MainScene scene = new MainScene(new StackPane());
    private Stage primary = new Stage();
    private MainController mainController = new MainController(this);
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
    public void drawMovables(MultiMap<String, Movables> movablesMultiMap){
        scene.drawMovables(movablesMultiMap);
    }

}
