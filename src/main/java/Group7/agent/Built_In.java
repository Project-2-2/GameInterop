package Group7.agent;

import Interop.Action.DropPheromone;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;


public class Built_In {


    public boolean hasObjectInView(ArrayList<ObjectPercept> ls, ObjectPerceptType objectPerceptType){
        boolean val = false;

        for (int i = 0;i<ls.size();i++){

            if (ls.get(i).getType().equals(objectPerceptType)){
                val = true;
            }

        }

        return val;

    }


    public void printFieldOfView(ArrayList<ObjectPercept> ls){
        for (int i = 0;i<ls.size();i++){

            System.out.println(ls.get(i).getType());
            break;
        }

    }

    public boolean hasDetectYell(ArrayList<SoundPercept> soundPerceptArrayList, SoundPerceptType soundPerceptType){
        boolean val = false;
        for (int i = 0;i<soundPerceptArrayList.size();i++){

            if (soundPerceptArrayList.get(i).getType().equals(soundPerceptType)){
                val = true;
            }

        }

        return val;

    }

    public Direction getYellDirection(ArrayList<SoundPercept> soundPerceptArrayList){
        //first find Yell
        boolean hasFind = false;

        int indexOfYell = -1;

        for (int i = 0;i<soundPerceptArrayList.size();i++){

            if (!hasFind && soundPerceptArrayList.get(i).getType().equals(SoundPerceptType.Yell)){
                indexOfYell = i;
                hasFind = true;
            }

        }

        return soundPerceptArrayList.get(indexOfYell).getDirection();
    }

    /**
     * get direction of a point with specific type
     * Useful for intruder, teleport, sentry tower. Not good for wall
     * @param objectPerceptArrayList the list of objects in field of view
     * @param objectPerceptType object type
     * @return clock direction of the object.
     */
    public Direction getObjectDirection(ArrayList<ObjectPercept> objectPerceptArrayList, ObjectPerceptType objectPerceptType){
        //first find Yell
        boolean hasFind = false;

        int index = -1;

        for (int i = 0;i<objectPerceptArrayList.size();i++){

            if (!hasFind && objectPerceptArrayList.get(i).getType().equals(objectPerceptType)){
                index = i;
                hasFind = true;
            }

        }

        return objectPerceptArrayList.get(index).getPoint().getClockDirection();
    }

    /**
     * check whether specific type is in field of view.
     * @param objectPerceptArrayList
     * @param objectPerceptType
     * @return if there exist input object type in field of view.
     */
    public boolean hasDetectObject(ArrayList<ObjectPercept> objectPerceptArrayList, ObjectPerceptType objectPerceptType){
        boolean val = false;
        for (int i = 0;i<objectPerceptArrayList.size();i++){

            if (objectPerceptArrayList.get(i).getType().equals(objectPerceptType)){
                val = true;
            }

        }

        return val;

    }

    public boolean hasDetectSmell(ArrayList<SmellPercept> objectPerceptArrayList, SmellPerceptType smellPerceptType){
        boolean val = false;
        for (int i = 0;i<objectPerceptArrayList.size();i++){

            if (objectPerceptArrayList.get(i).getType().equals(smellPerceptType)){
                val = true;
            }

        }

        return val;

    }

    public Distance distanceToObject(ArrayList<ObjectPercept> objectPerceptArrayList, ObjectPerceptType objectPerceptType){

        Distance val = new Distance(0);

        boolean hasFind = false;

        int index = -1;

        for (int i = 0;i<objectPerceptArrayList.size();i++){

            if (!hasFind && objectPerceptArrayList.get(i).getType().equals(objectPerceptType)){
                index = i;
                hasFind = true;
            }

        }

        return objectPerceptArrayList.get(index).getPoint().getDistanceFromOrigin();

    }

    public ArrayList<Point> getObjectList(ArrayList<ObjectPercept> objectPerceptArrayList, ObjectPerceptType objectPerceptType){
        ArrayList<Point> returnList = new ArrayList<>();

        for (int i = 0;i<objectPerceptArrayList.size();i++){
            if (objectPerceptArrayList.get(i).getType().equals(objectPerceptType)){
                returnList.add(objectPerceptArrayList.get(i).getPoint());
            }
        }

        return returnList;
    }

    public Point[] findNearAndFarPoint(ArrayList<ObjectPercept> objectPerceptArrayList) {

        ArrayList<Point> targetPoint = getObjectList(objectPerceptArrayList, ObjectPerceptType.TargetArea);

        Point nearPoint = targetPoint.get(0);
        Point farPoint = targetPoint.get(0);
        Distance curr = new Distance(0);

        for (int i = 1;i<targetPoint.size();i++){
            if (targetPoint.get(i).getDistanceFromOrigin().getValue()>farPoint.getDistanceFromOrigin().getValue()){
                farPoint = targetPoint.get(i);
            }
        }

        for (int i = 1;i<targetPoint.size();i++){
            if (targetPoint.get(i).getDistanceFromOrigin().getValue()<nearPoint.getDistanceFromOrigin().getValue()){
                nearPoint = targetPoint.get(i);
            }
        }

        Point[] val = new Point[2];
        val[0] = nearPoint;
        val[1] = farPoint;

        return val;
    }

