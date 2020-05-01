package Group8.Agent;

import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
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
    static public List<Intruder> createIntruders(int number) {
        ArrayList<Intruder> intruders = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            intruders.add(IntruderFactory.createIntruder());
        }
        return intruders;
    }
    static public List<Guard> createGuards(int number) {
        ArrayList<Guard> guards = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            guards.add(GuardFactory.createGuard());
        }
        return guards;
    }
}