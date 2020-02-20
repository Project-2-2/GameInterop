import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import Interop.InteropTests;
import Interop.Percept.PerceptsTest;
import Interop.Utils.UtilsTest;
import SimpleUnitTest.SimpleUnitTest;
import javafx.scene.Group;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Tomasz Darmetko
 */
public class AllTests extends SimpleUnitTest {

    public static void main(String[] args) {
        InteropTests.main(args);
        testAgentFactories();
    }

    private static void testAgentFactories() {
        System.out.println("\n\nAgentsFactory:\n");
        testAgentsFactory(Group1.AgentsFactory.class);
        testAgentsFactory(Group2.AgentsFactory.class);
        testAgentsFactory(Group3.AgentsFactory.class);
        testAgentsFactory(Group4.AgentsFactory.class);
        testAgentsFactory(Group5.AgentsFactory.class);
        testAgentsFactory(Group6.AgentsFactory.class);
        testAgentsFactory(Group7.AgentsFactory.class);
        testAgentsFactory(Group8.AgentsFactory.class);
        testAgentsFactory(Group9.AgentsFactory.class);
        testAgentsFactory(Group10.AgentsFactory.class);
        testAgentsFactory(Group11.AgentsFactory.class);
    }

    @SuppressWarnings("rawtypes")
    public static void testAgentsFactory(Class factoryClass) {

        String groupName = factoryClass.getPackage().getName();
        it("allows to build agents of " + groupName, () -> {

            boolean executed = false;
            String potentialExceptionMessage = "";

            try {

                Method createIntrudersMethod = factoryClass.getMethod("createIntruders", int.class);
                Method createGuardsMethod = factoryClass.getMethod("createGuards", int.class);
                List<Intruder> intruders = (List<Intruder>)createIntrudersMethod.invoke(null, 0);
                List<Guard> guards = (List<Guard>)createGuardsMethod.invoke(null, 0);

                executed = true;

            } catch (Exception e) {
                // Please, review the details yourself.
                potentialExceptionMessage = e.getMessage();
            }

            assertTrue(executed, "Building of agents failed!\n\n" + potentialExceptionMessage);

        });

    }

}
