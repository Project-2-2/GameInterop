import Geometry.*;
import Percept.*;
import Percept.Sound.*;
import Percept.Vision.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PerceptsTest extends SimpleUnitTest {

    public static void main(String[] args) {

        it("allows to create percepts", () -> {

            VisionPrecepts visionPercepts = new VisionPrecepts(
                new FieldOfView(
                    new Point(-1, 1),
                    new Point(1, 1)
                ),
                new AgentPercepts(new HashSet<>(Arrays.asList(
                    new AgentPercept(AgentPerceptType.Guard, new Point(0, 0), 1)
                ))),
                new ObjectPercepts(new HashSet<>(Arrays.asList(
                    new ObjectPercept(ObjectPerceptType.Wall, new LineCurve(Arrays.asList(
                        new Point(1, 1),
                        new Point(2, 2)
                    )))
                )))
            );

            SoundPercepts soundPercepts = new SoundPercepts();

            Percepts percepts = new Percepts(visionPercepts, soundPercepts);

            assertInstanceOf(percepts, Percepts.class);
            assertInstanceOf(percepts.getVision(), VisionPrecepts.class);
            assertInstanceOf(percepts.getSounds(), SoundPercepts.class);

        });

    }

}