    public double findSlope(Point shortP, Point farP) {
        double slope = 0;


        double xF = farP.getX();
        double yF = farP.getY();

        double xS = shortP.getX();
        double yS = shortP.getY();

        slope = (yF - yS) / (xF - xS);

        return slope;
    }

    public Point getIntruder(ArrayList<ObjectPercept> objectPerceptArrayList){

        Point point = new Point(-1000,-1000);
        ArrayList<Point> pointt = new ArrayList<>();


        for (int i = 0;i<objectPerceptArrayList.size();i++){
            if (objectPerceptArrayList.get(i).getType().equals(ObjectPerceptType.Intruder)){

                point = objectPerceptArrayList.get(i).getPoint();
                pointt.add(objectPerceptArrayList.get(i).getPoint());

            }
        }

        if (pointt.size() == 1){
            return point;
        }

        //when field of view get multiple point of intruder, in order to get better position, use the mean value of x,y

        double size = pointt.size();
        double sumX = 0;
        double sumY = 0;

        for (int i = 0;i<size;i++){

            sumX = pointt.get(i).getX() + sumX;
            sumY = pointt.get(i).getY() + sumY;
        }

        double x = sumX/size;

        double y = sumY/size;

        return new Point(x,y);

    }


}

class TypeOfAction {

    private double val;
    private int actionType;//rotate, move
    private int type;


    public TypeOfAction( double val, int type){
        this.type = type;

        this.val = val;

    }


    public int getType(){
        return type;
    }

    public void setType(int val){
        type = val;
    }

    public double getVal(){
        return val;
    }

    public int getActionType(){
        return actionType;
    }

}

class encapAction {

    public ArrayList<GuardAction> avoid;
    public ArrayList<GuardAction> targetPatrol;
    public ArrayList<GuardAction> capture;

    public boolean debug = false;

    public encapAction(){
        initialAvoid();
        initialPatrol(18);


    }

    public encapAction(Point x){

        initialCapure(x);
    }

    /**
     *
     * @param x
     */
    public void initialCapure(Point x){
        capture = new ArrayList<>();

        double degree = x.getClockDirection().getDegrees();

        if (degree>180) degree = degree - 360;

        double absDegree = Math.abs(degree);

        int rotateTimes = (int)Math.ceil((absDegree)/(45.0));

        double thres = 6;

        if (degree<0){
            if (rotateTimes <=1){
                double addition = 45 - Math.abs(degree);
                if (addition>=thres){
                    capture.add(new Rotate(Angle.fromDegrees(-degree-thres)));
                }else {
                    capture.add(new Rotate(Angle.fromDegrees(-degree-addition)));
                }

            }else {
                for (int i = 1;i<rotateTimes;i++){
                    capture.add(new Rotate(Angle.fromDegrees(45)));
                }
                double addition = absDegree - (rotateTimes-1)*45;
                capture.add(new Rotate(Angle.fromDegrees(addition)));
            }
        }else {
            if (rotateTimes <=1){
                double addition = 45 - Math.abs(degree);
                if (addition>=thres){
                    capture.add(new Rotate(Angle.fromDegrees(degree+thres)));
                }else {
                    capture.add(new Rotate(Angle.fromDegrees(degree)));
                }

            }else {
                for (int i = 1;i<rotateTimes;i++){
                    capture.add(new Rotate(Angle.fromDegrees(-45)));
                }
                double addition = -(absDegree - (rotateTimes-1)*45);
                capture.add(new Rotate(Angle.fromDegrees(addition)));
            }
        }

        int moveTimes = (int)Math.ceil((x.getDistanceFromOrigin().getValue())/(1.40));

        double additionMove = x.getDistanceFromOrigin().getValue() - (moveTimes-1)*1.4;
        if (additionMove>0.3) additionMove = additionMove - 0.3;

        if (moveTimes>1 && moveTimes!=0){
            for (int i = 0;i<moveTimes-1;i++){
                capture.add(new Move(new Distance(1.4)));
            }

            capture.add(new Move(new Distance(additionMove)));

        }else if (moveTimes ==1){
            capture.add(new Move(new Distance(additionMove)));
        }else{
            System.out.println("no-------------------");
        }

    }


    public void initialAvoid(){
        avoid = new ArrayList<>();

        avoid.add(new Rotate(Angle.fromDegrees(45)));
        avoid.add(new Rotate(Angle.fromDegrees(45)));
        avoid.add(new Move(new Distance(1.4)));
        avoid.add(new Move(new Distance(1.4)));
        avoid.add(new Move(new Distance(1.4)));
        avoid.add(new Move(new Distance(1.4)));
    }

    public void initialPatrol(double length){
        targetPatrol = new ArrayList<>();
        int step = (int) (length/1.4)+1;
        if (debug) System.out.println(step);
        for (int i = 0;i<step;i++){
            targetPatrol.add(new Move(new Distance(1.4)));
        }

        for (int i = 0;i<2;i++){
            targetPatrol.add(new Rotate(Angle.fromDegrees(-45)));
        }

        targetPatrol.add(new DropPheromone(SmellPerceptType.Pheromone2));
    }

    public static void main(String[] args) {


        encapAction e = new encapAction();

        for (int i = 0;i<e.targetPatrol.size();i++){
            System.out.println(e.targetPatrol.get(i).getClass());
        }
    }



}