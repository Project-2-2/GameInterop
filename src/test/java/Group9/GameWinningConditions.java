package Group9;

import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.agent.factories.DummyAgentFactory;
import Group9.map.GameMap;
import Group9.map.GameSettings;
import Group9.map.ViewRange;
import Group9.map.objects.MapObject;
import Group9.map.objects.Spawn;
import Group9.map.objects.TargetArea;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.Scenario.GameMode;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import SimpleUnitTest.SimpleUnitTest;

import java.util.ArrayList;
import java.util.List;

public class GameWinningConditions extends SimpleUnitTest {

    public static void main(String[] args) {
        it("GameWinningConditions::CaptureAllIntruders::<guard_captures_intruder>",
                GameWinningConditions::_test_capture_all_intruders_guard_captures_intruder);
        it("GameWinningConditions::CaptureAllIntruders::<guard_captures_two_moving_intruders>",
                GameWinningConditions::_test_capture_all_intruders_guard_captures_two_seperate_intruders);
        it("GameWinningConditions::CaptureAllIntruders::<intruder_wins_in_three_turns>",
                () -> _test_intruder_wins_in_three_rounds(GameMode.CaptureAllIntruders));
        it("GameWinningConditions::CaptureOneIntruder::<guard_captures_one_of_two_intruder>",
                GameWinningConditions::_test_capture_one_intruder_guard_captures_intruder);
        it("GameWinningConditions::CaptureOneIntruder::<intruder_wins_in_three_turns>",
                () -> _test_intruder_wins_in_three_rounds(GameMode.CaptureOneIntruder));
    }

