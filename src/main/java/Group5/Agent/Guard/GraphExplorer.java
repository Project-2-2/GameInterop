package Group5.Agent.Guard;

import Group9.Game;
import Group9.map.GameSettings;
import Interop.Action.DropPheromone;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Smell.SmellPercepts;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
    private Node guardTargetNode;
    private boolean keepGoing;

    private boolean clearQueue;

    private HashMap<ObjectPerceptType, Integer> weightMap;

    private HashMap<Point,Integer> myPheromone;
    private ArrayList<SmellPercept> smelledPheromone;




    //saves the nextNode that will be visited by the exploration algorithm
    //if it cannot be reached remove this node from the list
    private Node nextNode;

    private boolean stayInNode;

    private int maxWallsOntheWay;

    public GraphExplorer() {
        position = new Point(0, 0);
        nodes = new ArrayList<>();
        angle = Angle.fromDegrees(0);
        radius = 30;
        currentTime = 0;
        epsilon = 0.5;
        maxWallsOntheWay = 10;
        mode = "graph";
        weightMap = new HashMap<>();
        myPheromone = new HashMap<>();
        smelledPheromone = new ArrayList<>();
        weightMap.put(ObjectPerceptType.Wall, 2);
        weightMap.put(ObjectPerceptType.Window, 5);
        weightMap.put(ObjectPerceptType.Door, 5);
        weightMap.put(ObjectPerceptType.SentryTower, 10);
        weightMap.put(ObjectPerceptType.Teleport, 5);
        weightMap.put(ObjectPerceptType.ShadedArea, 1);
        weightMap.put(ObjectPerceptType.EmptySpace, 1);
        weightMap.put(ObjectPerceptType.Intruder, 100);
        clearQueue = true;

    }

    /**
     * creates a new node for a new area
     *
     * @param percepts The world as the agent perceives it at the moment
     */
    public void addAreaToGraph(GuardPercepts percepts) {
        boolean newNodeBoolean = true;
        for (int i = 0; i < nodes.size(); i++) {
            //it is correct that newNodeBoolean is always false since we already adding the adjacent nodes each time we
            //visit a node
            if (nodes.get(i).agentInNode(position)) {
                previousNodeVisited = nodes.get(i);

                //create the adjacent nodes only if node was never visited before
                if (previousNodeVisited.isNeverVisited()) {
//                    //System.out.println("biem");
                    generateAdjacentNodes(previousNodeVisited);
                }
                nodes.get(i).visitNodeAgain(percepts, position, epsilon);
//                //System.out.println(previousNodeVisited.getCenter().toString());
//                //System.out.println(position.toString());
                newNodeBoolean = false;
                break;
            }
        }

        boolean allNodesVisited = true;
        for (int i =0; i<nodes.size();i++){
            if (nodes.get(i).isNeverVisited()){
                allNodesVisited = false;
            }
        }
        if (allNodesVisited&&nodes.size()!=0){
//            //System.out.println(nodes.size());
            //System.out.println("ALL NODES ARE VISITED");
            //System.out.println("NEW NODE CREATION HAS STOPPED");
        }


//        //System.out.println(nodes.size());


        ////System.out.println(nodes.size());
        if (newNodeBoolean) {
            if (previousNodeVisited != null) {
                Point centerOldNode = previousNodeVisited.getCenter();
                //System.out.println("created new Area");
//                Node newNode = new Node(percepts, computeCenterNewNode(centerOldNode, percepts.getVision().getFieldOfView().getRange().getValue()));
                Node newNode = new Node(percepts, computeCenterNewNode(centerOldNode, radius), this.position, radius);
                generateAdjacentNodes(newNode);
                previousNodeVisited = newNode;
                nodes.add(newNode);
//                //System.out.println(nodes.size());
            } else {
                //System.out.println("created new Area");
                Node newNode = new Node(percepts, position, position, radius);
                generateAdjacentNodes(newNode);
                previousNodeVisited = newNode;
                nodes.add(newNode);
            }
        }
    }

    /**
     * generates the 8 neighbours of a node, and give them a big idleness value
     *
     * @param currentNode node you want to generate neighbours
     */
    public void generateAdjacentNodes(Node currentNode) {
        boolean generateNode;
        Node newNode;
        Point oldCenter = currentNode.getCenter();
        Point newCenter;

//        //System.out.println(oldCenter.toString());
        for (double j = 0; j < 2; j += 0.25) {
            generateNode = true;

            if (j % 0.5 == 0) {
                newCenter = new Point(oldCenter.getX() + radius * Math.cos(j * Math.PI), oldCenter.getY() + radius * Math.sin(j * Math.PI));

            } else {
                //the distance between the centers of diagonal squares is different
                double radiusDiagonal = Math.sqrt(Math.pow(radius, 2) + Math.pow(radius, 2));
                newCenter = new Point(oldCenter.getX() + radiusDiagonal * Math.cos(j * Math.PI), oldCenter.getY() + radiusDiagonal * Math.sin(j * Math.PI));

            }

            for (Node node : nodes) {
                if (Node.getDistance(node.getCenter(), newCenter) < radius - 1) {
                    generateNode = false;
//                  //System.out.println("kak");
//                  //System.out.println(Node.getDistance(node.getCenter(),newCenter));
                    break;
                }
            }

            if (generateNode) {
                newNode = new Node(newCenter, radius);
//                //System.out.println(newNode.getCenter());
                nodes.add(newNode);
                currentNode.addNeighbour(newNode);
                //              //System.out.println(Node.getDistance(oldCenter,newNode.getCenter()));
            }
        }
    }

    /**
     * basically the guard can move in 360 degrees and there are 8 squares adjacent to one square
     * we compute the center of each square and the shortest distance between the agent and the  center of a square is
     * the square or node that the agent is currently in. This node will be added to the map.
     * This way the map will be represented as a sort of grid structure where nodes cannot overlap and the complete map can be covered
     *
     * @param centerOldNode
     * @return
     */
    public Point computeCenterNewNode(Point centerOldNode, double radius) { //TODO: add all nodes to the graph if not wall without door and centers not in graph
        //compute the center of all possible directions the agent could go
        double radiusDiagonal = Math.sqrt(Math.pow(radius, 2) + Math.pow(radius, 2));
        Point upperLeft = new Point(centerOldNode.getX() + radiusDiagonal * Math.cos(0.75 * Math.PI), centerOldNode.getY() + radiusDiagonal * Math.sin(0.75 * Math.PI));
        Point left = new Point(centerOldNode.getX() + radius * Math.cos(Math.PI), centerOldNode.getY() + radius * Math.sin(Math.PI));
        Point downLeft = new Point(centerOldNode.getX() + radiusDiagonal * Math.cos(1.25 * Math.PI), centerOldNode.getY() + radiusDiagonal * Math.sin(1.25 * Math.PI));
        Point bottom = new Point(centerOldNode.getX() + radius * Math.cos(1.5 * Math.PI), centerOldNode.getY() + radius * Math.sin(1.5 * Math.PI));
        Point downRight = new Point(centerOldNode.getX() + radiusDiagonal * Math.cos(1.75 * Math.PI), centerOldNode.getY() + radiusDiagonal * Math.sin(1.75 * Math.PI));
        Point right = new Point(centerOldNode.getX() + radius * Math.cos(0), centerOldNode.getY() + radius * Math.sin(0));
        Point upperRight = new Point(centerOldNode.getX() + radiusDiagonal * Math.cos(0.25 * Math.PI), centerOldNode.getY() + radiusDiagonal * Math.sin(0.25 * Math.PI));
        Point top = new Point(centerOldNode.getX() + radius * Math.cos(0.5 * Math.PI), centerOldNode.getY() + radius * Math.sin(0.5 * Math.PI));

        Point[] centers = new Point[]{upperLeft, left, downLeft, bottom, downRight, right, upperRight, top};

        Double[] distances = new Double[]{Node.getDistance(upperLeft, position), Node.getDistance(left, position), Node.getDistance(downLeft, position), Node.getDistance(bottom, position), Node.getDistance(downRight, position), Node.getDistance(right, position), Node.getDistance(upperRight, position), Node.getDistance(top, position)};

        int minimumIndex = 0;
        for (int i = 1; i < distances.length; i++) {
            if (distances[i] < distances[minimumIndex]) {
                minimumIndex = i;
            }
        }
        return centers[minimumIndex];
    }

    /**
     * call this method to move and update the guard's internal position
     *
     * @param move
     * @return
     */
    public GuardAction move(Move move, GuardPercepts percepts) {
        addAreaToGraph(percepts);
        Point movement = new Point(move.getDistance().getValue() * Math.cos(angle.getRadians()), move.getDistance().getValue() * Math.sin(angle.getRadians()));
        position = new Point(position.getX() + movement.getX(), position.getY() + movement.getY());

//        //System.out.println("Moving ..." + position.toString());
        return move;
    }

    /**
     * call this method to do a rotation
     *
     * @param rotate
     * @return
     */
    public GuardAction rotate(Rotate rotate) {
            angle = Angle.fromDegrees(angle.getDegrees() + rotate.getAngle().getDegrees());
            while (angle.getDegrees() > 360) {
                angle = Angle.fromDegrees(angle.getDegrees() - 360);
            }
            while (angle.getDegrees() < -360) {
                angle = Angle.fromDegrees(angle.getDegrees() + 360);
            }
        return rotate;
    }

    public void updateNodeIdleness() {
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).updateIdleness(weightMap);
        }
    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        //return explore(percepts);
        //if queue is empty otherwise do actions inside queue
