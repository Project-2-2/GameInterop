package Interop.Competition.Agent;

import Group9.agent.factories.IAgentFactory;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import Interop.Competition.Console;
import Interop.Utils.Require;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tomasz Darmetko
 */
public class CompetitionIAgentsFactory implements IAgentFactory {

    private final Class<?> intrudersFactoryClass;
    private final Class<?> guardsFactoryClass;

    public CompetitionIAgentsFactory(Class<?> intrudersFactoryClass, Class<?> guardsFactoryClass) {
        this.intrudersFactoryClass = intrudersFactoryClass;
        this.guardsFactoryClass = guardsFactoryClass;
    }

    public List<Intruder> createIntruders(int amount) {
        return decorateIntrudersWithCatchException(Console.runSilent(this::loudCreateIntruders, amount));
    }

    public List<Guard> createGuards(int amount) {
        return decorateGuardsWithCatchException(Console.runSilent(this::loudCreateGuards, amount));
    }

    public List<Intruder> loudCreateIntruders(int amount) {
        try {

            Method createMethod = intrudersFactoryClass.getMethod("createIntruders", int.class);

            @SuppressWarnings("unchecked")
            List<Intruder> agents = (List<Intruder>) createMethod.invoke(null, amount);

            Require.notNull(agents);

            return agents;

        } catch (Exception e)  {
            throw new RuntimeException(e);
        }
    }

    public List<Guard> loudCreateGuards(int amount) {
        try {

            Method createMethod = guardsFactoryClass.getMethod("createGuards", int.class);

            @SuppressWarnings("unchecked")
            List<Guard> agents = (List<Guard>) createMethod.invoke(null, amount);

            Require.notNull(agents);

            return agents;

        } catch (Exception e)  {
            throw new RuntimeException(e);
        }
    }

    public List<Intruder> decorateIntrudersWithCatchException(List<Intruder> agents) {
        return agents.stream().map(CatchExceptionIntruder::new).collect(Collectors.toList());
    }

    public List<Guard> decorateGuardsWithCatchException(List<Guard> agents) {
        return agents.stream().map(CatchExceptionGuard::new).collect(Collectors.toList());
    }

}