package Interop.Geometry;

import Interop.Utils.Utils;
import SimpleUnitTest.SimpleUnitTest;

import static java.lang.Double.isNaN;

/**
 * @author Tomasz Darmetko
 */
public class DirectionTest extends SimpleUnitTest {

    public static void main(String[] args) {
        System.out.println("\n\n\nDirection Test\n");
        fromClockAngle();
    }

    static void fromClockAngle() {
        System.out.println("Direction.fromClockAngle(x, y):");
        it("returns 0/4*PI for ( 0,  1)", () -> {
            assertEqual(Direction.fromClockAngle(new Point(0., 1.)).getRadians(), 0., 0.);
        });
        it("returns 1/4*PI for ( 1,  1)", () -> {
            assertEqual(Direction.fromClockAngle(new Point(1., 1.)).getRadians(), 1. / 4. * Math.PI, 0.);
        });
        it("returns 2/4*PI for ( 1,  0)", () -> {
            assertEqual(Direction.fromClockAngle(new Point(1., 0.)).getRadians(), 2. / 4. * Math.PI, 0.);
        });
        it("returns 3/4*PI for ( 1, -1)", () -> {
            assertEqual(Direction.fromClockAngle(new Point(1., -1.)).getRadians(), 3. / 4. * Math.PI, 0.);
        });
        it("returns 4/4*PI for ( 0, -1)", () -> {
            assertEqual(Direction.fromClockAngle(new Point(0., -1.)).getRadians(), Math.PI, 0.);
        });
        it("returns 5/4*PI for (-1, -1)", () -> {
            assertEqual(Direction.fromClockAngle(new Point(-1., -1.)).getRadians(), 5. / 4. * Math.PI, 0.);
        });
        it("returns 6/4*PI for (-1,  0)", () -> {
            assertEqual(Direction.fromClockAngle(new Point(-1., 0.)).getRadians(), 6. / 4. * Math.PI, 0.);
        });
        it("returns 7/4*PI for (-1,  1)", () -> {
            assertEqual(Direction.fromClockAngle(new Point(-1., 1.)).getRadians(), 7. / 4. * Math.PI, 0.);
        });
        it("stays between 0 and 2*PI for any value besides (0, 0)", () -> {
            for (int i = -10; i <= 10; i++) {
                for (int j = -10; j <= 10; j++) {
                    if(i == 0 && j == 0) continue;
                    assertTrue(Direction.fromClockAngle(new Point(i, j)).getRadians() >= 0);
                    assertTrue(Direction.fromClockAngle(new Point(i, j)).getRadians() <= Math.PI * 2);
                    double length = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
                    assertEqual(
                        Direction.fromClockAngle(new Point(i, j)).getRadians(), // full length vector
                        Direction.fromClockAngle(new Point(i / length, j / length)).getRadians(), // unit vector
                        0.00001
                    );
                }
            }
        });
        it("computes equal angles for a unit vector and a different vector in the same direction", () -> {
            for (int i = -10; i <= 10; i++) {
                for (int j = -10; j <= 10; j++) {
                    if(i == 0 && j == 0) continue;
                    double length = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
                    assertEqual(
                        Direction.fromClockAngle(new Point(i, j)).getRadians(), // full length vector
                        Direction.fromClockAngle(new Point(i / length, j / length)).getRadians(), // unit vector
                        0.000000001
                    );
                    assertEqual(
                        Direction.fromClockAngle(new Point(i / 100., j / 100.)).getRadians(), // scaled down vector
                        Direction.fromClockAngle(new Point(i / length, j / length)).getRadians(), // unit vector
                        0.000000001
                    );
                }
            }
        });
        it("handles (+/- Double.MIN_VALUE or 0, +/- Double.MIN_VALUE or 0)", () -> {
            assertInstanceOf(Direction.fromClockAngle(new Point(Double.MIN_VALUE,   Double.MIN_VALUE)), Direction.class);
            assertInstanceOf(Direction.fromClockAngle(new Point(Double.MIN_VALUE,  -Double.MIN_VALUE)), Direction.class);
            assertInstanceOf(Direction.fromClockAngle(new Point(Double.MIN_VALUE,   0)), Direction.class);
            assertInstanceOf(Direction.fromClockAngle(new Point(-Double.MIN_VALUE,  Double.MIN_VALUE)), Direction.class);
            assertInstanceOf(Direction.fromClockAngle(new Point(-Double.MIN_VALUE, -Double.MIN_VALUE)), Direction.class);
            assertInstanceOf(Direction.fromClockAngle(new Point(-Double.MIN_VALUE,  0)), Direction.class);
            assertInstanceOf(Direction.fromClockAngle(new Point(0,   Double.MIN_VALUE)), Direction.class);
            assertInstanceOf(Direction.fromClockAngle(new Point(0,  -Double.MIN_VALUE)), Direction.class);
            assertInstanceOf(Direction.fromClockAngle(new Point(0,  Double.MIN_VALUE)), Direction.class);
            assertInstanceOf(Direction.fromClockAngle(new Point(0, -Double.MIN_VALUE)), Direction.class);
        });
        it("returns 0/4*PI for (-5.712759522500189E-29, 1.4921397450962104E-13)", () -> {
            assertInstanceOf(
                Direction.fromClockAngle(new Point(-5.712759522500189E-29, 1.4921397450962104E-13)),
                Direction.class
            );
        });
    }

}
