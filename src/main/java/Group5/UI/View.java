package Group5.UI;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class View extends Group {
    public final static double CELL_WIDTH = 600 / 30;

    @FXML
    private int rowCount;
    @FXML
    private int columnCount;

    private ImageView[][] cellViews;
    private Image wallImage;
    private Image teleportImage;
    private Image towerImage;
    private Image intruderImage;

    public View() {
        this.wallImage = new Image(getClass().getResourceAsStream("/src/main/java/Group5/Assets/wall.png"));
        this.teleportImage = new Image(getClass().getResourceAsStream("/src/main/java/Group5/Assets/teleport.png"));
        this.towerImage = new Image(getClass().getResourceAsStream("/src/main/java/Group5/Assets/tower.png"));
        this.intruderImage = new Image(getClass().getResourceAsStream("/src/main/java/Group5/Assets/intruder.png"));
    }

    private void initializeGrid() {
        if (this.rowCount > 0 && this.columnCount > 0) {
            this.cellViews = new ImageView[this.rowCount][this.columnCount];
            for (int row = 0; row < this.rowCount; row++) {
                for (int column = 0; column < this.columnCount; column++) {
                    ImageView imageView = new ImageView();
                    imageView.setX((double) column * CELL_WIDTH);
                    imageView.setY((double) row * CELL_WIDTH);
                    imageView.setFitWidth(CELL_WIDTH);
                    imageView.setFitHeight(CELL_WIDTH);
                    this.cellViews[row][column] = imageView;
                    this.getChildren().add(imageView);
                }
            }
        }
    }

    public void update(DrawableMapModel drawableMapModel) {
        assert drawableMapModel.getRowCount() == this.rowCount && drawableMapModel.getColumnCount() == this.columnCount;
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                DrawableMapModel.CellValue value = drawableMapModel.getCellValue(row, column);
                if (value == DrawableMapModel.CellValue.WALL) {
                    this.cellViews[row][column].setImage(this.wallImage);
                } else if (value == DrawableMapModel.CellValue.TOWER) {
                    this.cellViews[row][column].setImage(this.towerImage);
                } else if (value == DrawableMapModel.CellValue.INTRUDER) {
                    this.cellViews[row][column].setImage(this.intruderImage);
                    this.cellViews[row][column].setRotate(this.cellViews[row][column].getRotate() + 90);
                } else if (value == DrawableMapModel.CellValue.TELEPORT) {
                    this.cellViews[row][column].setImage(this.teleportImage);
                } else {
                    this.cellViews[row][column].setImage(null);
                }
            }
        }
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        this.initializeGrid();
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.initializeGrid();
    }

}
