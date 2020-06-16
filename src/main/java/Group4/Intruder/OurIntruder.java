package Group4.Intruder;

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
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Percept.*;
import Interop.Percept.Scenario.*;
import Interop.Percept.Smell.*;
import Interop.Percept.Sound.*;
import Interop.Percept.Vision.*;

import static java.lang.Math.abs;

public class OurIntruder implements Intruder{
    private int numberOfMoves = 0;
    private int counter = 0;
    boolean inRandomMoves = false;
    private double error = 7.5;
    int x;
    int y;
    public File mapFile = new File("C:\\Users\\Mark\\Documents\\Year 2 second half\\Project 2.2\\DKE-Project-2-2\\GameInterop-master\\src\\main\\java\\Group9\\map\\maps\\test_2.map");
    private final static Charset ENCODING = StandardCharsets.UTF_8;
    private final Path filePath = Paths.get(String.valueOf(mapFile));
    static final int MAXIMUM_MOVES_BEFORE_THRESHOLD_CHANGE = 500;

    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        //if (1==1) return new Rotate(new Angle(1*Math.PI));

        if(percepts.getAreaPercepts().isJustTeleported()){
            counter = 0;
            inRandomMoves = false;
        }

        double rand = Math.random();
        if (counter > MAXIMUM_MOVES_BEFORE_THRESHOLD_CHANGE) {
            inRandomMoves = true;
        }
        for(ObjectPercept obj : percepts.getVision().getObjects().getAll()){
            if(obj.getType() == ObjectPerceptType.TargetArea){
//                System.out.println("I see the target!");
                //Change this to 0.1, otherwise we might constantly just be missing the target
                if (percepts.getTargetDirection().getDegrees() < error || 360 - percepts.getTargetDirection().getDegrees()<error) {
                    counter++;
//                    System.out.println("Move towards target performed");
                    return new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
                } else {
                    counter++;
//                    System.out.println("Rotation");
//                    System.out.println("degrees to rotate: " + percepts.getTargetDirection().getDegrees());
                    if (percepts.getTargetDirection().getDegrees()> 180){
                        return new Rotate(new Angle(percepts.getTargetDirection().getRadians()-2*Math.PI));
                    }
                    else {
                        return new Rotate(new Angle(percepts.getTargetDirection().getRadians()));
                    }
                }
            }

            if(obj.getType() == ObjectPerceptType.Guard){
                return new Rotate(new Angle(Math.PI));
            }

            if(obj.getType() == ObjectPerceptType.Teleport) {
//                System.out.println("i see teleport");
                if (obj.getPoint().getClockDirection().getDegrees() < error || 360 - obj.getPoint().getClockDirection().getDegrees() < error) {
                    return new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
                } else {
//                    System.out.println("Degrees towards target: " + obj.getPoint().getClockDirection().getDegrees());
                    if (obj.getPoint().getClockDirection().getDegrees() > 180){
//                        System.out.println("Rotating: " + (Math.toDegrees(obj.getPoint().getClockDirection().getRadians()-2*Math.PI)));
                        return new Rotate(new Angle(-1* (obj.getPoint().getClockDirection().getRadians()-2*Math.PI)));
                    }
                    else {
//                        System.out.println("Rotating: " + (Math.toDegrees(obj.getPoint().getClockDirection().getRadians())));
                        return new Rotate(new Angle(-1 * (obj.getPoint().getClockDirection().getRadians())));
                    }
                }
            }

        }

