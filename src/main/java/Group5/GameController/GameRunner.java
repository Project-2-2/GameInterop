package Group5.GameController;


import Group5.UI.AlertBox;
import Group5.UI.DrawableMapModel;
import Group5.UI.View;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GameRunner {

//    protected String mapDoc;
//    protected Scenario scenario;
//
//    GamePlayer p;
//
//    public static void main(String[] args){
//        // the mapscenario should be passed as a parameter
//        String mapD="/Users/slav/Documents/Maastricht University - DKE/Year 2/Project 2-2/Project22/GameControllerSample/testmap.txt";
//        GameRunner game = new GameRunner(mapD);
//        game.p.setup();
//        //game.writeGameFile();
//        game.p.start();
//    }
//
//    public GameRunner(String scn){
//        mapDoc=scn;
//        scenario = new Scenario(mapDoc);
//        p = new GamePlayer(scenario);
//    }
//
//    public void runWholeGame(){
//        this.p.setup();
//        this.p.start();
//        Explorer explorer = new Explorer(gameRunner.getPath());
//        Explorer.runExplorer(explorer);
//    }
//
//    private String getPath(){
//        return this.mapDoc;
//    }

    final private static double FRAMES_PER_SECOND = 5;

    @FXML
    private View mapView;
    @FXML
    private BorderPane gameBorder;
    private DrawableMapModel drawableMapModel;

    private Timer timer;

    public GameRunner() {
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

