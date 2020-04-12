package Group9.gui2;

import Group9.Callback;
import Group9.Game;
import Group9.agent.container.AgentContainer;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.agent.factories.DefaultAgentFactory;
import Group9.map.dynamic.DynamicObject;
import Group9.map.parser.Parser;
import javafx.animation.AnimationTimer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MainController implements Runnable {

    private final Gui gui;
    private final Game game;

    private int historyIndex = 0;
    private List<History> history = new LinkedList<>();

    public MainController(Gui gui){
        this.gui = gui;
        File file = new File("./src/main/java/Group9/map/maps/test_2.map");
        game = new Game(Parser.parseFile(file.getAbsolutePath()), new DefaultAgentFactory(), false, -1, new Callback<Game>() {
            @Override
            public void call(Game game) {
                historyIndex++;
            }
        }, new Callback<>() {
            @Override
            public void call(AgentContainer<?> agentContainer) {
                History entry = null;
                if (historyIndex == history.size()) {
                    history.add(historyIndex, entry = new History());
                } else
                {
                    entry = history.get(historyIndex);
                }

                if(agentContainer instanceof GuardContainer)
                {
                    entry.guardContainers.add((GuardContainer) agentContainer);
                }
                else
                {
                    entry.intruderContainers.add((IntruderContainer) agentContainer);
                }

                entry.dynamicObjects.addAll(
                        game.getGameMap().getDynamicObjects().stream()
                                .map(DynamicObject::clone)
                                .collect(Collectors.toList())
                );
            }
        });

        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void run() {
        AnimationTimer animator = new AnimationTimer(){
            @Override
            public void handle(long now){
                if(!history.isEmpty())
                {
                    History entry = history.get(historyIndex == history.size() ? historyIndex - 1 : historyIndex).clone();
                    gui.drawMovables(entry.guardContainers, entry.intruderContainers, entry.dynamicObjects);
                }
            }};
        animator.start();
    }

    public static class History implements Cloneable {

        public List<GuardContainer> guardContainers = new ArrayList<>();
        public List<IntruderContainer> intruderContainers = new ArrayList<>();
        public List<DynamicObject<?>> dynamicObjects = new ArrayList<>();

        public History() {}

        @Override
        protected History clone() {
            History history = new History();
            history.guardContainers.addAll(guardContainers);
            history.intruderContainers.addAll(intruderContainers);
            history.dynamicObjects.addAll(dynamicObjects);
            return history;
        }
    }

}