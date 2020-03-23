package Group6.Geometry;

import Group6.Agent.Factory.RandomAgentFactory;
import Group6.Agent.Guard.RandomGuard;
import Group6.ExtendedUnitTest;
import Group6.WorldState.GuardState;
import Interop.Agent.Guard;
import Interop.Percept.Vision.FieldOfView;
import SimpleUnitTest.SimpleUnitTest;

import java.util.List;

public class RayCastTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nRay Cast Test\n");

        it("allows to generate rays 4 rays in all 4 directions", () -> {

            FieldOfView fieldOfView = new FieldOfView(
                new Distance(10).toInteropDistance(),
                Angle.fromDegrees(360).toInteropAngle()
            );

            GuardState guardState = new GuardState(
                new RandomGuard(),
                new Point(0,0),   //set the agent position here.
                Direction.fromDegrees(0)//set the agent direction here.
            );

            List<LineSegment> rays = RayCast.generateRays(guardState, fieldOfView, 4);

            assertEqual(rays.size(), 4);
            assertEqual(rays.get(0), new LineSegment(new Point(0,0), new Point(0, -10)));
            assertEqual(rays.get(1), new LineSegment(new Point(0,0), new Point(-10, 0)));
            assertEqual(rays.get(2), new LineSegment(new Point(0,0), new Point(0, 10)));
            assertEqual(rays.get(3), new LineSegment(new Point(0,0), new Point(10, 0)));

        });

        it("allows to generate rays in all 4 direction when an agent is moved", () -> {

            FieldOfView fieldOfView = new FieldOfView(
                new Distance(10).toInteropDistance(),
                Angle.fromDegrees(360).toInteropAngle()
            );

            GuardState guardState = new GuardState(
                new RandomGuard(),
                new Point(7,11),   //set the agent position here.
                Direction.fromDegrees(0)//set the agent direction here.
            );

            List<LineSegment> rays = RayCast.generateRays(guardState, fieldOfView, 4);

            assertEqual(rays.size(), 4);

            assertEqual(
                rays.get(0),
                new LineSegment(
                    guardState.getLocation(),
                    new Point(0, -10).add(guardState.getLocation()).toPoint()
                )
            );

            assertEqual(
                rays.get(1),
                new LineSegment(
                    guardState.getLocation(),
                    new Point(-10, 0).add(guardState.getLocation()).toPoint()
                )
            );

            assertEqual(
                rays.get(2),
                new LineSegment(
                    guardState.getLocation(),
                    new Point(0, 10).add(guardState.getLocation()).toPoint()
                )
            );

            assertEqual(
                rays.get(3),
                new LineSegment(
                    guardState.getLocation(),
                    new Point(10, 0).add(guardState.getLocation()).toPoint()
                )
            );

        });

        it("allows to generate rays in all 4 direction when an agent is rotated", () -> {

            FieldOfView fieldOfView = new FieldOfView(
                new Distance(10).toInteropDistance(),
                Angle.fromDegrees(360).toInteropAngle()
            );

            GuardState guardState = new GuardState(
                new RandomGuard(),
                new Point(0,0),   //set the agent position here.
                Direction.fromDegrees(90)//set the agent direction here.
            );

            List<LineSegment> rays = RayCast.generateRays(guardState, fieldOfView, 4);

            assertEqual(rays.size(), 4);

            assertEqual(
                rays.get(3),
                new LineSegment(
                    guardState.getLocation(),
                    new Point(0, -10).add(guardState.getLocation()).toPoint()
                )
            );

            assertEqual(
                rays.get(0),
                new LineSegment(
                    guardState.getLocation(),
                    new Point(-10, 0).add(guardState.getLocation()).toPoint()
                )
            );

            assertEqual(
                rays.get(1),
                new LineSegment(
                    guardState.getLocation(),
                    new Point(0, 10).add(guardState.getLocation()).toPoint()
                )
            );

            assertEqual(
                rays.get(2),
                new LineSegment(
                    guardState.getLocation(),
                    new Point(10, 0).add(guardState.getLocation()).toPoint()
                )
            );

        });

    }

}




