package Group9.agent.Intruder;

import java.util.LinkedList;

public class Cell {
    //x,y are the corners of the left top corner of a cell
    private double x;
    private double y;
    // midX,midY represent the middle of the square
    private double midX;
    private double midY;
    //The cells above, below, left, and right represent the cells that are in the indicated directions. They are null if unexplored
    private Cell above;
    private Cell below;
    private Cell left;
    private Cell right;
    private final static double size = 1;
    private double exploredPercentage;
    private boolean reachable;
    public static int numOfCells;
    private int visitedCount;
    private boolean processed;

    private int wall = 0;
    private int window = 0;
    private int door = 0;
    private int guard = 0;
    private int sentryTower = 0;
    private int target = 0;
    private int teleport = 0;
    public Cell(double x, double y)
    {
        above = null;
        below = null;
        left = null;
        right = null;
        setMidX(x);
        setMidY(y);
        numOfCells++;
        processed = false;
    }
    public Cell()
    {
        above = null;
        below = null;
        left = null;
        right = null;
    }
    public LinkedList<Cell> getUnprocessed()
    {
        LinkedList<Cell> unprocessed = new LinkedList<>();
        if (above == null)
        {
            addAbove();
        }
        if (below == null)
        {
            addBelow();
        }
        if(left == null)
        {
            addLeft();
        }
        if(right == null)
        {
            addRight();
        }
        if (!above.getProcessed())
        {
            unprocessed.add(above);
        }
        if (!below.getProcessed())
        {
            unprocessed.add(below);
        }
        if (!left.getProcessed())
        {
            unprocessed.add(below);
        }
        if (!right.getProcessed())
        {
            unprocessed.add(right);
        }
         return unprocessed;
    }
    public double getScore()
    {
        //calculate the score;
        double wallWeight = 1;
        double windowWeight = 1;
        double doorWeight = 1;
        double guardWeight = -1;
        double sentryWeight = 1;
        double targetWeight = 10000;
        double teleportWeight = 1;
        double score = wall * wallWeight + window * windowWeight + doorWeight * door + guardWeight * guard + sentryWeight * sentryTower + targetWeight * target + teleportWeight * teleport;
        return score;
    }

    /**
     * Finds the cell closest to the given coordinates
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return the cell closest to the given coordinates
     */
    public Cell find(double x, double y)
    {
        x = Math.floor(x);
        y = Math.floor(y);
        Cell result = this;
        while(result.getMidX() != x && result.getMidY() != y)
        {
            //looking for the cell with the right x coordinate
            if(x > result.getMidX())
            {
                if (result.getRight() != null)
                {
                    result = result.getRight();
                }
                else
                {
                    x = result.getX();
                }
            }
            else if(x < result.getMidX())
            {
                if (result.getLeft() != null)
                {
                    result = result.getLeft();
                }
                else
                {
                    x = result.getMidX();
                }
            }

            // looking for the cell with the right y coordinate
            if(y > result.getMidY())
            {
                if (result.getBelow() != null)
                {
                    result = result.getBelow();
                }
                else
                {
                    y = result.getY();
                }
            }
            else if(y < result.getMidY())
            {
                if (result.getAbove() != null)
                {
                    result = result.getAbove();
                }
                else
                {
                    y = result.getMidY();
                }
            }
        }
        return  result;
    }
    public boolean getProcessed()
    {
        return processed;
    }
    public void setProcessed(boolean b)
    {
        this.processed = b;
    }
    public void setAbove(Cell above) {
        this.above = above;
    }

    public void setBelow(Cell below) {
        this.below = below;
    }

    public void setLeft(Cell left) {
        this.left = left;
    }

    public void setRight(Cell right) {
        this.right = right;
    }
    public Cell addAbove()
    {
        Cell above = new Cell();
        setAbove(above);
        above.setBelow(this);
        return above;
    }
    public Cell addBelow()
    {
        Cell below = new Cell();
        setBelow(below);
        below.setAbove(this);
        return below;
    }
    public Cell addLeft()
    {
        Cell left = new Cell();
        setLeft(left);
        left.setRight(this);
        return left;
    }
    public Cell addRight()
    {
        Cell right = new Cell();
        setRight(right);
        right.setLeft(this);
        return right;
    }

    public void setMidX(double midX) {
        this.midX = midX;
        this.x = midX - .5 * size;
    }

    public void setMidY(double midY) {
        this.midY = midY;
        this.y = midY - .5 * size;
    }

    public void setX(double x) {
        this.x = x;
        this.midX = x + .5 * size;
    }

    public void setY(double y) {
        this.y = y;
        this.midY = y + .5 * size;
    }

    public Cell getAbove()
    {
        return above;
    }

    public Cell getBelow() {
        return below;
    }

    public Cell getLeft() {
        return left;
    }

    public Cell getRight() {
        return right;
    }

    public double getMidX() {
        return midX;
    }

    public double getMidY() {
        return midY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public Coordinate[] getPoints()
    {
        Coordinate[] points = {new Coordinate(midX + 0.5 * size, midY + 0.5 * size), new Coordinate(midX - 0.5 * size, midY + 0.5 * size), new Coordinate(midX - 0.5 * size, midY - 0.5 * size), new Coordinate(midX + 0.5 * size, midY - 0.5 * size)};
        return points;
    }
    public int getCount(){
        return visitedCount;
    }

    public int hasWall(){
        return wall;
    }
    public int hasDoor(){
        return door;
    }

    public int hasWindow(){
        return window;
    }
    public int hasTower(){
        return sentryTower;
    }
    public int hasGuard(){
        return guard;
    }
    public int hasTarget(){
        return target;
    }

    public int hasTeleport(){
        return teleport;
    }
    public void addVisitedCount()
    {
        visitedCount++;
    }
}
