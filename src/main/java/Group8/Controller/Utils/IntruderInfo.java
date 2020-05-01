package Group8.Controller.Utils;

import Interop.Agent.Intruder;

public class IntruderInfo extends AgentInfo {
    private Intruder intruder;
    public IntruderInfo(double x, double y, double r) {
        super(x, y, r,AgentType.INTRUDER);
    }
    public void attachIntruder(Intruder i){
        this.intruder = i;
    }
    public Intruder getIntruder() {
        return intruder;
    }
}
