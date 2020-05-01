//package Group8.Agent;
//
//import Group8.Controller.Utils.Area;
//import Group8.Controller.Utils.TelePortal;
//import Interop.Action.Move;
//import Interop.Action.NoAction;
//import Interop.Action.Rotate;
//import Interop.Action.Sprint;
//import Interop.Geometry.Angle;
//import Interop.Geometry.Direction;
//import Interop.Geometry.Distance;
//import Interop.Geometry.Point;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.paint.Color;
//
///**
// * Class RandomAgent builds a simple random exploration agent which will be the basis for Phase 2 when we implement the percepts
// */
//
//public class RandomAgent{
//    //Basic movesets.  Does not include Yell and Pheromone yet.
//    private Move move;
//    private Rotate rotate;
//    private Sprint sprint;
//    private NoAction noAction;
//
//    //Environment mappings.
//    private Area area;
//    private TelePortal telePortal;
//
//    //agent geometries
//    private Point point;
//    private Angle angle;
//    private Direction direction;
//    private Distance distance;
//
//    private boolean isHit;
//
//    /**
//     * @param isHit where agent has collided with a wall
//     */
//    public RandomAgent(boolean isHit) {
//        this.isHit = isHit;
//    }
//
//    /**
//     *
//     * @param startingPostion initial stating point of agent
//     * @param startingAngle initial starting angle of agent
//     * @param startingDistance initial speed of agent
//     * @param startingDirection initial direction of agent
//     * @param isHit checks if the next time step is a a wall.
//     */
//    public RandomAgent(Point startingPostion, Angle startingAngle, Distance startingDistance, Direction startingDirection, boolean isHit) {
//        this.point = startingPostion;
//        this.angle = startingAngle;
//        this.distance = startingDistance;
//        this.direction = startingDirection;
//
//        this.isHit = isHit;
//
//        this.move = new Move(distance, direction);
//    }
//
//    /**
//     * @param dis sets a new Distance for the agent
//     * @param dir rotates by this angle should agent have collided with obstacle
//     * @param angle a new angle for direction dir
//     */
//    public void changeDirection(Distance dis, Direction dir, Angle angle) {
//        if(isHit) {
//            rotate = new Rotate(angle);
//            move = new Move(dis, dir);
//        } else {
//            move = new Move(dis, dir);
//        }
//    }
//
//    /**
//     *
//     * @param gc is the render method for the visual.  This allows us to use Timeline or just use a simple tick method in the GUI.
//     */
//    public void render(GraphicsContext gc) {
//        //adjust as necessary for size, color, etch.
//        gc.setFill(Color.GREEN);
//        gc.setStroke(Color.BLUE);
//    }
//
//}