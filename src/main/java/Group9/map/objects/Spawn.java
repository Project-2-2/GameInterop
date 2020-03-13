package Group9.map.objects;

import Group9.map.area.EffectArea;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Geometry.Vector;
import Interop.Percept.Vision.ObjectPerceptType;

import java.awt.*;
import java.util.List;
import java.util.Random;

public abstract class Spawn extends MapObject  {

    private PointContainer.Quadrilateral area;

    public Spawn(PointContainer.Quadrilateral area) {
        super(area, ObjectPerceptType.EmptySpace);
        this.area = area;
    }

    public static class Intruder extends Spawn {
        public Intruder(PointContainer.Quadrilateral area) {
            super(area);
        }
    }
    public static class Guard extends Spawn {
        public Guard(PointContainer.Quadrilateral area) {
            super(area);
        }
    }

    public Vector generateRandomLocation()
    {
        // TODO generate random location within the spawn area
        // TODO: this is pretty bad.. but I guess it'll do for now.
        Random r = new Random();
        PointContainer.Quadrilateral.Rectangle rectangle = PointContainer.Quadrilateral.containingRectangle(area);

        // worst case scenario we take center as "random" location
        Vector2 retVector = rectangle.getCenter();

        assert retVector != null;
        boolean foundInside = false;
        int tries = 0;
        while (!foundInside && tries < 1000) {
            double ranX = rectangle.getLeftmostX() + rectangle.getHorizonalSize() * r.nextDouble();
            double ranY = rectangle.getBottomY() + rectangle.getVerticalSize() * r.nextDouble();

            if (PointContainer.isPointInside(rectangle, new Vector2(ranX, ranY))) {
                foundInside = true;
                retVector = new Vector2(ranX, ranY);
            }
            tries++;
        }

        // Vector2 -> Vector
        return new Vector(retVector.getX(), retVector.getY());
    }

}
