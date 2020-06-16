package Group7.agent;

import Group9.Game;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class Capturer implements Guard {

    public static boolean debug = true;

    public boolean detectIntruderFirst = false;

    public boolean detectIntruderSecond = false;

    private int trackSequence = 0;

    private int trackBuffer = 0;

    ArrayList<ActionHistory> actionHistory;

    public Capturer(){
        actionHistory = new ArrayList<>();
    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        Txt IO = new Txt();

        Built_In  bi = new Built_In();

        Set<ObjectPercept> objectPercepts = percepts.getVision().getObjects().getAll();
        ArrayList<ObjectPercept> objectPerceptArrayList = new ArrayList<ObjectPercept>(objectPercepts);


        if (!detectIntruderFirst&&!detectIntruderSecond){
           if (bi.hasObjectInView(objectPerceptArrayList, ObjectPerceptType.Intruder)){
               Point point = bi.getIntruder(objectPerceptArrayList);

               if (debug){
                   printPointXY(point);
               }

               IO.writePoint1(IO.point1,point);


               if (debug) System.out.println("---------------------------First found Intruder, stay to find--------------------------------");
               detectIntruderFirst = true;

               return new NoAction();
           }
        }

        if (detectIntruderFirst&&!detectIntruderSecond){

            if (bi.hasObjectInView(objectPerceptArrayList, ObjectPerceptType.Intruder)){
                if (debug) System.out.println("---------------------------Second found Intruder, stay to find--------------------------------");
                detectIntruderSecond = true;

                Point point1 = IO.readPoint(IO.point1);
                Point point2 = bi.getIntruder(objectPerceptArrayList);

                if (possibleCapturePoints(predict(point1,point2,10)).size()!=0){
                    Point possibleCapturePoint = possibleCapturePoints(predict(point1,point2,10)).get(0);
                    System.out.println("The capture point is: --------------------");
                    printPointXY(possibleCapturePoint);
                    IO.writePoint1(IO.targetPoint,possibleCapturePoint);
                    IO.writeTime(IO.times,turnsRequired(possibleCapturePoint));

                    encapAction e = new encapAction(possibleCapturePoint);
                    if (debug) System.out.println(e.capture.get(trackSequence).getClass());


                    return e.capture.get(0);
                }

            }else {
                if (debug) System.out.println("fail to track, add buffer");
                detectIntruderSecond = false;
                addTrackBuffer(1);
            }

        }

        if (trackBuffer == 1){
            setTrackBuffer(0);
            detectIntruderFirst = false;
        }

        int times = IO.readTime(IO.times);

        if (detectIntruderFirst&&detectIntruderSecond&& trackSequence<times-1 && times!=0){

            if (debug) System.out.println("---------------------------Start Tracking--------------------------------");

            addTrackSequence(1);

            Point x = IO.readPoint(IO.targetPoint);

            encapAction e = new encapAction(x);

            if (debug) System.out.println("Track sequence is: "+trackSequence);

            if (debug) System.out.println(e.capture.get(trackSequence).getClass());

            return e.capture.get(trackSequence);

        }

        if (detectIntruderFirst&&detectIntruderSecond&&trackSequence==times-1){

            detectIntruderSecond=false;
            detectIntruderFirst=false;
            setTrackSequence(0);
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





    public  ArrayList<Point> predict(Point a, Point b, int numP){
        ArrayList<Point> val = new ArrayList<>();

        Built_In bi = new Built_In();
        double slope = bi.findSlope(a,b);

        double ax = a.getX();
        double ay = a.getY();

        double bx = b.getX();
        double by = b.getY();

        double xD = bx-ax;
        double yD = by-ay;

        double currentX = bx;
        double currentY = by;

        for (int i = 0;i<numP;i++){

            val.add(new Point(currentX+(i+1)*xD,currentY+(i+1)*yD));
            //currentX = currentX+xD;
            //currentY = currentY+xD;
        }

        return val;
    }

    public Point findCapturePoint(ArrayList<Point> pp){
        double[] distanceArray = new double[pp.size()];

        for (int i = 0;i<distanceArray.length;i++){
            distanceArray[i] = pp.get(i).getDistanceFromOrigin().getValue();
        }

        int index = 0;

        for (int i = 0;i<distanceArray.length;i++){

            if (distanceArray[i]<distanceArray[index]){
                index = i;
            }

        }

        return pp.get(index);


    }

    public  void printPointXY(Point x){
        System.out.println("X is: "+ x.getX());
        System.out.println("Y is: "+ x.getY());
        System.out.println("------------------------------");


    }


    public  void printArraylist(ArrayList<Point> pp){
        for (int i = 0;i<pp.size();i++){

            System.out.println("X id: "+pp.get(i).getX());
            System.out.println("Y is: "+pp.get(i).getY());
            System.out.println("----------------------------");

        }
    }


    public int turnsRequired(Point x){

        double degree = x.getClockDirection().getDegrees();

        if (degree>200) degree = degree - 360;

        double absDegree = Math.abs(degree);

        int rotateTimes = (int)Math.ceil((absDegree)/(45.0));

       // if (debug) System.out.println("Angle is:  ----"+absDegree);

       // if(debug) System.out.println("rotate time is: "+rotateTimes);

        int moveTimes = (int)Math.ceil((x.getDistanceFromOrigin().getValue())/(1.40));

        //if (debug) System.out.println("Distance is:  ----"+x.getDistanceFromOrigin().getValue());



        //if(debug) System.out.println("move time is: "+moveTimes);


        return rotateTimes+moveTimes;
    }

    public  ArrayList<Point> possibleCapturePoints(ArrayList<Point> points){
        int[] turnsRequire = new int[points.size()];

        for (int i = 0;i<turnsRequire.length;i++){

            turnsRequire[i] = turnsRequired(points.get(i));

        }


        ArrayList<Point> val = new ArrayList<>();

        for (int i = 0;i<turnsRequire.length;i++){

            if (turnsRequire[i]<=(i+1)){
                val.add(points.get(i));
            }

        }


        return val;


    }

    public void setTrackSequence(int val){
        trackSequence = val;
    }

    public void addTrackSequence(int val){
        trackSequence = trackSequence + val;
    }

    public int getTrackSequence(){
        return  trackSequence;
    }

    public void setTrackBuffer(int val){
        trackBuffer = val;
    }

    public void addTrackBuffer(int val)
    {
        trackBuffer = trackBuffer + val;

    }

    public int getTrackBuffer(){
        return trackBuffer;
    }

    public void printActionList(ArrayList<GuardAction> list){
        for (int i = 0;i< list.size();i++){
            System.out.println("The action is: "+list.get(i).getClass());
        }

    }

    public static void main(String[] args) {
        Point p1 = new Point(0,-1.4);
        Point p2 = new Point( 0,1.3);



    }

}


class Txt{

    public  String track= "src/main/resources/encap.txt";
    public  String point1= "src/main/resources/point1.txt";
    public  String targetPoint= "src/main/resources/targetPoint.txt";
    public  String times= "src/main/resources/times.txt";

    public  void writePoint1(String fileName, Point x){

        try{
            File writename = new File(fileName);

            BufferedWriter out = new BufferedWriter(new FileWriter(writename));

            out.write(""+x.getX()+"\r\n");
            out.write(""+x.getY()+"\r\n");

            out.flush();

            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }




    }

    public void writeTime(String fileName,int i){

        try{
            File writename = new File(fileName);

            BufferedWriter out = new BufferedWriter(new FileWriter(writename));

            out.write(""+i+"\r\n");

            out.flush();

            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void writeActionSequence(String fileName,double[] array){
        try{
            File writename = new File(fileName);

            BufferedWriter out = new BufferedWriter(new FileWriter(writename));


            for (int i = 0;i<array.length;i++){
                out.write(""+array[i]+"\r\n");
            }


            out.flush();

            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Integer readTime(String fileName){

        File file = new File(fileName);
        BufferedReader reader = null;

        int ii = -1;

        try {

            reader = new BufferedReader(new FileReader(file));

            String tempString = null;

            int line = 0;

            while ((tempString = reader.readLine()) != null) {

                int i = Integer.parseInt(tempString);

                ii = i;

                //System.out.println("line " + line + ": " + tempString);

                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ii;


    }




    public Point readPoint(String fileName){
        File file = new File(fileName);
        BufferedReader reader = null;

        double[] array = new double[2];

        try {

            reader = new BufferedReader(new FileReader(file));

            String tempString = null;

            int line = 0;

            while ((tempString = reader.readLine()) != null) {

                Double i = Double.parseDouble(tempString);

                array[line] = i;

                //System.out.println("line " + line + ": " + tempString);

                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Point point = new Point(array[0],array[1]);

        return point;


    }



    public static void main(String[] args) {
        double[] a = new double[3];
        a[0] = 0;
        a[1] = 1;
        a[2] = 2;
        Point x = new Point(1,2);
       // writePoint1(point1,x);


        Capturer cp = new Capturer();

        ArrayList<Integer> aa = new ArrayList<>();
        aa.add(1);
        aa.add(2);
        //System.out.println(aa.get(1));


        //System.out.println(readTime(times));

    }


}

class tttt{

    public static void main(String[] args) {
        Capturer cp = new Capturer();

        Built_In bi = new Built_In();



        Point point = new Point(2.124,
        2.5235);

        encapAction e = new encapAction(point);

       // cp.printActionList(e.capture);

        RL rt = new RL();


        Point p1 = new Point(-3,2);
        Point p2 = new Point(-3.5,2);
        Point p3 = new Point(-2.7,5);
        Point p4 = new Point(-2.5,2);
        Point p5 = new Point(-5.6,3);
        Point p6 = new Point(-0.5,2);
        Point p7 = new Point(0.6,3);
        Point p8 = new Point(1.3,4);
        Point p9 = new Point(2.3,6);

        ArrayList<Point> p = new ArrayList<>();

        p.add(p1);
        p.add(p2);
        p.add(p3);
        p.add(p4);
        p.add(p5);
        p.add(p6);
        p.add(p7);
        p.add(p8);
        p.add(p9);

        ArrayList<Point> aaa = rt.sortPoints(p);

        cp.printArraylist(aaa);
        System.out.println(aaa.size());



    }
}
