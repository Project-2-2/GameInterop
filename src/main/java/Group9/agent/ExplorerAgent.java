package Group9.agent;

import Group9.Game;
import Group9.math.graph.Graph;
import Group9.math.graph.Vertex;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Percept.GuardPercepts;

public class ExplorerAgent implements Guard {

    private final Graph<DataContainer> graph = new Graph<>();

    public ExplorerAgent() {}

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        graph.add(new Vertex<>(new DataContainer(percepts)));
        if(percepts.wasLastActionExecuted())
        {
            System.out.println("move");
            return new Move(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
        }
        else
        {
            System.out.println("rotate");
            return new Rotate(Angle.fromRadians(
                percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()
            ));
        }
    }

    public static class DataContainer {
        private GuardPercepts percepts;
        public DataContainer(GuardPercepts percepts)
        {
            this.percepts = percepts;
        }

        public GuardPercepts getPercepts() {
            return percepts;
        }
    }
}
