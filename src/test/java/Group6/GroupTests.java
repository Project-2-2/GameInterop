package Group6;

import Group6.WorldState.Scenario;
import Group6.WorldState.ScenarioTest;
import SimpleUnitTest.SimpleUnitTest;

import java.nio.file.Paths;

public class GroupTests extends SimpleUnitTest {

    public final static String resources = Paths
        .get("")
        .toAbsolutePath()
        .toString()
        .concat("/src/test/java/Group6/Resources");

    public static void main(String[] args) {
        ScenarioTest.main(args);
    }

}
