package Group6;

import Group6.Agent.AgentsTests;
import Group6.Controller.ControllerTest;
import Group6.Geometry.GeometryTest;
import Group6.Percept.Vision.RaysTest;
import Group6.WorldState.CollisionTest;
import Group6.WorldState.WorldStateTest;
import SimpleUnitTest.SimpleUnitTest;

import java.nio.file.Paths;

/**
 * @author Tomasz Darmetko
 */
public class GroupTests extends SimpleUnitTest {

    public final static String resources = Paths
        .get("")
        .toAbsolutePath()
        .toString()
        .concat("/src/test/java/Group6/Resources");

    public static void main(String[] args) {
        GeometryTest.main(args);
        WorldStateTest.main(args);
        AgentsTests.main(args);
        RaysTest.main(args);
        CollisionTest.main(args);
        ControllerTest.main(args);
    }

}
