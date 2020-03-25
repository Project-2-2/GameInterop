package Group9.gui;

import Group9.agent.container.AgentContainer;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.map.dynamic.DynamicObject;
import Group9.map.dynamic.Pheromone;
import Group9.map.dynamic.Sound;
import Group9.map.objects.*;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.FieldOfView;
import javafx.scene.Node;

public class GUIConverter {

    public static Node convert(AgentContainer<?> agentContainer, FieldOfView fov)
    {
        PointContainer.Circle circle = agentContainer.getShape();
        if(agentContainer instanceof GuardContainer)
        {
            return new GuardGui(circle.getCenter().getX(), circle.getCenter().getY(), circle.getRadius(),
                    agentContainer.getDirection(), fov.getRange().getValue());
        }
        else if(agentContainer instanceof IntruderContainer)
        {
            return new IntruderGui(circle.getCenter().getX(), circle.getCenter().getY(), circle.getRadius(),
                    agentContainer.getDirection(), fov.getRange().getValue());
        }

        throw new IllegalArgumentException();
    }

    public static Node convert(DynamicObject<?> dynamicObject)
    {
        if(dynamicObject instanceof Pheromone)
        {
            return new PheromoneGui(dynamicObject.getCenter().getX(), dynamicObject.getCenter().getY(),
                    dynamicObject.getRadius(), ((Pheromone) dynamicObject).getType());
        }
        else if(dynamicObject instanceof Sound)
        {
            switch (((Sound) dynamicObject).getType())
            {
                case Yell:
                    return new YellGui(dynamicObject.getCenter().getX(), dynamicObject.getCenter().getY(), dynamicObject.getRadius());
                case Noise:
                    return new EmptySpace();
            }
        }
        throw new IllegalArgumentException();
    }

    public static Node convert(MapObject object)
    {
        if(object instanceof Door)
        {
            Vector2[] points = object.getArea().getAsPolygon().getPoints();
            return new DoorGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(),
                    points[2].getY(), points[3].getX(), points[3].getY());
        }
        else if (object instanceof SentryTower)
        {
            Vector2[] points = object.getArea().getAsPolygon().getPoints();
            return new SentryGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(),
                    points[2].getY(), points[3].getX(), points[3].getY());
        }
        else if(object instanceof TargetArea)
        {
            Vector2[] points = object.getArea().getAsPolygon().getPoints();
            return new targetAreaGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(),
                    points[2].getY(), points[3].getX(), points[3].getY());
        }
        else if(object instanceof Wall)
        {
            Vector2[] points = object.getArea().getAsPolygon().getPoints();
            return new InternalWallGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(),
                    points[2].getY(), points[3].getX(), points[3].getY());
        }
        else if(object instanceof ShadedArea)
        {
            Vector2[] points = object.getArea().getAsPolygon().getPoints();
            return new ShadedareaGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(),
                    points[2].getY(), points[3].getX(), points[3].getY());
        }
        else if(object instanceof Spawn)
        {
            return new EmptySpace();
        }
        else if(object instanceof TeleportArea)
        {
            Vector2[] points = object.getArea().getAsPolygon().getPoints();
            return new targetAreaGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(),
                    points[2].getY(), points[3].getX(), points[3].getY());
        }
        else if(object instanceof Window)
        {
            Vector2[] points = object.getArea().getAsPolygon().getPoints();
            return new WindowsGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(),
                    points[2].getY(), points[3].getX(), points[3].getY());
        }
        throw new IllegalArgumentException(String.format("Unknown object type: %s", object.getClass().getName()));
    }

}
