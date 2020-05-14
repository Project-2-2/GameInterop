package Group6.Agent;

import Group6.Agent.Intruder.RandomIntruderTest;
import Group6.ExtendedUnitTest;
import Interop.Geometry.Point;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Utils.Utils;

import java.util.Arrays;
import java.util.HashSet;

public class PerceptsServiceTest extends ExtendedUnitTest {
    public static void main(String[] args) {

        System.out.println("\n\nPercepts Service Test\n");

        it("gives direction of object percepts if they are symmetrical", () -> {

            ObjectPercepts percepts = new ObjectPercepts(
                new HashSet<>(Arrays.asList(
                    new ObjectPercept(ObjectPerceptType.Intruder, new Point(-1, 10)),
                    new ObjectPercept(ObjectPerceptType.Intruder, new Point( 1, 10))
                ))
            );

            assertEqual(
                PerceptsService.getMeanDirection(percepts),
                0.0,
                "Objects are symmetrically on both sides."
            );

        });

        it("gives direction of object if they are right or left", () -> {

            ObjectPercepts perceptsLeft = new ObjectPercepts(
                new HashSet<>(Arrays.asList(
                    new ObjectPercept(ObjectPerceptType.Intruder, new Point( -1, 10))
                ))
            );

            ObjectPercepts perceptsRight = new ObjectPercepts(
                new HashSet<>(Arrays.asList(
                    new ObjectPercept(ObjectPerceptType.Intruder, new Point( 1, 10))
                ))
            );

            assertEqual(
                PerceptsService.getMeanDirection(perceptsLeft),
                -5.71,
                0.01,
                "Objects are left side."
            );

            assertEqual(
                PerceptsService.getMeanDirection(perceptsRight),
                5.71,
                0.01,
                "Objects are on the right side."
            );

        });

    }
}
