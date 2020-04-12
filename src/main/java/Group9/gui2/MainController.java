package Group9.gui2;

import Group9.Callback;
import Group9.Game;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainController implements Runnable {

    private final Gui gui;
    private final Game game;

    private AtomicInteger historyViewIndex = new AtomicInteger(-1);

    private int historyIndex = -1;
    private final List<History> history = new LinkedList<>();

    private AnimationTimer animator;
    public MainController(Gui gui, File mapFile){
        this.gui = gui;
        game = new Game(Parser.parseFile(mapFile.getAbsolutePath()), new DefaultAgentFactory(), false, -1, new Callback<Game>() {
            @Override
            public void call(Game game) {
                synchronized (history)
                {
                    historyIndex++;
                    History entry = new History();
                    history.add(historyIndex, entry);

                    entry.guardContainers = game.getGuards().stream().map(e -> e.clone(game)).collect(Collectors.toList());
                    entry.intruderContainers = game.getIntruders().stream().map(e -> e.clone(game)).collect(Collectors.toList());


                    entry.dynamicObjects = game.getGameMap().getDynamicObjects().stream()
                            .map(DynamicObject::clone)
                            .collect(Collectors.toList());

                }
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
        animator = new AnimationTimer(){
            @Override
            public void handle(long now){
                if(game.getWinner()!=null){
                    gui.activateHistory();
                }
                synchronized (history)
                {
                    if(!history.isEmpty())
                    {
                        int index = historyViewIndex.get() == -1 ? (historyIndex == history.size() ? historyIndex - 1 : historyIndex) :
                                (historyViewIndex.get() == history.size() ? historyViewIndex.get() - 1 : historyViewIndex.get());
                        History entry = history.get(index).clone();
                        gui.drawMovables(entry.guardContainers, entry.intruderContainers, entry.dynamicObjects);
                    }
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
    public void kill(){
        game.getRunningLoop().set(false);
        if(animator!=null){
            animator.stop();
        }
    }
    public void updateGameSpeed(int gameSpeed){
        game.getTicks().set(gameSpeed);
    }

    public AtomicInteger getHistoryViewIndex() {
        return historyViewIndex;
    }

    public int getHistoryIndex() {
        return historyIndex;
    }
}