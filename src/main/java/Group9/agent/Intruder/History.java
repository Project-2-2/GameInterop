package Group9.agent.Intruder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class History {
    Map<Integer, Cell> history = new HashMap<>();

    public void addCell(Coordinate p, Cell cell){
        int store = (int)p.getX() * 10000 +(int) p.getY();
        history.put(store, cell);
    }

    public Cell getCell(Coordinate p){
        int location = (int)p.getX() * 10000 +(int) p.getY();
        return history.get(location);
    }
    public int getCellCount(Coordinate p){
        int location = (int)p.getX() * 10000 +(int) p.getY();
        return history.get(location).getCount();
    }

    public void clearHistory(Coordinate p){
        int location = (int)p.getX() * 10000 +(int) p.getY();
        history.remove(location);
    }
    public void addAll(LinkedList<Cell> cells)
    {
        for (Cell c: cells)
        {
            Coordinate location = new Coordinate(c.getMidX(), c.getMidY());
            this.addCell(location, c);
        }
    }
}
