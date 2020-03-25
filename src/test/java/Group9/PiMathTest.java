package Group9;

import SimpleUnitTest.SimpleUnitTest;

public class PiMathTest extends SimpleUnitTest {

    public static void main(String[] args) {

        it("PiMath::clamp", () -> {
            assertEqual(PiMath.clamp(3, 1, 5), 3, 0,"Expected value 3");
            assertEqual(PiMath.clamp(-1, 1, 5), 1, 0, "Expected value 1");
            assertEqual(PiMath.clamp(6, -1, 5), 5, 0, "Expected value 5");
            assertEqual(PiMath.clamp(2, 2, 2), 2, 0, "Expected value 2");
        });

    }

}
