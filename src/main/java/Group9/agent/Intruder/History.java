package Group9.agent.Intruder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class History {
    private Map<Integer, Cell> history = new HashMap<>();

    public void addCell(Cell cell){


        history.putIfAbsent(makeKey(cell.getMidX(), cell.getMidY()), cell);
    }
    public int makeKey(double x, double y)
    {
        int firstPart;
        int secondPart;
        if(x < 0)
        {
            firstPart = (int)Math.abs(x) * 10000 + 200000;
        }
        else
        {
            firstPart = (int) x * 10000 + 100000;
        }
        if(y < 0)
        {
            secondPart = (int)Math.abs(y) + 2000;
        }
        else
        {
            secondPart = (int)y + 1000;
        }
       return firstPart + secondPart;
    }

    public Cell getCell(Coordinate p){
        int location = makeKey(p.getX(),p.getY());
        return history.get(location);
    }
    public Cell getCell(double x, double y)
    {
        int location = makeKey(x,y);
        return history.get(location);
    }
    public int getCellCount(Coordinate p){
        int location = makeKey(p.getX(), p.getY());
        return history.get(location).getCount();
    }

    public void clearHistory(Coordinate p){
        int location = makeKey(p.getX(), p.getY());
        history.remove(location);
    }
    public void addAll(LinkedList<Cell> cells)
    {
        for (Cell c: cells)
        {
            this.addCell(c);
        }
    }
    public int size()
    {
        return history.size();
    }
    public void setUnProcessed()
    {
        Set<Map.Entry<Integer, Cell>> allCells = history.entrySet();
        allCells.forEach(c -> c.getValue().deProcess());
    }
    public void printall()
    {
        Set<Map.Entry<Integer, Cell>> allCells = history.entrySet();
        allCells.forEach(c -> System.out.println("(key, value) " + c.getKey() + ", " + c.getValue()));
    }
}
