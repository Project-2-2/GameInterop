package Group6.Agent.Factory;

import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import java.util.List;

/**
 * @author Tomasz Darmetko
 */
public interface AgentFactory {
    public List<Intruder> createIntruders(int number);
    public List<Guard> createGuards(int number);
}
