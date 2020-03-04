package Group5.UI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * !!! THIS CONTROLLER IS ONLY FOR TESTING PURPOSES UNTIL THE ACTUAL GAME CONTROLLER IS IMPLEMENTED !!!
 */

public class DummyController {
    final private static double FRAMES_PER_SECOND = 5;

    @FXML
    private MapViewer mapViewer;
    @FXML
    private AnchorPane gameBorder;
    private Timer timer;
    private boolean paused;


    public DummyController() {
        this.paused = false;
    }

    @FXML
    public void initialize() throws IOException {
        File file = DrawableDialogueBox.getFile();
        this.update();
        this.startTimer();
        mapViewer.setFocusTraversable(true);
        mapViewer.requestFocus();
    }

    @FXML
    public void keyHandler(KeyEvent event) {
        if (event.getCode() == KeyCode.P && !paused) {
            paused = true;
            pause();
        }
        if (event.getCode() == KeyCode.R && paused) {
            paused = false;
            this.startTimer();
        }
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
        this.mapViewer.moveIntruder(10, 10, true);
    }

    // Pause the timer and simulation
    public void pause() {
        this.timer.cancel();
    }
}
