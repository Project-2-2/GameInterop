package Group9.agent.odyssey;

import Group9.agent.Intruder.Cell;
import Group9.math.Vector2;

import java.util.List;

public class Odyssey {

    public static void main(String[] args) {
        GridMap gridMap = new GridMap(1, 10, 10);
        gridMap.set(-1, 5, (short) 1);
        gridMap.set(-2, 5, (short) 1);
        gridMap.set(-3, 5, (short) 1);
        gridMap.set(-4, 5, (short) 1);
        gridMap.set(-5, 5, (short) 1);
        gridMap.set(0, 5, (short) 1);
        gridMap.set(1, 5, (short) 1);
        gridMap.set(2, 5, (short) 1);
        gridMap.set(3, 5, (short) 1);
        gridMap.set(4, 5, (short) 1);
        gridMap.set(5, 5, (short) 1);
        gridMap.set(0, 12, (short) 1);

        gridMap.set(-1, -3, (short) 1);
        gridMap.set(-2, -3, (short) 1);
        gridMap.set(-3, -3, (short) 1);
        gridMap.set(-4, -3, (short) 1);
        gridMap.set(-5, -3, (short) 1);
        gridMap.set(0, -3, (short) 1);
        gridMap.set(1, -3, (short) 1);
        gridMap.set(2, -3, (short) 1);
        gridMap.set(3, -3, (short) 1);
        gridMap.set(4, -3, (short) 1);
        gridMap.set(5, -3, (short) 1);

        System.out.println(gridMap);
        List<GridMap.Cell> path = gridMap.path(new Vector2(-2,-5), new Vector2(0, 10));
        for(GridMap.Cell move : path) {
            gridMap.map[gridMap.map.length- move.y()][move.x()] = 2;
        }

        System.out.println(gridMap);

    }

}
