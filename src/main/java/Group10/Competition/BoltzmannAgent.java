package Group10.Competition;

import Group9.Game;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.AreaPercepts;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.ScenarioGuardPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Percept.Vision.VisionPrecepts;

import java.util.ArrayList;
import java.util.Random;

public class BoltzmannAgent implements Guard{

    private ArrayList<int[]> memory;

    public BoltzmannAgent() {
    }


    public GuardAction getAction(GuardPercepts percepts) {

        boolean lastActionExecuted = percepts.wasLastActionExecuted();
        AreaPercepts area = percepts.getAreaPercepts();
        ScenarioGuardPercepts scenario = percepts.getScenarioGuardPercepts();
        //SmellPercepts smells = percepts.getSmells();
        //SoundPercepts sounds = percepts.getSounds();
        VisionPrecepts vision = percepts.getVision();
        ObjectPercepts objects = vision.getObjects();

        GuardAction action = new NoAction();
        Distance distance = new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts));
        Angle maxAngle = scenario.getScenarioPercepts().getMaxRotationAngle();

        // creating a random angle from -45 - 45 degrees
        Random rand = new Random();
        // nextInt as provided by Random is exclusive of the top value so you need to add 1
        int randomNum = rand.nextInt(((int) maxAngle.getDegrees()) + 1) + (int)maxAngle.getDegrees();
        Angle randomAngle = Angle.fromDegrees(randomNum);

        double [][] actionArray = new double[3][2];
        Angle angleToSentryTower;

        // iterate through vision to check for sentry tower
        for(ObjectPercept objectPercept : objects.getAll()){
            if(objectPercept.getType() == ObjectPerceptType.SentryTower){
                angleToSentryTower = findAngle(objectPercept.getPoint(), objects);
                // then the sentry tower will be in our current direction
                if(angleToSentryTower.getDegrees() < 1){
                    return new Move(objectPercept.getPoint().getDistanceFromOrigin());
                }
                return new Rotate(angleToSentryTower);
            }
        }
        // TODO: remember and update previous temp
        double temperature = 5;

        // check if agent is inside a door or window -> then always move not rotate
        if(area.isInDoor() || area.isInWindow()){
            Distance newDistance = new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts));
            return new Move(newDistance);
        }

        double actionMoveSingle = Math.exp(evaluateAction(new Move(distance), objects)*temperature);
        double actionRotateSingle = Math.exp(evaluateAction(new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble())), objects)*temperature);

        double sumOfAllActions = actionMoveSingle + actionRotateSingle;

        // move
        actionArray[0][0] = 1;
        actionArray[0][1] = actionMoveSingle/sumOfAllActions;
        // rotate
        actionArray[1][0] = 2;
        actionArray[1][1] = actionRotateSingle/sumOfAllActions;


        double maxAction = Double.NEGATIVE_INFINITY;
        double whatAction = 0;

        for(int i = 0; i<actionArray.length; i++){
            //System.out.println("Max action: " + maxAction + " " + actionArray[i][1]);
            if(maxAction < actionArray[i][1]){
                maxAction = actionArray[i][1];
                whatAction = i;
            }
        }
        if(whatAction == 0){
            action = new Move(distance);
        }

        if(whatAction == 1 || !lastActionExecuted){
            action = new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));;
        }



        return action;
    }

    // TODO: check for explored space
    public double evaluateAction(GuardAction action, ObjectPercepts objects){

        int countWalls = 0;

        if(action instanceof Move){
            // check if future square is visited/explored/unexplored
            for(ObjectPercept objectPercept : objects.getAll()){
                if(objectPercept.getType() == ObjectPerceptType.Wall){
                    countWalls++;
                    /*if(objectPercept.getPoint().getDistanceFromOrigin().getValue() < 2.5){
                        countWalls = countWalls/10000;
                    }*/
                }
            }
            // evaluation based on countWalls
            return Math.exp(-countWalls/10);

        }
        if(action instanceof Rotate){
            // check if after rotation, the future move is visited/explored/unexplored
            // check if future square is visited/explored/unexplored
            for(ObjectPercept objectPercept : objects.getAll()){
                if(objectPercept.getType() == ObjectPerceptType.Wall){
                    countWalls++;
                }
            }
            // evaluation based on countWalls
            return Math.exp(countWalls/10);
        }
        return 0;
    }

    /**
     * Given a point and the vision percepts
     *
     * @param point
     * @param objectPercepts
     * @return
     */
    public Angle findAngle(Point point, ObjectPercepts objectPercepts){

        double yMean = 0;
        double xMean = 0;

        // calculate middle point
        for(ObjectPercept objectPercept : objectPercepts.getAll()){
            xMean += objectPercept.getPoint().getX();
            yMean += objectPercept.getPoint().getY();
        }

        xMean = xMean/objectPercepts.getAll().size();
        yMean = yMean/objectPercepts.getAll().size();

        double m = (yMean-point.getY())/(xMean-point.getX());
        double angle = Math.atan(m);

        return Angle.fromRadians(angle);
    }

    private double getSpeedModifier(GuardPercepts guardPercepts)
    {
        SlowDownModifiers slowDownModifiers =  guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers();
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

    public double changeTemp(double startTemp){

        double newTemp = 0;

        // decrease temperature over time to get from exploration to exploitation
        // TODO: make better function
        newTemp = startTemp*0.9;

        return newTemp;
    }

    public ArrayList<int[]> getMemory() {
        return memory;
    }
    // for now: store all the nodes visited
    public ArrayList<int[]> updateMemory(){

        if(memory == null){
            memory = new ArrayList<>();
            int[] firstIter = new int[]{0, 0};
            memory.add(firstIter);
        }else{


            // get last action executed, update coordinates and add to the list
        }
        return memory;
    }
}
