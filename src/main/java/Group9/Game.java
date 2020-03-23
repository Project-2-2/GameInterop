package Group9;

import Group9.agent.container.AgentContainer;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.map.GameMap;
import Group9.map.area.*;
import Group9.map.dynamic.DynamicObject;
import Group9.map.dynamic.Pheromone;
import Group9.map.dynamic.Sound;
import Group9.map.objects.*;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
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
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPercepts;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Sound.SoundPercepts;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Percept.Vision.VisionPrecepts;
import Interop.Utils.Utils;
import javafx.scene.Group;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Game implements Runnable {

    public final static Random _RANDOM = new Random();

    private GameMap gameMap;
    private ScenarioPercepts scenarioPercepts;

    private List<GuardContainer> guards = new ArrayList<>();
    private List<IntruderContainer> intruders = new ArrayList<>();

    private Map<AgentContainer<?>, Boolean> actionSuccess = new HashMap<>();
    private Set<AgentContainer<?>> justTeleported = new HashSet<>();

    private Team winner = null;

    //--
    private Semaphore lock = new Semaphore(1);

    public Game(GameMap gameMap, int teamSize)
    {

        this.gameMap = gameMap;
        this.scenarioPercepts = gameMap.getScenarioPercepts();

        Spawn.Guard guardSpawn = gameMap.getObjects(Spawn.Guard.class).get(0);
        Spawn.Intruder intruderSpawn = gameMap.getObjects(Spawn.Intruder.class).get(0);

        AgentsFactory.createGuards(teamSize).forEach(a -> this.guards.add(new GuardContainer(a,
                guardSpawn.getContainer().getAsPolygon().generateRandomLocation().toVexing(), new Vector2(0, 1).normalise().toVexing(),
                new FieldOfView(gameMap.getGuardViewRangeNormal(), gameMap.getViewAngle()))));
        AgentsFactory.createIntruders(teamSize).forEach(a -> this.intruders.add(new IntruderContainer(a,
                intruderSpawn.getContainer().getAsPolygon().generateRandomLocation().toVexing(), new Vector2(0, 1).normalise().toVexing(),
                new FieldOfView(gameMap.getIntruderViewRangeNormal(), gameMap.getViewAngle()))));
    }

    /**
     * This method is mainly used for UI updates or for other threads accessing any data structures in an async manner.
     * The method will acquire a mutex, and stop the game controller from updating during the method call.
     *
     * @param callback The method which should be called once the lock has been acquired.
     */
    public void query(QueryUpdate callback)
    {
        try {
            //@todo the 10 ms basically guarantee that it will get a lock when this method gets called. this leads to
            //  smoother ui updates but it is not guaranteed, so if someone has a fairly week computer this method
            //  might lead to a choppy ui experience.
            lock.tryAcquire(10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callback.update();
        lock.release();
    }

    public List<GuardContainer> getGuards() {
        return guards;
    }

    public List<IntruderContainer> getIntruders() {
        return intruders;
    }

    public GameMap getGameMap()
    {
        return gameMap;
    }

    /**
     * @return Returns the winner of the match, otherwise null.
     */
    public Team getWinner()
    {
        return winner;
    }

    /**
     * Runs the game controller in a loop.
     */
    @Override
    public void run()
    {
        while (this.winner == null)
        {
            this.winner = this.turn();
            if(false)
            {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checks whether any of the teams fulfil their win condition.
     * @return A team that has won, otherwise null.
     */
    private Team checkForWinner()
    {
        final long intrudersCaptured = intruders.stream().filter(IntruderContainer::isCaptured).count();
        final long intrudersWins = intruders.stream().filter(e -> e.getZoneCounter() >= gameMap.getTurnsInTargetAreaToWin()).count();

        if(intrudersWins > 0)
        {
            return Team.INTRUDERS;
        }

        switch (gameMap.getScenarioPercepts().getGameMode())
        {
            case CaptureOneIntruder:
                if(intrudersCaptured > 0)
                {
                    return Team.GUARDS;
                }

                break;
            case CaptureAllIntruders:
                if(intrudersCaptured == intruders.size() && !intruders.isEmpty())
                {
                    return Team.GUARDS;
                }
                break;
        }
        return null;
    }

    /**
     * Executes one full turn of the game.
     * @return
     */
    private Team turn()
    {

        this.cooldown();
        Team winner = null;

        // Note: Intruders move first.
        for(IntruderContainer intruder : this.intruders)
        {
            if(!(intruder.isCaptured()))
            {
                try {
                    lock.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final IntruderAction action = intruder.getAgent().getAction(this.generateIntruderPercepts(intruder));
                actionSuccess.put(intruder, executeAction(intruder, action));

                lock.release();
                if((winner = checkForWinner()) != null)
                {
                    return winner;
                }
            }
        }

        for(GuardContainer guard : this.guards)
        {
            try {
                lock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final GuardAction action = guard.getAgent().getAction(this.generateGuardPercepts(guard));
            actionSuccess.put(guard, executeAction(guard, action));

            lock.release();
            if((winner = checkForWinner()) != null)
            {
                return winner;
            }
        }

        return null;
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
        //@performance cleanup
        Set<EffectArea> effectAreas = gameMap.getEffectAreas(agentContainer);
        Optional<EffectArea> modifySpeedEffect = effectAreas.stream().filter(e -> e instanceof ModifySpeedEffect).findAny();
        Optional<EffectArea> soundEffect = effectAreas.stream().filter(e -> e instanceof SoundEffect).findAny();
        Optional<EffectArea> modifyViewEffect = effectAreas.stream().filter(e -> e instanceof ModifyViewEffect).findAny();
        //---


        if(action instanceof Move || action instanceof Sprint)
        {
            final double slowdownModifier = (double) modifySpeedEffect.orElseGet(NoModify::new).get(agentContainer);
            double distance = ((action instanceof Move) ?
                    ((Move) action).getDistance().getValue() : ((Sprint) action).getDistance().getValue()) * slowdownModifier;

            assert distance != -1;

            final double minSprint = isGuard ?
                    gameMap.getGuardMaxMoveDistance().getValue() : gameMap.getIntruderMaxMoveDistance().getValue();
            final double maxSprint = isGuard ?
                    gameMap.getGuardMaxMoveDistance().getValue() : gameMap.getIntruderMaxSprintDistance().getValue();

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
                    if(agentContainer.getCooldown(AgentContainer.Cooldown.SPRINTING) > 0 || distance > maxSprint)
                    {
                        return false;
                    }
                }
            }

            //--- check for movement collision
            {
                final Vector2 end = agentContainer.getPosition().add(agentContainer.getDirection().mul(distance, distance));
                PointContainer.Line line = new PointContainer.Line(agentContainer.getPosition(), end);
                Vector2 temp = line.getNormal().mul(agentContainer.getShape().getRadius());
                Vector2 pointA = temp.add(line.getStart());
                Vector2 pointC = temp.add(line.getEnd());
                Vector2 pointB = temp.flip().add(line.getStart());
                Vector2 pointD = temp.flip().add(line.getEnd());
                PointContainer.Polygon quadrilateral = new PointContainer.Polygon(pointA, pointB, pointC, pointD);
                if(gameMap.isMoveIntersecting(quadrilateral, ObjectPerceptType::isSolid))
                {
                    return false;
                }
            }

            if(isSprinting)
            {
                agentContainer.addCooldown(AgentContainer.Cooldown.SPRINTING, gameMap.getSprintCooldown());
            }

            //--- move and then get new effects
            gameMap.getDynamicObjects().add(new Sound(SoundPerceptType.Noise, agentContainer, gameMap.getMoveMaxSoundRadius().getValue(), 1));
            agentContainer.move(distance);
            Set<EffectArea> movedEffectAreas = gameMap.getEffectAreas(agentContainer);
            soundEffect = movedEffectAreas.stream().filter(e -> e instanceof SoundEffect).findAny();


            Optional<EffectArea> locationEffect = movedEffectAreas.stream().filter(e -> e instanceof ModifyLocationEffect).findAny();

            if(!justTeleported.contains(agentContainer) && locationEffect.isPresent())
            {
                Vector2 pos = ((ModifyLocationEffect) locationEffect.get()).get(agentContainer);
                agentContainer.moveTo(pos);
                justTeleported.add(agentContainer);
            }
            else if(justTeleported.contains(agentContainer) && !locationEffect.isPresent())
            {
                justTeleported.remove(agentContainer);
            }

            soundEffect.ifPresent(effectArea -> {
                SoundEffect s = (SoundEffect) effectArea;
                gameMap.getDynamicObjects().add(new Sound(s.getType(), agentContainer,
                        s.get(agentContainer) * (distance / maxSprint),
                        1
                ));

            });

            //--- check if intruder is in target area
            if(isIntruder)
            {
                IntruderContainer intruderContainer = (IntruderContainer) agentContainer;
                if(gameMap.getObjects(TargetArea.class).stream().anyMatch(e -> PointContainer.intersect(e.getContainer(), agentContainer.getShape())))
                {
                    intruderContainer.setZoneCounter(intruderContainer.getZoneCounter() + 1);
                }
                else
                {
                    intruderContainer.setZoneCounter(0);
                }
            }
            //--- check if guard is close enough to capture
            else
            {
                this.intruders.stream()
                        .filter(e -> e.getPosition().distance(agentContainer.getPosition()) <= gameMap.getScenarioPercepts().getCaptureDistance().getValue())
                        .forEach(e -> e.setCaptured(true));
            }
            return true;
        }
        else if(action instanceof Rotate)
        {
            Rotate rotate = (Rotate) action;
            if(Math.abs(rotate.getAngle().getRadians()) > gameMap.getScenarioPercepts().getMaxRotationAngle().getRadians())
            {
                return false;
            }

            agentContainer.rotate(rotate.getAngle().getRadians());
            return true;
        }
        else if(action instanceof Yell)
        {
            if(!(agentContainer.getAgent() instanceof Guard))
            {
                return false;
            }
            gameMap.getDynamicObjects().add(new Sound(
                    SoundPerceptType.Yell,
                    agentContainer,
                    gameMap.getYellSoundRadius().getValue(),
                    1
            ));
            return true;
        }
        else if(action instanceof DropPheromone)
        {
            //--- check cooldown
            if(agentContainer.hasCooldown(AgentContainer.Cooldown.PHEROMONE))
            {
                return false;
            }

            //--- check whether there is already one in this place
            if(gameMap.getDynamicObjects(Pheromone.class).stream()
                    .filter(e -> e.getSource().getClass().isAssignableFrom(agentContainer.getClass()))
                    .anyMatch(e -> PointContainer.intersect(e.getAsCircle(),
                            new PointContainer.Circle(agentContainer.getPosition(), scenarioPercepts.getRadiusPheromone().getValue())))
            )
            {
                return false;
            }
            DropPheromone dropPheromone = (DropPheromone) action;

            gameMap.getDynamicObjects().add(new Pheromone(
                    dropPheromone.getType(),
                    agentContainer,
                    agentContainer.getPosition(),
                    scenarioPercepts.getRadiusPheromone().getValue(),
                    gameMap.getPheromoneExpireRounds()
            ));
            return true;
        }

        throw new IllegalArgumentException(String.format("Tried to execute an unsupported action: %s", action));

    }

    private void cooldown()
    {
        // --- iterate over dynamic objects (sounds) and adjust lifetime or remove
        {
            Iterator<DynamicObject> iterator = gameMap.getDynamicObjects().iterator();
            while (iterator.hasNext()) {
                DynamicObject e = iterator.next();
                e.setLifetime(e.getLifetime() - 1);
                if(e.getLifetime() == 0)
                {
                    iterator.remove();
                }
                else if(e instanceof Pheromone)
                {
                    Pheromone p = (Pheromone)e;
                    e.setRadius(p.getInitialRadius() * (p.getLifetime() / (double) p.getInitialLifetime()));
                }
            }
        }

        // --- sprint cooldown
        {
            this.intruders.forEach(AgentContainer::cooldown);
            this.guards.forEach(AgentContainer::cooldown);
        }

    }

    private GuardPercepts generateGuardPercepts(GuardContainer guard)
    {
        return new GuardPercepts(
                generateVisionPercepts(guard),
                generateSoundPercepts(guard),
                generateSmellPercepts(guard),
                generateAreaPercepts(guard),
                new ScenarioGuardPercepts(this.gameMap.getScenarioPercepts(), this.gameMap.getGuardMaxMoveDistance()),
                this.actionSuccess.getOrDefault(guard, true)
        );
    }

    private IntruderPercepts generateIntruderPercepts(IntruderContainer intruder)
    {

        Vector2 direction = this.gameMap.getObjects(TargetArea.class).get(0).getContainer()
                                            .getCenter().sub(intruder.getDirection()).normalise();

        return new IntruderPercepts(
                Direction.fromClockAngle(new Vector(direction.getX(), direction.getY())),
                generateVisionPercepts(intruder),
                generateSoundPercepts(intruder),
                generateSmellPercepts(intruder),
                generateAreaPercepts(intruder),
                new ScenarioIntruderPercepts(
                        this.gameMap.getScenarioPercepts(),
                        this.gameMap.getTurnsInTargetAreaToWin(),
                        this.gameMap.getIntruderMaxMoveDistance(),
                        this.gameMap.getIntruderMaxSprintDistance(),
                        intruder.getCooldown(AgentContainer.Cooldown.SPRINTING)
                ),
                this.actionSuccess.getOrDefault(intruder, true)
        );
    }

    private <T> VisionPrecepts generateVisionPercepts(AgentContainer<T> agentContainer)
    {
        final FieldOfView fov = agentContainer.getFOV(gameMap.getEffectAreas(agentContainer));
        return new VisionPrecepts(
                fov,
                new ObjectPercepts(gameMap.getObjectPerceptsForAgent(agentContainer, fov))
        );
    }

    private <T> AreaPercepts generateAreaPercepts(AgentContainer<T> agentContainer)
    {
        return new AreaPercepts(
                gameMap.isInMapObject(agentContainer, Window.class),
                gameMap.isInMapObject(agentContainer, Door.class),
                gameMap.isInMapObject(agentContainer, SentryTower.class),
                justTeleported.contains(agentContainer)
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
                            Direction.fromRadians(Utils.mod(dynamicObject.getCenter().getClockDirection() - agentContainer.getPosition().getClockDirection(), Utils.TAU))                    );
                }).collect(Collectors.toUnmodifiableSet()));
    }

    private <T> SmellPercepts generateSmellPercepts(AgentContainer<T> agentContainer)
    {
        return new SmellPercepts(this.gameMap.getDynamicObjects().stream()
                .filter(e -> e instanceof Pheromone && agentContainer.getClass().isAssignableFrom(e.getSource().getClass()))
                .filter(e -> PointContainer.intersect(e.getAsCircle(), agentContainer.getShape()))
                .map(dynamicObject -> {
                    Pheromone pheromone = (Pheromone) dynamicObject;
                    return new SmellPercept(
                            pheromone.getType(),
                            new Distance(dynamicObject.getCenter().distance(agentContainer.getPosition()))
                    );
                }).collect(Collectors.toUnmodifiableSet()));
    }

    public enum Team
    {
        INTRUDERS,
        GUARDS
    }

    public interface QueryUpdate
    {
        void update();
    }

}
