package Group6;

import Group6.Agent.Behaviour.*;
import Group6.Agent.Guard.BehaviourBasedGuard;
import Group6.Agent.Intruder.BehaviourBasedIntruder;
import Group9.Callback;
import Group9.Game;
import Group9.agent.factories.IAgentFactory;
import Group9.map.parser.Parser;
import Interop.Action.GuardAction;
import Interop.Action.IntruderAction;
import Interop.Action.NoAction;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Experiments {

    public static class LimitRounds implements Callback<Game> {
        private int counter;
        private final int maxRounds;
        public LimitRounds(int maxRounds) {
            this.maxRounds = maxRounds;
        }
        public void call(Game v) {
            counter++;
            if(maxRounds < counter) v.getRunningLoop().set(false);
        }
        public int getCounter() {
            return counter;
        }
    }

    public static void main(String[] args) {

        int guardsWin = 0;
        int intrudersWin = 0;
        int draw = 0;

        int totalRounds = 0;
        for (int i = 0; i < 10; i++) {

            LimitRounds limitRounds =  new LimitRounds(5000);
            Game game = getGame(limitRounds);
            game.run();

            totalRounds += limitRounds.getCounter();

            System.out.println(limitRounds.getCounter());

            if(game.getWinner() == null) {
                draw++;
            } else {
                switch (game.getWinner()) {
                    case GUARDS:
                        guardsWin++;
                        break;
                    case INTRUDERS:
                        intrudersWin++;
                        break;
                }
            }

            System.out.println("I: \t" + intrudersWin + "\tG: " + guardsWin + "\tD: " + draw);

        }

        System.out.println("Total Rounds: " + totalRounds);

    }

    private static Game getGame(Callback<Game> callback) {
        return new Game(
                Parser.parseFile("./src/main/java/Group6/maps/spiral.map"),
                AgentsFactory.getIAgentFactoryInstance(),
                false,
                -1,
                callback
            );
    }
}
