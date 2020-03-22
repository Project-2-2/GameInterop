package Group6.Geometry;

import Group6.Agent.Factory.RandomAgentFactory;
import Group6.Agent.Guard.RandomGuard;
import Group6.WorldState.GuardState;
import Interop.Agent.Guard;
import Interop.Percept.Vision.FieldOfView;
import SimpleUnitTest.SimpleUnitTest;

import java.util.List;

public class ReyCastTest extends SimpleUnitTest {

    public static void main(String[] args) {
        //create field of view
        Distance range = new Distance(10);          //set the length of the viewField here.
        Angle viewAngle = Angle.fromDegrees(360);   //set the degree of the viewField here.
        FieldOfView fieldOfView = new FieldOfView(range.toInteropDistance(), viewAngle.toInteropAngle());

        //create Guard State
        GuardState guardState = new GuardState(
                new RandomGuard(),
                new Point(0,0),   //set the agent position here.
                Direction.fromDegrees(0)//set the agent direction here.
        );

        System.out.println(RayCast.generateRays(guardState, fieldOfView, 4));

    }

}




