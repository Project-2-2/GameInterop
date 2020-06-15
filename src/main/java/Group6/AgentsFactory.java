package Group6;

import Group6.Agent.Behaviour.*;
import Group6.Agent.Guard.BehaviourBasedGuard;
import Group6.Agent.Intruder.BehaviourBasedIntruder;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    static public List<Intruder> createIntruders(int amount) {
        List<Intruder> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            list.add(new BehaviourBasedIntruder(Arrays.asList(
                new ToTargetBehaviour(),
                new ToTeleportBehaviour(),
                new ToPassageBehaviour(),
                new AvoidWallsBehaviour(),
                new DisperseBehaviour(),
                new ExploreBehaviour()
            )));
        }
        return list;
    }

    static public List<Guard> createGuards(int amount) {
        List<Guard> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            list.add(new BehaviourBasedGuard(Arrays.asList(
                new YellBehaviour(),
                new FollowIntruderBehaviour(),
                new FollowYellBehaviour(),
                new ExploreBehaviour()
            )));
        }
        return list;
    }

}