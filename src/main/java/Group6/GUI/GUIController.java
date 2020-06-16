package Group6.GUI;

import Group6.WorldState.Object.AgentState;
import Group6.WorldState.Object.GuardState;
import Group6.WorldState.Object.IntruderState;
import Group6.WorldState.WorldState;

import java.util.ArrayList;
import java.util.List;

public  class GUIController {
    private ArrayList<Intruder> intruders = new ArrayList<>();
    private ArrayList<Guard> guards = new ArrayList<>();


    public void update(WorldState worldState){
            buildComponents(worldState);


    }

    private void buildComponents(WorldState worldState) {
        buildGuards(worldState.getGuardStates());
        buildIntruders(worldState.getIntruderStates());

    }

    private void buildGuards(List<GuardState> guardStateList) {
        for(int i=0; i<guardStateList.size(); i++){
            GuardState guardState = guardStateList.get(i);
            guards.add(new Guard(guardState));
        }
    }

    private void buildIntruders(List<IntruderState> intruderStateList){
        for(int i=0; i<intruderStateList.size(); i++){
            IntruderState intruderState = intruderStateList.get(i);
            intruders.add(new Intruder(intruderState));
        }
    }



}
