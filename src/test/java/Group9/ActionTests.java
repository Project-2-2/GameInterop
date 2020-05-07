package Group9;

import Group9.agent.container.AgentContainer;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.agent.factories.IAgentFactory;
import Group9.map.GameMap;
import Group9.map.GameSettings;
import Group9.map.ViewRange;
import Group9.map.dynamic.Pheromone;
import Group9.map.dynamic.Sound;
import Group9.map.objects.MapObject;
import Group9.map.objects.Spawn;
import Group9.map.objects.TargetArea;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Scenario.GameMode;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import SimpleUnitTest.SimpleUnitTest;

import java.util.*;

public class ActionTests extends SimpleUnitTest {

    public static void main(String[] args) {

        it("Action::Guard::DropPheromone", ActionTests::_test_guard_drop_pheromone);
        it("Action::Guard::Yell", ActionTests::_test_guard_yell);
        it("Action::Guard::Rotate", ActionTests::_test_guard_rotate);
        it("Action::Guard::Move", ActionTests::_test_guard_move);
        it("Action::Guard::<UnsupportedAction>", ActionTests::_test_guard_unsupported_action);
        it("Action::Intruder::DropPheromone", ActionTests::_test_intruder_drop_pheromone);
        it("Action::Intruder::Yell", ActionTests::_test_intruder_yell);
        it("Action::Intruder::Rotate", ActionTests::_test_intruder_rotate);
        it("Action::Guard::Move", ActionTests::_test_intruder_move_and_sprinting);
        it("Action::Intruder::<UnsupportedAction>", ActionTests::_test_intruder_unsupported_action);

    }

    private static Game _createGame(final List<Guard> guards, final List<Intruder> intruders)
    {

        ScenarioPercepts scenarioPercepts = new ScenarioPercepts(GameMode.CaptureAllIntruders, new Distance(1),
                Angle.fromRadians(Math.PI / 4), new SlowDownModifiers(1, 1, 1),
                new Distance(1), 3);
        Distance one = new Distance(1);
        List<MapObject> objects = new ArrayList<>();

        objects.add(new Spawn.Guard(new PointContainer.Polygon(
                new Vector2.Origin(), new Vector2(0, 10), new Vector2(1, 10), new Vector2(1, 0)
        )));
        objects.add(new Spawn.Intruder(new PointContainer.Polygon(
                new Vector2.Origin(), new Vector2(0, 10), new Vector2(1, 10), new Vector2(1, 0)
        )));

        objects.add(new TargetArea(new PointContainer.Polygon(
                new Vector2(50, 50), new Vector2(50, 55), new Vector2(51, 55), new Vector2(51, 50)
        )));

        GameMap gameMap = new GameMap(new GameSettings(scenarioPercepts, 100, 100, one, 1,
                one, new Distance(2), 3, guards.size(), intruders.size(), new Distance(6), one, new Distance(6),
                one, new ViewRange(0, 1), new Distance(5), one,  one, one, Angle.fromRadians(Math.PI / 4), 45, 4), objects);
        return new Game(gameMap, new IAgentFactory() {
            @Override
            public List<Intruder> createIntruders(int amount) {
                return intruders;
            }

            @Override
            public List<Guard> createGuards(int amount) {
                return guards;
            }
        }, false);
    }

