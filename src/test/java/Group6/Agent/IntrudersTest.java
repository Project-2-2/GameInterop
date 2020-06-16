package Group6.Agent;

import Group6.Agent.Intruder.RandomIntruderTest;
import Group6.Agent.Intruder.WalkAroundWallIntruderTest;
import Group6.ExtendedUnitTest;

/**
 * @author Tomasz Darmetko
 */
public class IntrudersTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nIntruders Test\n");

        RandomIntruderTest.main(args);
        WalkAroundWallIntruderTest.main(args);

    }

}
