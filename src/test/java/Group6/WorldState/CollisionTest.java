package Group6.WorldState;

import Group6.Agent.Guard.RandomGuard;
import Group6.ExtendedUnitTest;
import Group6.Geometry.Direction;
import Group6.Geometry.Distance;
import Group6.Geometry.Point;
import Group6.GroupTests;
import Group6.WorldState.Object.AgentState;
import Group6.WorldState.Object.GuardState;
import Interop.Agent.Guard;


public class CollisionTest extends ExtendedUnitTest {
    public static void main(String [] args){

        Scenario scenario= new Scenario(GroupTests.resources + "/scenario.txt");
        System.out.println("COLLISION TEST");
        it("checks end collison",()->{
            //COLLISION WHEN END STATE
            Distance distance= new Distance(10);
            Point point= new Point(30,10);

            AgentState agent= new GuardState(new RandomGuard(),point,Direction.fromRadians(1));

            Collision collision= new Collision(agent,distance,scenario);
            //System.out.println(collision.checkCollision());

            assertTrue(collision.checkCollision());
        });


        it("checks inbetween collision", ()->{
            Distance distance= new Distance(100);
            Point point= new Point(10,30);

            AgentState agent= new GuardState(new RandomGuard(),point,Direction.fromRadians(0));

            Collision collision= new Collision(agent,distance,scenario);
            //System.out.println(collision.checkCollision());

            assertTrue(collision.checkCollision());
        });
    }

}
