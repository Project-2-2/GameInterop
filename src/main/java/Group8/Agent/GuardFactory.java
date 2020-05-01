package Group8.Agent;


import Group8.Controller.G8Launcher;
import Group8.Controller.Utils.Scenario;
import Group8.Controller.Utils.GuardInfo;
import Interop.Agent.Guard;

import java.util.ArrayList;

public class GuardFactory{
    public static Algorithms algorithm = Algorithms.RANDOM;

    private static Scenario scenario = G8Launcher.Scenario;
    private static ArrayList<GuardInfo> guardInfos = scenario.getGuards();
    private static int currentGuardInfoIndex = 0;

    public enum Algorithms {
        RANDOM,RANDOM_AGENT
    }

    public static Guard createGuard(){
        switch (algorithm){
            case RANDOM:return new RandomGuard(guardInfos.get(currentGuardInfoIndex++));
            case RANDOM_AGENT:return null;
        }
        return null; //This shouldn't be here
    }

}
