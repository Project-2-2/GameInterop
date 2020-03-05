package Group9;

import Group9.math.Vector2;
import Group9.tree.Node;
import Group9.tree.PointContainer;

public class Main {

    public static void main(String[] args) {
        Node node = new Node(new Vector2(0, 0), 300, 300, 1);
        node.add(new TestObject(10, 10));
        node.add(new TestObject(-10, 10));
        node.add(new TestObject(-10, 8));

        node.toString();
    }

    public static class TestObject implements PointContainer.Circle {

        private double x, y;

        public TestObject(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public Vector2 getCenter() {
            return new Vector2(x, y);
        }

        @Override
        public double getRadius() {
            return 0;
        }
    }

}
