package Group9.experiments;

import Group9.Game;
import Group9.agent.factories.DefaultAgentFactory;
import Group9.map.parser.Parser;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Sample {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        AtomicInteger intruders = new AtomicInteger(0);
        AtomicInteger guards = new AtomicInteger();

        final int N = 1000;

        CountDownLatch countDownLatch = new CountDownLatch(N);

        for(int i = 0; i < N; i++)
        {
            executorService.submit(() -> {
                Game game = new Game(Parser.parseFile("./src/main/java/Group9/map/maps/test_2.map"), new DefaultAgentFactory(),
                        false);
                game.run();

                if(game.getWinner() == Game.Team.INTRUDERS) intruders.getAndIncrement();
                if(game.getWinner() == Game.Team.GUARDS) guards.getAndIncrement();

                System.out.println(intruders.get() + " <-> " + guards.get());
                countDownLatch.countDown();
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
