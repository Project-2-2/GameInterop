package Group4.Guards;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Group4.Agent;
import Group4.OurInterop.*;
import Group9.Game;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Group9.agent.container.AgentContainer;
import Group9.agent.container.IntruderContainer;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.*;
import Interop.Percept.Scenario.*;
import Interop.Percept.Smell.*;
import Interop.Percept.Sound.*;
import Interop.Percept.Vision.*;

public class PatrolGuard implements Guard{

    private int counter = 0;
    boolean inRandomMoves = false;

    private double error = 7.5;
    ArrayList<Action> trace = new ArrayList<Action>();
    private boolean positioning = false;
    private boolean chasing = false;
    private double totalAngle;
    static final int MAXIMUM_MOVES_BEFORE_THRESHOLD_CHANGE_GUARD = 500;


    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        if (counter > MAXIMUM_MOVES_BEFORE_THRESHOLD_CHANGE_GUARD) {
            inRandomMoves = true;
            chasing = false;
            positioning = false;
        }

        if (inRandomMoves) {
            counter--;
            if (counter == 0) {
                inRandomMoves = false;
            }
            if (!percepts.wasLastActionExecuted()) {
                return new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
            } else {
                return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()));
            }

        }
        for (ObjectPercept obj : percepts.getVision().getObjects().getAll()) {

            if (inRandomMoves) {
                if (!percepts.wasLastActionExecuted()) {
                    if (inRandomMoves) {
                        counter=counter-10;
                        if (counter == 0) {
                            inRandomMoves = false;
                        }
                    }
                    return new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
                } else {
                    return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()));
                }
            }
            if (obj.getType() == ObjectPerceptType.Intruder) {
                return chase(obj);
            } else if (obj.getType() == ObjectPerceptType.TargetArea && (!positioning || chasing)) {
                positioning = true;
            } else if (positioning) {
                if (new Distance(obj.getPoint(), new Point(0.0, 0.0)).getValue() > 0.5) {
                    if (obj.getPoint().getClockDirection().getDegrees() < error || 360 - obj.getPoint().getClockDirection().getDegrees() < error) {
                        return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()));
                    }
                    if (obj.getPoint().getClockDirection().getDegrees() > 180) {
                        return new Rotate(Angle.fromRadians(-1 * (obj.getPoint().getClockDirection().getRadians() - 2 * Math.PI)));
                    } else {
                        return new Rotate(Angle.fromRadians(-1 * (obj.getPoint().getClockDirection().getRadians())));
                    }
                } else if (new Distance(obj.getPoint(), new Point(0.0, 0.0)).getValue() < 0.5) {
                    totalAngle = obj.getPoint().getClockDirection().getRadians();
                    if (totalAngle > percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians()) {
                        totalAngle = totalAngle - percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians();
                        return new Rotate(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle());
                    } else {
                        return new Rotate(obj.getPoint().getClockDirection());
                    }
                } else if (totalAngle == 0) {
                    positioning = false;
                    totalAngle = Math.PI / 2;
                    return new Move(new Distance(10));
                }
            } else {
                if (totalAngle > Math.PI / 6 || totalAngle < -1 * Math.PI / 6) {
                    if (totalAngle > percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians()) {
                        totalAngle = totalAngle - percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians();
                        counter++;
                        return new Rotate(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle());
                    } else {
                        totalAngle = 0;
                        counter++;
                        return new Rotate(Angle.fromRadians(totalAngle));
                    }
                }
                totalAngle = Math.PI;
                counter++;
                return new Move(new Distance(15));
            }

        }return null;
    }


    //TODO implement code for the guard to chase and intruder agent
    public GuardAction chase(ObjectPercept o){
        return null;
    }
}
