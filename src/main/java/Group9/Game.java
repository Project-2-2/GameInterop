package Group9;

import Group9.agent.AgentContainer;
import Group9.agent.GuardAgent;
import Group9.agent.IntruderAgent;
import Group9.map.GameMap;
import Group9.map.area.TargetArea;
import Group9.map.dynamic.DynamicObject;
import Group9.map.dynamic.Pheromone;
import Group9.map.dynamic.Sound;
import Group9.map.objects.Door;
import Group9.map.objects.SentryTower;
import Group9.map.objects.Spawn;
import Group9.map.objects.Window;
import Group9.math.Line;
import Group9.math.Vector2;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Vector;
import Interop.Percept.AreaPercepts;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Scenario.ScenarioGuardPercepts;
import Interop.Percept.Scenario.ScenarioIntruderPercepts;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Smell.SmellPercepts;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Sound.SoundPercepts;

import java.util.*;
import java.util.stream.Collectors;

public class Game {

    private GameMap gameMap;
    private ScenarioPercepts scenarioPercepts;

    private List<GuardAgent> guards = new ArrayList<>();
    private List<IntruderAgent> intruders = new ArrayList<>();

    private Map<AgentContainer<?>, Boolean> actionSuccess = new HashMap<>();

    public Game(GameMap gameMap, int teamSize)
    {

        this.gameMap = gameMap;
        this.scenarioPercepts = gameMap.getScenarioPercepts();

        Spawn.Guard guardSpawn = gameMap.getObjects(Spawn.Guard.class).get(0);
        Spawn.Intruder intruderSpawn = gameMap.getObjects(Spawn.Intruder.class).get(0);

        AgentsFactory.createGuards(teamSize).forEach(a -> {
            this.guards.add(new GuardAgent(a, guardSpawn.generateRandomLocation(), new Vector(1, 1)));
        });
        AgentsFactory.createIntruders(teamSize).forEach(a -> {
            this.intruders.add(new IntruderAgent(a, intruderSpawn.generateRandomLocation(), new Vector(1, 1)));
        });
    }

    public void start()
    {

        while (true)
        {
            this.turn();
        }

    }

    private void turn()
    {
        // --- iterate over dynamic objects (sounds) and adjust lifetime or remove
        Iterator<DynamicObject> iterator = gameMap.getDynamicObjects().iterator();
        while (iterator.hasNext()) {
            DynamicObject e = iterator.next();
            if(e.getLifetime() == 0)
            {
                iterator.remove();
            }

            e.setLifetime(e.getLifetime() - 1);
        }

        for(GuardAgent guard : this.guards)
        {
            final GuardAction action = guard.getAction(this.generateGuardPercepts(guard));
            actionSuccess.put(guard, executeAction(guard, action));
        }

        //TODO we can ignore this for now since the ExplorerAgent is implementing the Guard interface, so we currently
        //  only have to support that
        /*
        for(IntruderAgent intruder : this.intruders)
        {
            final IntruderAction action = intruder.getAction(this.generateIntruderPercepts(intruder));
        }*/

    }

    private <T> boolean executeAction(AgentContainer<T> agentContainer, Action action)
    {

        boolean isGuard = agentContainer.getAgent() instanceof Guard;
        boolean isIntruder = agentContainer.getAgent() instanceof Intruder;

        assert isGuard != isIntruder : "What m8?";

        if(action instanceof NoAction)
        {
            return true;
        }
        else if(action instanceof Move || action instanceof Sprint)
        {
            AreaPercepts areaPercepts = generateAreaPercepts(agentContainer);

            final double slowdownModifier = getSlowdownModifier(areaPercepts);
            final double distance = ((action instanceof Move) ?
                    ((Move) action).getDistance().getValue() : ((Sprint) action).getDistance().getValue()) * slowdownModifier;

            final double minSprint = isGuard ?
                    gameMap.getGuardMaxMoveDistance().getValue() : gameMap.getIntruderMaxMoveDistance().getValue();
            final double maxSprint = isGuard ?
                    Double.MAX_VALUE : gameMap.getIntruderMaxSprintDistance().getValue();

            boolean isSprinting = (distance > minSprint);

            if(isSprinting)
            {
                //--- guards are not allowed to sprint
                if(isGuard)
                {
                    return false;
                }
                else
                {
                    assert isIntruder;
                    if(agentContainer.getSprintCooldown() > 0 || distance > maxSprint)
                    {
                        return false;
                    }
                }
            }

            //---


            //---

            final Vector2 end = agentContainer.getPosition().add(agentContainer.getDirection().mul(distance, distance));

            //TODO we are currently only checking whether a single line is intersecting with something but not whether
            // or not we are too wide
            Line line = new Line(agentContainer.getPosition(), end);
            if(gameMap.isRayIntersectingSolidObject(line))
            {
                return false;
            }

            if(isSprinting)
            {
                agentContainer.setSprintCooldown(gameMap.getSprintCooldown());
            }

            agentContainer.move(distance);
            return true;
        }
        else if(action instanceof Rotate)
        {
            Rotate rotate = (Rotate) action;
            if(gameMap.getScenarioPercepts().getMaxRotationAngle().getRadians() > rotate.getAngle().getRadians())
            {
                return false;
            }

            agentContainer.rotate(rotate.getAngle().getRadians());
            return true;
        }
        else if(action instanceof Yell)
        {
            gameMap.getDynamicObjects().add(new Sound(
                    SoundPerceptType.Yell,
                    agentContainer,
                    agentContainer.getPosition(),
                    1, //TODO replace with correct values
                    1 //TODO replace with correct values
            ));
            return true;
        }
        else if(action instanceof DropPheromone)
        {
            DropPheromone dropPheromone = (DropPheromone) action;
            gameMap.getDynamicObjects().add(new Pheromone(
                    SmellPerceptType.Pheromone1, //TODO there is currently not a way to figure out which one it is...
                    agentContainer,
                    agentContainer.getPosition(),
                    scenarioPercepts.getRadiusPheromone().getValue(),
                    scenarioPercepts.getPheromoneCooldown()
            ));
            return true;
        }

        throw new IllegalArgumentException(String.format("Tried to execute an unsupported action: %s", action));

    }

