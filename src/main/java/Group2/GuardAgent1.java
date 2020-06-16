package Group2;

import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Random;
import java.util.Set;

public class GuardAgent1 implements Guard {

    boolean DEBUG = false;

    //Patrol stuff
    int moveCounter;  //Counts amount of turns that the guard could walk uninterrupted
    int goodPatrolLength = 30; //Min amount of steps guard needs to take to consider path a viable patrol path
    double defaultPatrolRotation = 180; //Aka walking up and down a line
    double defaultSpinRotation = 360; //Aka complete spins
    int distanceBetweenSpins = 10; //Spin after 10 steps if in Final Patrol Phase
    double rotationLeft = 0; //Counter to keep track of how much rotation is left for multi-turn rotations
    boolean patrolFound = false; //Boolean to indicate goodPatrolLength has been reached, also initializes the patrol
    int maxPatrolLength; //Keeps track of the maximum length a patrol can be (max moveCounter)
    int patrolLength; //Current amount of steps a patrolLength is
    int patrolBuffer = 2; //Buffer so the patrol doesn't get too close to walls (You can see the area in front of a wall before hitting it
    int patrolPhase = 0; //What phase of patrol setup the guard is in
    boolean patrolMode = false; //Boolean to indicate if guard is patrolling

    //Chase stuff
    boolean chaseMode = false;
    ObjectPercept intruder;
    double chaseAngleOffset = 8; //If angle between looking and intruder is less than this, then walk straight. Else allign with where intruder is
    boolean found = false; //Check if an intruder was spotted in FoV
    int turnsMissing = 0; //Turns since guard lost track of the intruder
    int giveUp = 8; //Turns until the guard gives up after losing track
    int chaseCounter = 0;

    //Yelling stuff
    Direction yellDirection;
    boolean yellInvestigateMode = false; //Turn on when a yell from another agent was heard
    boolean yelledLastTurn = false;
    int investigationTurns = 0;
    int maxInvestigationTurns = 15; //Amount of turns that a guard will spend walking towards a yell


    int counter;
    int roamPhaseLength = 80;
    int ID;
    Random random = new Random();




    public GuardAgent1(int ID){
        this.ID = ID;
        System.out.println("Made Guard "+ID);
    }


