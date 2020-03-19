package Group6.Geometry;

import Group6.Agent.Factory.AgentFactory;
import Group6.Agent.Factory.RandomAgentFactory;
import Group6.Agent.Guard.RandomGuard;
import Group6.Agent.RandomAgent;
import Group6.Examples.Percepts.GuardPerceptsBuilder;
import Group6.WorldState.AgentState;
import Group6.WorldState.GuardState;
import Interop.Action.Action;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Vision.FieldOfView;
import SimpleUnitTest.SimpleUnitTest;
import jdk.dynalink.linker.support.Guards;

import java.util.List;

public class ReyCastTest extends SimpleUnitTest {

    public static void main(String[] args) {
        //create field of view
        Distance range = new Distance(10);          //set the length of the viewField here.
        Angle viewAngle = Angle.fromDegrees(360);   //set the degree of the viewField here.
        FieldOfView fieldOfView = new FieldOfView(range.toInteropDistance(), viewAngle.toInteropAngle());

        //create Guard
        RandomAgentFactory agentFactory = new RandomAgentFactory();
        List<Guard> guards = agentFactory.createGuards(1);
        Guard guard = guards.get(0);

        //create Guard State
        GuardState guardState = new GuardState(
                guard,
                new Point(0,0),   //set the agent position here.
                Direction.fromDegrees(0)//set the agent direction here.
        );


        ReyCast reyCast = new ReyCast(guardState, fieldOfView);
        System.out.println(reyCast);

    }

}




