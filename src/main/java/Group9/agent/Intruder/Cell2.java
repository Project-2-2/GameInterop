package Group9.agent.Intruder;

public class Cell2 {
    public static int numOfCells;
    private int visitedCount;

    private boolean wall = false;
    private boolean window = false;
    private boolean door = false;
    private boolean guard = false;
    private boolean sentryTower = false;
    private boolean target = false;
    private boolean teleport = false;

    public Cell2(){
        numOfCells++;
        visitedCount = 0;
    }

    public int getCount(){
        return visitedCount;
    }

    public boolean hasWall(){
        return wall;
    }
    public boolean hasDoor(){
        return door;
    }

    public boolean hasWindow(){
        return window;
    }
    public boolean hasTower(){
        return sentryTower;
    }
    public boolean hasGuard(){
        return guard;
    }
    public boolean hasTarget(){
        return target;
    }

    public boolean hasTeleport(){
        return teleport;
    }
}

}