    @Override
    public GuardAction getAction(GuardPercepts guardPercepts) {
        GuardAction plannedAction = null;
        Set<ObjectPercept> inView = guardPercepts.getVision().getObjects().getAll();
        Set<SoundPercept> inHearingRange = guardPercepts.getSounds().getAll();
        double maxRotation = guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees();

        //-----------Spotting an intruder-----------------------------------------------------------------------------
        found = false;
        for(ObjectPercept object : inView){
            if(object.getType().equals(ObjectPerceptType.Intruder)){
                found = true;
                intruder = object;
                turnsMissing = 0;


                if(chaseCounter==0) {
                    System.out.println(ID+" Intruder spotted");
                    chaseMode = true;
                    yellInvestigateMode = false;
                    investigationTurns = 0;
                    patrolMode = false;
                    moveCounter = 0;
                    chaseCounter++;
                }
            }
        }

        //Listen for other Guards yells, ignore sound if you just yelled (don't listen to yourself)
        if(yelledLastTurn == false && chaseMode == false && yellInvestigateMode == false) {
            for (SoundPercept sound : inHearingRange) {
                if (sound.getType().equals(SoundPerceptType.Yell)) {
                    yellDirection = sound.getDirection();
                    rotationLeft = yellDirection.getDegrees();
                    patrolMode = false;
                    moveCounter = 0;
                    yellInvestigateMode = true;
                    investigationTurns = 0;
                    System.out.println(ID+ " Yell Detected");
                }
            }
        }else{
            yelledLastTurn = false;
        }



        //Signal other guards to come to your location when you spot an intruder
        if(chaseCounter==1){
            yelledLastTurn = true;
            chaseCounter++;
            return new Yell();
        }


        if(found == false && chaseCounter>0){
            turnsMissing++;
            //MAKE A TEMPORARY FAKE INTRUDER SO THE GUARD CAN TEMPORARILY KEEP RUNNING IN THAT DIRECTION
        }

        if(turnsMissing==giveUp){
            chaseMode = false;
            intruder = null;
            turnsMissing = 0;
            chaseCounter = 0;
            System.out.println(ID+" Chase stopped");
        }

        //If you are patrolling but see another guard, look for new patrol route
        if (patrolMode == true) {
            for(ObjectPercept object : inView){
                if(object.getType().equals(ObjectPerceptType.Guard)){
                    patrolMode = false;
                    moveCounter = 0;
                }
            }
        }

        //----------Yell Investigation--------------------------------------------------------------------------------
        if(yellInvestigateMode==true){
            if(rotationLeft>0) {
                if (maxRotation > rotationLeft) {
                    plannedAction = new Rotate(Angle.fromDegrees(rotationLeft));
                    rotationLeft = 0;
                } else {
                    plannedAction = new Rotate(Angle.fromDegrees(maxRotation));
                    rotationLeft = rotationLeft - maxRotation;
                }
            }else{
                if(investigationTurns<maxInvestigationTurns) {
                    plannedAction = new Move(guardPercepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
                    investigationTurns++;
                }else{
                    yellInvestigateMode = false;
                    investigationTurns = 0;
                    System.out.println(ID+" Yell investigation stopped");
                }
            }

        }

        //----------Chasing--------------------------------------------------------------------------------------------
        if (chaseMode==true){
            double intruderDirection = intruder.getPoint().getClockDirection().getDegrees();
            double intruderDistance = intruder.getPoint().getDistanceFromOrigin().getValue();

            //Check if the angle is close enough to engage, or if some rotation is required
            if(intruderDirection<chaseAngleOffset || 360-intruderDirection<chaseAngleOffset){
                //Check how close the intruder is. Either run as close as possible or run the distance between the agents
                if(intruderDistance <guardPercepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()){
                    plannedAction = new Move(new Distance(intruderDistance));
                }else{
                    plannedAction = new Move(guardPercepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
                }
            }else{
                if(intruderDirection<maxRotation || (360-intruderDirection<maxRotation && 360-intruderDirection<0)){
                    if(intruderDirection<180) {
                        plannedAction = new Rotate(Angle.fromDegrees(intruderDirection));
                    }else{
                        System.out.println("Negative rotation");
                        plannedAction = new Rotate(Angle.fromDegrees(-1*(360-intruderDirection)));
                    }
                }else{
                    if(intruderDirection<180) {
                        plannedAction = new Rotate(Angle.fromDegrees(maxRotation));
                    }else{
                        System.out.println("Negative rotation");
                        plannedAction = new Rotate(Angle.fromDegrees(-1*maxRotation));
                    }
                }
            }
        }

        //----------Roaming the map------------------------------------------------------------------------------------
        if(patrolMode==false && chaseMode == false) {
            //A possible patrol path was found
            if (moveCounter == goodPatrolLength && counter >= roamPhaseLength) {
                patrolFound = true;
            }

            ObjectPercept closest = null;
            for (ObjectPercept object : inView) {
                if (!object.getType().equals(ObjectPerceptType.EmptySpace)) {
                    if (closest == null) {
                        closest = object;
                    } else {
                        if (object.getPoint().getDistance(new Point(0, 0)).getValue() < closest.getPoint().getDistance(new Point(0, 0)).getValue())
                            closest = object;
                    }
                }


                //Turn when you see a wall
                if (closest != null) {
                    if (closest.getType().equals(ObjectPerceptType.Wall)) {

                        double rotation = random.nextDouble()*maxRotation;
                        if(random.nextDouble()<0.5){
                            rotation = rotation*-1;
                        }

                        plannedAction = new Rotate(Angle.fromDegrees(rotation));
                        if (patrolFound == true) {
                            maxPatrolLength = moveCounter;
                            patrolMode = true;
                        } else {
                            moveCounter = 0;
                        }
                    }
                }
            }

            //Move if roaming and not hitting wall
            if (plannedAction == null) {
                plannedAction = new Move(guardPercepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
                moveCounter++;
            }
        }

        //--------------------Patrolling--------------------------------------------------------------------------
        if(patrolMode==true && chaseMode==false){
            plannedAction = null;

            //initialize patrol
            if(patrolFound==true){
                System.out.println(ID+" Setting up Patrol");
                patrolLength = moveCounter-patrolBuffer;
                rotationLeft = defaultPatrolRotation;
                patrolPhase = 1;
                patrolFound = false;
            }

            //Phase 2, walk patrolLength once
            if (patrolPhase == 2) {
                if(moveCounter<patrolLength) {
                    plannedAction = new Move(guardPercepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
                    moveCounter++;
                }else {
                    patrolLength = patrolLength-patrolBuffer;
                    patrolPhase = 3;
                }
            }

            //Final patrol, quick 360 check
            if(patrolPhase == 5){
                if (maxRotation > rotationLeft) {
                    plannedAction = new Rotate(Angle.fromDegrees(rotationLeft));
                    rotationLeft = defaultPatrolRotation;
                    patrolPhase = 4;
                } else {
                    plannedAction = new Rotate(Angle.fromDegrees(maxRotation));
                    rotationLeft = rotationLeft - maxRotation;
                }
            }

            //Final patrol, walking phase
            if(patrolPhase == 4) {
                if(moveCounter<patrolLength) {
                    if(moveCounter%distanceBetweenSpins==0){
                        rotationLeft = defaultSpinRotation;
                        patrolPhase = 5;
                    }
                    plannedAction = new Move(guardPercepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
                    moveCounter++;
                }else {
                    rotationLeft = defaultPatrolRotation;
                    patrolPhase = 3;
                }
            }

            //Final patrol, turning phase
            if(patrolPhase == 3) {
                if (maxRotation > rotationLeft) {
                    plannedAction = new Rotate(Angle.fromDegrees(rotationLeft));
                    rotationLeft = 0;
                    moveCounter = 0;
                    patrolPhase = 4;
                } else {
                    plannedAction = new Rotate(Angle.fromDegrees(maxRotation));
                    rotationLeft = rotationLeft - maxRotation;
                }
            }


            //Phase 1, rotate 180 degrees
            if(patrolPhase==1){
                if (maxRotation > rotationLeft) {
                    plannedAction = new Rotate(Angle.fromDegrees(rotationLeft));
                    rotationLeft = defaultPatrolRotation;
                    moveCounter = 0;
                    patrolPhase = 2;
                } else {
                    plannedAction = new Rotate(Angle.fromDegrees(maxRotation));
                    rotationLeft = rotationLeft - maxRotation;
                }
            }
        }
//---------------------------------------------------------------------------------------------------------------



        counter++;
        //map.updateMap(plannedAction,guardPercepts);
        return plannedAction;
        }

}
