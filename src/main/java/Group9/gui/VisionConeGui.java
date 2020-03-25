package Group9.gui;

import Group9.math.Vector2;
import javafx.scene.shape.*;

import java.util.Iterator;
import java.util.Set;

public class VisionConeGui extends Path implements GameObject {
    double range;
    double angle = 45;
    double maxX;
    double maxY;
    double x;
    double y;
    double[] leftCoordinate;
    double[] middleCoordinate;
    double[] rightCoordinate;
    Vector2 direction;
    boolean moreAccurate = true;
    public VisionConeGui(Vector2 direction, double x, double y, double range, Set<Vector2[]> visionRays)
    {
        super(new MoveTo(x,y));
        double directionAngle = direction.getAngle();
        Vector2[][] rays = visionRays.toArray(new Vector2[][]{});
        this.range = range;
        this.direction = direction;
        leftCoordinate = getCoordinate(directionAngle - angle*0.5, range);
        rightCoordinate = getCoordinate(directionAngle + angle*0.5, range);

        this.getElements().add(new LineTo(leftCoordinate[0], -leftCoordinate[1]));
        this.getElements().add(new QuadCurveTo(direction.getX(), -direction.getY(), rightCoordinate[0], -rightCoordinate[1]));
        this.getElements().add(new ClosePath());
        this.maxX = this.getLayoutBounds().getMaxX();
        this.maxY = this.getLayoutBounds().getMaxY();
        this.x = x;
        this.y = y;

        //more accurate vision cone
        if(moreAccurate)
        {
            leftCoordinate = makeArray(rays[0][1].getX(), rays[0][1].getY());
            middleCoordinate = makeArray(rays[22][1].getX(), rays[22][1].getY());
            rightCoordinate = makeArray(rays[44][1].getX(), rays[44][1].getY());
        }
    }
    public double[] getCoordinate(double angle, double range)
    {
        double[] coordinate = {-Math.cos(angle) * range, -Math.sin(angle) * range};
        return coordinate;
    }
    public double[] makeArray(double x, double y)
    {
        return new double[]{x,y};
    }
    public void updateScale()
    {
        double scale = Scale.scale;
        this.getElements().clear();
        this.getElements().add(new MoveTo(x*scale, y*scale));
        this.getElements().add(new LineTo((x+leftCoordinate[0])*scale, (y+leftCoordinate[1])*scale));
        this.getElements().add(new QuadCurveTo((x + direction.getX()*range)*scale, (y+direction.getY()*range)*scale, (x+rightCoordinate[0])*scale, (y+rightCoordinate[1])*scale));
        this.getElements().add(new ClosePath());

        if(moreAccurate)
        {
            //more accurate vision cone
            this.getElements().clear();
            this.getElements().add(new MoveTo(x*scale, y*scale));
            this.getElements().add(new LineTo((leftCoordinate[0]*scale), (leftCoordinate[1]*scale)));
            this.getElements().add(new QuadCurveTo((middleCoordinate[0]*scale), (middleCoordinate[1]*scale), (rightCoordinate[0]*scale), (rightCoordinate[1]*scale)));
            this.getElements().add(new ClosePath());
        }

    }
    public void updatePosition(double x, double y)
    {
        this.x = x;
        this.y = y;
        updateScale();
    }
}
