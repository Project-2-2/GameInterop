package Group6.Percept.Vision;

import Group6.Agent.Guard.RandomGuard;
import Group6.Agent.Intruder.RandomIntruder;
import Group6.ExtendedUnitTest;
import Group6.Geometry.*;
import Group6.Geometry.Collection.Points;
import Group6.Percept.Vision.Ray;
import Group6.Percept.Vision.Rays;
import Group6.WorldState.Object.GuardState;
import Group6.WorldState.Object.IntruderState;
import Group6.WorldState.Object.WorldStateObjects;
import Interop.Percept.Vision.FieldOfView;

import java.util.List;

public class RaysTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nRays Test\n");

        generateRaysTests();
        getObjectPerceptsTests();

    }

    private static void getObjectPerceptsTests() {

        xit("allows to get percepts", () -> {

            FieldOfView fieldOfView = new FieldOfView(
                new Distance(10).toInteropDistance(),
                Angle.fromDegrees(360).toInteropAngle()
            );

            GuardState guardState = new GuardState(
                new RandomGuard(),
                new Point(0,0),
                Direction.fromDegrees(0)
            );

            IntruderState intruderState = new IntruderState(
                new RandomIntruder(),
                new Point(0,5.5),
                Direction.fromDegrees(0)
            );

            Rays guardRays = new Rays(guardState, fieldOfView, 4);
            ObjectPercepts guardPercepts = guardRays.getObjectPercepts(
                new WorldStateObjects(intruderState)
            );

            assertEqual(guardPercepts.toPoints(), new Points(new Point(0, 5)));

        });

    }

    private static void generateRaysTests() {

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

            List<LineSegment> rays = new Rays(guardState, fieldOfView, 4).toLineSegments();

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

            List<LineSegment> rays = new Rays(guardState, fieldOfView, 4).toLineSegments();

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

            List<LineSegment> rays = new Rays(guardState, fieldOfView, 4).toLineSegments();

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




