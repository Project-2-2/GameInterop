package Group9.agent.Intruder;

import java.util.HashMap;

public class History {
    Map<Integer, Cell> history = new HashMap<>();

    public void addCell(int i, Cell cell){
        history.put(i, cell);
    }

    public Cell getCell(int i){
        return histroy.get(i);
    }
    public int getCellCount(int i){
        return history.get(i).getCount();
    }

    public void clearHistory(int i){
        history.remove(i);
    }
}
