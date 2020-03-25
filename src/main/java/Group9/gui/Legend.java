package Group9.gui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class Legend extends Group {
    public Legend(double x, double y)
    {
        Rectangle border = new Rectangle();
        border.setX(x-25);
        border.setY(y-10);
        border.setWidth(150);
        border.setFill(Color.WHITE);
        border.setStroke(Color.BLACK);


        Text[] names = new Text[]{new Text("Door"), new Text("Guard"), new Text("Wall"), new Text("Intruder"), new Text("Yell"), new Text("Sentry"), new Text("Shaded area"), new Text("Spawn area guard"), new Text("Spawn area intruder"), new Text("Target area"), new Text("Teleport area"), new Text("Window")};

        double spaceY = 20;
        for(int i=0; i<names.length; i++)
        {
            names[i].setX(x);
            names[i].setY(y + spaceY * i);

        }

        Rectangle[] ids = new Rectangle[names.length];
        Color[] colors = new Color[]{Color.BROWN, Color.BLUE, Color.LAVENDER, Color.RED, Color.YELLOW, Color.PURPLE, Color.BLACK, Color.LIGHTBLUE, Color.LIGHTSALMON, Color.RED, Color.DARKBLUE, Color.LIGHTGREY};

        for(int i=0; i<colors.length; i++)
        {
            ids[i] = new Rectangle(10, 10, colors[i]);
            ids[i].setX(x-20);
            ids[i].setY(y + spaceY * i - 8);
            ids[i].setStroke(Color.BLACK);
        }

        border.setHeight(names.length*spaceY);

        this.getChildren().addAll(border);
        this.getChildren().addAll(names);
        this.getChildren().addAll(ids);
    }
}
