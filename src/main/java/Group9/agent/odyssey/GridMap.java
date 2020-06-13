package Group9.agent.odyssey;

import Group9.math.Vector2;

import java.util.*;

public class GridMap {

    //TODO: The Growth Factor cannot be less than 2 right now. This is because the map right now does not have the ability
    // to only grow in one direction.
    private final static double GROWTH_FACTOR = 2;
    private final double resolution;
    public short[][] map;

    public GridMap(double resolution, double initialWidth, double initialHeight){
        this.resolution = resolution;
        this.map = new short[ceil(initialHeight/resolution)][ceil(initialWidth/resolution)];
    }

    public double getWidth()
    {
        return this.resolution * this.map[0].length;
    }

    public double getHeight()
    {
        return this.resolution * this.map.length;
    }

    public void set(double x, double y, short value)
    {
        this.checkForGrowth(x, y);
        Cell cell = toCell(x, y);
        this.map[(verticalLength()-cell.y()-1)][cell.x()] = value;
    }

    public short get(double x, double y)
    {
        Cell cell = toCell(x, y);
        if(!hasCell(cell))
        {
            return -1;
        }
        return cellGet(cell);
    }

    private short cellGet(Cell cell)
    {
        return this.map[(verticalLength()-cell.y()-1)][cell.x()];
    }

    private boolean hasCell(Cell cell)
    {
        return (cell.x() >= horizontalLength() || cell.y() >= verticalLength() || cell.x() < 0 || cell.y() < 0);
    }

    public void ray(Vector2 a, Vector2 b)
    {
        final double length = b.distance(a);
        final Vector2 dir = b.sub(a).normalise().mul(resolution);
        for(double dx = 0; dx <= length / resolution; dx++)
        {
            Vector2 p = a.add(dir.mul(dx));
            set(p.getX(), p.getY(), (short) 1);
        }
    }

    public List<Cell> path(Vector2 start, Vector2 target)
    {
        Cell startCell = toCell(start.getX(), start.getY());
        Cell targetCell = toCell(target.getX(), target.getY());

        Map<Cell, Double> fScore = new HashMap<>();
        fScore.put(startCell, h(startCell, targetCell));

        List<Cell> openSet = new LinkedList<>();
        openSet.add(startCell);

        Map<Cell, Cell> cameFrom = new HashMap<>();

        Map<Cell, Double> gScore = new HashMap<>();
        gScore.put(startCell, 0D);

        while (!openSet.isEmpty())
        {
            Cell current = openSet.get(0);

            if(current.equals(targetCell))
            {
                System.out.println("done");
                List<Cell> total_path = new LinkedList<>();
                total_path.add(current);
                while (cameFrom.containsKey(current))
                {
                    current = cameFrom.get(current);
                    total_path.add(current);
                }
                Collections.reverse(total_path);
                return total_path;
            }

            openSet.remove(0);

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    Cell neighbour = new Cell(current.x() + x, current.y() + y);
                    if(this.hasCell(neighbour) || cellGet(neighbour) == 1) continue;

                    double tentative_gScore = gScore.getOrDefault(current, Double.POSITIVE_INFINITY) + cellGet(neighbour);
                    if(tentative_gScore < gScore.getOrDefault(neighbour, Double.POSITIVE_INFINITY))
                    {
                        cameFrom.put(neighbour, current);
                        gScore.put(neighbour, tentative_gScore);
                        fScore.put(neighbour, tentative_gScore + h(neighbour, targetCell));
                        if(!openSet.contains(neighbour))
                        {
                            openSet.add(neighbour);
                        }
                    }
                }
            }

            openSet.sort(Comparator.comparingDouble(o -> fScore.getOrDefault(o, Double.POSITIVE_INFINITY)));

        }
        return null;
    }

    private double h(Cell cell, Cell target)
    {
        return 0; // Equivalent to Dijkstra
        //return Math.sqrt(Math.pow(cell.x() - target.x(), 2) + Math.pow(cell.y() - target.y(), 2)); //Euclidian distance
        //return Math.abs(cell.x() - cell.y()) + Math.abs(target.x() - target.y()); //Manhattan distance
    }

    private Cell toCell(double x, double y)
    {
        return new Cell(
                floor((x + getWidth() / 2) / resolution),
                floor((y + getHeight() / 2) / resolution)
        );
    }

    private void checkForGrowth(double x, double y)
    {
        //--- calculate the cell position. use the abs values to avoid handling negative values further down in the pipeline
        Cell cell = toCell(Math.abs(x), Math.abs(y));
        final double horizontalGrowth = (cell.x() / (double) (horizontalLength()));
        final double verticalGrowth = (cell.y() / (double) (verticalLength()));
        /*if(horizontalGrowth > 1 && verticalGrowth > 1)
        {
            grow(ceil(horizontalGrowth * horizontalLength() * GROWTH_FACTOR), ceil(verticalGrowth * verticalLength() * GROWTH_FACTOR));
        }
        else if(horizontalGrowth > 1)
        {
            grow(ceil(horizontalGrowth * horizontalLength() * GROWTH_FACTOR), verticalLength());
        }
        else if(verticalGrowth > 1)
        {
            grow(horizontalLength(), ceil(verticalGrowth * verticalLength() * GROWTH_FACTOR));
        }*/
        if(cell.x() >= horizontalLength() && cell.y() >= verticalLength())
        {
            grow(ceil(cell.x() * GROWTH_FACTOR), ceil(cell.y() * GROWTH_FACTOR));
        }
        else if(cell.x() >= horizontalLength())
        {
            grow(ceil(cell.x() * GROWTH_FACTOR), verticalLength());
        }
        else if(cell.y() >= verticalLength())
        {
            grow(horizontalLength(), ceil(cell.y() * GROWTH_FACTOR));
        }
    }

    private int verticalLength()
    {
        return this.map.length;
    }

    private int horizontalLength()
    {
        return this.map[0].length;
    }

    private void grow(int newWidth, int newHeight)
    {
        long time = System.currentTimeMillis();
        Cell oldCenter = toCell(0, 0);
        short[][] newMap = new short[newHeight][newWidth];
        short[][] oldMap = this.map;
        this.map = newMap;
        Cell newCenter = toCell(0, 0);

        final int xOffset = newCenter.x() - oldCenter.x();
        final int yOffset = newCenter.y() - oldCenter.y();

        for (int y = 0; y < oldMap.length; y++)
        {
            System.arraycopy(oldMap[y], 0, newMap[y + yOffset], xOffset, oldMap[y].length);
            oldMap[y] = null;
        }

        System.out.println("grow time: " + (System.currentTimeMillis() - time) + " grow to " + horizontalLength() + "x" + verticalLength());

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int y = 0; y < verticalLength(); y++)
        {
            for (int x = 0; x < horizontalLength(); x++) {
                builder.append(this.map[y][x] + " ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private static int floor(double a)
    {
        return (int) Math.floor(a);
    }

    private static int ceil(double a)
    {
        return (int) Math.ceil(a);
    }

    public static class Cell
    {
        private final int x;
        private final int y;

        public Cell(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public int x()
        {
            return this.x;
        }

        public int y()
        {
            return this.y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return x == cell.x &&
                    y == cell.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}
