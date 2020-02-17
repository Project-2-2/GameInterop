package Utils;

import SimpleUnitTest.*;
import static java.lang.Double.isNaN;

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
            assertEqual(Utils.mod(-45., -360.), 45., 0.);
        });
        it("returns -315 for (45, -360)", () -> {
            assertEqual(Utils.mod(45., -360.), -315., 0.);
        });
        it("returns 315 for (-45, 360)", () -> {
            assertEqual(Utils.mod(-45., 360.), 315., 0.);
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
    }

    static void angleClockTest() {
        System.out.println("\nUtils.angleClock(x, y):");
        it("returns 0 for (0, 1)", () -> {
            assertEqual(Utils.clockAngle(0., 1.), 0., 0.);
        });
        it("returns PI/2 for (1, 0)", () -> {
            assertEqual(Utils.clockAngle(1., 0.), Math.PI / 2, 0.);
        });
        it("returns PI for (0, -1)", () -> {
            assertEqual(Utils.clockAngle(0., -1.), Math.PI, 0.);
        });
        it("returns PI for (0, -1)", () -> {
            assertEqual(Utils.clockAngle(-1., 0.), Math.PI + Math.PI / 2, 0.);
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
                }
            }
        });
    }

}
