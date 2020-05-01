package Group8.Controller;


import Group8.Agent.AgentsFactory;
import Group8.Controller.Utils.*;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Point;
import Interop.Geometry.Distance;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;


import javafx.application.Application;
import javafx.scene.paint.Color;


import java.util.*;

public class Controller {

    private static final int FPS = 60;
    private static final long frameTimeMillis = 1000/FPS;
    private static boolean running;
    private static double degreeOfError = 0.1;
    private static List<Intruder> intruders;
    private static List<Guard> guards;
    private static Scenario scenario;
    private static Hashtable<Intruder, IntruderInfo> infoHashtableI;
    private static Hashtable<Guard, GuardInfo> infoHashtableG;


    public static void start(boolean GUI){
        running = false;
        scenario = G8Launcher.Scenario;

        infoHashtableI = new Hashtable<>();
        infoHashtableG = new Hashtable<>();

        createAgents(scenario.getNumIntruders(),scenario.getNumGuards());
        if(GUI) {
            Application.launch(LauncherGUI.class, null);
        }else {
            run();
        }
    }


    private static void createAgents(int nIntruders, int nGuards){
        ArrayList<IntruderInfo> intruderInfoArrayList = scenario.getIntruders();
        ArrayList<GuardInfo> guardInfoArrayList = scenario.getGuards();

        intruders = AgentsFactory.createIntruders(nIntruders);
        guards = AgentsFactory.createGuards(nGuards);

        for (int i = 0; i < intruderInfoArrayList.size(); i++) {
            infoHashtableI.put(intruders.get(i), intruderInfoArrayList.get(i));
        }
        for (int i = 0; i < guardInfoArrayList.size(); i++) {
            infoHashtableG.put(guards.get(i),guardInfoArrayList.get(i));
        }
    }

    public static void gameStep(){

       // perceptSteps(); //Create the vision of the agents before doing an action

        //TODO: implement simulation loop
        for (Intruder i:
                intruders) {
            //Update percepts
            //TODO: create method to get percepts?
            //IntruderPercepts intruderPercepts = new IntruderPercepts(null,null,null,null,null,null,false);
            //Finding Action
            IntruderAction a = i.getAction(null);
            //Execute Action
            executeIntruderAction(a,i);
        }
        for (Guard g :
                guards) {
            //Update percepts
            //GuardPercepts guardPercepts = new GuardPercepts(null,null,null,null,null,false);

            updateVision(infoHashtableG.get(g));

            //Finding Action
            GuardAction a = g.getAction(null);


            //Execute Action
            executeGuardAction(a,g);

        }
    }

    private static void perceptSteps(){
        //like GameStep(), but updates the percepts of each agent
        //We do not care if intruders or guards are updated first, because they all proceeded to make their move
        for(Guard g: guards){
            updateVision(infoHashtableG.get(g));
        }
    }

