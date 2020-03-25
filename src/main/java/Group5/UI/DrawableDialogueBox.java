package Group5.UI;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Implementing a pop-up window, which takes a .txt file as input and passes it to the Game Controller,
 * where the initial conditions for the map are being read and generated.
 */

public class DrawableDialogueBox {

    private static File file;

    /**
     * @param title   is the title of the string.
     * @param message text to be displayed on the screen.
     */
    protected static void display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setMinHeight(300);
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setHgap(10);
        layout.setVgap(10);
        Label label_random = new Label(message);
        GridPane.setConstraints(label_random, 0, 0);

        FileChooser file_chooser = new FileChooser();
        Button file_button = new Button("Choose File");
        file_button.setOnMouseEntered(e -> {
            file_button.setStyle("-fx-background-color: #f9acf0");
        });
        file_button.setOnMouseExited(e -> {
            file_button.setStyle("-fx-background-color: #cccccc");
        });
        file_button.setOnAction(e -> {
            file = file_chooser.showOpenDialog(window);
            if (file != null) {
                try {
                    // Load the FXML for Mode 0 of the game
                    Parent root;
                    MapFileParser.readMapFile(file);
                    FXMLLoader loader = new FXMLLoader(DrawableDialogueBox.class.getResource("/src/main/java/Group5/UI/mode0.fxml"));
                    root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    root.getChildrenUnmodifiable();
                    root.requestFocus();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        GridPane.setConstraints(file_button, 0, 7);
        layout.getChildren().addAll(label_random, file_button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static File getFile() {
        return file;
    }
}
