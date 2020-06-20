package Interop.Competition;

import Group9.Callback;
import Group9.Game;
import Group9.agent.factories.IAgentFactory;
import Group9.map.GameMap;
import Interop.Competition.Agent.CompetitionIAgentsFactory;

public class LimitedRoundsGame {

    private final Game game;
    private final LimitRounds limitRoundsCounter;

    public static class LimitRounds implements Callback<Game> {
        private int counter;
        private final int maxRounds;
        public LimitRounds(int maxRounds) {
            this.maxRounds = maxRounds;
        }
        public void call(Game v) {
            counter++;
            if(maxRounds <= counter) v.getRunningLoop().set(false);
        }
        public int getCounter() {
            return counter;
        }
    }

    public LimitedRoundsGame(GameMap gameMap, IAgentFactory agentsFactory, int maxRounds) {

        game = new Game(
            gameMap,
            agentsFactory,
            false,
            -1,
            limitRoundsCounter = new LimitRounds(maxRounds)
        );

    }

    public Game.Team getWinner() {
        return getGame().getWinner();
    }

    public int getCurrentRoundsCount() {
        return getLimitRoundsCounter().getCounter();
    }

    public void play() {
        getGame().run();
    }

    public Game getGame() {
        return game;
    }

    public LimitRounds getLimitRoundsCounter() {
        return limitRoundsCounter;
    }

}
