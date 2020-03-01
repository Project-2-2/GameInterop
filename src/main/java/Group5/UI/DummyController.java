package Group5.UI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * !!! THIS CONTROLLER IS ONLY FOR TESTING PURPOSES UNTIL THE ACTUAL GAME CONTROLLER IS IMPLEMENTED !!!
 */

public class DummyController {
    final private static double FRAMES_PER_SECOND = 5;

    @FXML
    private View mapView;
    @FXML
    private BorderPane gameBorder;
    private DrawableMapModel drawableMapModel;

    private Timer timer;

    public DummyController() {
    }

    @FXML
    public void initialize() throws IOException {
        String file = AlertBox.getFile();
        this.drawableMapModel = new DrawableMapModel();
        this.update();
        this.startTimer();
    }

    private void startTimer() {
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        try {
                            update();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        long frameTimeInMilliseconds = (long) (1000.0 / FRAMES_PER_SECOND);
        this.timer.schedule(timerTask, 100, frameTimeInMilliseconds);
    }

    private void update() throws IOException {
        this.drawableMapModel.step();
        this.mapView.update(drawableMapModel);
    }
}
