package Group8.Agent;

import Group8.Controller.G8Launcher;
import Group8.Controller.Utils.Scenario;
import Group8.Controller.Utils.IntruderInfo;
import Interop.Agent.Intruder;

import java.util.ArrayList;

public class IntruderFactory {
    public static Algorithms algorithm = Algorithms.RANDOM;

    private static Scenario scenario = G8Launcher.Scenario;
    private static ArrayList<IntruderInfo> intruderInfos = scenario.getIntruders();
    private static int currentIntruderInfoIndex = 0;

    public enum Algorithms {
        RANDOM,RANDOM_AGENT
    }

    public static Intruder createIntruder(){
        switch (algorithm){
            case RANDOM:return new RandomIntruder(intruderInfos.get(currentIntruderInfoIndex++));
            case RANDOM_AGENT:return null;
        }
        return null; //This shouldn't be here
    }
}
