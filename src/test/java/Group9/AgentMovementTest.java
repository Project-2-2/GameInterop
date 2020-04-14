package Group9;

import Group9.agent.container.GuardContainer;
import Group9.map.parser.Parser;
import Group9.math.Vector2;
import SimpleUnitTest.SimpleUnitTest;

public class AgentMovementTest extends SimpleUnitTest {

    public static void main(String[] args) {
        it("Game::<check_if_agent_gets_stuck>", () -> {
            Game game = new Game(Parser.parseFile("./src/test/java/Group9/movement_test_map.map"), false);

            GuardContainer guard = game.getGuards().get(0);
            Vector2 lastPosition = guard.getPosition().clone();

            final int STUCK_LIMIT = 100;

            int max = 0;
            int counter = 0;
            int step = 0;

            long time = System.currentTimeMillis();
            for(; step < 100_000; step++) {
                game.turn();

                if (lastPosition.distance(guard.getPosition()) < 1E-1) {
                    counter++;

                    if(counter >= STUCK_LIMIT)
                    {
                        max = counter;
                        break;
                    }
                }
                else
                {
                    counter = 0;
                }

                lastPosition = guard.getPosition().clone();
            }
            System.out.println("time: " + (System.currentTimeMillis() - time));

            assertTrue(max < STUCK_LIMIT, String.format("Agent got stuck after %d steps. Check seed to reproduce. " +
                    "Seed: %d", step, Game._RANDOM_SEED));

        });
    }



}