    private static void _test_capture_all_intruders_guard_captures_intruder() {
        ScenarioPercepts scenarioPercepts = new ScenarioPercepts(GameMode.CaptureAllIntruders, new Distance(6),
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

        // --- out of reach
        objects.add(new TargetArea(new PointContainer.Polygon(
                new Vector2(100, 0), new Vector2(100, 1), new Vector2(101, 1), new Vector2(101, 0)
        )));

        GameMap gameMap = new GameMap(new GameSettings(scenarioPercepts, 100, 100, one, 3,
                one, one, 1, 1, 1, new Distance(6), one, new Distance(6),
                one, new ViewRange(0, 1), one, one, one, one, Angle.fromRadians(Math.PI / 2), 45, 1), objects);
        Game game = new Game(gameMap, new DummyAgentFactory(true), true);

        IntruderContainer intruder = game.getIntruders().get(0);
        intruder.moveTo(new Vector2(0.5, 2));

        GuardContainer guard = game.getGuards().get(0);
        guard.moveTo(new Vector2(0.5, 0));

        assertTrue(game.turn() == Game.Team.GUARDS);
        assertTrue(intruder.isCaptured());
        assertTrue(game.getWinner() == Game.Team.GUARDS);
    }

    private static void _test_capture_all_intruders_guard_captures_two_seperate_intruders() {
        ScenarioPercepts scenarioPercepts = new ScenarioPercepts(GameMode.CaptureAllIntruders, new Distance(6),
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

        // --- out of reach
        objects.add(new TargetArea(new PointContainer.Polygon(
                new Vector2(100, 0), new Vector2(100, 1), new Vector2(101, 1), new Vector2(101, 0)
        )));

        GameMap gameMap = new GameMap(new GameSettings(scenarioPercepts, 100, 100, one, 3,
                one, one, 1, 1, 2, new Distance(6), one, new Distance(6),
                one, new ViewRange(0, 1), one, one, one, one, Angle.fromRadians(Math.PI / 2), 45, 1), objects);
        Game game = new Game(gameMap, new DummyAgentFactory(true), true);

        // spawn intruders and move on away, so it cannot be captured right away
        IntruderContainer intruderA = game.getIntruders().get(0);
        IntruderContainer intruderB = game.getIntruders().get(1);

        intruderA.moveTo(new Vector2(0.5, 2));
        intruderB.moveTo(new Vector2(50, 50));

        // spawn guard
        GuardContainer guard = game.getGuards().get(0);
        guard.moveTo(new Vector2(0.5, 0));

        assertTrue(game.turn() == null);
        assertTrue(intruderA.isCaptured());
        assertTrue(!intruderB.isCaptured());

        // move intruder back so it can be captured in the next turn
        intruderB.moveTo(new Vector2(0, 2));

        assertTrue(game.turn() == Game.Team.GUARDS);
        assertTrue(intruderA.isCaptured());
        assertTrue(intruderB.isCaptured());
    }

    private static void _test_intruder_wins_in_three_rounds(GameMode gameMode) {
        ScenarioPercepts scenarioPercepts = new ScenarioPercepts(gameMode, new Distance(6),
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

        // --- out of reach
        objects.add(new TargetArea(new PointContainer.Polygon(
                new Vector2(50, 0), new Vector2(50, 1), new Vector2(51, 1), new Vector2(51, 0)
        )));

        GameMap gameMap = new GameMap(new GameSettings(scenarioPercepts, 100, 100, one, 3,
                one, one, 1, 0, 1, new Distance(6), one, new Distance(6),
                one, new ViewRange(0, 1), one, one, one, one, Angle.fromRadians(Math.PI / 2), 45, 1), objects);
        Game game = new Game(gameMap, new DummyAgentFactory(true), true);

        final Vector2 inside = new Vector2(50.5, 0.5);
        final Vector2 outside = new Vector2.Origin();

        // spawn intruder
        IntruderContainer intruder = game.getIntruders().get(0);

        assertTrue(game.turn() == null);
        assertEqual(intruder.getZoneCounter(), 0);

        // move inside
        intruder.moveTo(inside);

        assertTrue(game.turn() == null);
        assertEqual(intruder.getZoneCounter(), 1);

        // move inside
        intruder.moveTo(inside);

        assertTrue(game.turn() == null);
        assertEqual(intruder.getZoneCounter(), 2);

        // move outside
        intruder.moveTo(outside);

        assertTrue(game.turn() == null);
        assertEqual(intruder.getZoneCounter(), 0);

        // move inside and win
        intruder.moveTo(inside);

        game.turn();
        game.turn();
        assertTrue(game.turn() == Game.Team.INTRUDERS);
        assertEqual(intruder.getZoneCounter(), 3);
    }

    private static void _test_capture_one_intruder_guard_captures_intruder() {
        ScenarioPercepts scenarioPercepts = new ScenarioPercepts(GameMode.CaptureOneIntruder, new Distance(6),
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

        // --- out of reach
        objects.add(new TargetArea(new PointContainer.Polygon(
                new Vector2(100, 0), new Vector2(100, 1), new Vector2(101, 1), new Vector2(101, 0)
        )));

        GameMap gameMap = new GameMap(new GameSettings(scenarioPercepts, 100, 100, one, 3,
                one, one, 1, 1, 2, new Distance(6), one, new Distance(6),
                one, new ViewRange(0, 1), one, one, one, one, Angle.fromRadians(Math.PI / 2), 45, 1), objects);
        Game game = new Game(gameMap, new DummyAgentFactory(true), true);

        IntruderContainer intruderA = game.getIntruders().get(0);
        IntruderContainer intruderB = game.getIntruders().get(1);

        intruderA.moveTo(new Vector2(0.5, 2));
        intruderB.moveTo(new Vector2(50, 50));

        GuardContainer guard = game.getGuards().get(0);
        guard.moveTo(new Vector2(0.5, 0));

        assertTrue(game.turn() == Game.Team.GUARDS);
        assertTrue(game.getWinner() == Game.Team.GUARDS);
        assertTrue(intruderA.isCaptured());
        assertTrue(!intruderB.isCaptured());
    }
}
