 package Group7.agent;

 import Group7.agent.Intruder.AsSearch;
 import Group7.agent.Intruder.MindMap;
 import Group9.Game;
 import Interop.Action.IntruderAction;
 import Interop.Action.Move;
 import Interop.Action.NoAction;
 import Interop.Action.Rotate;
 import Interop.Agent.Intruder;
 import Interop.Geometry.Angle;
 import Interop.Geometry.Distance;
 import Interop.Percept.IntruderPercepts;
 import Interop.Percept.Scenario.SlowDownModifiers;
 import Interop.Percept.Smell.SmellPercept;
 import Interop.Percept.Sound.SoundPercept;
 import Interop.Percept.Vision.ObjectPercept;

 import java.util.ArrayList;
 import java.util.Set;

 public class AstarAgent implements Intruder {

    private MindMap map = new MindMap();
    private boolean firstTurn = true;
    private int lastMove = -1;

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
private int turn =0;

    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        System.out.println("AstarAgent.getAction");

        Set<ObjectPercept> objectPercepts = percepts.getVision().getObjects().getAll();
        Set<SoundPercept> soundPercepts = percepts.getSounds().getAll();
        Set<SmellPercept> smellPercepts = percepts.getSmells().getAll();
        //change the set to list, imo list is more convenient
        ArrayList<ObjectPercept> objectPerceptArrayList = new ArrayList<ObjectPercept>(objectPercepts);
        ArrayList<SoundPercept> soundPerceptArrayList = new ArrayList<SoundPercept>(soundPercepts);
        ArrayList<SmellPercept> smellPerceptArrayList = new ArrayList<>(smellPercepts);
        encapAction e = new encapAction();

        map.updateGridMap(percepts);
        map.computeTargetPoint(percepts.getTargetDirection());

        IntruderAction out_action =null;

//        List<int[]> listPositions =  AsSearch.computePath(map);
        //ArrayList<Integer> listMoves = AsSearch.getListOfActionsDirections(listPositions);
//        ArrayList array = AsSearch.getListDirections();
        ArrayList<Integer> listOfActions = AsSearch.computePath(map);
        System.out.println("listOfActions = " + listOfActions);
//        ArrayList<int[]> listConsecutivesActions = AsSearch.toConsecutiveMoves(listOfActions);

        if(listOfActions.size() ==0){
            System.out.println("no path found");
            return new NoAction();
        }
           int astar_move = listOfActions.get(0);
        System.out.println("astar_move = " + astar_move);
//        int astar_move = listConsecutivesActions.get(0)[0];
        //ArrayList array = AsSearch.getListDirections();
       // ArrayList<Integer> directions = AsSearch.getListOfActionsDirections(listPositions);
       // List<int[]> listConsecutivesActions = AsSearch.getNumberConsecutiveMoves(directions);

        int counter = 0;
        /* System.out.println("directions.get(0) = " + directions.get(0));
        int move = listConsecutivesActions.get(0)[0];
        System.out.println("move = " + move);
        int numberConsecutiveMove = listConsecutivesActions.get(0)[1];
        System.out.println("numberConsecutiveMove = " + numberConsecutiveMove);
         */

        if(!percepts.wasLastActionExecuted())
        {
            System.out.println("AstarAgent.getAction rejected");
//            // get the angle to rotate
//            if((lastMove == 1 && move == 3) || (lastMove == 3 && move == 2) || (lastMove == 2 && move == 4) || (lastMove == 4 && move == 1)){
//                Angle angle = Angle.fromDegrees(90);
//                return new Rotate(angle);
//            }
//            else if ((lastMove == 1 && move == 2) || (lastMove == 3 && move == 4) || (lastMove == 2 && move == 1) || (lastMove == 4 && move == 3)){
//                Angle angle = Angle.fromDegrees(180);
//                return new Rotate(angle);
//            }
//            else if ((lastMove == 1 && move == 4) || (lastMove == 3 && move == 1) || (lastMove == 2 && move == 3) || (lastMove == 4 && move == 2)) {
//                Angle angle = Angle.fromDegrees(270);
//                return new Rotate(angle);
//            }
            // otherwise just rotate randomly
//            else {
           Angle random_rotation_angle =  Angle.fromRadians(percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble());
            System.out.println("rotation_angle = " + random_rotation_angle.getRadians());
           return new Rotate(random_rotation_angle);
        }
     //       System.out.println("not accepted or 1st turn");

            // changed the first turn boolean
     //       if (firstTurn) {
     //           firstTurn=false;
        //          System.out.println("firstTurn = " + firstTurn);
      //      }
      //      System.out.println("turn = " + turn);
      //      turn++;

    /*        // get the angle to rotate
            if((lastMove == 1 && move == 3) || (lastMove == 3 && move == 2) || (lastMove == 2 && move == 4) || (lastMove == 4 && move == 1)){
                Angle angle = Angle.fromDegrees(90);
                System.out.println("Case 1");
                return new Rotate(angle);
            }
            else if ((lastMove == 1 && move == 2) || (lastMove == 3 && move == 4) || (lastMove == 2 && move == 1) || (lastMove == 4 && move == 3)){
                Angle angle = Angle.fromDegrees(180);
                System.out.println("Case 2");
                return new Rotate(angle);
            }
            else if ((lastMove == 1 && move == 4) || (lastMove == 3 && move == 1) || (lastMove == 2 && move == 3) || (lastMove == 4 && move == 2)) {
                Angle angle = Angle.fromDegrees(270);
                System.out.println("Case 3");
                return new Rotate(angle);
            }
            // otherwise just rotate randomly
            else {
                System.out.println("Case 4");
                return new Rotate(Angle.fromRadians(percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
            }
        }


        else
        { */

            System.out.println("last action accepted");
         //   if ((astar_move == 1)) {
// changed the first turn boolean
//        System.out.println("turn = " + turn);
//        turn++;
//        if (firstTurn) {
//            firstTurn = false;
//            System.out.println("firstTurn = " + firstTurn);
//        }else
//        {
            if ((astar_move == 1)){
                System.out.println("map.getState().getAngle().getDegrees()  = " + map.getState().getAngle().getDegrees() );
                if (map.getState().getAngle().getDegrees() == 180) {
                   Move forward =  new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
                   System.out.println("Move 1 "+forward.getDistance().getValue());
                   out_action = forward;
               }
                /*
                else if(map.getState().getAngle().getDegrees() == 90){
                    Angle angle = Angle.fromDegrees(270);
                    return new Rotate(angle);
                }*/
                    else {
                        out_action = rotateTo(180, percepts);
                    }
                }


            if ((astar_move == 2)){
                System.out.println("map.getState().getAngle().getDegrees()  = " + map.getState().getAngle().getDegrees() );
                if (map.getState().getAngle().getDegrees() == 0) {
                    Move forward =new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
                    System.out.println("Move 2 "+forward.getDistance().getValue());
                    out_action = forward;
                }
                else {
                    out_action = rotateTo(0, percepts);
                }
            }

            if ((astar_move == 3)){
                System.out.println("map.getState().getAngle().getDegrees()  = " + map.getState().getAngle().getDegrees() );
                if (map.getState().getAngle().getDegrees() == 90) {
                    Move forward = new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
                    System.out.println("Move 3 "+forward.getDistance().getValue());
                    out_action = forward;
                }
                else {
                    out_action = rotateTo(90, percepts);
                }
            }

            if ((astar_move == 4)){
                System.out.println("map.getState().getAngle().getDegrees()  = " + map.getState().getAngle().getDegrees() );
                if (map.getState().getAngle().getDegrees() == 270) {
                    Move forward = new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
                    System.out.println("Move 4 "+forward.getDistance().getValue());
                    out_action = forward;
                }
                else {
                    out_action = rotateTo(270, percepts);
                }
            }

            if(out_action ==null){
                Move ac =  new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
                System.out.println("else Move "+ac.getDistance().getValue());
                out_action = ac;
            }
            map.updateState(out_action);
            return out_action;
//        }

    }


    private Rotate rotateTo(int a, IntruderPercepts percepts){

        double old = map.getState().getAngle().getDegrees();
        Angle rotation_angle = Angle.fromDegrees(a-old);

        System.out.println("percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees() = " + percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees());

        if(rotation_angle.getDegrees() > percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()){
            rotation_angle = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();
        }else if(rotation_angle.getDegrees() < -percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()){
            rotation_angle = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();
            rotation_angle = Angle.fromRadians(-rotation_angle.getRadians());
        }
        System.out.println("Rotate to "+a+"; new angle is "+(old+rotation_angle.getDegrees()));

        return new Rotate(Angle.fromDegrees(rotation_angle.getDegrees()));
    }

}

