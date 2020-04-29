
//What the triangulation method has to return
//What class or method will call the Intrude1's triangulation method

package Group9.agent.Intruder;

import Group9.agent.container.IntruderContainer;
import Group9.math.Vector2;
import Interop.Action.IntruderAction;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.VisionPrecepts;

public class Intruder1 implements Intruder {
    Cell position = new Cell();
    History history = new History();
    

    /*
    //Attribute
    Cell position;

    //Constructor
    public Intruder1(Cell startPosition)
    {
        this.position = startPosition;
    }
    */

     
    //////////////////////////////
    //Main
    /*
    Cell target = new Cell(); Object address of memory
    target.setX(3.57688);
    target.setY(3.50009);

    Intruder1 I1 = new Intruder1();

    I1.triangulation(target);   target --> address of Cell Object
    //from here on the object is in target
     */
    ////////////////////////////////

    /*
    //Triangulation method
    public double triangulation(Cell target_coords) { //target_coords --> Cell Object same of target
        //from here on the object is in target_coords
        double target_x = target_coords.getX();
        double target_y = target_coords.getY();

        double my_x = this.position.getX();
        double my_y = this.position.getY();

        double distance_x = Math.abs(target_x - my_x);
        double distance_y = Math.abs(target_y - my_y);
        double distance_to_target = Math.sqrt(distance_x*distance_x + distance_y*distance_y);
        return distance_to_target;
    }
    */


    //Methods
    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        //building the "mind-map" of the building
        Direction direction = percepts.getTargetDirection();
        Angle alpha = percepts.getVision().getFieldOfView().getViewAngle();
        Distance range = percepts.getVision().getFieldOfView().getRange();
        return null;
    }
}
