package Percepts;

import Geometry.*;
import Percept.*;
import Percept.Sound.*;
import Percept.Vision.*;
import SimpleUnitTest.*;

import java.util.Arrays;
import java.util.HashSet;

public class PerceptsTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nPercepts Test\n");

        it("allows to create percepts", () -> {

            Percepts percepts = new Percepts(
                new VisionPrecepts(
                    new FieldOfView(
                        new Distance(7),
                        Angle.fromDegrees(45)
                    ),
                    new ObjectPercepts(new HashSet<>(Arrays.asList(
                        new ObjectPercept(
                            ObjectPerceptType.Guard,
                            new Point(-1, 1)
                        ),
                        new ObjectPercept(
                            ObjectPerceptType.Wall,
                            new Point(1, 1)
                        )
                    )))
                ),
                new SoundPercepts(),
                false
            );

            assertInstanceOf(percepts, Percepts.class);
            assertInstanceOf(percepts.getVision(), VisionPrecepts.class);
            assertInstanceOf(percepts.getSounds(), SoundPercepts.class);

        });

    }

}
