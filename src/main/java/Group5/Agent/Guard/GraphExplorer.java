package Group5.Agent.Guard;

import Group9.Game;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphExplorer extends GuardExplorer {

    private Node previousNodeVisited;
    //list of all areas the guard has visited
    private ArrayList<Node> nodes;

    private Point position;
    private Angle angle;

    private double radius;
    private int currentTime;
    private double epsilon; // two ObjectPercepts of the same type are considered identical of their distance is less than epsilon

    private String mode;

    public GraphExplorer(){
        position = new Point(0,0);
        nodes = new ArrayList<>();
        angle = Angle.fromDegrees(0);
        radius = 30;
        currentTime = 0;
        epsilon = 0.5;
        mode = "graph";
    }

    /**
     * creates a new node for a new area
     * @param percepts
     */
    public void addAreaToGraph(GuardPercepts percepts){
        boolean newNodeBoolean = true;
        for (int i = 0; i<nodes.size();i++){
            if (nodes.get(i).agentInNode(position)){
                nodes.get(i).visitNodeAgain(percepts, position, epsilon);
                previousNodeVisited = nodes.get(i);
                newNodeBoolean = false;
                break;
            }
        }
        if (newNodeBoolean) {
            if (previousNodeVisited!=null) {
                Point centerOldNode = previousNodeVisited.getCenter();
                System.out.println("created new Area");
//                Node newNode = new Node(percepts, computeCenterNewNode(centerOldNode, percepts.getVision().getFieldOfView().getRange().getValue()));
                Node newNode = new Node(percepts, computeCenterNewNode(centerOldNode, radius), this.position, radius);
                generateAdjacentNodes(centerOldNode);
                previousNodeVisited = newNode;
                nodes.add(newNode);
//                System.out.println(nodes.size());
            }else{
                System.out.println("created new Area");
                Node newNode = new Node(percepts, position, position, radius);
                generateAdjacentNodes(position);
                previousNodeVisited = newNode;
                nodes.add(newNode);
            }
        }
    }

    /**
     * generates the 8 neighbours of a node, and give them a big idleness value
     * @param oldCenter center of node you want to generate neighbours
     */
    public void generateAdjacentNodes(Point oldCenter) {
        boolean generateNode;
        Node newNode;
        for (double j= 0; j < 2; j+=0.25) {
            generateNode= true;
            Point newCenter = new Point(oldCenter.getX()+radius*Math.cos(j*Math.PI),oldCenter.getY()+radius*Math.sin(j*Math.PI));

            for (Node node: nodes) {
                if (node.getCenter() == newCenter) {
                    generateNode = false;
                    break;
                }
            }

            if (generateNode) {
                newNode = new Node(newCenter, radius);
                nodes.add(newNode) ;
            }
        }
    }

    /**
     * basically the guard can move in 360 degrees and there are 8 squares adjacent to one square
     * we compute the center of each square and the shortest distance between the agent and the  center of a square is
     * the square or node that the agent is currently in. This node will be added to the map.
     * This way the map will be represented as a sort of grid structure where nodes cannot overlap and the complete map can be covered
     * @param centerOldNode
     * @return
     */
    public Point computeCenterNewNode(Point centerOldNode, double radius){ //TODO: add all nodes to the graph if not wall without door and centers not in graph
        //compute the center of all possible directions the agent could go
        Point upperLeft = new Point(centerOldNode.getX()+radius*Math.cos(0.75*Math.PI),centerOldNode.getY()+radius*Math.sin(0.75*Math.PI));
        Point left = new Point(centerOldNode.getX()+radius*Math.cos(Math.PI),centerOldNode.getY()+radius*Math.sin(Math.PI));
        Point downLeft = new Point(centerOldNode.getX()+radius*Math.cos(1.25*Math.PI),centerOldNode.getY()+radius*Math.sin(1.25*Math.PI));
        Point bottom = new Point(centerOldNode.getX()+radius*Math.cos(1.5*Math.PI),centerOldNode.getY()+radius*Math.sin(1.5*Math.PI));
        Point downRight = new Point(centerOldNode.getX()+radius*Math.cos(1.75*Math.PI),centerOldNode.getY()+radius*Math.sin(1.75*Math.PI));
        Point right = new Point(centerOldNode.getX()+radius*Math.cos(0),centerOldNode.getY()+radius*Math.sin(0));
        Point upperRight = new Point(centerOldNode.getX()+radius*Math.cos(0.25*Math.PI),centerOldNode.getY()+radius*Math.sin(0.25*Math.PI));
        Point top = new Point(centerOldNode.getX()+radius*Math.cos(0.5*Math.PI),centerOldNode.getY()+radius*Math.sin(0.5*Math.PI));

        Point[] centers = new Point[]{upperLeft,left,downLeft,bottom,downRight,right,upperRight,top};

        Double[] distances = new Double[]{Node.getDistance(upperLeft,position), Node.getDistance(left,position),Node.getDistance(downLeft,position),Node.getDistance(bottom,position), Node.getDistance(downRight,position),Node.getDistance(right,position), Node.getDistance(upperRight,position), Node.getDistance(top,position)};

        int minimumIndex = 0;
        for (int i =1; i<distances.length;i++){
            if (distances[i]<distances[minimumIndex]){
                minimumIndex = i;
            }
        }
        return centers[minimumIndex];
    }

    /**
     * call this method to move and update the guard's internal position
     * @param move
     * @return
     */
    public GuardAction move(Move move, GuardPercepts percepts){
        Point movement = new Point(move.getDistance().getValue()*Math.cos(angle.getRadians()),move.getDistance().getValue()*Math.sin(angle.getRadians()));
        position = new Point(position.getX()+movement.getX(),position.getY()+movement.getY());
            addAreaToGraph(percepts);
//        System.out.println("Moving ..." + position.toString());
        return move;
    }

    /**
     * call this method to do a rotation
     * @param rotate
     * @return
     */
    public GuardAction rotate(Rotate rotate){
        angle = Angle.fromDegrees(angle.getDegrees()+rotate.getAngle().getDegrees());
        while (angle.getDegrees()>360){
            angle = Angle.fromDegrees(angle.getDegrees()-360);
        }
        while (angle.getDegrees()<-360){
            angle = Angle.fromDegrees(angle.getDegrees()+360);
        }
        return rotate;
    }

    public void updateNodeIdleness(){
        for (int i =0; i<nodes.size();i++){
            nodes.get(i).updateIdleness();
        }
    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        //return explore(percepts);
        //if queue is empty otherwise do actions inside queue
//        if (!percepts.wasLastActionExecuted()){
//            actionQueue.clear();
//        }

        GuardAction a = super.getAction(percepts); //TODO: Wrap everything that happens in Guard Explorer into "Move"and "Rotate" methods
            if(a instanceof Rotate){
                a = this.rotate((Rotate)a);
            }
            else if(a instanceof  Move){
                a = this.move((Move)a, percepts);
            }
        return a;
    }

    /**
     * just a random agent to start with
     * @param percepts The precepts represent the world as perceived by that agent.
     * @return
     */
    @Override
    public void explore(GuardPercepts percepts) { //TODO: Check when to ignore exploration and hunt intruder
        updateNodeIdleness();
        currentTime++;

        boolean switchOffGuardMode = true;
        for(ObjectPercept o: percepts.getVision().getObjects().getAll()) {
            if(o.getType()==ObjectPerceptType.Door || o.getType()==ObjectPerceptType.Window) {
                switchOffGuardMode = false;
                break;
            }
        }
        if (switchOffGuardMode) this.mode = "graph";

        // TODO: account for the intruder

        if(this.mode.equals("guard")){
            super.explore(percepts);

        }
        else {

            if (!percepts.wasLastActionExecuted()) {
                if (Math.random() < 0.1) {
                    addActionToQueue(new DropPheromone(SmellPerceptType.values()[(int) (Math.random() * SmellPerceptType.values().length)]), percepts);
                }

                addActionToQueue(new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble())), percepts);

            } else {
                double maxMovementDistance = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts);
//                for(Node n: nodes){
//                    System.out.println(n.getCenter());
//                }
                Node nextNode = chooseNextNode(maxMovementDistance);

                if(nextNode != null){
                    moveToNode(nextNode, percepts, maxMovementDistance);
                }
                if (percepts.getAreaPercepts().isInDoor() && getDroppedPheromone() == 0) {
//            System.out.println("door: drop pheromone type 2");
//                    dropPheromone(percepts, SmellPerceptType.Pheromone2); //TODO: Throws Nullpointer exception after door
                    super.setDroppedPheromone(500);
                }

                if (percepts.getAreaPercepts().isInWindow() && getDroppedPheromone() == 0) {
//            System.out.println("window: drop pheromone type 2");
                    dropPheromone(percepts, SmellPerceptType.Pheromone2);
                    super.setDroppedPheromone(500);
                }

                if (percepts.getAreaPercepts().isJustTeleported() && getDroppedPheromone() == 0) {
//            System.out.println("teleported: drop pheromone type 2");
//                    dropPheromone(percepts, SmellPerceptType.Pheromone2); //TODO: Throws Nullpointer exception after teleport
                    super.setDroppedPheromone(500);
                }

            }
        }
    }

    /**
     * Chooses the node with maximum utility (i.e. a trade off between highest idleness and time it takes to get there)
     * @param velocity Movement speed of the agent. Currently simply the maximum movement distance
     * @return The next node to visit based on idleness
     */
    private Node chooseNextNode(double velocity){
        Node nextNode = null;
        double utility = 0;
        for(Node n: this.nodes){
            double distance = this.previousNodeVisited.getCenter().getDistance(n.getCenter()).getValue();
            if (distance < 2*radius){
//                double deltaT = distance/velocity;
//                double tNext = currentTime + deltaT;
//                double expectedIdleness = tNext - n.getNodeIdleness();
                if(distance > 0.0005 && n.getNodeIdleness() > utility) {
                    nextNode = n;
                    utility = n.getNodeIdleness();
                }
            }
        }

        return nextNode;
    }

    /**
     * Not done
     * @param node
     */
    private void moveToNode(Node node, GuardPercepts percepts, double maxMovementDistance){ //TODO: What about new nodes? I think it might break it, becauee there are no percepts stored yet.
        System.out.println("Moving to node");
        //check doors/windows
        if (node.getObjectMap().keySet().contains(ObjectPerceptType.Door) ||
                node.getObjectMap().keySet().contains(ObjectPerceptType.Window)) {
            System.out.println("There is a passable object on the way.");
            HashMap<String, Angle[]> angleRanges = new HashMap<>();
            angleRanges.put("right", new Angle[]{Angle.fromRadians(-Math.PI / 2), Angle.fromRadians(Math.PI / 2)});
            angleRanges.put("top", new Angle[]{Angle.fromRadians(0), Angle.fromRadians(Math.PI)});
            angleRanges.put("left", new Angle[]{Angle.fromRadians(Math.PI / 2), Angle.fromRadians(3 * Math.PI / 2)});
            angleRanges.put("bottom", new Angle[]{Angle.fromRadians(Math.PI), Angle.fromRadians(2 * Math.PI)});

            Angle angleBetweenCenters = vectorAngle(this.previousNodeVisited.getCenter(), node.getCenter());
            Angle angleBetweenCentersFull = Angle.fromRadians((angleBetweenCenters.getRadians() + 2 * Math.PI) % (2 * Math.PI));
            String directionKey = "";
            if (angleBetweenCenters.getRadians() < 0.05) directionKey = "right";
            else if (angleBetweenCenters.getRadians() - Math.PI / 2 < 0.05) directionKey = "top";
            else if (angleBetweenCentersFull.getRadians() - Math.PI < 0.05) directionKey = "left";
            else if (angleBetweenCentersFull.getRadians() + Math.PI / 2 < 0.05) directionKey = "bottom";

            ArrayList<Point> doorList = new ArrayList<>();
            for(Point p: node.getObjectMap().get(ObjectPerceptType.Door)){
                if (doorOrWindowsOnTheWay(angleRanges, directionKey, p)) doorList.add(p);
            }
            if(doorList.isEmpty() && node.getObjectMap().keySet().contains(ObjectPerceptType.Window)){
                for(Point p: node.getObjectMap().get(ObjectPerceptType.Window)) {
                    if (doorOrWindowsOnTheWay(angleRanges, directionKey, p)) doorList.add(p);
                }

            }

            if(!doorList.isEmpty()) {
                Angle angleToPassable = this.getRelativeAngle(new Point(doorList.get(0).getX() - position.getX(),
                        doorList.get(0).getY() - position.getY()), this.angle);
                addActionToQueue(new Rotate(angleToPassable), percepts);
            }

            // TODO: rotate first
            for(Point p: doorList){
                for (ObjectPercept o: percepts.getVision().getObjects().getAll()){
                    Point q = new Point(o.getPoint().getX()+position.getX(), o.getPoint().getY()+position.getY());
                    if(new Distance(p, q).getValue() < this.epsilon) {
                        System.out.println("Switching to guard mode to go through door/window");
                        this.mode = "guard";
                        break;
                    }
                }
            }
            if(!this.mode.equals("guard")){
                addActionToQueue(new Move(new Distance(maxMovementDistance)), percepts);
            }


        }

        //check walls or does Ionas do that already?

        else{
            System.out.println("No doors. No Windows.");
            //determine direction
            Angle angleToNextNode = getRelativeAngle(new Point(node.getCenter().getX()-this.position.getX(),
                    node.getCenter().getY()-this.position.getY()), this.angle);
            addActionToQueue(new Rotate(angleToNextNode), percepts);
            addActionToQueue(new Move(new Distance(5)), percepts);
        }
    }

    /**
     * Checks if there is a door or a Window on the way
     * @param angleRanges
     * @param directionKey
     * @param p
     * @return
     */
    private boolean doorOrWindowsOnTheWay(HashMap<String, Angle[]> angleRanges, String directionKey, Point p) {
        boolean onTheWay = false;
        Angle pointAngle = vectorAngle(this.previousNodeVisited.getCenter(), p);
        if (directionKey.equals("left") || directionKey.equals("bottom"))
            pointAngle = Angle.fromRadians((pointAngle.getRadians() + 2 * Math.PI) % (2 * Math.PI));
        if (pointAngle.getRadians() >= angleRanges.get(directionKey)[0].getRadians() &&
                pointAngle.getRadians() < angleRanges.get(directionKey)[1].getRadians()) {
            onTheWay = true;
        }
        return onTheWay;
    }

    /**
     *
     * @param p Any point relative to the spawn position
     * @param a Any angle relative to the original orientation of the agent
     * @return The angle between the point when interpreted as a vector and angle a
     */
    private Angle getRelativeAngle(Point p, Angle a){
        Angle pointAngle = Angle.fromRadians(Math.atan2(p.getY(), p.getX()));
        return Angle.fromRadians(pointAngle.getRadians() - a.getRadians());
    }
    /**
     *
     * @param p1 Any point relative to the spawn position
     * @param p2 Any point relative to the spawn position
     * @return The angle between the two points when interpreted as vectors
     */
    private Angle getRelativeAngle(Point p1, Point p2){
        Angle pointAngle1 = Angle.fromRadians(Math.atan2(p1.getY(), p1.getX()));
        Angle pointAngle2 = Angle.fromRadians(Math.atan2(p2.getY(), p2.getX()));
        return Angle.fromRadians(pointAngle2.getRadians() - pointAngle1.getRadians());
    }

    /**
     *
     * @param p1 Any point relative to the spawn position
     * @param p2 Any point relative to the spawn position
     * @return The angle of a vector between the two points from the x-axis of the spawn location
     */
    private Angle vectorAngle(Point p1, Point p2){
       return Angle.fromRadians(Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
    }

    private double getSpeedModifier(GuardPercepts guardPercepts) {
        SlowDownModifiers slowDownModifiers =  guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers();
        if(guardPercepts.getAreaPercepts().isInWindow()) {
            return slowDownModifiers.getInWindow();

        } else if(guardPercepts.getAreaPercepts().isInSentryTower()) {
            return slowDownModifiers.getInSentryTower();

        } else if(guardPercepts.getAreaPercepts().isInDoor()) {
            return slowDownModifiers.getInDoor();
        }

        return 1;
    }

}