    private static void updateVision(AgentInfo agentInfo){

        //Will send a list of points in the visual field of the agent + their type

        Point actualPos = agentInfo.getActualPos();
        Vector2d direction = agentInfo.getDirection();
        FieldOfView vision = new FieldOfView(new Distance(6), Angle.fromRadians(Math.PI/2));

        //Scales the directions vectors to have x or y of length 1
        if(Math.abs(direction.getX())>Math.abs(direction.getY())){
            if(direction.getX()>0) {
                direction = new Vector2d(direction.getX() / direction.getX(), direction.getY() / direction.getX());
            } else{
                direction = new Vector2d(-direction.getX() / direction.getX(), direction.getY() / direction.getX());
            }
        } else {
            if(direction.getY()>0) {
                direction = new Vector2d(direction.getX() / direction.getY(), direction.getY() / direction.getY());
            } else{
                direction = new Vector2d(direction.getX() / direction.getY(), -direction.getY() / direction.getY());
            }
        }

        System.out.println("POSITION:" + actualPos.getX() + " " + actualPos.getY());
        System.out.println("DIRECTION: " + direction.getX() + ", " + direction.getY());

       Vector2d[] raycasts = vision.translateRaycastwithPos(actualPos, direction, vision.createRays(45,direction,45, 6));

        AllowMove cop = new AllowMove();
        Point NEXT;

        int[] cx1 = new int[]{0,0,2,2};
        int[] cx2 = new int[]{2,0,2,0};
        int[] cy1 = new int[]{1,1,3,3};
        int[] cy2 = new int[]{1,3,1,3};

        ArrayList<Point> visualPerception = new ArrayList<>();

        for(int i=0; i<raycasts.length;i++){    //iterates through rays

            NEXT = new Point(raycasts[i].getX(), raycasts[i].getY());   //Raycasting Vector

            for(Area w : Scenario.getWalls()){
                if(NEXT.getX()> (w.getCorner(0)-degreeOfError) && NEXT.getX()<(w.getCorner(1)+degreeOfError) && NEXT.getY()>(w.getCorner(2)-degreeOfError) && NEXT.getY()<(w.getCorner(3)+degreeOfError)) {
                    //put raycast vector as hitting something
                    visualPerception.add( cop.pointIntersection(actualPos, NEXT, new Point(w.getCorner(0), w.getCorner(2)), new Point(w.getCorner(1), w.getCorner(3)) ));
                  }
            }

            for(int j=0; j<scenario.getWalls().size();j++){ //through walls

                for(int k=0; k<4; k++){ //through sides of walls --> returns false if found an obstacle. Will add method for exact point when this will be working

                    //Needs to return false, meaning the ray encountered an obstacle
                    if(!cop.isLegal(agentInfo.getActualPos(),NEXT,
                            new Point(scenario.getWalls().get(j).getCorner(cx1[k]), scenario.getWalls().get(j).getCorner(cy1[k])), //top left
                            new Point(scenario.getWalls().get(j).getCorner(cx2[k]), scenario.getWalls().get(j).getCorner(cy2[k]))  //top right
                            )){

                        visualPerception.add( cop.pointIntersection(actualPos,
                                NEXT,
                                new Point(scenario.getWalls().get(j).getCorner(cx1[k]), scenario.getWalls().get(j).getCorner(cy1[k])),
                                new Point(scenario.getWalls().get(j).getCorner(cx2[k]), scenario.getWalls().get(j).getCorner(cy2[k]))
                        ));
                    }
                }
            }
        }

        for(int i=0; i<visualPerception.size();i++){
            for(int j=0; j<visualPerception.size();j++){
                if(visualPerception.get(i).getX() == visualPerception.get(j).getX()
                && visualPerception.get(i).getY() == visualPerception.get(j).getY()
                        && i != j
                ){
                    visualPerception.remove(j);
                }
            }
        }

        Set<ObjectPercept> objectPercepts = new HashSet<ObjectPercept>();

        for(int i=0; i<visualPerception.size();i++){ objectPercepts.add(new ObjectPercept(ObjectPerceptType.Wall, visualPerception.get(i))); }

        for(Guard g: guards){
            if(!infoHashtableG.get(g).equals(agentInfo)){
                if(vision.isInView(infoHashtableG.get(g).getActualPos())){
                    objectPercepts.add(new ObjectPercept(ObjectPerceptType.Guard, infoHashtableG.get(g).getActualPos()));
                }
            }
        }
        for(Intruder in: intruders){
            if(vision.isInView(infoHashtableI.get(in).getActualPos())){
                objectPercepts.add(new ObjectPercept(ObjectPerceptType.Intruder, infoHashtableG.get(in).getActualPos()));
            }
        }

        ObjectPercepts totalVision = new ObjectPercepts(objectPercepts);


    }


    private static void run(){
        running = true;
        while(running){
            gameStep();
            try{
                Thread.sleep(frameTimeMillis);
            }catch(InterruptedException irEx){
                irEx.printStackTrace();
                System.exit(-1);
            }
        }
    }


    private static void executeGuardAction(GuardAction guardAction, Guard guard){
        GuardInfo GI = infoHashtableG.get(guard);
        if(guardAction instanceof Move){
            executeMoveAction((Move)guardAction,GI);
        }
        else if(guardAction instanceof Rotate){
            executeRotateAction((Rotate)guardAction,GI);
        }
        else if(guardAction instanceof NoAction){
            return;
        }
        else if(guardAction instanceof Yell){
            executeYell((Yell)guardAction,GI);
        }
    }

    private static void executeIntruderAction(IntruderAction intruderAction, Intruder intruder){
        IntruderInfo II = infoHashtableI.get(intruder);
        if(intruderAction instanceof Move){
            executeMoveAction((Move)intruderAction,II);
        }
        else if(intruderAction instanceof Rotate){
            executeRotateAction((Rotate)intruderAction,II);
        }
        else if(intruderAction instanceof NoAction){
            return;
        }
        else if(intruderAction instanceof Sprint){
            Sprint s = (Sprint) intruderAction;
            executeMoveAction(new Move(s.getDistance()),II);
        }
    }

