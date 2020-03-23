package Group5.GameController;

import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Vision {
    ArrayList<Area> areas = Area.getAreas();

    /**
     * @param agent the agent you want to update vision
     * @return An ObjectPercepts (object containing the perceived objects)
     */
    public ObjectPercepts vision(AgentController agent) {

        if (GameRunner.enterSentry(agent.getPosition(),agent.getPosition())){
            agent.onSentryTower=true;
        }else{
            agent.onSentryTower=false;
        }

        ArrayList<ObjectPercept> perceivedObjects = getObjectPerceived(agent);

        bubbleSort(perceivedObjects, agent);
        checkPerceivedObjects(perceivedObjects);
        Set<ObjectPercept> objectsPercepts = new HashSet<>(perceivedObjects);

        System.out.println("Vision return list of size: "+ objectsPercepts.size());
        return new ObjectPercepts(objectsPercepts);
    }

    public ArrayList<ObjectPercept> getObjectPerceived(AgentController agent) {
        ArrayList<ObjectPercept> toReturn = new ArrayList<>();
        double targetX, targetY, viewRange, viewShift = 0, xShift = 0, yShift = 0; //viewShift only if we are on a sentry tower
        Point intersectionPoint;
        ArrayList<ArrayList<Point>> positions;

        double currentX = agent.getPosition().getX();
        double currentY = agent.getPosition().getY();
        double angle = agent.getAngle().getDegrees();

        if (agent.isOnSentryTower()) {
            Distance[] dist = agent.getTowerViewRange();
            viewShift = dist[0].getValue();
            viewRange = dist[1].getValue();
        }else
            viewRange = agent.getViewRange().getValue();

        if (viewShift != 0) {   //If we are on a sentry tower wa cannot see from our position to our position+2
            xShift = viewShift * Math.cos(angle) + currentX;
            yShift = viewShift * Math.sin(angle) + currentY;

        }
        Point point1 = new Point(currentX+xShift, currentY+yShift);

        for (double i=-22.5; i <=22.5; i++){

            if (angle + i > 360) {
                targetX = viewRange * Math.cos(angle + i - 360) + currentX;
                targetY = viewRange * Math.sin(angle + i - 360) + currentY;

            }else if (angle + i < 0) {
                targetX = viewRange * Math.cos(angle + i + 360) + currentX;
                targetY = viewRange * Math.sin(angle + i + 360) + currentY;

            }else{
                targetX = viewRange * Math.cos(angle + i) + currentX;
                targetY = viewRange * Math.sin(angle + i) + currentY;

            }
            Point point2 = new Point(targetX, targetY);
            ArrayList<Point> vector1 = new ArrayList<>(List.of(point1, point2));

            for (Area area : areas) {
                positions = area.getPositions();
                for (ArrayList<Point> arr: positions) {
                    ArrayList<Point> vector2 = new ArrayList<>(List.of(arr.get(0),arr.get(1)));

                    if (Sat.hasCollided(vector1, vector2)) {
                        intersectionPoint = Area.getIntersectionPoint(vector1.get(0), vector1.get(1), vector2.get(0), vector2.get(1));
                        toReturn.add(new ObjectPercept(area.getObjectsPerceptType(), intersectionPoint));
                    }
                }
            }
        }
        return toReturn;
    }

    /**
     * Checks if there are some opaque object and removes the object that you can't see
     */
    private void checkPerceivedObjects(ArrayList<ObjectPercept> perceivedObjects) {
        boolean seeFarther = true; // false if there is an area in front that is not opaque

        for (ObjectPercept object : perceivedObjects) {
            if (!seeFarther) {
                perceivedObjects.remove(object);

            }else if (object.getType().isOpaque()) {
                seeFarther = false;

            }
        }
    }

    public static void bubbleSort(ArrayList<ObjectPercept> perceived, AgentController agent) {
        int n = perceived.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (getDistance(perceived.get(j), agent) > getDistance(perceived.get(j + 1),agent)) {
                    ObjectPercept temp = perceived.get(j);
                    perceived.set(j, perceived.get(j + 1));
                    perceived.set(j + 1, temp);
                }

            }
        }
    }

    /**
     * @param object ObjectPercept
     * @param agent AgentController
     * @return return distance between an agent and an object
     */
    public static double getDistance(ObjectPercept object, AgentController agent) {
        return Math.sqrt(Math.pow(agent.getPosition().getX() - object.getPoint().getX(), 2) +
                Math.pow(agent.getPosition().getY() - object.getPoint().getY(), 2));
    }
}
