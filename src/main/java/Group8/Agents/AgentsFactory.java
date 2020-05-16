package Interop.Agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class provides common way to build agents for the competition.
 *
 * Sharing knowledge between agents is NOT ALLOWED.
 *
 * For example:
 * Agents must not hold ANY references to common objects or references to each other.
 */
public class AgentsFactory {

    public enum Algo {
        AI1,
        AI2;
    }

    static public List<Intruder> createIntruders(int number, Algo whichAlgo) {
        List<Intruder> intruders = new ArrayList<>();

        for(int i=0; i<number; i++){
            switch(whichAlgo) {
                case (AI1):
                    //intruders.add(new IntruAlgo1());
                    break;
                case (AI2):
                    //intruders.add(new IntruAlgo2());
                    break;
            }
        }

        return intruders;
    }

    static public List<Guard> createGuards(int number, Algo whichAlgo) {
        List<Guard> guards = new ArrayList<>();

        for(int i=0; i<number; i++){
            switch(whichAlgo) {
                case (AI1):
                    //guards.add(new GuardAlgo1());
                    break;
                case (AI2):
                    //guards.add(new GuardAlgo2());
                    break;
            }
        }

        return guards;
    }

}