//        if (!percepts.wasLastActionExecuted()){
//            actionQueue.clear();
//        }
        for (Point pheromonePosition : myPheromone.keySet())
        {
            myPheromone.put(pheromonePosition,myPheromone.get(pheromonePosition)-1);
            if(myPheromone.get(pheromonePosition)==0)
                myPheromone.remove(pheromonePosition);
        }
        for(SmellPercept smelled : percepts.getSmells().getAll())
        {
            for ( Point p : myPheromone.keySet()){
                if(getDistance(position,p)!=smelled.getDistance().getValue())
                {
                    smelledPheromone.add(smelled);
                }
            }
        }

        GuardAction a = super.getAction(percepts);
        if (a instanceof Rotate) {
            a = this.rotate((Rotate) a);
        } else if (a instanceof Move) {
            a = this.move((Move) a, percepts);
        }
        return a;
    }

    /**
     * just a random agent to start with
     *
     * @param percepts The precepts represent the world as perceived by that agent.
     * @return
     */
    @Override
    public void explore(GuardPercepts percepts) {
        updateNodeIdleness();

        boolean switchOffGuardMode = true;
        ArrayList<ObjectPerceptType> visionPerceptTypes = new ArrayList<>();
        Set<ObjectPercept> vision = percepts.getVision().getObjects().getAll();
        for (ObjectPercept e : vision) {
            visionPerceptTypes.add(e.getType());
        }

        if(visionPerceptTypes.contains(ObjectPerceptType.Door) ||visionPerceptTypes.contains(ObjectPerceptType.Window)){
                switchOffGuardMode = false;
                if (clearQueue){
                    actionQueue.clear();
                }
                clearQueue = false;
        }
        else if(visionPerceptTypes.contains(ObjectPerceptType.Intruder)){
                switchOffGuardMode = false;
                if (clearQueue){
                    actionQueue.clear();
                }
                clearQueue = false;
                super.followIntruder(percepts,percepts.getVision().getObjects().getAll());
                return;
//                System.out.println("Switching to guard mode to go to interesting object.");
//                this.mode = "guard";
            }
        else if (super.getLastTimeSawIntruder()>0){
                stayInNode = true;
//                System.out.println("entered last time saw intruder");
//                super.reduceLastTimeSawIntruder();

            }
        else if(visionPerceptTypes.contains(ObjectPerceptType.SentryTower)){
            if(clearQueue){
                actionQueue.clear();
            }
            clearQueue = false;
            this.mode = "guard";
        }

        else if(super.getLastTimeSawIntruder()==0){
//            System.out.println("remove counter for intruder");
            stayInNode = false;
        }

        if(super.getLastTimeSawIntruder()==1){
            System.out.println("remove counter for intruder");
        }

//        if(!percepts.getSounds().getAll().isEmpty()){
//            this.mode = "guard";
//            switchOffGuardMode = false;
//        }
        if (switchOffGuardMode && this.mode.equals("guard")) {
            this.mode = "graph";
            clearQueue = true;
            System.out.println("Switching off guard mode");
            keepGoing = false;
        }

        // TODO: account for the intruder

        if (this.mode.equals("guard")) {
            System.out.println("entering guard mode");
            super.explore(percepts);

        } else {
            if (Math.random() < 0.1 && getDroppedPheromone()==0) {
                //addActionToQueue(new DropPheromone(SmellPerceptType.values()[(int) (Math.random() * SmellPerceptType.values().length)]), percepts);
                dropPheromone(percepts, SmellPerceptType.Pheromone1);
                setDroppedPheromone(500);
                return;
                //System.out.println("Random pheromone drop");
            }

            if (!percepts.wasLastActionExecuted()) {
                //cannot reach node so remove it from the map
                removeUnreachableNode();
                super.moveParallelToWall(percepts,percepts.getVision().getObjects().getAll());
                return;
//                addActionToQueue(new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble())), percepts);

            } else {
                double maxMovementDistance = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts);
//                for(Node n: nodes){
//                    //System.out.println(n.getCenter());
//                }
                Node previousNode = previousNodeVisited;
                Node nextNode = chooseNextNode();
                if(previousNodeVisited!=null&&nextNode!=null){
                    if(nextNode.equals(previousNode)){
                        System.out.println(nextNode.equals(previousNode));
                    }
//                    System.out.println(nextNode.equals(previousNode));
                }
//                System.out.println(nextNode == null);
                if (nextNode != null) {
//                    System.out.println("kak");
                    moveToNode(nextNode, percepts, maxMovementDistance);
                    return;
                }

                GuardAction toExploreZone=leaveExploredZone(percepts);
                if(toExploreZone!=null)
                {
                    addActionToQueue(toExploreZone,percepts);
                    return;
                }

                if (percepts.getAreaPercepts().isInDoor() && getDroppedPheromone() == 0) {
//                    System.out.println("door: drop pheromone type 2");
                    dropPheromone(percepts, SmellPerceptType.Pheromone2);
                    super.setDroppedPheromone(500);
                    return;
                }

                if (percepts.getAreaPercepts().isInWindow() && getDroppedPheromone() == 0) {
//            //System.out.println("window: drop pheromone type 2");
                    dropPheromone(percepts, SmellPerceptType.Pheromone2);
                    super.setDroppedPheromone(500);
                    return;
                }

                if (percepts.getAreaPercepts().isJustTeleported() && getDroppedPheromone() == 0) {
//            //System.out.println("teleported: drop pheromone type 2");
                    dropPheromone(percepts, SmellPerceptType.Pheromone2);
                    super.setDroppedPheromone(500);
                    return;
                }

            }
        }
    }

    /**
     * Chooses the node with maximum utility (i.e. a trade off between highest idleness and time it takes to get there)
     *
     * @return The next node to visit based on idleness
     */
    private Node chooseNextNode() {
        if (stayInNode){
            return previousNodeVisited;
        }
        Node nextNode = null;
        double utility = 0;
        if(previousNodeVisited!=null) {
            for (Node n : previousNodeVisited.getNeighbours()) {
                double distance = this.previousNodeVisited.getCenter().getDistance(n.getCenter()).getValue();
//            if (distance < 2 * radius) {
                if (distance > epsilon && n.getNodeIdleness() >= utility) {
                    nextNode = n;
                    utility = n.getNodeIdleness();
                }
//            }
            }
        }
        this.nextNode = nextNode;
        if (nextNode == null) return nextNode;
        objectsOnTheWay wallsOnTheWay = getObjectsOnTheWay(nextNode, ObjectPerceptType.Wall);
        objectsOnTheWay doorsOntheWay = getObjectsOnTheWay(nextNode, ObjectPerceptType.Door);
        objectsOnTheWay windowsOntheWay = getObjectsOnTheWay(nextNode, ObjectPerceptType.Window);
        if(wallsOnTheWay.perceptList.size() > maxWallsOntheWay && doorsOntheWay.perceptList.isEmpty() && windowsOntheWay.perceptList.isEmpty()){
            removeUnreachableNode();
            previousNodeVisited.getNeighbours().remove(nextNode);
            return chooseNextNode();
        }
        else
            return nextNode;
    }

    private void removeUnreachableNode() {
//        //System.out.println("biem");
//        //System.out.println(nodes.size());
        nodes.remove(nextNode);
//        //System.out.println(nodes.size());
        nextNode = null;
    }

    private objectsOnTheWay getObjectsOnTheWay(Node node, ObjectPerceptType op){
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

        ArrayList<Point> perceptList = new ArrayList<>();
        if (node.getObjectMap().keySet().contains(op)) {
            for (Point p : node.getObjectMap().get(op)) {
                if (doorOrWindowsOnTheWay(angleRanges, directionKey, p)) perceptList.add(p);
            }
        }

        return new objectsOnTheWay(directionKey, angleRanges, perceptList);
    }

    /**
     * Not done
     *
     * @param node
     */

    //TODO: check if smelling pheromone of type 2 when getting very close to the door and if so, turn around
    private void moveToNode(Node node, GuardPercepts percepts, double maxMovementDistance) {
        ////System.out.println("Moving to node");
        //check doors/windows


        if (node.getObjectMap().keySet().contains(ObjectPerceptType.Door) ||
                node.getObjectMap().keySet().contains(ObjectPerceptType.Window)) {
            System.out.println("node contains door or window");

            objectsOnTheWay objectsOnTheWay = getObjectsOnTheWay(node, ObjectPerceptType.Door);

            if (objectsOnTheWay.perceptList.isEmpty())
                objectsOnTheWay = getObjectsOnTheWay(node, ObjectPerceptType.Window);


            Angle angleToPassable = Angle.fromRadians(0);
            outerloop:
            for (ObjectPercept o : percepts.getVision().getObjects().getAll()) {
                Point q = new Point(o.getPoint().getX() + position.getX(), o.getPoint().getY() + position.getY());
                if ((o.getType() == ObjectPerceptType.Door || o.getType() == ObjectPerceptType.Window) &&
                        doorOrWindowsOnTheWay(objectsOnTheWay.angleRanges, objectsOnTheWay.directionKey, q)){
                    //System.out.println("Switching to guard mode to go through door/window");
                    for(SmellPercept pheromone : smelledPheromone)
                    {

                        if(pheromone.getType().equals(SmellPerceptType.Pheromone2))
                        {
                            addActionToQueue(new Rotate(Angle.fromRadians(Math.PI / 4)),percepts);
                            break outerloop;
                        }
                    }
                    this.mode = "guard";
                    this.guardTargetNode = node;
                    break;
                }
            }

            if (!this.mode.equals("guard")) {
                if(objectsOnTheWay.perceptList.isEmpty()){
                    //System.out.println("Moving");
                    Angle angleToNextNode = getRelativeAngle(new Point(node.getCenter().getX() - this.position.getX(),
                            node.getCenter().getY() - this.position.getY()), this.angle);
                    if (angleToNextNode.getRadians() > 0.05)
                        addActionToQueue(new Rotate(angleToNextNode), percepts);
                }
                else
                {

                    angleToPassable = this.getRelativeAngle(new Point(objectsOnTheWay.perceptList.get(0).getX() - position.getX(),
                            objectsOnTheWay.perceptList.get(0).getY() - position.getY()), this.angle);
                    //System.out.println("Angle to door: " + angleToPassable.getDegrees());
                    if(angleToPassable.getRadians() > 0.05)
                        addActionToQueue(new Rotate(angleToPassable), percepts);

                }

                this.keepGoing = true;
                addActionToQueue(new Move(new Distance(1)), percepts);
            }


        }



        else {
//             //System.out.println("No doors. No Windows.");
            //determine direction
            Angle angleToNextNode = getRelativeAngle(new Point(node.getCenter().getX() - this.position.getX(),
                    node.getCenter().getY() - this.position.getY()), this.angle);
            if (angleToNextNode.getRadians() > 0.05)
                addActionToQueue(new Rotate(angleToNextNode), percepts);
            addActionToQueue(new Move(new Distance(1)), percepts);
        }
    }

    private GuardAction leaveExploredZone(GuardPercepts p) {

            for (SmellPercept sp : smelledPheromone)
                if (sp.getType().toString().equals("Pheromone1"))
                    return new Rotate(Angle.fromRadians(Math.PI / 2));

        return null;
    }

    @Override
    protected void dropPheromone(GuardPercepts p, SmellPerceptType type) {
        DropPheromone action = new DropPheromone(type);
        addActionToQueue(action, p);
        if(type.equals("Pheromone1")) myPheromone.put(position, 5);


    }

    /**
     * Checks if there is a door or a Window on the way
     *
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
     * @param p Any point relative to the spawn position
     * @param a Any angle relative to the original orientation of the agent
     * @return The angle between the point when interpreted as a vector and angle a
     */
    private Angle getRelativeAngle(Point p, Angle a) {
        Angle pointAngle = Angle.fromRadians(Math.atan2(p.getY(), p.getX()));
        return convertAngleToNegative(Angle.fromRadians(pointAngle.getRadians() - a.getRadians()));
    }

    /**
     * @param p1 Any point relative to the spawn position
     * @param p2 Any point relative to the spawn position
     * @return The angle between the two points when interpreted as vectors
     */
    private Angle getRelativeAngle(Point p1, Point p2) {
        Angle pointAngle1 = Angle.fromRadians(Math.atan2(p1.getY(), p1.getX()));
        Angle pointAngle2 = Angle.fromRadians(Math.atan2(p2.getY(), p2.getX()));
        return  convertAngleToNegative(Angle.fromRadians(pointAngle2.getRadians() - pointAngle1.getRadians()));

    }

    /**
     * @param p1 Any point relative to the spawn position
     * @param p2 Any point relative to the spawn position
     * @return The angle of a vector between the two points from the x-axis of the spawn location
     */
    private Angle vectorAngle(Point p1, Point p2) {
        return convertAngleToNegative(Angle.fromRadians(Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX())));
    }

    /**
     *
     * @param a An angle, potentially using a full circle of 2Pi radians
     * @return An angle from 0 to Pi or -Pi to 0 radians
     */
    private Angle convertAngleToNegative(Angle a){
        if(a.getRadians() > Math.PI)
            return Angle.fromRadians(a.getRadians() - 2*Math.PI);
        else if(a.getRadians() < -Math.PI)
            return Angle.fromRadians(a.getRadians() + 2*Math.PI);
        return a;
    }

    private double getSpeedModifier(GuardPercepts guardPercepts) {
        SlowDownModifiers slowDownModifiers = guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers();
        if (guardPercepts.getAreaPercepts().isInWindow()) {
            return slowDownModifiers.getInWindow();

        } else if (guardPercepts.getAreaPercepts().isInSentryTower()) {
            return slowDownModifiers.getInSentryTower();

        } else if (guardPercepts.getAreaPercepts().isInDoor()) {
            return slowDownModifiers.getInDoor();
        }

        return 1;
    }
    public static double getDistance(Point x, Point y) {
        return Math.sqrt(Math.pow(x.getX() - y.getX(), 2) + Math.pow(x.getY() - y.getY(), 2));
    }

    private class objectsOnTheWay{

        public String directionKey;
        public HashMap<String, Angle[]> angleRanges;
        public List<Point> perceptList;

        objectsOnTheWay(String directionKey, HashMap<String, Angle[]> angleRanges, List<Point> perceptList){
            this.directionKey = directionKey;
            this.angleRanges = angleRanges;
            this.perceptList = perceptList;
        }
    }

}
