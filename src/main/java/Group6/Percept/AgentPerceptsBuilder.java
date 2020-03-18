package Group6.Percept;

import Group6.WorldState.GuardState;
import Group6.WorldState.IntruderState;
import Group6.WorldState.WorldState;
import Interop.Geometry.Direction;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;

public class AgentPerceptsBuilder {

    private VisionPerceptsBuilder visionPerceptsBuilder;
    private SoundPerceptsBuilder soundPerceptsBuilder;
    private SmellPreceptsBuilder smellPreceptsBuilder;
    private AreaPerceptsBuilder areaPerceptsBuilder;
    private ScenarioPerceptsBuilder scenarioPerceptsBuilder;

    public AgentPerceptsBuilder(
        VisionPerceptsBuilder visionPerceptsBuilder,
        SoundPerceptsBuilder soundPerceptsBuilder,
        SmellPreceptsBuilder smellPreceptsBuilder,
        AreaPerceptsBuilder areaPerceptsBuilder,
        ScenarioPerceptsBuilder scenarioPerceptsBuilder
    ) {
        this.visionPerceptsBuilder = visionPerceptsBuilder;
        this.soundPerceptsBuilder = soundPerceptsBuilder;
        this.smellPreceptsBuilder = smellPreceptsBuilder;
        this.areaPerceptsBuilder = areaPerceptsBuilder;
        this.scenarioPerceptsBuilder = scenarioPerceptsBuilder;
    }

    public IntruderPercepts buildIntruderPercepts(WorldState worldState, IntruderState agentState) {
        Direction targetDirectionAsPerceivedByAgent = agentState
            .getPerceivedDirectionTo(
                worldState
                    .getScenario()
                    .getTargetArea()
                    .getCenter()
            )
            .toInteropDirection();
        return new IntruderPercepts(
            targetDirectionAsPerceivedByAgent,
            visionPerceptsBuilder.buildPercepts(worldState, agentState),
            soundPerceptsBuilder.buildPercepts(worldState, agentState),
            smellPreceptsBuilder.buildPercepts(worldState, agentState),
            areaPerceptsBuilder.buildPrecepts(worldState, agentState),
            scenarioPerceptsBuilder.buildIntruderPercepts(worldState, agentState),
            agentState.wasLastActionExecuted()
        );
    }

    public GuardPercepts buildGuardPercepts(WorldState worldState, GuardState agentState) {
        return new GuardPercepts(
            visionPerceptsBuilder.buildPercepts(worldState, agentState),
            soundPerceptsBuilder.buildPercepts(worldState, agentState),
            smellPreceptsBuilder.buildPercepts(worldState, agentState),
            areaPerceptsBuilder.buildPrecepts(worldState, agentState),
            scenarioPerceptsBuilder.buildGuardPercepts(worldState, agentState),
            agentState.wasLastActionExecuted()
        );
    }

}