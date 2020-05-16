package Group8.Agents;

import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.List;

public interface IAgentFactory {
    List<Intruder> createIntruders(int num);
    List<Guard> createGuards(int num);
}
