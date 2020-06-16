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
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;
import java.util.Set;

public class RL implements Guard {

    ArrayList<ActionHistory> actionHistory;

    public boolean ss = false;

    public RL(){
        actionHistory = new ArrayList<>();
    }

    public int turns = 0;

    @Override
    public GuardAction getAction(GuardPercepts percepts) {


        System.out.println("turn is : "+turns);
        turns++;

        Txt IO = new Txt();

        Built_In  bi = new Built_In();

        Capturer cp =new Capturer();



        Set<ObjectPercept> objectPercepts = percepts.getVision().getObjects().getAll();
        Set<SoundPercept> soundPercepts = percepts.getSounds().getAll();
        Set<SmellPercept> smellPercepts = percepts.getSmells().getAll();
        ArrayList<ObjectPercept> objectPerceptArrayList = new ArrayList<ObjectPercept>(objectPercepts);
        ArrayList<SoundPercept> soundPerceptArrayList = new ArrayList<SoundPercept>(soundPercepts);
        ArrayList<SmellPercept> smellPerceptArrayList = new ArrayList<>(smellPercepts);


        if (objectPerceptArrayList.size() == 0){
            System.out.println("No object in view");
        }else {
            //has objects in the field of view:
        }





        for (int i = 0;i<objectPerceptArrayList.size();i++){
            //System.out.println("The "+(i+1)+" point for vision object is: "+objectPerceptArrayList.get(i).getType());


            if (objectPerceptArrayList.get(i).getType().equals(ObjectPerceptType.Intruder)){
                System.out.println(i+" intruder x point: "+objectPerceptArrayList.get(i).getPoint());
                System.out.println(i+" intruder y point: "+objectPerceptArrayList.get(i).getPoint().getY());
            }

        }







        if(!percepts.wasLastActionExecuted())
        {
            if(Math.random() < 0.1)
            {
                actionHistory.add(new ActionHistory(5,1));
                return new DropPheromone(SmellPerceptType.values()[(int) (Math.random() * SmellPerceptType.values().length)]);
            }

            actionHistory.add(new ActionHistory(2,percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
            return new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
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



    public int[] putPointInorder( ArrayList<ObjectPercept> objectPerceptArrayList){

        int[] input = new int[objectPerceptArrayList.size()];

        ArrayList<Point> points = new ArrayList<>();


        for (int i = 0;i<objectPerceptArrayList.size();i++){

            //if ()



        }
        return  input;

    }

    //make sure objectarraylist has size greater than 0;
    public ArrayList<Point> sortPoints(ArrayList<Point> point){

        ArrayList<Point> aaa = new ArrayList<>();

        ArrayList<Point> val = new ArrayList<>();

        for (int i = 0;i<point.size();i++){

            aaa.add(point.get(i));

        }

       for (int i = 0;i<aaa.size();i++){

           val.add(findSmallestX(point));
           point.remove(findSmallestX(point));

       }

        return val;

    }

    public Point findSmallestX (ArrayList<Point> point){
        Point small = point.get(0);

        for (int i = 0;i<point.size();i++){
            if(small.getX()>point.get(i).getX()){
                small = point.get(i);
            }

        }

        return small;

    }

}
