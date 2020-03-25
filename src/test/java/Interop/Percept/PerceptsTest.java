package Interop.Percept;

import Interop.Geometry.*;
import Interop.Percept.Scenario.GameMode;
import Interop.Percept.Scenario.ScenarioIntruderPercepts;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Smell.SmellPercepts;
import Interop.Percept.Sound.*;
import Interop.Percept.Vision.*;
import Interop.Percept.Vision.VisionPerceptsTest;
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
                new SoundPercepts(new HashSet<>(Arrays.asList(
                    new SoundPercept(
                        SoundPerceptType.Noise,
                        Direction.fromClockAngle(new Point(1, 1))
                    ),
                    new SoundPercept(
                        SoundPerceptType.Yell,
                        Direction.fromClockAngle(new Point(2, 2))
                    )
                ))),
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
                new AreaPercepts(
                    false,
                    false,
                    false,
                    true
                ),
                new ScenarioIntruderPercepts(
                    new ScenarioPercepts(
                        GameMode.CaptureOneIntruder,
                        new Distance(2),
                        Angle.fromDegrees(45),
                        new SlowDownModifiers(
                            0.5,
                            0.8,
                            0.1
                        ),
                        new Distance(10),
                        3
                    ),
                    3,
                    new Distance(1),
                    new Distance(3),
                    3
                ),
                true
            );

            assertInstanceOf(percepts, IntruderPercepts.class);
            assertInstanceOf(percepts.getTargetDirection(), Direction.class);
            assertInstanceOf(percepts.getVision(), VisionPrecepts.class);
            assertInstanceOf(percepts.getSounds(), SoundPercepts.class);
            assertInstanceOf(percepts.getSmells(), SmellPercepts.class);

        });

    }

}