            if (!inRandomMoves) {

                if (!percepts.wasLastActionExecuted()) {
                    counter++;
                    return new Rotate(Angle.fromRadians(percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
                } else {
                    if (abs(percepts.getTargetDirection().getDegrees()) < 0.1) {
                        counter = counter + 5;
                        return new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
                    } else {
                        counter = counter + 5;
                        return new Rotate(percepts.getTargetDirection());
                    }
                }
            }else{
                counter--;
                if(counter == 0){
                    inRandomMoves = false;
                }
                if(!percepts.wasLastActionExecuted())
                {
                    return new Rotate(Angle.fromRadians(percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
                }
                else
                {
                    return new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
                }

            }
    }

    private double getSpeedModifier(IntruderPercepts guardPercepts)
    {
        SlowDownModifiers slowDownModifiers =  guardPercepts.getScenarioIntruderPercepts().getScenarioPercepts().getSlowDownModifiers();
        if(guardPercepts.getAreaPercepts().isInWindow())
        {
            return slowDownModifiers.getInWindow();
        }
        else if(guardPercepts.getAreaPercepts().isInSentryTower())
        {
            return slowDownModifiers.getInSentryTower();
        }
        else if(guardPercepts.getAreaPercepts().isInDoor())
        {
            return slowDownModifiers.getInDoor();
        }

        return 1;
    }


//    public void readMap(){
//        try (Scanner scanner = new Scanner(filePath, ENCODING.name())){
//            while (scanner.hasNextLine()){
//                parseLine(scanner.nextLine());
//            }
//        }
//
//        catch(Exception e){
//
//        }
//    }

//    protected void parseLine(String line){
//        try(Scanner scanner = new Scanner(line)){
//            scanner.useDelimiter("=");
//            if(scanner.hasNext()){
//                String id = scanner.next();
//                String value = scanner.next();
//                value = value.trim();
//                id = id.trim();
//                String[] items = value.split(" ");
//                switch(id){
//                    case "targetArea":
//                        targetX = Integer.parseInt(items[0]);
//                        targetY = Integer.parseInt(items[1]);
//                }
//            }
//        }
//    }


/*
    public AMove Qlearning(ArrayList<AMove> moves, IntruderContainer agent){
        //first check, if there are many moves made. If there are too many moves done, increase the chance of doing a random move.
        //This is useful when we need to teleport to get to the right room. With a higher chance of doing random moves,
        //we increase the chance of finding the teleport
        if (numberOfMoves > maximumMovesBeforeThresholdChange && threshold < 1){
            //prevent threshold from increasing > 1
            double residue = 1-threshold;
            double growth = 0.1 * residue;
            threshold = threshold + growth;
            //set the number of moves to 0 again
            numberOfMoves = 0;
        }


        //If the random number is smaller than the threshold:
        //Pick a random move out of the set of all moves
        Random generator = new Random();
        double epsilon = Math.random();
        if (epsilon < threshold) {
            int randomIndex = generator.nextInt(moves.size());
            AMove randomMove = moves.get(randomIndex);
            numberOfMoves++;
            return randomMove;
        }
        //Else, evaluate all moves and pick the move with the highest evaluation (lowest error in this case)
        else{
            int bestIndex = 0;
            double lowestError = Double.POSITIVE_INFINITY;
            //Loop through all moves
            for (int i=0; i<moves.size();i++){
                //Calculate evaluation
                double currentAbsoluteError = evaluateMove(moves.get(i),targetDirection);
                //If evaluation is better than current best, set new current best
                if(currentAbsoluteError<lowestError){
                    bestIndex = i;
                    lowestError = currentAbsoluteError;
                }
            }
            numberOfMoves++;
            return moves.get(bestIndex);
        }

    }
*/


//    public double evaluateMove(AMove m, Direction targetDirection) {
//        double absoluteError;
//        double movingDirection;
//        double xVector = getCurrentXLocation() - m.getX();
//        double yVector = getCurrentYLocation() - m.getY();
//        if (yVector == 0) {
//            if (xVector > 0) {
//                movingDirection = 0;
//            } else {
//                movingDirection = 180;
//            }
//        } else if (xVector == 0) {
//            if (yVector > 0) {
//                movingDirection = 90;
//            } else {
//                movingDirection = 270;
//            }
//        } else {
//            movingDirection = Math.atan(yVector / xVector);
//        }
//        absoluteError = abs(targetDirection);
//
//        return absoluteError;
//
//    }
//
//    public void updateDirection(){
//        double xVector = targetX - getCurrentXLocation();
//        double yVector = targetY - getCurrentYLocation();
//
//        if (yVector == 0) {
//            if (xVector > 0) {
//                this.targetDirection = 0;
//            } else {
//                this.targetDirection = 180;
//            }
//        } else if (xVector == 0) {
//            if (yVector > 0) {
//                this.targetDirection = 90;
//            } else {
//                this.targetDirection = 270;
//            }
//        } else {
//
//            this.targetDirection = Math.toDegrees(Math.atan(yVector / xVector));
//        }
//    }
//
//    static double x = 1;
//    static double y = 1;



//    public static void main(String[] args){
//        OurIntruder q = new OurIntruder();
//        String mapD = System.getProperty("user.dir")+System.getProperty("file.separator")+"src"+System.getProperty("file.separator")+"GameControllerSample"+System.getProperty("file.separator")+"testmap.txt";
//        Scenario scenario = new Scenario(mapD);
//        //System.out.println(scenario.spawnAreaIntruders.getLeftBoundary());
//        q.setCurrentLocation(scenario.spawnAreaIntruders.getLeftBoundary() + (scenario.spawnAreaIntruders.getRightBoundary()-scenario.spawnAreaIntruders.getLeftBoundary())/2,
//                scenario.spawnAreaIntruders.getTopBoundary() + (scenario.spawnAreaIntruders.getBottomBoundary()-scenario.spawnAreaIntruders.getTopBoundary())/2);
//
//        //@Matt please check if this is the correct way to get the position out of the file
//        q.setTargetLocation(scenario.targetArea.getLeftBoundary() + (scenario.targetArea.getRightBoundary()-scenario.targetArea.getLeftBoundary())/2,
//                scenario.targetArea.getTopBoundary() + (scenario.targetArea.getBottomBoundary()-scenario.targetArea.getTopBoundary())/2);
//
//        //Set initial target direction
//        q.updateDirection();
//
//
//        Runnable run = new Runnable() {
//            public void run() {
//                ArrayList<AMove> moves = ActionsManager.radiusMoves(q.getCurrentXLocation(),q.getCurrentYLocation());
//                Move m =  q.Qlearning(moves);
//                double[] teleportCheck =  checkTeleport(q);
//                if(teleportCheck[0] != -1){
//                    moveExplorer(teleportCheck[1],teleportCheck[2],q);
//                    System.out.println("teleported");
//                }else{
//                    moveExplorer(m.x,m.y,q);
//                }
//                //The goal is to call this after every move, because direction to target changes
//                q.updateDirection();
//            }
//        };
//
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//        executor.scheduleAtFixedRate(run, 0, 5, TimeUnit.MILLISECONDS);
//
//    }

//    public static void moveExplorer(double x, double y,OurIntruder q){
//        String mapD = System.getProperty("user.dir")+System.getProperty("file.separator")+"src"+System.getProperty("file.separator")+"GameControllerSample"+System.getProperty("file.separator")+"testmap.txt";
//        String gameFileD = System.getProperty("user.dir")+System.getProperty("file.separator")+"src"+System.getProperty("file.separator")+"GameControllerSample"+System.getProperty("file.separator")+"gamefile.txt";
//        Scenario scenario = new Scenario(mapD);
//        double[][] guardPositions = new double[scenario.getNumGuards()][4];
//        int[] guardStates = new int[scenario.getNumGuards()];
//        //System.out.println(scenario.getGameFile().toString());
//        try{
//            //System.out.println("working");
//            FileWriter write = new FileWriter(gameFileD,false);
//            PrintWriter prtln = new PrintWriter(write);
//            prtln.println("signal = 1"); // semaphore code   0-> game controller, 1-> guards, 2-> intruder
//            prtln.println("scenario = " + scenario.getMapDoc());
//            for(int i=0;i<scenario.getNumGuards();i++){
//                prtln.println("guard test = "+String.valueOf(i)+" "+String.valueOf(guardStates[i])+" "+String.valueOf(guardPositions[i][0])+" "+String.valueOf(guardPositions[i][1])+" "+String.valueOf(guardPositions[i][2])+" "+String.valueOf(guardPositions[i][3]));
//            }
//            prtln.println("explorer = " + x + " " + y);
//            prtln.close();
//        }
//        catch(Exception e){
//            System.out.println("failed");
//            // we ar in trouble
//        }
//        q.setCurrentLocation(x,y);
//        System.out.println("agent moved " + x + " " + y);
//    }

//    public static double[] checkTeleport(OurIntruder q){
//        System.out.println("checking portals");
//        String mapD = System.getProperty("user.dir")+System.getProperty("file.separator")+"src"+System.getProperty("file.separator")+"GameControllerSample"+System.getProperty("file.separator")+"testmap.txt";
//        Scenario scenario = new Scenario(mapD);
//
//        double x = q.getCurrentLocation().getX();
//        double y = q.getCurrentLocation().getY();
//
//        ArrayList<TelePortal> portals = scenario.getTeleportals();
//
//        //System.out.println(scenario.getWalls().size());
//        for(TelePortal p: portals){
//            Rectangle2D portalRect = new Rectangle2D.Double(p.getLeftBoundary(),p.getBottomBoundary(), p.getRightBoundary() -p.getLeftBoundary() , p.getTopBoundary()-p.getBottomBoundary());
//            if(portalRect.contains(x,y)){
//                System.out.println("in portal");
//                return new double[]{1,Double.valueOf(p.getNewLocation()[0]),Double.valueOf(p.getNewLocation()[1])};
//                //moveExplorer(Double.valueOf(p.getNewLocation()[0]),Double.valueOf(p.getNewLocation()[1]),q);
//            }
//        }
//        return new double[]{-1,-1,-1};
//    }


}