    private double getSlowdownModifier(AreaPercepts areaPercepts)
    {
        SlowDownModifiers modifiers = gameMap.getScenarioPercepts().getSlowDownModifiers();
        double modifier = 1;
        if(areaPercepts.isInDoor())
        {
            modifier = modifiers.getInDoor();
        }
        else if(areaPercepts.isInSentryTower())
        {
            modifier = modifiers.getInSentryTower();
        }
        else if(areaPercepts.isInWindow())
        {
            modifier = modifiers.getInWindow();
        }
        return modifier;
    }

    private GuardPercepts generateGuardPercepts(GuardAgent guard)
    {
        //TODO generate data structure for the specific agent
        return new GuardPercepts(
                null,
                generateSoundPercepts(guard),
                generateSmellPercepts(guard),
                generateAreaPercepts(guard),
                new ScenarioGuardPercepts(this.gameMap.getScenarioPercepts(), this.gameMap.getGuardMaxMoveDistance()),
                this.actionSuccess.getOrDefault(guard, true)
        );
    }

    private IntruderPercepts generateIntruderPercepts(IntruderAgent intruder)
    {

        Vector2 direction = this.gameMap.getObjects(TargetArea.class).get(0).getContainer()
                                            .getCenter().sub(intruder.getDirection()).normalise();
        //TODO generate data structure for the specific agent
        return new IntruderPercepts(
                Direction.fromClockAngle(new Vector(direction.getX(), direction.getY())),
                null,
                generateSoundPercepts(intruder),
                generateSmellPercepts(intruder),
                generateAreaPercepts(intruder),
                new ScenarioIntruderPercepts(
                        this.gameMap.getScenarioPercepts(),
                        this.gameMap.getTurnsInTargetAreaToWin(),
                        this.gameMap.getIntruderMaxMoveDistance(),
                        this.gameMap.getIntruderMaxSprintDistance(),
                        intruder.getSprintCooldown()
                ),
                this.actionSuccess.getOrDefault(intruder, true)
        );
    }

    private <T> AreaPercepts generateAreaPercepts(AgentContainer<T> agentContainer)
    {
        return new AreaPercepts(
                gameMap.isInArea(agentContainer, Window.class),
                gameMap.isInArea(agentContainer, Door.class),
                gameMap.isInArea(agentContainer, SentryTower.class),
                false //TODO implement teleports
        );
    }

    private <T> SoundPercepts generateSoundPercepts(AgentContainer<T> agentContainer)
    {
        return new SoundPercepts(this.gameMap.getDynamicObjects().stream()
                .filter(e -> e instanceof Sound)
                .map(dynamicObject -> {
                    Sound sound = (Sound) dynamicObject;
                    return new SoundPercept(
                            sound.getType(),
                            //TODO check whether this is correct or not...
                            Direction.fromRadians(dynamicObject.getCenter().getClockDirection() - agentContainer.getPosition().getClockDirection())
                    );
                }).collect(Collectors.toUnmodifiableSet()));
    }

    private <T> SmellPercepts generateSmellPercepts(AgentContainer<T> agentContainer)
    {
        //TODO verify that 'agentContainer.getClass().isAssignableFrom(e.getSource().getClass()' is checking that they
        // actually belong to the same team
        return new SmellPercepts(this.gameMap.getDynamicObjects().stream()
                .filter(e -> e instanceof Pheromone && agentContainer.getClass().isAssignableFrom(e.getSource().getClass()))
                .map(dynamicObject -> {
                    Pheromone pheromone = (Pheromone) dynamicObject;
                    return new SmellPercept(
                            pheromone.getType(),
                            new Distance(dynamicObject.getCenter().distance(agentContainer.getPosition()))
                    );
                }).collect(Collectors.toUnmodifiableSet()));
    }

}
