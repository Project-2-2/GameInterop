package Group9;

import Group9.math.Vector2;
import Group9.tree.Node;
import Group9.tree.PointContainer;

public class Main {

    public static void main(String[] args) {
        Node node = new Node(new Vector2(0, 0), 300, 300, 2);
        node.add(new PointContainer.Circle(new Vector2(10, 10), 5));
        node.add(new PointContainer.Circle(new Vector2(10, 10), 40));

        //TODO limit tree depth
        //TODO check whether or not the order of the points matter

        node.toString();
    }


}
