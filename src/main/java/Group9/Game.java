package Group9;

import Group9.agent.AgentContainer;
import Group9.agent.GuardAgent;
import Group9.agent.IntruderAgent;
import Group9.map.GameMap;
import Group9.map.area.TargetArea;
import Group9.map.objects.Spawn;
import Group9.math.Vector2;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Geometry.Direction;
import Interop.Geometry.Vector;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Scenario.ScenarioGuardPercepts;
import Interop.Percept.Scenario.ScenarioIntruderPercepts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private GameMap gameMap;

    private List<GuardAgent> guards = new ArrayList<>();
    private List<IntruderAgent> intruders = new ArrayList<>();

    private Map<AgentContainer<?>, Boolean> actionSuccess = new HashMap<>();

    public Game(GameMap gameMap, int teamSize)
    {

        this.gameMap = gameMap;

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

        if(action instanceof NoAction)
        {
            return true;
        }
        else if(action instanceof Move)
        {
            Move move = (Move) action;

            // TODO check for collision -> false

            agentContainer.move(move.getDistance().getValue());
            return true;
        }
        else if(action instanceof Rotate)
        {
            Rotate rotate = (Rotate) action;
            agentContainer.rotate(rotate.getAngle().getRadians());
            return true;
        }
        else if(action instanceof Sprint)
        {
            if(agentContainer.getAgent() instanceof Guard)
            {
                return false;
            }

            Sprint sprint = (Sprint) action;
        }
        else if(action instanceof Yell)
        {
            Yell yell = (Yell) action;
        }
        else if(action instanceof DropPheromone)
        {
            DropPheromone dropPheromone = (DropPheromone) action;
        }

        throw new IllegalArgumentException(String.format("Tried to execute an unsupported action: %s", action));

    }

    private GuardPercepts generateGuardPercepts(GuardAgent guard)
    {
        //TODO generate data structure for the specific agent
        return new GuardPercepts(
                null,
                null,
                null,
                null,
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
                null,
                null,
                null,
                new ScenarioIntruderPercepts(
                        this.gameMap.getScenarioPercepts(),
                        this.gameMap.getTurnsInTargetAreaToWin(),
                        this.gameMap.getIntruderMaxMoveDistance(),
                        this.gameMap.getIntruderMaxSprintDistance(),
                        -1 //TODO data structure to keep track of cooldown
                ),
                this.actionSuccess.getOrDefault(intruder, true)
        );
    }



}
