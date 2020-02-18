package Percepts.Vision;

import Geometry.Angle;
import Geometry.Distance;
import Geometry.Point;
import Percept.Vision.*;
import Percepts.PerceptsTest;
import SimpleUnitTest.SimpleUnitTest;
import Utils.UtilsTest;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Tomasz Darmetko
 */
public class VisionPerceptsTest extends SimpleUnitTest {

    public static void main(String[] args) {
        it("allows to create simple vision percepts in 360 degree field of view", () -> {

            VisionPrecepts visionPrecepts = new VisionPrecepts(
                new FieldOfView(
                    new Distance(Math.sqrt(2)),
                    Angle.fromDegrees(360)
                ),
                new ObjectPercepts(new HashSet<>(Arrays.asList(
                    new ObjectPercept(
                        ObjectPerceptType.Wall,
                        new Point(1, 1)
                    ),
                    new ObjectPercept(
                        ObjectPerceptType.Wall,
                        new Point(1, 0)
                    ),
                    new ObjectPercept(
                        ObjectPerceptType.Wall,
                        new Point(-1, 0)
                    ),
                    new ObjectPercept(
                        ObjectPerceptType.Wall,
                        new Point(0, 1)
                    ),
                    new ObjectPercept(
                        ObjectPerceptType.Wall,
                        new Point(0, -1)
                    ),
                    new ObjectPercept(
                        ObjectPerceptType.Wall,
                        new Point(-1, -1)
                    )
                )))
            );

            assertInstanceOf(visionPrecepts, VisionPrecepts.class);
            assertInstanceOf(visionPrecepts.getFieldOfView(), FieldOfView.class);
            assertInstanceOf(visionPrecepts.getObjects(), ObjectPercepts.class);

        });
        it("allows to create simple vision percepts in 180 degree field of view", () -> {

            VisionPrecepts visionPrecepts = new VisionPrecepts(
                new FieldOfView(
                    new Distance(Math.sqrt(2)),
                    Angle.fromDegrees(180)
                ),
                new ObjectPercepts(new HashSet<>(Arrays.asList(
                    new ObjectPercept(
                        ObjectPerceptType.Wall,
                        new Point(1, 1)
                    ),
                    new ObjectPercept(
                        ObjectPerceptType.Wall,
                        new Point(1, 0)
                    ),
                    new ObjectPercept(
                        ObjectPerceptType.Wall,
                        new Point(-1, 0)
                    ),
                    new ObjectPercept(
                        ObjectPerceptType.Wall,
                        new Point(0, 1)
                    )
                )))
            );

            assertInstanceOf(visionPrecepts, VisionPrecepts.class);
            assertInstanceOf(visionPrecepts.getFieldOfView(), FieldOfView.class);
            assertInstanceOf(visionPrecepts.getObjects(), ObjectPercepts.class);

        });
        it("throws exception when vision percepts are outside of the field of view", () -> {

            boolean exeptionThrown = false;
            try {
                VisionPrecepts visionPrecepts = new VisionPrecepts(
                    new FieldOfView(
                        new Distance(3),
                        Angle.fromDegrees(180)
                    ),
                    new ObjectPercepts(new HashSet<>(Arrays.asList(
                        new ObjectPercept(
                            ObjectPerceptType.Wall,
                            new Point(1, 1)
                        ),
                        new ObjectPercept(
                            ObjectPerceptType.Wall,
                            new Point(1, 0)
                        ),
                        new ObjectPercept(
                            ObjectPerceptType.Wall,
                            new Point(-1, 0)
                        ),
                        new ObjectPercept(
                            ObjectPerceptType.Wall,
                            new Point(0, 1)
                        ),
                        new ObjectPercept(
                            ObjectPerceptType.Wall,
                            new Point(0, -1)
                        ),
                        new ObjectPercept(
                            ObjectPerceptType.Wall,
                            new Point(-1, -1)
                        )
                    )))
                );
            } catch (RuntimeException e) {
                exeptionThrown = true;
            }

            assertTrue(exeptionThrown, "An exception was expected!");

        });
    }

}
