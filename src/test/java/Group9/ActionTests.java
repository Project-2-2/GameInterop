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
import Interop.Percept.Sound.SoundPercepts;
import SimpleUnitTest.SimpleUnitTest;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ActionTests extends SimpleUnitTest {

    public static void main(String[] args) {

        it("Action::Guard::DropPheromone", ActionTests::_test_guard_drop_pheromone);
        it("Action::Guard::Yell", ActionTests::_test_guard_yell);
        it("Action::Guard::<UnsupportedAction>", ActionTests::_test_guard_unsupported_action);
        it("Action::Intruder::DropPheromone", ActionTests::_test_intruder_drop_pheromone);
        it("Action::Intruder::Yell", ActionTests::_test_intruder_yell);
        it("Action::Intruder::<UnsupportedAction>", ActionTests::_test_intruder_unsupported_action);

    }

    private static Game _createGame(final List<Guard> guards, final List<Intruder> intruders)
    {

        ScenarioPercepts scenarioPercepts = new ScenarioPercepts(GameMode.CaptureAllIntruders, new Distance(1),
                Angle.fromRadians(1), new SlowDownModifiers(1, 1, 1),
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
                one, one, 1, guards.size(), intruders.size(), new Distance(6), one, new Distance(6),
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

    private static void _test_guard_unsupported_action() {
        Game game = _createGame(new ArrayList<>() {{
            this.add(new QueueGuard(
                    new GuardAction() {}
            ));
        }}, new ArrayList<>());

        // --- turn
        try {
            game.turn();
            assertTrue(false);

        } catch (IllegalArgumentException expected)
        {
            assertTrue(true);
        }
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
        try {
            game.turn();
            assertTrue(false);
        } catch (ClassCastException expected)
        {
            assertTrue(true);
        }
    }

    private static void _test_intruder_unsupported_action() {
        Game game = _createGame(new ArrayList<>(), new ArrayList<>() {{
            this.add(new QueueIntruder(
                    new IntruderAction() {}
            ));
        }});

        // --- turn
        try {
            game.turn();
            assertTrue(false);

        } catch (IllegalArgumentException expected)
        {
            assertTrue(true);
        }
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
