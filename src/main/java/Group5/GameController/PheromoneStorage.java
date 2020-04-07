package Group5.GameController;

import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPerceptType;

import java.util.ArrayList;

public class PheromoneStorage {
    private ArrayList<Pheromone> guardPheromones=new ArrayList<>();
    private ArrayList<Pheromone> intruderPheromones=new ArrayList<>();


    public ArrayList<Pheromone> getGuardPheromones() {
        return guardPheromones;
    }

    public ArrayList<Pheromone> getIntruderPheromones() {
        return intruderPheromones;
    }

    public void updatePheromones() {
        ArrayList<Pheromone> noSmell = new ArrayList<>();

        for (int i = 0; i < guardPheromones.size(); i++) {
            guardPheromones.get(i).setTurnsLeft(guardPheromones.get(i).getTurnsLeft()-1);

            if (guardPheromones.get(i).getTurnsLeft() <= 0)
            { noSmell.add(guardPheromones.get(i)); }
        }
        guardPheromones.removeAll(noSmell);
        noSmell.clear();

        for (int i = 0; i < intruderPheromones.size(); i++) {
            intruderPheromones.get(i).setTurnsLeft(intruderPheromones.get(i).getTurnsLeft()-1);

            if (intruderPheromones.get(i).getTurnsLeft() <= 0)
            { noSmell.add(intruderPheromones.get(i)); }
        }
        intruderPheromones.removeAll(noSmell);
        noSmell.clear();
    }

    public void addPheromone( Pheromone pheromone, boolean isGuard) {

        if (isGuard) {
            guardPheromones.add(pheromone);
        }
        else {
            intruderPheromones.add(pheromone);
        }

    }
}
