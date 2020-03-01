package Group5;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Group5.UI.AlertBox;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/src/main/java/Group5/UI/startWindow.fxml"));
        primaryStage.setTitle("MARL Surveillance Simulation");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Starts Mode 0 of the game (thank you, captain Obvious)
     */
    private void mode0() {
        AlertBox.display("Load Sceneraio", "Load a .txt file with input parameters for the Map");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
