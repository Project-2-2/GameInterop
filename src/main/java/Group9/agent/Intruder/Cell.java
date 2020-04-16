package Group9.agent.Intruder;

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

    private boolean wall = false;
    private boolean window = false;
    private boolean door = false;
    private boolean guard = false;
    private boolean sentryTower = false;
    private boolean target = false;
    private boolean teleport = false;
    public Cell()
    {
        above = null;
        below = null;
        left = null;
        right = null;
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
