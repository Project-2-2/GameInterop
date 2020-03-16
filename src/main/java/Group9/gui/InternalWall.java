package Group9.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class InternalWall extends Rectangle implements GameObject {
    final double x;
    final double y;
    final double width;
    final double height;
    public InternalWall(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
    {
        super();
        width = findWidth(x1, x2, x3, x4);
        height = findHeight(y1, y2, y3, y4);
        this.x = findLowest(x1, x2, x3, x4);
        this.y = findLowest(y1, y2, y3, y4);
        setLayoutX(this.x);
        setLayoutY(this.y);
        setWidth(width);
        setHeight(height);
        setFill(Color.LAVENDER);
        setStroke(Color.BLACK);
    }
    public double findWidth(double x1, double x2, double x3, double x4)
    {
        double[] xs = {x1, x2, x3, x4};
        double highest = findHighest(x1, x2, x3, x4);
        double lowest = findLowest(x1, x2, x3, x4);

        double width = highest - lowest;
        return width;
    }
    public double findHeight(double y1, double y2, double y3, double y4)
    {
        return findWidth(y1, y2, y3, y4);
    }
    public double findHighest(double number1, double number2, double number3, double number4)
    {
        double[] numbers = {number1, number2, number3, number4};
        double highest = Double.NEGATIVE_INFINITY;
        for(int i=0; i<numbers.length; i++)
        {
            if(highest<numbers[i])
            {
                highest = numbers[i];
            }
        }
        return highest;
    }
    public double findLowest(double number1, double number2, double number3, double number4)
    {
        double[] numbers = {number1, number2, number3, number4};
        double lowest = Double.POSITIVE_INFINITY;
        for(int i=0; i<numbers.length; i++)
        {
            if(lowest > numbers[i])
            {
                lowest = numbers[i];
            }
        }
        return lowest;
    }
    public void updateScale()
    {
        double scale = Scale.scale;
        setLayoutX(x*scale);
        setLayoutY(y*scale);
        setWidth(width*scale);
        setHeight(height*scale);
    }
}
