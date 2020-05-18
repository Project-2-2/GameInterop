package Group6;


import Group6.Agent.Factory.AgentsFactories;
import Group6.Controller.Controller;
import Group6.Geometry.Quadrilateral;
import Group6.Percept.*;
import Group6.WorldState.Scenario;
import Group6.WorldState.WorldState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    public static void main(String[] args){

        launch();

    }
    public void start(Stage s){

        Group movingObjects = new Group();

        Group root = new Group();
        root.getChildren().add(movingObjects);
        s.setScene(new Scene(root, 1000, 1000));
        s.show();
        Scenario test = new Scenario("src/main/java/Group6/test.map");

        Controller controller = new Controller(
                new AgentPerceptsBuilder(
                        new VisionPerceptsBuilder(),
                        new SoundPerceptsBuilder(),
                        new SmellPreceptsBuilder(),
                        new AreaPerceptsBuilder(),
                        new ScenarioPerceptsBuilder()
                ),
            false
        );

        WorldState worldState = new WorldState(
                test,
                new AgentsFactories(),
                "random",
                "guard"
        );


        List<Quadrilateral> walls = test.getWalls().getAll();
        walls.forEach(w -> root.getChildren().add(w.getWallGui()));

        List<Quadrilateral> doors = test.getDoors().getAll();
        doors.forEach(w -> root.getChildren().add(w.getDoorGui()));

        List<Quadrilateral> sentry = test.getSentryTowers().getAll();
        sentry.forEach(w -> root.getChildren().add(w.getSentryGui()));

        List<Quadrilateral> shadedArea = test.getShadedAreas().getAll();
        shadedArea.forEach(w -> root.getChildren().add(w.getShadedAreaGui()));


        root.getChildren().add(test.getSpawnAreaGuards().getSpawnGui());
        root.getChildren().add(test.getSpawnAreaIntruders().getSpawnGui());


        root.getChildren().add(test.getTargetArea().getTargetGui());

        List<Quadrilateral> window = test.getSentryTowers().getAll();
        window.forEach(w -> root.getChildren().add(w.getWindowGui()));

        worldState.getGuardStates().forEach(w -> movingObjects.getChildren().add(w.getAgentGui()));

        worldState.getIntruderStates().forEach(w -> movingObjects.getChildren().add(w.getAgentGui()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            controller.executeTurn(worldState);
                            movingObjects.getChildren().clear();
                            worldState.getGuardStates().forEach(w -> movingObjects.getChildren().add(w.getAgentGui()));

                            worldState.getIntruderStates().forEach(w -> movingObjects.getChildren().add(w.getAgentGui()));
                        }
                    });
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();



    }
}
