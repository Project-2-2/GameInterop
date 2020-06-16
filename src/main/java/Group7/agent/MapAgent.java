package Group7.agent;

import Group9.Game;
import Interop.Action.DropPheromone;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;
import java.util.Set;

/**
 * sequence of update:
 *
 * 1:update current position, first is angle.
 * 2:update map.
 * 3:making decisions.
 *
 *
 * Pay attention that Math.sin using radians, not degree
 */

public class MapAgent implements Guard {

    public boolean debug = true;
    //----map parameter---
    final public double itself = 99;

    final public double unknownPlace = 0;

    final public double emptySpace = 11;

    final public double teleport = 22;

    final public double window = 33;

    final public double door = 44;

    final public double sentryTower = 55;

    final public double targetPlace = 66;

    final public double shadedArea = 77;

    final public double intruder = 88;

    final public double wall = 99;

    final public double guard = 10;

    //matrix for the map, needed be updated every turn
    public double[][] map;

    //summation of rotation angle.
    private double currentAngle;

    //The array list for the moving history of every turns.
    ArrayList<ActionHistory> actionHistory;

    //First element is row index, second is column index
    public double[] currentPosition;

    public int xx;

    public  int yy;

    public int turns = 0;

    public MapAgent(int height, int width){

        map = new double[height*2][width*2];

        map[height][width] = 10000;

        currentPosition = new double[2];


        currentPosition[0] = height;
        currentPosition[1] = width;

        currentAngle = 0;

        xx = height;
        yy = width;

        actionHistory = new ArrayList<ActionHistory>();
        actionHistory.add(new ActionHistory(2,0));
    }


    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        Set<ObjectPercept> objectPercepts = percepts.getVision().getObjects().getAll();
        ArrayList<ObjectPercept> objectPerceptArrayList = new ArrayList<ObjectPercept>(objectPercepts);

        updateCurrentAngle();
        updateCurrentPosition();
        printCurrentPosition();

        if (debug) System.out.println("Agnet's Angle is: "+currentAngle);
        updateMap(objectPerceptArrayList);

        if(!percepts.wasLastActionExecuted())
        {
            if(Math.random() < 0.1)
            {
                actionHistory.add(new ActionHistory(5,1));
                return new DropPheromone(SmellPerceptType.values()[(int) (Math.random() * SmellPerceptType.values().length)]);
            }

            Angle moveAngle = Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble());

