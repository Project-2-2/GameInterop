package Group11.View;

import Group11.AgentsFactory;
import Group11.CoolAgentFactory;
import Group9.Game;
import Group9.agent.factories.DefaultAgentFactory;
import Group9.map.parser.Parser;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ExperimentRunner {
     static class DoExperiment implements Runnable{
        public int winsGuard = 0;
        public int winsIntruder = 0;
        private int n;
        private CountDownLatch latch;
        public DoExperiment(int n, CountDownLatch latch){
            this.n = n;
            this.latch = latch;
        }
        @Override
        public void run() {
            for (int i = 0; i < n; i++) {
                Game game = new Game(Parser.parseFile("./src/main/java/Group11/MapFiles/map2.map"), new CoolAgentFactory(), false);
                game.run();
                if (game.getWinner().equals(Game.Team.GUARDS)) {
                    winsGuard++;
                } else {
                    winsIntruder++;
                }
                double percent = ((double) i / n) * 100;
                if ((int)percent % 10 == 0) {
                    System.out.println(percent + "%");
                }
            }
            latch.countDown();
        }
    }
    public static void main(String[] args) {
        int threads = 10;
        int n = 100;

        if(n % threads != 0){
            System.out.println("Invalid!!, n%threads must be 0 because im lazy");
            return;
        }
        CountDownLatch latch = new CountDownLatch(threads);
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        ArrayList<DoExperiment> experiments = new ArrayList<>();
        for(int i=0;i<threads;i++){
            DoExperiment experiment = new DoExperiment(n/threads, latch);
            experiments.add(experiment);
            executorService.submit(experiment);
        }
        try {
            latch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
        double intruderWin = 0;
        double guardWin = 0;
        for(DoExperiment experiment : experiments){
            intruderWin += experiment.winsIntruder;
            guardWin += experiment.winsGuard;
        }
        intruderWin = intruderWin/n;
        guardWin = guardWin/n;
        System.out.println("Intruders: " + intruderWin);
        System.out.println("Guards: " + guardWin);


    }
}
