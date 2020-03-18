package Group6.Geometry;

import Group6.ExtendedUnitTest;

public class DirectionTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nDirection Test\n");

        it("allows to compute relative direction 0 relative to 45", () -> {

            assertEqual(
                Direction
                    .fromDegrees(0)
                    .getRelativeTo(Direction.fromDegrees(45))
                    .getDegrees(),
                45,
                "0 relative to 45"
            );

        });

        it("allows to compute relative direction 45 relative to 90", () -> {

            assertEqual(
                Direction
                    .fromDegrees(45)
                    .getRelativeTo(Direction.fromDegrees(90))
                    .getDegrees(),
                45,
                "45 relative to 90"
            );

        });

        it("allows to compute relative direction 45 relative to 180", () -> {

            assertEqual(
                Direction
                    .fromDegrees(45)
                    .getRelativeTo(Direction.fromDegrees(180))
                    .getDegrees(),
                180 - 45,
                "45 relative to 180, expected: 180 - 45"
            );

        });

        it("allows to compute relative direction 270 relative to 90", () -> {

            assertEqual(
                Direction
                    .fromDegrees(270)
                    .getRelativeTo(Direction.fromDegrees(90))
                    .getDegrees(),
                270 - 90,
                "270 relative to 90, expected 270 - 90"
            );

        });

    }

}
