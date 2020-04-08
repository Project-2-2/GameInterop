package Group9;

import Group9.math.Vector2;


public class Main {

    public static void main(String[] args) {

        final Vector2 direction = new Vector2(0, -1).sub(new Vector2.Origin()).normalise();

        System.out.println(direction);
        final double angle = direction.angle(new Vector2(-1, 0).normalise());
        System.out.println(angle);

    }


}
