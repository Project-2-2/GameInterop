package Percepts;

import Geometry.*;
import Percept.*;
import Percept.Smell.SmellPercept;
import Percept.Smell.SmellPerceptType;
import Percept.Smell.SmellPercepts;
import Percept.Sound.*;
import Percept.Vision.*;
import Percepts.Vision.VisionPerceptsTest;
import SimpleUnitTest.*;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Tomasz Darmetko
 */
public class PerceptsTest extends SimpleUnitTest {

    public static void main(String[] args) {

        VisionPerceptsTest.main(args);

        System.out.println("\n\nPercepts Test\n");

        it("allows to create intruder percepts", () -> {

            IntruderPercepts percepts = new IntruderPercepts(
                Direction.fromDegrees(90),
                new VisionPrecepts(
                    new FieldOfView(
                        new Distance(7),
                        Angle.fromDegrees(45)
                    ),
                    new ObjectPercepts(new HashSet<>(Arrays.asList(
                        new ObjectPercept(
                            ObjectPerceptType.Guard,
                            new Point(-1, 5)
                        ),
                        new ObjectPercept(
                            ObjectPerceptType.Wall,
                            new Point(1, 5)
                        )
                    )))
                ),
                new SoundPercepts(),
                new SmellPercepts(new HashSet<>(Arrays.asList(
                    new SmellPercept(
                        SmellPerceptType.Pheromone1,
                        new Distance(1)
                    ),
                    new SmellPercept(
                        SmellPerceptType.Pheromone2,
                        new Distance(2)
                    )
                ))),
                true
            );

            assertInstanceOf(percepts, IntruderPercepts.class);
            assertInstanceOf(percepts.getTargetDirection(), Direction.class);
            assertInstanceOf(percepts.getVision(), VisionPrecepts.class);
            assertInstanceOf(percepts.getSounds(), SoundPercepts.class);
            assertInstanceOf(percepts.getSmells(), SmellPercepts.class);

        });

        it("allows to create empty guard percepts", () -> {

            GuardPercepts percepts = EmptyPercepts.createEmptyForGuard(true);

            assertInstanceOf(percepts, GuardPercepts.class);
            assertInstanceOf(percepts.getVision(), VisionPrecepts.class);
            assertInstanceOf(percepts.getSounds(), SoundPercepts.class);
            assertInstanceOf(percepts.getSmells(), SmellPercepts.class);

        });

        it("allows to create empty intruder percepts", () -> {

            IntruderPercepts percepts = EmptyPercepts.createEmptyForIntruder(true);

            assertInstanceOf(percepts, IntruderPercepts.class);
            assertInstanceOf(percepts.getVision(), VisionPrecepts.class);
            assertInstanceOf(percepts.getSounds(), SoundPercepts.class);
            assertInstanceOf(percepts.getSmells(), SmellPercepts.class);

        });

    }

}