    private static void executeMoveAction(Move m, AgentInfo agentInfo){

        Distance d = m.getDistance();
        Vector2d direction = agentInfo.getDirection();
        System.out.println("Name : " + agentInfo.getName());
        System.out.println("Position: " + agentInfo.getActualPos().getX() + " " + agentInfo.getActualPos().getY());
        System.out.println("Draw Position: " + agentInfo.getC().getCenterX() + " " + agentInfo.getC().getCenterY());
        System.out.println("----------------------------");
        Point destination = new Point(direction.getX()*d.getValue(),direction.getY()*d.getValue());
        Point NEXT = new Point(agentInfo.getActualPos().getX() + direction.getX()*d.getValue(),agentInfo.getActualPos().getY() + direction.getY()*d.getValue());

        AllowMove cop = new AllowMove();
        boolean actionIsLegal = true;

       /* AgentInfo NEXT = agentInfo;
        NEXT.setActualPos(agentInfo.getActualPos().getX(), agentInfo.getActualPos().getY());
        NEXT.setTranslateX(direction.getX()*d.getValue());
        NEXT.setTranslateY(direction.getY()*d.getValue());*/

        if(NEXT.getX()<1.5 || NEXT.getY()<1.5 || NEXT.getX()>scenario.getMapWidth()-1.5 || NEXT.getY()>scenario.getMapHeight()-1.5){
            System.out.println("Move Intention is not legal, wants to get through a wall to leave the map");
            return;
        }


        for(int i=0; i<scenario.getWalls().size();i++){
            if(!(cop.isLegal(agentInfo.getActualPos(),NEXT,
                    new Point(scenario.getWalls().get(i).getCorner(0), scenario.getWalls().get(i).getCorner(1)), //top left
                    new Point(scenario.getWalls().get(i).getCorner(2), scenario.getWalls().get(i).getCorner(1))  //top right
            ) &&
                    cop.isLegal(agentInfo.getActualPos(),NEXT,
                            new Point(scenario.getWalls().get(i).getCorner(0), scenario.getWalls().get(i).getCorner(1)), //top left
                            new Point(scenario.getWalls().get(i).getCorner(0), scenario.getWalls().get(i).getCorner(3))  //bottom left
                    ) &&
                    cop.isLegal(agentInfo.getActualPos(),NEXT,
                            new Point(scenario.getWalls().get(i).getCorner(2), scenario.getWalls().get(i).getCorner(3)), //bottom right
                            new Point(scenario.getWalls().get(i).getCorner(2), scenario.getWalls().get(i).getCorner(1))  //top right
                    )&&
                    cop.isLegal(agentInfo.getActualPos(),NEXT,
                            new Point(scenario.getWalls().get(i).getCorner(2), scenario.getWalls().get(i).getCorner(3)), //bottom right
                            new Point(scenario.getWalls().get(i).getCorner(0), scenario.getWalls().get(i).getCorner(3))  //top right
                    )
            )){
                //REJECT ACTION
                System.out.println("Action Rejected");
                actionIsLegal = false;
                System.out.println(agentInfo.getActualPos() + " " + NEXT);
                for(int j=0; j<4; j++){System.out.println(scenario.getWalls().get(i).getCorner(j));}
                break;
            }
        }
        System.out.println(agentInfo.getActualPos() + " " + NEXT);

        if(actionIsLegal) {
            agentInfo.setTranslateX(destination.getX());
            agentInfo.setTranslateY(destination.getY());
        }




    }

    /**
     * @param tp A teleportal area , defined in the area.txt file
     * @param agentInfo An agent (either intruder or guard)
     * @return true if the agent steps on the teleportal area
     */
    private static boolean isTeleported(TelePortal tp, AgentInfo agentInfo) {
        // Checks if the agent is in the teleportal  area a
        if(agentInfo.getActualPos().getX()> tp.getCorner(0) && agentInfo.getActualPos().getX()<tp.getCorner(1) && agentInfo.getActualPos().getY()>tp.getCorner(2) && agentInfo.getActualPos().getY()<tp.getCorner(3)){
            System.out.println("Agent teleported");
            agentInfo.getC().setFill(Color.GREEN);
            return true;
        }
        return false;
    }

    private static void executeRotateAction(Rotate r, AgentInfo agentInfo){
        //TODO implement this
        agentInfo.setDirection(agentInfo.getDirection().rotate(r.getAngle().getDegrees()));
    }
    private static void executeYell(Yell yell, AgentInfo agentInfo){
        //TODO implement this
    }
}
