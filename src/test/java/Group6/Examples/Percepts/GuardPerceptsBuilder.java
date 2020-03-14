package Group6.Examples.Percepts;

import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.AreaPercepts;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Scenario.*;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Smell.SmellPercepts;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Sound.SoundPercepts;
import Interop.Percept.Vision.*;

import java.util.Arrays;
import java.util.HashSet;

public class GuardPerceptsBuilder {

    public static GuardPercepts getSimple(boolean wasLastActionExecuted) {
        return new GuardPercepts(
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
            new ScenarioGuardPercepts(
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
                new Distance(3)
            ),
            wasLastActionExecuted
        );
    }

}
