package Group5.UI;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * !!! THIS MAP MODEL CLASS IS ONLY FOR TESTING PURPOSES UNTIL THE ACTUAL GAME CONTROLLER IS IMPLEMENTED !!!
 */

public class DrawableMapModel {

    public enum CellValue {
        EMPTY, TELEPORT, TOWER, WALL, INTRUDER, GUARD
    }

    @FXML
    private int rowCount;
    @FXML
    private int columnCount;

    private static CellValue[][] grid;
    private Point2D intruderLocation;
    private Point2D intruderVelocity;
    public static Point2D guard1Location;
    public static Point2D guard1Velocity;
    public static Point2D guard2Location;
    public static Point2D guard2Velocity;

    public DrawableMapModel() {
        this.startNewGame();
    }

    public void startNewGame() {
        rowCount = 0;
        columnCount = 0;
        this.initializeLevel(AlertBox.getFile());
    }

    public void initializeLevel(String fileName) {
        File file = new File(fileName);
        Scanner scanner1 = null;
        try {
            scanner1 = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner1.hasNextLine()) {
            String line = scanner1.nextLine();
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                lineScanner.next();
                columnCount++;
            }
            rowCount++;
        }
        columnCount = columnCount / rowCount;
        Scanner scanner2 = null;
        try {
            scanner2 = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        grid = new CellValue[rowCount][columnCount];
        int row = 0;
        int intruderRow = 0;
        int intruderColumn = 0;
        int guard1Row = 0;
        int guard1Column = 0;
        int guard2Row = 0;
        int guard2Column = 0;

        while (scanner2.hasNextLine()) {
            int column = 0;
            String line = scanner2.nextLine();
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                String value = lineScanner.next();
                CellValue thisValue;
                if (value.equals("W")) {
                    thisValue = CellValue.WALL;
                } else if (value.equals("T")) {
                    thisValue = CellValue.TELEPORT;
                } else if (value.equals("R")) {
                    thisValue = CellValue.TOWER;
                } else if (value.equals("1")) {
                    thisValue = CellValue.GUARD;
                    guard1Row = row;
                    guard1Column = column;
                } else if (value.equals("2")) {
                    thisValue = CellValue.GUARD;
                    guard2Row = row;
                    guard2Column = column;
                } else if (value.equals("I")) {
                    thisValue = CellValue.INTRUDER;
                    intruderRow = row;
                    intruderColumn = column;
                } else {
                    thisValue = CellValue.EMPTY;
                }
                grid[row][column] = thisValue;
                column++;
            }
            row++;
        }
        intruderLocation = new Point2D(intruderRow, intruderColumn);
        intruderVelocity = new Point2D(0, 0);
        guard1Location = new Point2D(guard1Row, guard1Column);
        guard1Velocity = new Point2D(-1, 0);
        guard2Location = new Point2D(guard2Row, guard2Column);
        guard2Velocity = new Point2D(-1, 0);
    }

    public void moveIntruder() {
        intruderVelocity = changeVelocity(0, 0);
        intruderLocation = intruderLocation.add(1, 0);
    }

    private Point2D changeVelocity(double x, double y) {
        return new Point2D(x, y);
    }

    public Point2D[] moveGuard(Point2D velocity, Point2D location) {
        Random generator = new Random();
        Point2D[] data = {velocity, location};
        return data;
    }

    public void step() {
        this.moveIntruder();
        if (intruderLocation.getX() < grid.length) {
            grid[(int) intruderLocation.getX()][(int) intruderLocation.getY()] = CellValue.INTRUDER;
            grid[(int) intruderLocation.getX() - 1][(int) intruderLocation.getY()] = CellValue.EMPTY;
        } else {
            intruderLocation.add(0, 0);
        }
    }

    public CellValue getCellValue(int row, int column) {
        assert row >= 0 && row < this.grid.length && column >= 0 && column < this.grid[0].length;
        return this.grid[row][column];
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }
}