    private static void _test_guard_drop_pheromone() {
        Game game = _createGame(new ArrayList<>() {{
            this.add(new QueueGuard(
                    new DropPheromone(SmellPerceptType.Pheromone1),
                    new DropPheromone(SmellPerceptType.Pheromone2),
                    new NoAction(),
                    new DropPheromone(SmellPerceptType.Pheromone4),
                    new DropPheromone(SmellPerceptType.Pheromone5)
            ));
        }}, new ArrayList<>());
        GuardContainer guard = game.getGuards().get(0);

        assertTrue(game.getGameMap().getDynamicObjects(Pheromone.class).isEmpty());

        // --- turn
        game.turn();

        {
            assertTrue(game.getActionSuccess().get(guard));
            assertTrue(!game.getGameMap().getDynamicObjects(Pheromone.class).isEmpty());
            Pheromone pheromone = (Pheromone) game.getGameMap().getDynamicObjects(Pheromone.class).get(0);

            assertTrue(pheromone.getType() == SmellPerceptType.Pheromone1);
            assertTrue(pheromone.getSource() == guard);
            assertEqual(pheromone.getCenter().distance(guard.getPosition()), 0, 0);
        }


        // --- turn - should be rejected because of cooldown
        game.turn();

        assertTrue(!game.getActionSuccess().get(guard));

        // --- turn - should be rejected because of overlap
        game.turn(); game.turn();

        assertTrue(!game.getActionSuccess().get(guard));

        // --- turn
        game.turn();

        {
            assertTrue(game.getActionSuccess().get(guard));
            assertTrue(!game.getGameMap().getDynamicObjects(Pheromone.class).isEmpty());
            Pheromone pheromone = (Pheromone) game.getGameMap().getDynamicObjects(Pheromone.class).get(0);

            assertTrue(pheromone.getType() == SmellPerceptType.Pheromone5);
            assertTrue(pheromone.getSource() == guard);
            assertEqual(pheromone.getCenter().distance(guard.getPosition()), 0, 0);
        }
    }

    private static void _test_guard_yell() {

        Game game = _createGame(new ArrayList<>() {{
            this.add(new QueueGuard(
                    new Yell()
            ));
            this.add(new QueueGuard(new NoAction()));
            this.add(new QueueGuard(new NoAction()));
        }}, new ArrayList<>());

        assertTrue(game.getGameMap().getDynamicObjects(Sound.class).isEmpty());

        {
            GuardContainer sender = game.getGuards().get(0);
            GuardContainer receiver = game.getGuards().get(1);

            receiver.moveTo(sender.getPosition().add(new Vector2.Random().normalise().mul(game.getGameMap().getGameSettings().getYellSoundRadius().getValue() * 0.8)));
        }

        // --- move agent way so he cannot hear the yell
        {
            GuardContainer farAway = game.getGuards().get(2);
            farAway.moveTo(new Vector2(100, 100));
        }


        // --- turn
        game.turn();

        {
            GuardContainer sender = game.getGuards().get(0);
            assertTrue(game.getActionSuccess().get(sender));
            assertTrue(!game.getGameMap().getDynamicObjects(Sound.class).isEmpty());
            Sound sound = (Sound) game.getGameMap().getDynamicObjects(Sound.class).get(0);

            assertTrue(sound.getType() == SoundPerceptType.Yell);
            assertTrue(sound.getSource() == sender);
            assertEqual(sound.getCenter().distance(sender.getPosition()), 0, 0);
        }

        {
            GuardContainer receiver = game.getGuards().get(1);
            Set<SoundPercept> sounds = ((QueueGuard)receiver.getAgent()).getPercepts().getSounds().getAll();
            assertEqual(sounds.size(), 1);
        }

        {
            GuardContainer farAway = game.getGuards().get(2);
            Set<SoundPercept> sounds = ((QueueGuard)farAway.getAgent()).getPercepts().getSounds().getAll();
            assertEqual(sounds.size(), 0);
        }
    }

    private static void _test_guard_rotate() {
        Game game = _createGame(new ArrayList<>() {{
            this.add(new QueueGuard(
                    new Rotate(Angle.fromRadians(Math.PI / 4)),
                    new Rotate(Angle.fromRadians(Math.PI / 4 + 0.1))
            ));
        }}, new ArrayList<>());

        GuardContainer guardContainer = game.getGuards().get(0);
        Vector2 originalDirection = guardContainer.getDirection().clone();

        // --- turn - should success because within max rotation limit
        game.turn();

        assertTrue(game.getActionSuccess().get(guardContainer));
        assertEqual(originalDirection.angle(guardContainer.getDirection()), Math.PI / 4, 1E-10);
        assertEqual(originalDirection.rotated(Math.PI / 4).angle(guardContainer.getDirection()), 0, 1E-10);

        // --- turn - should fail because rotation is more than limit
        game.turn();
        assertTrue(!game.getActionSuccess().get(guardContainer));
        assertEqual(originalDirection.angle(guardContainer.getDirection()), Math.PI / 4, 1E-10);
    }

