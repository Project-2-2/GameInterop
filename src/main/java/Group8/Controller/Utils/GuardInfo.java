package Group8.Controller.Utils;

import Group8.Controller.G8Launcher;
import Interop.Agent.Guard;

public class GuardInfo extends AgentInfo {

    private Guard guard;
    private Scenario scenario = G8Launcher.Scenario;

    public GuardInfo(double x, double y, double r) {
        super(x, y, r, AgentType.GUARD);
    }

    public void attachGuard(Guard guard){
        this.guard = guard;
    }

    public Guard getGuard(){
        return guard;
    }



}
