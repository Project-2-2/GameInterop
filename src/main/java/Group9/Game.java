package Group9;

import Group9.agent.container.AgentContainer;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.agent.factories.DefaultAgentFactory;
import Group9.agent.factories.IAgentFactory;
import Group9.map.GameMap;
import Group9.map.GameSettings;
import Group9.map.ViewRange;
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
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Percept.Vision.VisionPrecepts;
import Interop.Utils.Utils;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Game implements Runnable {

    public final static Random _RANDOM;
    public final static long _RANDOM_SEED = System.nanoTime();
    static {
        System.out.println("seed: " + _RANDOM_SEED);
        _RANDOM = new Random(_RANDOM_SEED);
    }

    private GameMap gameMap;
    private ScenarioPercepts scenarioPercepts;
    private GameSettings settings;

    private List<GuardContainer> guards = new ArrayList<>();
    private List<IntruderContainer> intruders = new ArrayList<>();

    private Map<AgentContainer<?>, Boolean> actionSuccess = new HashMap<>();
    private Set<AgentContainer<?>> justTeleported = new HashSet<>();

    private Team winner = null;

    private AtomicBoolean runningLoop = new AtomicBoolean(false);
    private final AtomicInteger ticks;
    private long lastTick = System.nanoTime();
    private final Callback<Game> turnTickCallback;

    //---
    private final boolean queryIntent;
    private Semaphore lock = new Semaphore(1);

    public Game(GameMap gameMap, final boolean queryIntent)
    {
        this(gameMap, new DefaultAgentFactory(), queryIntent, -1, null);
    }

    public Game(GameMap gameMap, IAgentFactory agentFactory, final boolean queryIntent)
    {
        this(gameMap, agentFactory, queryIntent, -1, null);
    }


    public Game(GameMap gameMap, IAgentFactory agentFactory, final boolean queryIntent, int ticks,
                Callback<Game> turnTickCallback)
    {
        gameMap.setGame(this);
        this.turnTickCallback = turnTickCallback;
        this.ticks = new AtomicInteger(ticks);


        this.queryIntent = queryIntent;
        this.gameMap = gameMap;
        this.scenarioPercepts = gameMap.getGameSettings().getScenarioPercepts();
        this.settings = gameMap.getGameSettings();
        List<MapObject> solids = this.getGameMap().getObjects().stream().filter(e -> e.getType().isSolid()).collect(Collectors.toList());

        {

            Spawn.Guard guardSpawn = gameMap.getObjects(Spawn.Guard.class).get(0);
            List<PointContainer.Circle> usedSpawns = new ArrayList<>();
            agentFactory.createGuards(settings.getNumGuards()).forEach(a -> {
                Vector2 spawn = generateRandomSpawnLocation(guardSpawn.getArea().getAsPolygon(),
                        new PointContainer.Circle(new Vector2.Origin(), AgentContainer._RADIUS), solids, usedSpawns);
                GuardContainer guardContainer = new GuardContainer(a, spawn, new Vector2(0, 1).normalise(),
                        new FieldOfView(settings.getGuardViewRangeNormal(), settings.getViewAngle()));
                this.guards.add(guardContainer);
                usedSpawns.add(guardContainer.getShape());
            });
        }

        {
            Spawn.Intruder intruderSpawn = gameMap.getObjects(Spawn.Intruder.class).get(0);
            List<PointContainer.Circle> usedSpawns = new ArrayList<>();
            agentFactory.createIntruders(settings.getNumIntruders()).forEach(e -> {
                Vector2 spawn = generateRandomSpawnLocation(intruderSpawn.getArea().getAsPolygon(),
                        new PointContainer.Circle(new Vector2.Origin(), AgentContainer._RADIUS), solids, usedSpawns);
                IntruderContainer intruderContainer = new IntruderContainer(e, spawn, new Vector2(0, 1).normalise(),
                        new FieldOfView(settings.getIntruderViewRangeNormal(), settings.getViewAngle()));
                this.intruders.add(intruderContainer);
                usedSpawns.add(intruderContainer.getShape());
            });
        }
    }

    public AtomicInteger getTicks() {
        return ticks;
    }

    public Map<AgentContainer<?>, Boolean> getActionSuccess() {
        return actionSuccess;
    }

    /**
     * Generates a random point within the area. The circle is the one that is supposed to be placed inside. If the
     * circle is not intersecting with anything in the avoid list, it might be placed along the border of the area.
     * @param area The area it should be placed inside in.
     * @param circle The circle that should be placed.
     * @param avoid The objects the circle is not allowed to intersect with.
     * @param occupied The circles that already have been placed, if no other objects need to be placed just pass an EmptyList.
     * @return A point where the circle can be placed without conflicts.
     */
    public static Vector2 generateRandomSpawnLocation(PointContainer.Polygon area, PointContainer.Circle circle,
                                                      List<MapObject> avoid, List<PointContainer.Circle> occupied)
    {

        final Vector2[] point = new Vector2[] { circle.getCenter() };
        final double radius = circle.getRadius();

        /*
          Note: Okay, the problem with generating a random location like this is that it might run into the issue
          of infinitely looping if it is actually impossible to place the circle inside. The packing density with circles
          of a square is roughly ~90%, and thus if all areas added up plus the new one are bigger than that it is impossible
          to place it inside.

          Source: http://datagenetics.com/blog/june32014/index.html
         */
        if(area.getArea() * 0.90 < occupied.stream().mapToDouble(PointContainer::getArea).sum() + circle.getArea())
        {
            throw new IllegalArgumentException(String.format("The area can only hold %d circles with radius %.2f. The new " +
                    "circle will not be able to fit.", (int) ((area.getArea() * 0.9D) / circle.getArea()), circle.getRadius()));
        }

        int i = 0;

        do
        {

            do {
                i++;

                if(i % 1000 == 0)
                {
                    System.err.printf("Game#generateRandomSpawnLocation tried %d to generate a valid location. - This " +
                            "can happen if the spawn area for either intruders or guards does not support the specified amount " +
                            "of agents.\n", i);
                }

                point[0] = area.generateRandomLocation();
            }
            while (occupied.stream().anyMatch(e -> e.getCenter().distance(point[0]) <= radius * 2D));

        }
        while (avoid.stream().anyMatch(e -> PointContainer.intersect(e.getContainer(), new PointContainer.Circle(point[0], radius))));

        return point[0];

    }

    /**
     * This method is mainly used for UI updates or for other threads accessing any data structures in an async manner.
     * The method will acquire a mutex, and stop the game controller from updating during the method call.
     *
     * @param callback The method which should be called once the lock has been acquired.
     */
    public void query(QueryUpdate callback, boolean safeRead)
    {
        if(safeRead)
        {
            callback.call(null);
            return;
        }
        else
        {
            if(!this.queryIntent)
            {
                throw new IllegalArgumentException("queryIntent=false and safeRead=false. Please indicate a queryIntent " +
                        "so that the controller can properly lock itself or perform a safe-read operation.");
            }
        }

        try {
            lock.acquireUninterruptibly();
            callback.call(lock);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public AtomicBoolean getRunningLoop() {
        return runningLoop;
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
        runningLoop.set(true);
        while (this.winner == null && runningLoop.get())
        {
            // --- at 0 ticks pause, if -1 we want to go as fas as possible
            if(ticks.get() == 0 ){
                continue;
            }

            this.winner = this.turn();
            if(this.turnTickCallback != null)
            {
                this.turnTickCallback.call(this);
            }

            // --- > 0 -> restrict
            if(this.ticks.get() > 0)
            {
                long delta = System.nanoTime() - lastTick;
                long frameTime = (long) (1E+9D / this.ticks.get());
                lastTick = System.nanoTime();

                if(delta < frameTime)
                {
                    try {
                        Thread.sleep(TimeUnit.NANOSECONDS.toMillis(frameTime - delta));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    /**
     * Checks whether any of the teams fulfil their win ropcondition.
     * @return A team that has won, otherwise null.
     */
    private Team checkForWinner()
    {
        final long intrudersCaptured = intruders.stream().filter(IntruderContainer::isCaptured).count();
        final long intrudersWins = intruders.stream().filter(e -> e.getZoneCounter() >= settings.getTurnsInTargetAreaToWin()).count();

        if(intrudersWins > 0)
        {
            return Team.INTRUDERS;
        }

        switch (settings.getScenarioPercepts().getGameMode())
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
    public final Team turn()
    {
        lockin(this::cooldown);

        // Note: Intruders move first.
        for(IntruderContainer intruder : this.intruders)
        {
            if(!(intruder.isCaptured()))
            {

                lockin(() -> {
                    final IntruderAction action = intruder.getAgent().getAction(this.generateIntruderPercepts(intruder));
                    actionSuccess.put(intruder, executeAction(intruder, action));
                });

                if((winner = checkForWinner()) != null)
                {
                    return winner;
                }
            }
        }

        for(GuardContainer guard : this.guards)
        {

            lockin(() -> {
                final GuardAction action = guard.getAgent().getAction(this.generateGuardPercepts(guard));
                actionSuccess.put(guard, executeAction(guard, action));
            });

            if((winner = checkForWinner()) != null)
            {
                return winner;
            }
        }

        return null;
    }

    private void lockin(Runnable callable)
    {
        if(this.queryIntent)
        {
            try {
                this.lock.acquire();
                callable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.lock.release();
            }
        }
        else {
            callable.run();
        }

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

        if(agentContainer.isCoolingDown())
        {
            return false;
        }

        //@performance cleanup
        Set<EffectArea> effectAreas = gameMap.getEffectAreas(agentContainer);
        Optional<EffectArea> modifySpeedEffect = effectAreas.stream().filter(e -> e instanceof ModifySpeedEffect).findAny();
        Optional<EffectArea> soundEffect = effectAreas.stream().filter(e -> e instanceof SoundEffect).findAny();
        Optional<EffectArea> modifyViewEffect = effectAreas.stream().filter(e -> e instanceof ModifyViewEffect).findAny();
        //---


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
        } else
        //--- check if guard is close enough to capture
        {
            this.intruders.stream()
                    .filter(e -> e.getPosition().distance(agentContainer.getPosition()) <= settings.getScenarioPercepts().getCaptureDistance().getValue())
                    .forEach(e -> e.setCaptured(true));
        }

        if(action instanceof Move || action instanceof Sprint)
        {
            final double slowdownModifier = (double) modifySpeedEffect.orElseGet(NoModify::new).get(agentContainer);
            double distance = ((action instanceof Move) ?
                    ((Move) action).getDistance().getValue() : ((Sprint) action).getDistance().getValue());

            assert distance != -1;

            final double minSprint = isGuard ?
                    settings.getGuardMaxMoveDistance().getValue() : settings.getIntruderMaxMoveDistance().getValue();
            final double maxSprint = (isGuard ?
                    settings.getGuardMaxMoveDistance().getValue() : settings.getIntruderMaxSprintDistance().getValue()) * slowdownModifier;

            if(distance > maxSprint)
            {
                return false;
            }

            final boolean isSprinting = (distance > minSprint);

            //--- check for movement collision
            {
                // --- To check for collisions, we create a bounding box. The length of this box has to be the
                // distance the agent wants to move
                // + the radius of the agent; because the center of the agent is moved
                final double length = distance + agentContainer.getShape().getRadius();


                final Vector2 end = agentContainer.getPosition().add(agentContainer.getDirection().mul(length));
                PointContainer.Line line = new PointContainer.Line(agentContainer.getPosition(), end);

                final Vector2 move = agentContainer.getDirection().mul(length);

                Vector2 pointA = agentContainer.getPosition().add(line.getNormal());
                Vector2 pointB = pointA.add(move);
                Vector2 pointD = agentContainer.getPosition().sub(line.getNormal());
                Vector2 pointC = pointD.add(move);

                PointContainer.Polygon quadrilateral = new PointContainer.Polygon(pointA, pointB, pointC, pointD);
                if(gameMap.isMoveIntersecting(agentContainer, quadrilateral))
                {
                    return false;
                }
            }

            if(isSprinting)
            {
                agentContainer.addCooldown(AgentContainer.Cooldown.SPRINTING, settings.getSprintCooldown());
            }

            //--- move and then get new effects
            gameMap.getDynamicObjects().add(new Sound(SoundPerceptType.Noise, agentContainer, settings.getMoveMaxSoundRadius().getValue(), 1));
            agentContainer.move(distance);
            Set<EffectArea> movedEffectAreas = gameMap.getEffectAreas(agentContainer);
            soundEffect = movedEffectAreas.stream().filter(e -> e instanceof SoundEffect).findAny();


            Optional<EffectArea> locationEffect = movedEffectAreas.stream().filter(e -> e instanceof ModifyLocationEffect).findAny();

            if(!justTeleported.contains(agentContainer) && locationEffect.isPresent())
            {
                if(locationEffect.get().getParent() instanceof TeleportArea)
                {
                    PointContainer.Polygon connectedArea = ((TeleportArea) locationEffect.get().getParent()).getConnected().getArea().getAsPolygon();
                    List<MapObject> solids = this.getGameMap().getObjects().stream().filter(e -> e.getType().isSolid()).collect(Collectors.toList());

                    final Vector2 position = generateRandomSpawnLocation(connectedArea, agentContainer.getShape(), solids,
                            new ArrayList<>());
                    agentContainer.moveTo(position);
                    justTeleported.add(agentContainer);
                }
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

            return true;
        }
        else if(action instanceof Rotate)
        {
            Rotate rotate = (Rotate) action;
            if(Math.abs(rotate.getAngle().getRadians()) > settings.getScenarioPercepts().getMaxRotationAngle().getRadians())
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
                    settings.getYellSoundRadius().getValue(),
                    1
            ));
            return true;
        }
        else if(action instanceof DropPheromone)
        {

            //--- check whether there is already one in this place
            if(gameMap.getDynamicObjects(Pheromone.class).stream()
                    .filter(e -> e.getSource().getClass().isAssignableFrom(agentContainer.getClass()))
                    .anyMatch(e -> PointContainer.intersect(e.getAsCircle(),
                            new PointContainer.Circle(agentContainer.getPosition(), scenarioPercepts.getRadiusPheromone().getValue())))
            )
            {
                return false;
            }

            agentContainer.addCooldown(AgentContainer.Cooldown.PHEROMONE, gameMap.getGameSettings().getScenarioPercepts().getPheromoneCooldown());

            DropPheromone dropPheromone = (DropPheromone) action;

            gameMap.getDynamicObjects().add(new Pheromone(
                    dropPheromone.getType(),
                    agentContainer,
                    agentContainer.getPosition(),
                    scenarioPercepts.getRadiusPheromone().getValue(),
                    settings.getPheromoneExpireRounds()
            ));
            return true;
        }

        throw new IllegalArgumentException(String.format("Tried to execute an unsupported action: %s", action));

    }

    private void cooldown()
    {
        // --- iterate over dynamic objects (sounds) and adjust lifetime or remove
        {
            Iterator<DynamicObject<?>> iterator = gameMap.getDynamicObjects().iterator();
            while (iterator.hasNext()) {
                DynamicObject<?> e = iterator.next();
                e.setLifetime(e.getLifetime() - 1);
                if(e.getLifetime() <= 0)
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
                new ScenarioGuardPercepts(this.settings.getScenarioPercepts(), this.settings.getGuardMaxMoveDistance()),
                this.actionSuccess.getOrDefault(guard, true)
        );
    }

    private IntruderPercepts generateIntruderPercepts(IntruderContainer intruder)
    {

        final Vector2 direction = this.gameMap.getObjects(TargetArea.class).get(0).getContainer().getCenter()
                .sub(intruder.getPosition()).normalise();

        final double angle = Math.acos(intruder.getDirection().dot(direction));

        return new IntruderPercepts(
                Direction.fromRadians(angle),
                generateVisionPercepts(intruder),
                generateSoundPercepts(intruder),
                generateSmellPercepts(intruder),
                generateAreaPercepts(intruder),
                new ScenarioIntruderPercepts(
                        this.settings.getScenarioPercepts(),
                        this.settings.getTurnsInTargetAreaToWin(),
                        this.settings.getIntruderMaxMoveDistance(),
                        this.settings.getIntruderMaxSprintDistance(),
                        intruder.getCooldown(AgentContainer.Cooldown.SPRINTING)
                ),
                this.actionSuccess.getOrDefault(intruder, true)
        );
    }

    private <T> VisionPrecepts generateVisionPercepts(AgentContainer<T> agentContainer)
    {
        Set<EffectArea> effectAreas = gameMap.getEffectAreas(agentContainer);
        final FieldOfView fov = agentContainer.getFOV(effectAreas);

        Optional<ModifyViewRangeEffect> viewRangeEffect = effectAreas.stream()
                .filter(a -> a instanceof ModifyViewRangeEffect)
                .map(a -> (ModifyViewRangeEffect) a).findAny();

        ViewRange viewRange = null;
        if(viewRangeEffect.isPresent())
        {
            viewRange = viewRangeEffect.get().get(agentContainer);
        }

        return new VisionPrecepts(
                fov,
                new ObjectPercepts(gameMap.getObjectPerceptsForAgent(agentContainer, fov, viewRange))
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
                .filter(e -> agentContainer.getPosition().distance(e.getCenter()) <= e.getRadius())
                .map(dynamicObject -> {
                    Sound sound = (Sound) dynamicObject;
                    double angle = (_RANDOM.nextBoolean() ? 1 : -1) * (0.174533 * _RANDOM.nextDouble());
                    return new SoundPercept(
                            sound.getType(),
                            Direction.fromRadians(Utils.mod((dynamicObject.getCenter().getClockDirection() - agentContainer.getPosition().getClockDirection()) + angle, Utils.TAU))
                    );
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
        /**
         * Is called once the game logic thread has been locked, and operations can be sable performed. -
         *  Note (!!!): It is the responsibility of the caller to release {@link Semaphore#release()} the lock once
         *  it has completed its operations.
         * @param lock
         */
        void call(Semaphore lock);
    }

}
