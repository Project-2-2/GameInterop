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

        for (Pheromone pheromone : guardPheromones) {
            pheromone.setTurnsLeft(pheromone.getTurnsLeft()-1);

            if (pheromone.getTurnsLeft() <= 0)
            { guardPheromones.remove(pheromone); }
        }

        for (Pheromone pheromone : intruderPheromones) {
            pheromone.setTurnsLeft(pheromone.getTurnsLeft()-1);

            if (pheromone.getTurnsLeft() <= 0)
            { intruderPheromones.remove(pheromone); }
        }
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
