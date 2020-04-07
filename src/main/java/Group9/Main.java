package Group9;

import Group9.map.GameMap;
import Group9.map.parser.Parser;
import Group9.math.Vector2;
import Group9.math.graph.Graph;
import Group9.math.graph.Vertex;
import Group9.tree.PointContainer;


public class Main {

    public static void main(String[] args) {

        // ,,,
        PointContainer.Polygon wall = new PointContainer.Polygon(
                new Vector2(60.0,12.0), new Vector2(60.0,36.0), new Vector2(61.0,36.0),
                new Vector2(61.0,12.0)
        );


        PointContainer.Polygon move = new PointContainer.Polygon(
                new Vector2(59.43349636341895,14.333224990404418),
                new Vector2(61.318334753821354, 14.093674907821876),
                new Vector2(61.06617677215552, 12.109634496871985),
                new Vector2(59.18133838175312, 12.349184579454526)
        );
        //System.out.println(PointContainer.intersect(move, wall));

        PointContainer.Line a = new PointContainer.Line(new Vector2(59.43349636341895,14.333224990404418), new Vector2(61.318334753821354, 14.093674907821876));
        PointContainer.Line b = new PointContainer.Line(new Vector2(60.0,12.0), new Vector2(60.0,36.0));

        PointContainer.intersect(a, b);
        System.out.println(PointContainer.intersect(a, b));


    }


}
