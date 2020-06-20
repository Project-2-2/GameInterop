package Interop.Competition.Agent;

import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tomasz Darmetko
 */
public class SittingDuckAgentsFactory {
    static public List<Intruder> createIntruders(int amount) {
        List<Intruder> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            list.add(new SittingDuckAgent());
        }
        return list;
    }

    static public List<Guard> createGuards(int amount) {
        List<Guard> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            list.add(new SittingDuckAgent());
        }
        return list;
    }
}
