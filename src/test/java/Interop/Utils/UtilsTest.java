package Interop.Utils;

import SimpleUnitTest.*;
import static java.lang.Double.isNaN;

/**
 * @author Tomasz Darmetko
 */
public class UtilsTest extends SimpleUnitTest {

    public static void main(String[] args) {
        System.out.println("\n\n\nUtils Test\n");
        modTest();
        angleClockTest();
    }

    static void modTest() {
        System.out.println("Utils.mod(x, y):");
        it("returns 45 for (45, 360)", () -> {
            assertEqual(Utils.mod(45., 360.), 45., 0.);
        });
        it("returns -45 for (-45, -360)", () -> {
            assertEqual(Utils.mod(-45., -360.), -45., 0.);
        });
        it("returns -315 for (45, -360)", () -> {
            assertEqual(Utils.mod(45., -360.), -315., 0.);
        });
        it("returns 315 for (-45, 360)", () -> {
            assertEqual(Utils.mod(-45., 360.), 315., 0.);
        });
        it("returns 0 for (-4.440892098500626E-16, Utils.TAU)", () -> {
            assertEqual(Utils.mod(-4.440892098500626E-16, Utils.TAU), 0., 0.);
        });
        it("returns 0 for (n, 1) where n is any number", () -> {
            for (int n = -10; n <= 10; n++) {
                assertEqual(Utils.mod(n, 1.), 0., 0., "n is " + n);
            }
        });
        it("returns 0 for (n, -1) where n is any number", () -> {
            for (int n = -10; n <= 10; n++) {
                assertEqual(Utils.mod(n, -1.), 0., 0., "n is " + n);
            }
        });
        it("returns NaN for (n, 0) where n is any number", () -> {
            for (int n = -10; n <= 10; n++) {
                assertTrue(isNaN(Utils.mod(n, 0.)), "n is " + n);
            }
        });
        it("returns 0 for (+/- n, +/- n) where n is any number, but 0", () -> {
            for (int n = -10; n <= 10; n++) {
                if(n == 0) continue;
                assertEqual(Utils.mod(+n, +n), 0., 0., "(+n, +n) where n is " + n);
                assertEqual(Utils.mod(+n, -n), 0., 0., "(+n, -n) where n is " + n);
                assertEqual(Utils.mod(-n, +n), 0., 0., "(-n, +n) where n is " + n);
                assertEqual(Utils.mod(-n, -n), 0., 0., "(-n, -n) where n is " + n);
            };
        });
    }

    static void angleClockTest() {
        System.out.println("\nUtils.angleClock(x, y):");
        it("returns 0/4*PI for ( 0,  1)", () -> {
            assertEqual(Utils.clockAngle(0., 1.), 0., 0.);
        });
        it("returns 1/4*PI for ( 1,  1)", () -> {
            assertEqual(Utils.clockAngle(1., 1.), 1. / 4. * Math.PI, 0.);
        });
        it("returns 2/4*PI for ( 1,  0)", () -> {
            assertEqual(Utils.clockAngle(1., 0.), 2. / 4. * Math.PI, 0.);
        });
        it("returns 3/4*PI for ( 1, -1)", () -> {
            assertEqual(Utils.clockAngle(1., -1.), 3. / 4. * Math.PI, 0.);
        });
        it("returns 4/4*PI for ( 0, -1)", () -> {
            assertEqual(Utils.clockAngle(0., -1.), Math.PI, 0.);
        });
        it("returns 5/4*PI for (-1, -1)", () -> {
            assertEqual(Utils.clockAngle(-1., -1.), 5. / 4. * Math.PI, 0.);
        });
        it("returns 6/4*PI for (-1,  0)", () -> {
            assertEqual(Utils.clockAngle(-1., 0.), 6. / 4. * Math.PI, 0.);
        });
        it("returns 7/4*PI for (-1,  1)", () -> {
            assertEqual(Utils.clockAngle(-1., 1.), 7. / 4. * Math.PI, 0.);
        });
        it("returns NaN for (0, 0)", () -> {
            assertTrue(isNaN(Utils.clockAngle(0., 0.)));
        });
        it("stays between 0 and 2*PI for any value besides (0, 0)", () -> {
            for (int i = -10; i <= 10; i++) {
                for (int j = -10; j <= 10; j++) {
                    if(i == 0 && j == 0) continue;
                    assertTrue(Utils.clockAngle(i, j) >= 0);
                    assertTrue(Utils.clockAngle(i, j) <= Math.PI * 2);
                    double length = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
                    assertEqual(
                        Utils.clockAngle(i, j), // full length vector
                        Utils.clockAngle(i / length, j / length), // unit vector
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
                        Utils.clockAngle(i, j), // full length vector
                        Utils.clockAngle(i / length, j / length), // unit vector
                        0.000000001
                    );
                    assertEqual(
                        Utils.clockAngle(i / 100., j / 100.), // scaled down vector
                        Utils.clockAngle(i / length, j / length), // unit vector
                        0.000000001
                    );
                }
            }
        });
    }

}