            actionHistory.add(new ActionHistory(2,moveAngle.getDegrees()));
            return new Rotate(moveAngle);
        }
        else
        {
            actionHistory.add(new ActionHistory(1,percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
            return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
        }

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










    //-----------------------update methods------------------------

    /**
     * Update the map based on what it perceived
     * @param objectPerceptArrayList
     */
    public void updateMap(ArrayList<ObjectPercept> objectPerceptArrayList){

            double relativeX = currentPosition[0] - xx;
            if (debug) System.out.println("Relative X is: "+relativeX);

            double relativeY = currentPosition[1] - yy;
            if (debug) System.out.println("Relative Y is: "+relativeY);

            for (int i = 0;i<objectPerceptArrayList.size();i++){

                ObjectPerceptType type = objectPerceptArrayList.get(i).getType();

                double[] cor = getRelativeLocationOfOrigin(objectPerceptArrayList.get(i).getPoint());

                int x = (int)(cor[0] + relativeX +currentPosition[0]);
                if (debug) System.out.println("X is: "+x);
                int y = (int)(cor[1] + relativeY+currentPosition[1]);
                if (debug) System.out.println("final Y is: "+y);
                if (debug) System.out.println("Relative Y is: "+relativeY);
                if (debug) System.out.println("Cor y is："+ cor[1]);
                if (debug) System.out.println("---------------------");



                    if (debug) System.out.println("The point is: "+objectPerceptArrayList.get(i).getType().name());

                switch (type) {

                    case Wall: if (debug) System.out.println("Update point successfully: wall");map[x][y] = wall;
                    break;

                    case EmptySpace: if (debug) System.out.println("Update point successfully: empty space");map[x][y] = emptySpace;
                    break;

                    case Door:if (debug) System.out.println("Update point successfully: door"); map[x][y] = door;
                    break;

                    //case Intruder: map[x][y] = intruder;
                    //break;

                    case TargetArea: if (debug) System.out.println("Update point successfully: target Place");map[x][y] = targetPlace;
                    break;

                    case SentryTower:if (debug) System.out.println("Update point successfully:sentry tower");map[x][y] = sentryTower;
                    break;

                    case Window: if (debug) System.out.println("Update point successfully: window");map[x][y] = window;
                    break;

                   // case Guard:map[x][y] = guard;
                    //break;

                    case ShadedArea:if (debug) System.out.println("Update point successfully: shaded Area");map[x][y] = shadedArea;
                    break;

                    case Teleport: if (debug) System.out.println("Update point successfully: Teleport");map[x][y] = teleport;
                    break;

                }

            }

    }

    public double[] updateCurrentPosition(){


        int type = actionHistory.get(actionHistory.size()-1).type;


        if ( type== 1){

            double moveLength = actionHistory.get(actionHistory.size()-1).val;

            double angle = getProperAngle();
            if (debug) System.out.println("Proper Angle is: "+angle);

            double xABS = Math.cos(Math.toRadians(angle))*moveLength;

            double yABS = Math.sin(Math.toRadians(angle))*moveLength;

            if (getQuadrant() == 1){
                currentPosition[0] = xABS + currentPosition[0];
                currentPosition[1] = yABS + currentPosition[1];
            }else if (getQuadrant() == 2){
                currentPosition[0] = -xABS + currentPosition[0];
                currentPosition[1] = yABS + currentPosition[1];
            }else if (getQuadrant() == 3){
                currentPosition[0] = -xABS + currentPosition[0];
                currentPosition[1] = -yABS + currentPosition[1];
            }else if (getQuadrant() == 4){
                currentPosition[0] = xABS + currentPosition[0];
                currentPosition[1] = -yABS + currentPosition[1];
            }

            return currentPosition;

            // if only rotate, then current position won't change, just update current angle
        }else{
            return currentPosition;
        }



    }

    /**
     * If current angle is greater than 360, it needs to be set as angle - 360
     */
    public void updateCurrentAngle(){



        if (actionHistory.get(actionHistory.size()-1).type == 2){
            if (debug) System.out.println("update angle");
           setCurrentAngle(currentAngle + actionHistory.get(actionHistory.size()-1).val);

        }

        if (currentAngle>360){
            setCurrentAngle(currentAngle - 360);
        }


    }


    //-----------------------get methods------------------------

    public double getProperAngle(){
        double val = 0;

        int quadrant = getQuadrant();

        if (quadrant == 1){
            val = 90 - currentAngle;
        }else if(quadrant == 2){
            val = currentAngle - 270;
        }else if (quadrant == 3){
            val = 90 - (currentAngle - 180);
        }else{
            val = currentAngle - 90;
        }

        return val;
    }

    public int getQuadrant(){

        if (currentAngle>= 0 && currentAngle<=90){
            return 1;
        }else if(currentAngle>90 && currentAngle<=180){
            return 4;
        }else if (currentAngle>180 && currentAngle<=270){
            return 3;
        }else {
            return 2;
        }
    }

    /**
     * Find the relative location of the origin point.
     * @param point
     * @return element 1 is the row position, 2nd is column position
     * done!
     */
    public double[] getRelativeLocationOfOrigin(Point point){

        double[] val = new double[2];

        double x = point.getX();
        //if (debug) System.out.println("x is: "+x);

        double y = point.getY();
       // if (debug) System.out.println("y is: "+y);

        //if (debug) System.out.println(currentAngle);

        val[0] = x*Math.cos(Math.toRadians(-currentAngle)) - y*Math.sin(Math.toRadians(-currentAngle));

        val[1] = y*Math.cos(Math.toRadians(-currentAngle)) + x*Math.sin(Math.toRadians(-currentAngle));

        return val;
    }

    //-----------------------print methods------------------------

    public void printMemoryMap(){
        for (int i = 0;i<map.length;i++){
            for(int j = 0;j<map[0].length;j++){

                System.out.print(map[i][j]+" ");


            }
            System.out.println();
        }
    }

    public void printCurrentPosition(){
        System.out.println("------------------Currently the position of guard agent is---------------------");
        System.out.println("In row: "+currentPosition[0]);
        System.out.println("In column: "+currentPosition[1]);
    }

    public void printCurrentAngle(){

        System.out.println("Current Angle is: "+currentAngle);
    }

    public void printArray(double[] array){
        for (int i = 0;i<array.length;i++){
            System.out.println("The "+i+" element is: "+array[i]);
        }
    }

    public void printArray(int[] array){
        for (int i = 0;i<array.length;i++){
            System.out.println("The "+i+" element is: "+array[i]);
        }
    }




    //------------------getter and setter---------------------

    public double getCurrentAngle(){
        return currentAngle;
    }

    public void setCurrentAngle(double val){
        currentAngle = val;
    }



}


class ActionHistory{

    int type;
    /*
    1: move
    2: rotate
    3: NoAction
    4: Yell
    5: DropPheromone
     */

    double val;

    public ActionHistory(int type, double val){
        this.type = type;

        if (type == 3||type == 4){
            val = 0;
        }else {
            this.val = val;
        }
    }

    /**
     * debug method to see the action
     */
    public void printAction(){

        System.out.println("------------------The action you are checking is:---------------------");
        switch (type){
            case 1: System.out.println("Action is Move, with value:  "+val);
            break;

            case 2: System.out.println("Action is Rotate, with value:  "+val);
            break;

            case 3: System.out.println("Action is NoAction, with value: "+val);
            break;

            case 4: System.out.println("Action is Yell, with value: "+val);
            break;

            case 5: System.out.println("Action is Drop_Pheromone, with Pheromone Type:  "+val);
            break;
        }


    }
}