    private static void _test_guard_move() {
        Game game = _createGame(new ArrayList<>() {{
            this.add(new QueueGuard(
                    new Move(new Distance(1)),
                    new Move(new Distance(1.1))
            ));
        }}, new ArrayList<>());

        GuardContainer guard = game.getGuards().get(0);
        Vector2 originalPosition = guard.getPosition().clone();

        // --- turn - valid within move distance
        game.turn();

        {
            assertTrue(game.getActionSuccess().get(guard));
            assertEqual(originalPosition.add(guard.getDirection().mul(1)).distance(guard.getPosition()), 0, 1E-9);
        }

        // --- turn - invalid, guards cannot sprint
        game.turn();

        {
            assertTrue(!game.getActionSuccess().get(guard));
            assertEqual(originalPosition.add(guard.getDirection().mul(1)).distance(guard.getPosition()), 0, 1E-9);
        }
    }

    private static void _test_guard_unsupported_action() {
        Game game = _createGame(new ArrayList<>() {{
            this.add(new QueueGuard(
                    new GuardAction() {}
            ));
        }}, new ArrayList<>());

        // --- turn
        Asserts.assertException(game::turn, IllegalArgumentException.class);
    }

    private static void _test_intruder_drop_pheromone() {
        Game game = _createGame(new ArrayList<>(), new ArrayList<>() {{
            this.add(new QueueIntruder(
                    new DropPheromone(SmellPerceptType.Pheromone1),
                    new DropPheromone(SmellPerceptType.Pheromone2),
                    new NoAction(),
                    new DropPheromone(SmellPerceptType.Pheromone4),
                    new DropPheromone(SmellPerceptType.Pheromone5)
            ));
        }});
        IntruderContainer intruder = game.getIntruders().get(0);

        assertTrue(game.getGameMap().getDynamicObjects(Pheromone.class).isEmpty());

        // --- turn
        game.turn();

        {
            assertTrue(game.getActionSuccess().get(intruder));
            assertTrue(!game.getGameMap().getDynamicObjects(Pheromone.class).isEmpty());
            Pheromone pheromone = (Pheromone) game.getGameMap().getDynamicObjects(Pheromone.class).get(0);

            assertTrue(pheromone.getType() == SmellPerceptType.Pheromone1);
            assertTrue(pheromone.getSource() == intruder);
            assertEqual(pheromone.getCenter().distance(intruder.getPosition()), 0, 0);
        }


        // --- turn - should be rejected because of cooldown
        game.turn();

        assertTrue(!game.getActionSuccess().get(intruder));

        // --- turn - should be rejected because of overlap
        game.turn(); game.turn();

        assertTrue(!game.getActionSuccess().get(intruder));

        // --- turn
        game.turn();

        {
            assertTrue(game.getActionSuccess().get(intruder));
            assertTrue(!game.getGameMap().getDynamicObjects(Pheromone.class).isEmpty());
            Pheromone pheromone = (Pheromone) game.getGameMap().getDynamicObjects(Pheromone.class).get(0);

            assertTrue(pheromone.getType() == SmellPerceptType.Pheromone5);
            assertTrue(pheromone.getSource() == intruder);
            assertEqual(pheromone.getCenter().distance(intruder.getPosition()), 0, 0);
        }
    }

    private static void _test_intruder_yell() {
        Game game = _createGame(new ArrayList<>(), new ArrayList<>() {{
            this.add(new QueueIntruder(
                    new Yell()
            ));
        }});
        IntruderContainer intruder = game.getIntruders().get(0);

        assertTrue(game.getGameMap().getDynamicObjects(Sound.class).isEmpty());

        // --- turn
        Asserts.assertException(game::turn, ClassCastException.class);
    }

