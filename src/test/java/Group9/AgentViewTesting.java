package Group9;

import Group9.agent.container.AgentContainer;
import Group9.agent.factories.DummyAgentFactory;
import Group9.map.GameMap;
import Group9.map.GameSettings;
import Group9.map.ViewRange;
import Group9.map.objects.*;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.Scenario.GameMode;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import SimpleUnitTest.SimpleUnitTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AgentViewTesting extends SimpleUnitTest {

    public static void main(String[] args) {
        it("GameMap#getObjectPerceptsForAgent (EmptySpace)", AgentViewTesting::_test_getObjectPerceptsForAgent_EmptySpace);
        it("GameMap#getObjectPerceptsForAgent (Wall)", AgentViewTesting::_test_getObjectPerceptsForAgent_Wall);
        it("GameMap#getObjectPerceptsForAgent (Window)", AgentViewTesting::_test_getObjectPerceptsForAgent_Window);
        it("GameMap#getObjectPerceptsForAgent (Door)", AgentViewTesting::_test_getObjectPerceptsForAgent_Door);
        it("GameMap#getObjectPerceptsForAgent (Teleport)", AgentViewTesting::_test_getObjectPerceptsForAgent_Teleport);
        it("GameMap#getObjectPerceptsForAgent (SentryTower)", AgentViewTesting::_test_getObjectPerceptsForAgent_SentryTower);
        it("GameMap#getObjectPerceptsForAgent (Guard -> TargetArea)", AgentViewTesting::_test_getObjectPerceptsForAgent_TargetArea);
        it("GameMap#getObjectPerceptsForAgent (Mix)", AgentViewTesting::_test_ObjectPerceptsForAgent_WithEmpty_Walls_Doors_Windows_SentryTower);
        it("GameMap#getObjectPerceptsForAgent (Guard <sees> Intruder)", AgentViewTesting::_test_getObjectPerceptsForAgent_Guard_Sees_Intruder);
        it("GameMap#getObjectPerceptsForAgent (Intruder <sees> Guard)", AgentViewTesting::_test_getObjectPerceptsForAgent_Intruder_Sees_Guard);
    }

    private static Game _createGame(double viewDistance, double viewAngle, int guards, int intruders)
    {
        ScenarioPercepts scenarioPercepts = new ScenarioPercepts(GameMode.CaptureAllIntruders, new Distance(1),
                Angle.fromRadians(1), new SlowDownModifiers(1, 1, 1),
                new Distance(1), 1);
        Distance one = new Distance(1);
        List<MapObject> objects = new ArrayList<>();

        objects.add(new Spawn.Guard(new PointContainer.Polygon(
                new Vector2.Origin(), new Vector2(0, 5), new Vector2(1, 5), new Vector2(1, 0)
        )));
        objects.add(new Spawn.Intruder(new PointContainer.Polygon(
                new Vector2.Origin(), new Vector2(0, 5), new Vector2(1, 5), new Vector2(1, 0)
        )));

        GameMap gameMap = new GameMap(new GameSettings(scenarioPercepts, 100, 100, one, 1,
                one, one, 1, guards, intruders, new Distance(viewDistance), one, new Distance(viewDistance),
                one, new ViewRange(0, 1), one, one,  one, one, Angle.fromRadians(viewAngle), 45, 1), objects);
        return new Game(gameMap, new DummyAgentFactory(false), true);
    }

    private static void _test_ObjectPerceptsForAgent_WithEmpty_Walls_Doors_Windows_SentryTower() {

        //---
        double viewDistance = 6;
        double viewAngle = Math.PI / 2D;
        //---

        Game game = _createGame(viewDistance, viewAngle, 1, 0);
        GameMap gameMap = game.getGameMap();

        AgentContainer<Guard> agentContainer = game.getGuards().get(0);
        agentContainer.moveTo(new Vector2.Origin());

        assertEqual(gameMap.getGameSettings().getGuardViewRangeNormal().getValue(), viewDistance, 0);
        assertEqual(gameMap.getGameSettings().getViewAngle().getRadians(), viewAngle, 0);

        // --- Test if agent only perceives Empty Space
        {
            Set<ObjectPercept> perception = gameMap.getObjectPerceptsForAgent(agentContainer, new FieldOfView(new Distance(viewDistance), Angle.fromRadians(viewAngle)), null);

            assertTrue(perception.stream().allMatch(e -> e.getType() == ObjectPerceptType.EmptySpace));
        }

        // --- Test if agent only perceives Empty Spaces, and Walls
        {
            gameMap.getObjects().add(new Wall(new PointContainer.Polygon(
                    new Vector2(-10, 4), new Vector2(-10, 6), new Vector2(3, 6), new Vector2(3, 4)
            )));

            Set<ObjectPercept> perception = gameMap.getObjectPerceptsForAgent(agentContainer, new FieldOfView(new Distance(viewDistance), Angle.fromRadians(viewAngle)), null);

            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.EmptySpace));
            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.Wall));
            assertTrue(perception.stream().allMatch(e -> e.getType() == ObjectPerceptType.EmptySpace || e.getType() == ObjectPerceptType.Wall));
        }

        // --- Test if agent only perceives Empty Spaces, Walls, and a Door
        {
            gameMap.getObjects().add(new Door(new PointContainer.Polygon(
                    new Vector2(3.01, 4), new Vector2(3.01, 6), new Vector2(10, 6), new Vector2(10, 4)
            ), 1, 1, 1, 1, 1));


            Set<ObjectPercept> perception = gameMap.getObjectPerceptsForAgent(agentContainer, new FieldOfView(new Distance(viewDistance), Angle.fromRadians(viewAngle)), null);

            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.EmptySpace));
            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.Wall));
            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.Door));
            assertTrue(perception.stream().allMatch(e -> e.getType() == ObjectPerceptType.EmptySpace || e.getType() == ObjectPerceptType.Wall
                || e.getType() == ObjectPerceptType.Door));

        }
    }


    private static void _test_getObjectPerceptsForAgent_EmptySpace() {
        double viewDistance = 6;
        double viewAngle = Math.PI / 2D;
        //---

        Game game = _createGame(viewDistance, viewAngle, 1, 0);
        GameMap gameMap = game.getGameMap();

        AgentContainer<Guard> agentContainer = game.getGuards().get(0);
        agentContainer.moveTo(new Vector2.Origin());

        assertEqual(gameMap.getGameSettings().getGuardViewRangeNormal().getValue(), viewDistance, 0);
        assertEqual(gameMap.getGameSettings().getViewAngle().getRadians(), viewAngle, 0);

        // --- Test if agent only perceives Empty Space
        {
            Set<ObjectPercept> perception = gameMap.getObjectPerceptsForAgent(agentContainer, new FieldOfView(new Distance(viewDistance), Angle.fromRadians(viewAngle)), null);
            assertTrue(perception.stream().allMatch(e -> e.getType() == ObjectPerceptType.EmptySpace));
        }
    }

    private static void _test_getObjectPerceptsForAgent_Wall() {
        double viewDistance = 6;
        double viewAngle = Math.PI / 2D;
        //---

        Game game = _createGame(viewDistance, viewAngle, 1, 0);
        GameMap gameMap = game.getGameMap();

        AgentContainer<Guard> agentContainer = game.getGuards().get(0);
        agentContainer.moveTo(new Vector2.Origin());

        assertEqual(gameMap.getGameSettings().getGuardViewRangeNormal().getValue(), viewDistance, 0);
        assertEqual(gameMap.getGameSettings().getViewAngle().getRadians(), viewAngle, 0);

        // --- Test if agent only perceives Empty Space
        {
            gameMap.getObjects().add(new Wall(new PointContainer.Polygon(
                    new Vector2(-10, 4), new Vector2(-10, 6), new Vector2(3, 6), new Vector2(3, 4)
            )));

            Set<ObjectPercept> perception = gameMap.getObjectPerceptsForAgent(agentContainer, new FieldOfView(new Distance(viewDistance), Angle.fromRadians(viewAngle)), null);

            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.EmptySpace));
            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.Wall));
            assertTrue(perception.stream().allMatch(e -> e.getType() == ObjectPerceptType.EmptySpace || e.getType() == ObjectPerceptType.Wall));
        }
    }

    private static void _test_getObjectPerceptsForAgent_Window() {
        double viewDistance = 6;
        double viewAngle = Math.PI / 2D;
        //---

        Game game = _createGame(viewDistance, viewAngle, 1, 0);
        GameMap gameMap = game.getGameMap();

        AgentContainer<Guard> agentContainer = game.getGuards().get(0);
        agentContainer.moveTo(new Vector2.Origin());

        assertEqual(gameMap.getGameSettings().getGuardViewRangeNormal().getValue(), viewDistance, 0);
        assertEqual(gameMap.getGameSettings().getViewAngle().getRadians(), viewAngle, 0);

        // --- Test if agent only perceives Empty Space
        {
            gameMap.getObjects().add(new Window(new PointContainer.Polygon(
                    new Vector2(-10, 4), new Vector2(-10, 6), new Vector2(3, 6), new Vector2(3, 4)
            ), 1, 1, 1, 1, 1));

            Set<ObjectPercept> perception = gameMap.getObjectPerceptsForAgent(agentContainer, new FieldOfView(new Distance(viewDistance), Angle.fromRadians(viewAngle)), null);

            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.EmptySpace));
            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.Window));
            assertTrue(perception.stream().allMatch(e -> e.getType() == ObjectPerceptType.EmptySpace || e.getType() == ObjectPerceptType.Window));
        }
    }

    private static void _test_getObjectPerceptsForAgent_Door() {
        double viewDistance = 6;
        double viewAngle = Math.PI / 2D;
        //---

        Game game = _createGame(viewDistance, viewAngle, 1, 0);
        GameMap gameMap = game.getGameMap();

        AgentContainer<Guard> agentContainer = game.getGuards().get(0);
        agentContainer.moveTo(new Vector2.Origin());

        assertEqual(gameMap.getGameSettings().getGuardViewRangeNormal().getValue(), viewDistance, 0);
        assertEqual(gameMap.getGameSettings().getViewAngle().getRadians(), viewAngle, 0);

        // --- Test if agent only perceives Empty Space
        {
            gameMap.getObjects().add(new Door(new PointContainer.Polygon(
                    new Vector2(-10, 4), new Vector2(-10, 6), new Vector2(3, 6), new Vector2(3, 4)
            ), 1, 1, 1, 1, 1));

            Set<ObjectPercept> perception = gameMap.getObjectPerceptsForAgent(agentContainer, new FieldOfView(new Distance(viewDistance), Angle.fromRadians(viewAngle)), null);

            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.EmptySpace));
            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.Door));
            assertTrue(perception.stream().allMatch(e -> e.getType() == ObjectPerceptType.EmptySpace || e.getType() == ObjectPerceptType.Door));
        }
    }

    private static void _test_getObjectPerceptsForAgent_Teleport() {
        double viewDistance = 6;
        double viewAngle = Math.PI / 2D;
        //---

        Game game = _createGame(viewDistance, viewAngle, 1, 0);
        GameMap gameMap = game.getGameMap();

        AgentContainer<Guard> agentContainer = game.getGuards().get(0);
        agentContainer.moveTo(new Vector2.Origin());

        assertEqual(gameMap.getGameSettings().getGuardViewRangeNormal().getValue(), viewDistance, 0);
        assertEqual(gameMap.getGameSettings().getViewAngle().getRadians(), viewAngle, 0);

        // --- Test if agent only perceives Empty Space
        {
            gameMap.getObjects().add(new TeleportArea(new PointContainer.Polygon(
                    new Vector2(-10, 4), new Vector2(-10, 6), new Vector2(3, 6), new Vector2(3, 4)
            ), null));

            Set<ObjectPercept> perception = gameMap.getObjectPerceptsForAgent(agentContainer, new FieldOfView(new Distance(viewDistance), Angle.fromRadians(viewAngle)), null);

            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.EmptySpace));
            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.Teleport));
            assertTrue(perception.stream().allMatch(e -> e.getType() == ObjectPerceptType.EmptySpace || e.getType() == ObjectPerceptType.Teleport));
        }
    }

    private static void _test_getObjectPerceptsForAgent_SentryTower() {
        double viewDistance = 6;
        double viewAngle = Math.PI / 2D;
        //---

        Game game = _createGame(viewDistance, viewAngle, 1, 0);
        GameMap gameMap = game.getGameMap();

        AgentContainer<Guard> agentContainer = game.getGuards().get(0);
        agentContainer.moveTo(new Vector2.Origin());

        assertEqual(gameMap.getGameSettings().getGuardViewRangeNormal().getValue(), viewDistance, 0);
        assertEqual(gameMap.getGameSettings().getViewAngle().getRadians(), viewAngle, 0);

        // --- Test if agent only perceives Empty Space
        {
            gameMap.getObjects().add(new SentryTower(new PointContainer.Polygon(
                    new Vector2(-10, 4), new Vector2(-10, 6), new Vector2(3, 6), new Vector2(3, 4)
            ), 1, null));

            Set<ObjectPercept> perception = gameMap.getObjectPerceptsForAgent(agentContainer, new FieldOfView(new Distance(viewDistance), Angle.fromRadians(viewAngle)), null);

            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.EmptySpace));
            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.SentryTower));
            assertTrue(perception.stream().allMatch(e -> e.getType() == ObjectPerceptType.EmptySpace || e.getType() == ObjectPerceptType.SentryTower));
        }
    }

    private static void _test_getObjectPerceptsForAgent_TargetArea() {
        double viewDistance = 6;
        double viewAngle = Math.PI / 2D;
        //---

        Game game = _createGame(viewDistance, viewAngle, 1, 0);
        GameMap gameMap = game.getGameMap();

        AgentContainer<Guard> agentContainer = game.getGuards().get(0);
        agentContainer.moveTo(new Vector2.Origin());

        assertEqual(gameMap.getGameSettings().getGuardViewRangeNormal().getValue(), viewDistance, 0);
        assertEqual(gameMap.getGameSettings().getViewAngle().getRadians(), viewAngle, 0);

        // --- Test if agent only perceives Empty Space
        {
            gameMap.getObjects().add(new TargetArea(new PointContainer.Polygon(
                    new Vector2(-10, 4), new Vector2(-10, 6), new Vector2(3, 6), new Vector2(3, 4)
            )));

            Set<ObjectPercept> perception = gameMap.getObjectPerceptsForAgent(agentContainer, new FieldOfView(new Distance(viewDistance), Angle.fromRadians(viewAngle)), null);

            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.EmptySpace));
            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.TargetArea));
            assertTrue(perception.stream().allMatch(e -> e.getType() == ObjectPerceptType.EmptySpace || e.getType() == ObjectPerceptType.TargetArea));
        }
    }

    private static void _test_getObjectPerceptsForAgent_Guard_Sees_Intruder() {
        double viewDistance = 6;
        double viewAngle = Math.PI / 2D;
        //---

        Game game = _createGame(viewDistance, viewAngle, 1, 1);
        GameMap gameMap = game.getGameMap();

        AgentContainer<Guard> guard = game.getGuards().get(0);
        AgentContainer<Intruder> intruder = game.getIntruders().get(0);

        guard.moveTo(new Vector2.Origin());
        intruder.moveTo(new Vector2(0, 2));

        assertEqual(gameMap.getGameSettings().getGuardViewRangeNormal().getValue(), viewDistance, 0);
        assertEqual(gameMap.getGameSettings().getViewAngle().getRadians(), viewAngle, 0);

        // --- Test if agent only perceives Empty Space
        {
            Set<ObjectPercept> perception = gameMap.getObjectPerceptsForAgent(guard, new FieldOfView(new Distance(viewDistance), Angle.fromRadians(viewAngle)), null);

            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.EmptySpace));
            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.Intruder));
            assertTrue(perception.stream().allMatch(e -> e.getType() == ObjectPerceptType.EmptySpace || e.getType() == ObjectPerceptType.Intruder));
        }
    }

    private static void _test_getObjectPerceptsForAgent_Intruder_Sees_Guard() {
        double viewDistance = 6;
        double viewAngle = Math.PI / 2D;
        //---

        Game game = _createGame(viewDistance, viewAngle, 1, 1);
        GameMap gameMap = game.getGameMap();

        AgentContainer<Guard> guard = game.getGuards().get(0);
        AgentContainer<Intruder> intruder = game.getIntruders().get(0);

        intruder.moveTo(new Vector2.Origin());
        guard.moveTo(new Vector2(0, 2));

        assertEqual(gameMap.getGameSettings().getIntruderViewRangeNormal().getValue(), viewDistance, 0);
        assertEqual(gameMap.getGameSettings().getViewAngle().getRadians(), viewAngle, 0);

        // --- Test if agent only perceives Empty Space
        {
            Set<ObjectPercept> perception = gameMap.getObjectPerceptsForAgent(intruder, new FieldOfView(new Distance(viewDistance), Angle.fromRadians(viewAngle)), null);

            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.EmptySpace));
            assertTrue(perception.stream().anyMatch(e -> e.getType() == ObjectPerceptType.Guard));
            assertTrue(perception.stream().allMatch(e -> e.getType() == ObjectPerceptType.EmptySpace || e.getType() == ObjectPerceptType.Guard));
        }
    }
}
