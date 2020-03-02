package Group5.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class DrawableMenuController {

    @FXML
    private Button button1 = new Button("Mode 0");

    @FXML
    void button1Handle(MouseEvent event) throws IOException {
        mode0();
    }

    @FXML
    void button2Handle(MouseEvent event) throws IOException {
    }

    @FXML
    void button3Handle(MouseEvent event) throws IOException {
    }

    @FXML
    private ImageView PlVSCompButton;

    @FXML
    private ImageView PlVSPlButton;

    private void mode0() {
        AlertBox.display("Load Scenario", "Load a .txt file with input parameters for the Map");
    }

}
