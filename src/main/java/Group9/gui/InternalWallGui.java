package Group9.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class InternalWallGui extends Polygon implements GameObject {
    final double[] xs;
    final double[] ys;
    public InternalWallGui(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
    {
        super(x1, y1, x2, y2, x3, y3, x4, y4);
        double[] xs = {x1, x2, x3, x4};
        double[] ys = {y1, y2, y3, y4};
        this.xs = xs;
        this.ys = ys;
        setFill(Color.LAVENDER);
        setStroke(Color.BLACK);
    }
    public InternalWallGui()
    {
        xs = ;
        ys = null;
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
        this.getPoints().setAll(
                xs[0]*scale, ys[0]*scale,
                xs[1]*scale, ys[1]*scale,
                xs[2]*scale, ys[2]*scale,
                xs[3]*scale, ys[3]*scale
        );
    }
}
