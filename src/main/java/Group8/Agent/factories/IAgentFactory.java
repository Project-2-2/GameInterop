package Group8.Agent.factories;

import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.List;

public interface IAgentFactory {

    List<Intruder> createIntruders(int amount);
    List<Guard> createGuards(int amount);

}