    private static void _test_intruder_rotate() {
        Game game = _createGame(new ArrayList<>(), new ArrayList<>() {{
            this.add(new QueueIntruder(
                    new Rotate(Angle.fromRadians(Math.PI / 4)),
                    new Rotate(Angle.fromRadians(Math.PI / 4 + 0.1))
            ));
        }});

        IntruderContainer guardContainer = game.getIntruders().get(0);
        Vector2 originalDirection = guardContainer.getDirection().clone();

        // --- turn - should success because within max rotation limit
        game.turn();

        assertTrue(game.getActionSuccess().get(guardContainer));
        assertEqual(originalDirection.angle(guardContainer.getDirection()), Math.PI / 4, 1E-10);
        assertEqual(originalDirection.rotated(Math.PI / 4).angle(guardContainer.getDirection()), 0, 1E-10);

        // --- turn - should fail because rotation is more than limit
        game.turn();
        assertTrue(!game.getActionSuccess().get(guardContainer));
        assertEqual(originalDirection.angle(guardContainer.getDirection()), Math.PI / 4, 1E-10);
    }


    private static void _test_intruder_move_and_sprinting() {
        Game game = _createGame(new ArrayList<>(), new ArrayList<>() {{
            this.add(new QueueIntruder(
                    new Move(new Distance(1)),
                    new Move(new Distance(2)),
                    new Move(new Distance(0.5)),
                    new NoAction(), new NoAction(),
                    new Move(new Distance(2.1))
            ));
        }});

        IntruderContainer intruder = game.getIntruders().get(0);
        Vector2 originalPosition = intruder.getPosition().clone();

        // --- turn - valid within move distance
        game.turn();

        {
            assertTrue(game.getActionSuccess().get(intruder));
            assertEqual(originalPosition.add(intruder.getDirection().mul(1)).distance(intruder.getPosition()), 0, 1E-9);
        }

        // --- turn - valid sprint
        game.turn();

        {
            assertTrue(game.getActionSuccess().get(intruder));
            assertEqual(originalPosition.add(intruder.getDirection().mul(3)).distance(intruder.getPosition()), 0, 1E-9);
            assertTrue(intruder.hasCooldown(AgentContainer.Cooldown.SPRINTING));
        }

        // --- turn - invalid, within cooldown period
        game.turn();

        {
            assertTrue(!game.getActionSuccess().get(intruder));
            assertEqual(originalPosition.add(intruder.getDirection().mul(3)).distance(intruder.getPosition()), 0, 1E-9);
            assertTrue(intruder.hasCooldown(AgentContainer.Cooldown.SPRINTING));
        }

        // --- turn - invalid, cannot sprint that far
        game.turn(); game.turn();
        game.turn();

        {
            assertTrue(!game.getActionSuccess().get(intruder));
            assertEqual(originalPosition.add(intruder.getDirection().mul(3)).distance(intruder.getPosition()), 0, 1E-9);
            assertTrue(!intruder.hasCooldown(AgentContainer.Cooldown.SPRINTING));
        }
    }

    private static void _test_intruder_unsupported_action() {
        Game game = _createGame(new ArrayList<>(), new ArrayList<>() {{
            this.add(new QueueIntruder(
                    new IntruderAction() {}
            ));
        }});

        // --- turn
        Asserts.assertException(game::turn, IllegalArgumentException.class);
    }

    private static class QueueGuard extends QueueAgent implements Guard {

        private GuardPercepts percepts;

        public QueueGuard(Action ...actions)
        {
            super(actions);
        }

        public GuardPercepts getPercepts() {
            return percepts;
        }

        @Override
        public GuardAction getAction(GuardPercepts percepts) {
            this.percepts = percepts;
            return (GuardAction) super.getAction();
        }
    }
    private static class QueueIntruder extends QueueAgent implements Intruder {

        private Percepts percepts;

        public QueueIntruder(Action ...actions)
        {
            super(actions);
        }

        public Percepts getPercepts() {
            return percepts;
        }

        @Override
        public IntruderAction getAction(IntruderPercepts percepts) {
            this.percepts = percepts;
            return (IntruderAction) super.getAction();
        }
    }

    private static class QueueAgent {

        private Queue<Action> actions = new LinkedList<>();

        public QueueAgent(Action ...actions)
        {
            this.actions.addAll(Arrays.asList(actions));
        }

        public Action getAction()
        {
            return actions.poll();
        }

    }


}
