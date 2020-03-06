package Group9;

import Group9.map.Map;
import Group9.map.parser.Parser;
import Group9.math.Vector2;
import Group9.tree.PointContainer;

public class Main {

    public static void main(String[] args) {

        Map map = Parser.parseFile("./src/main/java/Group9/map/maps/test.map");
        map.toString();

        //\left(-60,-40\right),\left(-60,-39\right),\left(60,-39\right),\left(60,-40\right)

        /*System.out.println(PointContainer.intersect(
            new PointContainer.Quadrilateral(
                new Vector2(-60, -40), new Vector2(-60, -39), new Vector2(60, -39), new Vector2(60, -40)
            ),
            //\left(-55,-39.5\right),\left(-53,-39.5\right),\left(-55,-37.5\right),\left(-53,-37.5\right)
            new PointContainer.Quadrilateral(
                new Vector2(-55, -39.5), new Vector2(-55, -37.5), new Vector2(-53, -37.5), new Vector2(-53, -39.5)
            )

        ));
            /*new PointContainer.Quadrilateral(
                    new Vector2(-4, -28), new Vector2(0, -28), new Vector2(0, -29), new Vector2(-4, -29)
            )*/
        //TODO limit tree depth
        //TODO check whether or not the order of the points matter

    }


}
