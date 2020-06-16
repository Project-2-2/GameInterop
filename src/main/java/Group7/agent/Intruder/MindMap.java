package Group7.agent.Intruder;

import Interop.Action.Action;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Direction;
import Interop.Geometry.Point;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


// Store the visual percepts information of the agent gained all over the different turns


public class MindMap {

    private AgentState state; //position + direction of the agent
    private Point targetPos =null;
    private Direction directionFirstTurn;
    private Point posFirstTurn;

    //any 0 in this array represents a empty space on the or an are which has not already been explored
    //any 1 represents a wall
    private int[][] mapData;
    private static final int Unvisited = 0;
    private static final int Visited = 1;
    private static final int Wall = 2;
    private static final int Door = 3;
    private static final int Teleport = 4;
    private static final int TargetArea = 5;
    private static final int Window = 6;
    private static final int Shaded = 7;
    private static final int Guard = 8;
    private static final int Intruder =9;
    private static final int Sentry =10;
    private static final int Empty = 11;
    //default size is arbitrary 20
    public MindMap(){
        int height =20;
        int width = 20;
        Direction d = Direction.fromRadians(0);
        mapData = new int[(int)height][(int)width];
        this.state = new AgentState(new Point(height/2,width/2),d);
    }

// instanciate the mindmap. The agent is set at the center of the matrix.


    public MindMap(double height, double width, Direction angle){
        mapData = new int[(int)height][(int)width];
        this.state = new AgentState(new Point(height/2,width/2),angle);
    }

    private void checkExpention(int height,int width){
//        System.out.println(mapData.length);
//        System.out.println(mapData[0].length);
        if(height<0){
            ExtendMap(height,0);
        }
        if(width<0){
            ExtendMap(0,width);
        }
        if(width>=mapData[0].length){
            ExtendMap(0,width-mapData[0].length+1);
        }
        if(height>=mapData.length){
            ExtendMap(height-mapData.length+1,0);
        }
    }
    private void ExtendMap(int height, int width){
      if(height>0){
          expandBottom(height);
      }
      if(height<0){
          expandTop(-height);
      }
      if(width>0){
          expandRight(width);
      }
      if(width<0){
          expandLeft(-width);
      }
    }

    //reset the map data
    private void resetMap(){
        for(int i=0 ; i<mapData.length-1 ; i++){
            for(int j=0 ; i<mapData.length-1; j++){
                mapData[i][j] =  Unvisited;
            }
        }
    }

    // a matrix cell can store only one integer. Thus, when updating the matrix, first set the visited area, then were specific items such as walls
    // are perceived, overwrite the visited area and change the corresponding matrix cells to store the specific item.

    public void setMapData(int[][] newMapData){
        mapData = newMapData;
    }

    public int[][] getMapData(){
        return mapData;
    }

    public Point findClosestUnvisitedPoint(Direction d){
        Vector direction = new Vector(d);
        System.out.println("direction = " + direction.getString());
        direction.addLength(state.getAngle().getRadians());
        System.out.println("direction = " + direction.getString());
//        direction.setLength(0.1);
        direction.setLength(10);
        Vector pos = state.vectorPos();
        direction.add(pos);
//        double increment = 0.1;
//        boolean found = false;
//        while(!found){
//            Vector target = pos.add2(direction);
//            if(!isVisited(target)){
//                found = true;
//            }
//            else{
//                direction.addLength(increment);
//            }
//        }
//        pos.add(direction);
        checkExpention((int)direction.x,(int)direction.y);
        if(direction.x<0){
            pos.x =0;
        }
        if(direction.y<0){
            direction.y =0;
        }
        return new Point(direction.x,direction.y);
    }

    public Point findIntersection(Direction d){
        if(state.getX()==posFirstTurn.getX() && state.getY()==posFirstTurn.getY()){ //the agent has not moved
            directionFirstTurn = d;
            return findClosestUnvisitedPoint(d);
        }
        System.out.println("d = " + d.getRadians());
        System.out.println(posFirstTurn);
        System.out.println("directionFirstTurn = " + directionFirstTurn.getRadians());
        System.out.println(state.getPos());
//        building 4 points
        Point a1 = posFirstTurn;
//        System.out.println("a1= "+a1.getX()+" "+a1.getY());
        Vector va1 = new Vector(posFirstTurn);
        Point a2 = state.getPos();
//        System.out.println("a2= "+a2.getX()+" "+a2.getY());
        Vector vb2 = state.vectorPos().add(new Vector(d));
        Vector vb1 = va1.add(new Vector(directionFirstTurn));
        Point b1 = new Point(vb1.x,vb1.y);
 //       System.out.println("b1= "+b1.getX()+" "+b1.getY());
        Point b2 = new Point(vb2.x,vb2.y);
 //       System.out.println("b2= "+b2.getX()+" "+b2.getY());

//        calculating the equations of the lines
        double m1 = (a1.getY()-a2.getY())/(a1.getX()-a2.getX());
//        System.out.println("m1 = " + m1);
        double m2 = (b1.getY()-b2.getY())/(b1.getX()-b2.getX());
//        System.out.println("m2 = " + m2);
//        case lines are parallel
        if(Math.abs(m1-m2)<=0.05){
            System.out.println("parallel");
            return findClosestUnvisitedPoint(d);
        }
        double c1 = a1.getY()-m1*a1.getX();
//        System.out.println("c1 = " + c1);
        double c2 = b1.getY()-m2*b1.getX();
//        System.out.println("c2 = " + c2);

//        solving the system
        double interX =(c2-c1)/(m1-m2);
//        System.out.println("interX = " + interX);
        double interY = m1*interX+c1;
//        System.out.println("interY = " + interY);
        checkExpention((int)interX,(int)interY);
        return new Point(interX,interY);
    }

    public void computeTargetPoint(Direction d){
//        if(null==directionFirstTurn){
//            directionFirstTurn = d;
//            posFirstTurn = state.getPos();
//            targetPos = findClosestUnvisitedPoint(d);
//        System.out.println("targetPos = " + targetPos.toString());
//        }else{
//           targetPos = findIntersection(d);
//        }

        if(targetPos==null) {
            targetPos = new Point(100,100);
            checkExpention((int)targetPos.getX(),(int)targetPos.getY());
            if(targetPos.getY()<0){
                targetPos= new Point(targetPos.getX(),0);
            }
            if(targetPos.getX()<0){
                targetPos= new Point(0,targetPos.getY());
            }
        }
//        targetPos = findClosestUnvisitedPoint(d);
//
    }

    public boolean isVisited(Vector v){
        return getData(v)!=Unvisited;
    }

    public boolean isVisited(Point p){
        return getData(p)!=Unvisited;
    }

    public boolean isVisited(double x, double y){
        return getData(x,y)!=Unvisited;
    }

    public boolean isVisited(int a, int b){
        return getData(a,b)!=Unvisited;
    }

    public void setState(AgentState state) {
        this.state = state;
    }

    public int getData(Point p){
        return getData(p.getY(),p.getY());
    }

    public int getData(Vector v){
        return getData(v.x,v.y);
    }
    public int getData(double x, double y){
        return getData((int)Math.round(x),(int)Math.round(y));
    }

    public int getData(int x, int y){
        checkExpention(x,y);
        if(x<0){
            x=0;
        }
        if(y<0){
            y=0;
        }
        return mapData[x][y];
    }

    public ArrayList<String> toStringInfo(ArrayList<Integer> a ){
        ArrayList<String> result = new ArrayList<>();
        for(Integer i : a){
            result.add(toStringInfo(i));
        }
        return result;
    }

    public String toStringInfo(int x){
        switch (x){
            case 0:
                return "Unvisited";
            case 1:
                return "Visited";
            case 2:
                return "Wall";
            case 3:
                return "Door";
            case 4:
                return "Teleport";
        }
        System.out.println(x+" is not corresponding to any data.");
        return ("error " + x);
    }

    public ArrayList<Integer> getAreaData(Point a1, Point a2, Point a3, Point a4){

        ArrayList<Integer> info = new ArrayList<>();
        int minX=(int)Math.round(a1.getX());
        int maxX=(int)Math.round(a1.getX());
        int minY=(int)Math.round(a1.getY());
        int maxY=(int)Math.round(a1.getY());


        minX = Math.min(minX,(int)Math.round(a2.getX()));
        maxX = Math.max(maxX,(int)Math.round(a2.getX()));
        minY = Math.min(minY,(int)Math.round(a2.getY()));
        maxY = Math.max(maxY,(int)Math.round(a2.getY()));

        minX = Math.min(minX,(int)Math.round(a3.getX()));
        maxX = Math.max(maxX,(int)Math.round(a3.getX()));
        minY = Math.min(minY,(int)Math.round(a3.getY()));
        maxY = Math.max(maxY,(int)Math.round(a3.getY()));

        minX = Math.min(minX,(int)Math.round(a4.getX()));
        maxX = Math.max(maxX,(int)Math.round(a4.getX()));
        minY = Math.min(minY,(int)Math.round(a4.getY()));
        maxY = Math.max(maxY,(int)Math.round(a4.getY()));

        for(int i = minX; i<maxX; i++){
            for(int j = minY; j<maxY; j++){
                if(!info.contains(mapData[i][j])){
                    info.add(mapData[i][j]);
                }
            }
        }
        return info;
    }

/**
     * update the Map after executing an action
     * @param
     * @param
     * @return a new map being updated
 * */
    public void updateGridMap(IntruderPercepts percepts){

        //currently, the explore agent only need to execute move or rotate
        //all the objects in vision
        Set<ObjectPercept> objectPercepts = percepts.getVision().getObjects().getAll();

        List<ObjectPercept> ls = new ArrayList<ObjectPercept>(objectPercepts);
        System.out.println("ls = " + ls.size());
        Iterator<ObjectPercept> iterator = objectPercepts.iterator();

        to:for (int i = 0;i<ls.size();i++){

            //TODO change the bellow codes, they should compute the object coordinates in the matrix. (they are different form the coordinates
            //in the field of view of the agent.
            Vector agentPos = new Vector(state.getRealPos());
//            agentPos.printLnVector();
            Vector direction = new Vector(state.getAngle());
//            direction.printLnVector();
            Vector xAxis = direction.get2DPerpendicularVector();

            Point ls_point = ls.get(i).getPoint();
//            System.out.println(ls_point.toString());

            Vector x = xAxis.setLength2((ls_point.getX()));
            Vector y = direction.setLength2(ls_point.getY());

            Vector objectCoos = agentPos.add(x).add(y);

            int ox = (int) Math.round(objectCoos.x);
            int oy = (int) Math.round(objectCoos.y);

            checkExpention(ox,oy);
            if(ox<0){
                ox=0;
            }
            if(oy<0){
                oy=0;
            }
//            System.out.println("oy = " + oy);
//            System.out.println("ox = " + ox);

            ObjectPerceptType type = ls.get(i).getType();
//            System.out.println("type = " + type.toString());
            switch (type) {
                case Wall:
//                    if(mapData[ox][oy] == Unvisited)
                       mapData[ox][oy] = Wall;
                continue to;

                case Door  :
//                    if(mapData[ox][oy] == Unvisited)
                        mapData[ox][oy] = Door;
                    continue to;

                case Window  :
//                    if(mapData[ox][oy] == Unvisited)
                       mapData[ox][oy] = Window;
                    continue to;

                case Teleport:
//                    if(mapData[ox][oy] == Unvisited)
                        mapData[ox][oy] = Teleport;
                    continue to;

                case SentryTower:
//                    if(mapData[ox][oy] == Unvisited)
                        mapData[ox][oy] = Sentry;
                    continue to;

                case EmptySpace:
//                    if(mapData[ox][oy] == Unvisited)
                        mapData[ox][oy] = Empty;
                    continue to;

                case ShadedArea:
//                    if(mapData[ox][oy] == Unvisited)
                        mapData[ox][oy] = Shaded;
                    continue to;

                case Guard:
//                    if(mapData[ox][oy] == Unvisited)
                        mapData[ox][oy] = Guard;
                    continue to;

                case Intruder:
//                    if(mapData[ox][oy] == Unvisited)
                        mapData[ox][oy] = Intruder;
                    continue to;
                case TargetArea:
//                    if(mapData[ox][oy] == Unvisited)
                        mapData[ox][oy] = TargetArea;
                    continue to;


            }
    }
        //System.out.println();
      // System.out.println("after update");
    //   printMatrix(mapData,targetPos);
  }
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";


    public static void printMatrix(int[][] matrix, Point targetPos){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int type = matrix[i][j];
                if(targetPos!=null && i==targetPos.getX() && j==targetPos.getY()){
                    System.out.print(ANSI_GREEN+ "T "+ANSI_RESET);
                }else if(type == 2){
                    System.out.print(ANSI_RED+ matrix[i][j] + " "+ANSI_RESET);
                }
                else if(type == 8){
                    System.out.print(ANSI_GREEN+ matrix[i][j] + " "+ANSI_RESET);
                }
                else if(type == 11){
                    System.out.print(ANSI_BLUE+ matrix[i][j] + " "+ANSI_RESET);
                }
                else {
                    System.out.print(matrix[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println("-----------");
        System.out.println();
    }

    //method to update the state based on the chosen action
    public void updateState(Action a){
       if( a instanceof Move){
           mapData[state.getX()][state.getY()] = Empty;
           Vector pos = new Vector(state.getPos());
               System.out.println("pos = " + pos.getString());
               Vector agentOrientation = new Vector(state.getAngle());
           System.out.println("agentOrientation = " + agentOrientation.getString());
               agentOrientation.setLength(((Move) a).getDistance().getValue());
               Vector newpos = pos.add(agentOrientation);
           System.out.println("newpos = " + newpos.getString());
               state.setPos(newpos.x,newpos.y);
           mapData[state.getX()][state.getY()] = Intruder;
       }else if ( a instanceof Rotate){
           mapData[state.getX()][state.getY()] = Intruder;
           System.out.println("old angle "+state.getAngle().getDegrees());
           state.setAngle(Direction.fromRadians(state.getAngle().getRadians() + ((Rotate) a).getAngle().getRadians()));
           System.out.println("new angle "+state.getAngle().getDegrees());
       }else{

       }
    }

    public boolean isGridMapEmpty(){
        for (int i = 0;i<mapData.length;i++){
            for (int j = 0;j<mapData[0].length;j++){

                if (mapData[i][j] != 0){
                    return false;
                }
            }
        }
        return true;
    }

    public Point getTargetPos() {
        return targetPos;
    }

    public void setTargetPos(Point targetPos) {
        this.targetPos = targetPos;
    }

    public AgentState getState() {
        return state;
    }

    /**
     * Method to check the walkable areas so it doesn't go on the walls etc
     * @return 0 if can walk on a "case" of the map and 1 if it can not because there is an obstacle there
     */
    public int[][] walkable(){
//        System.out.println("MindMap.walkable");
//        printMatrix(mapData);
        int[][] out = new int[mapData.length][mapData[0].length];

        for (int i = 0; i < mapData.length; i++) {
            for (int j = 0; j < mapData[0].length ; j++) {
                if(mapData[i][j]==Wall){
                    out[i][j] = 1;
                }
            }
        }
//        printMatrix(out);
        return out;
    }

    /**
     * @param size to expand
     * @return new matrix (expanded) with 1 extra Left column
     */
    public void expandLeft(int size) {
        // expand in the y direction if needed to the left
        int diff = 1;
        int[][] tmp = new int[mapData.length][mapData[0].length + size];
        for (int i = size; i < mapData.length; i++) {
            //for (int j = 0; j < matrix[0].length; j++) {
            //    tmp[i][j + 1] = matrix[i][j];
            // }
            System.arraycopy(mapData[i], 0, tmp[i], 0, mapData[0].length);
        }
        targetPos = new Point(targetPos.getX(),targetPos.getY()+1);
        System.out.println(targetPos.toString());
        state.setPos(state.getX(),state.getY()+1);
        System.out.println(state.getPos().toString());
        mapData = tmp;
    }
    /**
     * @param size to expand
     * @return new matrix (expanded) with 1 extra bottom row
     */
    public void expandBottom(int size){
        // expand in the x direction if needed  to the bottom
        int[][] tmp = new int[mapData.length + size][mapData[0].length];
        for(int i = 0; i < mapData.length; i++){
            System.arraycopy(mapData[i], 0, tmp[i], 0, mapData[0].length);
        }
        mapData = tmp;
    }


    /**
     * @param size to expand
     * @return new matrix (expanded) with 1 extra top row
     */
    public void expandTop(int size){
        // expand in the x direction if needed  to the top
        int[][] tmp = new int[mapData.length + size][mapData[0].length];
        for(int i = 0; i < mapData.length; i++){
            System.arraycopy(mapData[i], 0, tmp[i + size], 0, mapData[0].length);
        }
        targetPos = new Point(targetPos.getX()+1,targetPos.getY());
        state.setPos(state.getX()+1,state.getY());
        mapData = tmp;
    }

    /**
     * @param size to expand
     * @return new matrix (expanded) with 1 extra right column
     */
    public void expandRight(int size){
        // expand in the y direction if needed to the right
        int[][] tmp = new int[mapData.length][mapData[0].length + size];
        for(int i = 0; i < mapData.length; i++){
            System.arraycopy(mapData[i], 0, tmp[i], 0, mapData[0].length);
        }
        mapData = tmp;
    }
}

//
//    //set an area as visited
//    public void visitAera(Point a1, Point a2, Point a3, Point a4){
//        updateAreaMemory(a1,a2,a3,a4,Visited);
//    }
//
//    public void visit(Point a){
//        visit(a.getX(),a.getY());
//    }
//
//    public void visit(double x, double y) {
//        visit((int)Math.round(x),(int)Math.round(y));
//    }
//
//    public void visit(int a, int b){
//        mapData[a][b] = Visited;
//    }

//    //this area must have a rectangular shape
//    public void updateAreaMemory(Point a1, Point a2, Point a3, Point a4, int objectType){
//        int minX=(int)Math.round(a1.getX());
//        int maxX=(int)Math.round(a1.getX());
//        int minY=(int)Math.round(a1.getY());
//        int maxY=(int)Math.round(a1.getY());
//
//
//        minX = Math.min(minX,(int)Math.round(a2.getX()));
//        maxX = Math.max(maxX,(int)Math.round(a2.getX()));
//        minY = Math.min(minY,(int)Math.round(a2.getY()));
//        maxY = Math.max(maxY,(int)Math.round(a2.getY()));
//
//        minX = Math.min(minX,(int)Math.round(a3.getX()));
//        maxX = Math.max(maxX,(int)Math.round(a3.getX()));
//        minY = Math.min(minY,(int)Math.round(a3.getY()));
//        maxY = Math.max(maxY,(int)Math.round(a3.getY()));
//
//        minX = Math.min(minX,(int)Math.round(a4.getX()));
//        maxX = Math.max(maxX,(int)Math.round(a4.getX()));
//        minY = Math.min(minY,(int)Math.round(a4.getY()));
//        maxY = Math.max(maxY,(int)Math.round(a4.getY()));
//
//        for(int i = minX; i<maxX; i++){
//            for(int j = minY; j<maxY; j++){
//                mapData[i][j] = objectType;
//            }
//        }
//    }
//
//    public void setLineObject(Point a1, Point a2, int objectType){
//        updateAreaMemory(a1,a2,a1,a2, objectType);
//    }
//
//    public void setCornerWall(Point a1, Point corner, Point a2){
//        setWall(a1,corner);
//        setWall(corner,a2);
//    }
//
//    public void setWall(Point a1, Point a2){
//        setLineObject( a1, a2,Wall);
//    }
//
//    public void setDoor(Point a1, Point a2){
//        setLineObject( a1, a2,Door);
//    }
//
//    public void setTeleport(Point a1, Point a2, Point a3, Point a4){
//        updateAreaMemory(a1,a2,a3,a4,Teleport);
//    }
//
//    //checks if a point is in a  triangular shape. (a view field has a triangular shape)
//    //TODO test the method
//    public boolean isInTriangle(Point a, Point b, Point c, Point m){
//        boolean ok = true;
//        if (0 < new Vector(a,b).vectorialProduct(new Vector(a,m)).scalarProduct(new Vector(a,m).vectorialProduct(new Vector(a,c))))
//            ok = false;
//        if (0 < new Vector(b,a).vectorialProduct(new Vector(b,m)).scalarProduct(new Vector(b,m).vectorialProduct(new Vector(b,c))))
//            ok = false;
//        if (0 < new Vector(a,c).vectorialProduct(new Vector(c,m)).scalarProduct(new Vector(c,m).vectorialProduct(new Vector(b,c))))
//            ok = false;
//        return ok;
//    }
//
//    /*
//     *//**
// // * @param currX perceived x
// // * @param currY perceived y
// // * @return x value based on the coordinate of initial point
// *//*
//    public double changeToStartingPointCoordinateX(double currX, double currY){
//        double val = 0;
//
//        double sumOfRotation = 0;
//
//        for (int i = 0;i<moveHistory.size();i++){
//
//            //if the action is rotation
//            if (moveHistory.get(i).getActionType() == 2){
//                sumOfRotation = sumOfRotation + moveHistory.get(i).getVal();
//            }
//        }
//
//        double X = coordinateBasedOnInitialPoint(currX,currY,sumOfRotation)[0];
//
//        val = X - selfLocation[0];
//
//        return val;
//    }*/
//
//
////The coordinate for object before making rotation.
//public double[] coordinateBasedOnInitialPoint(double x, double y,double sumOfRotateAngle) {
//    double[] xy = new double[2];
//
//    double previousX =  (x*Math.cos(Math.toRadians(sumOfRotateAngle)) + y*Math.sin(Math.toRadians(sumOfRotateAngle)));
//
//    double previousY =  (y*Math.cos(Math.toRadians(sumOfRotateAngle)) - x*Math.sin(Math.toRadians(sumOfRotateAngle)));
//
//    xy[0] = previousX;
//
//    xy[1] = previousY;
//
//    return xy;
//
//}
//
//
//
//
//  /*  //after doing re-rotate calculation, these method will return a value compare to ini valuel
//    public double changeToStartingPointCoordinateY(double currX,double currY){
//        double val = 0;
//
//        double sumOfRotation = 0;
//
//        for (int i = 0;i<moveHistory.size();i++){
//
//            //if the action is rotation
//            if (moveHistory.get(i).getActionType() == 2){
//                sumOfRotation = sumOfRotation + moveHistory.get(i).getVal();
//            }
//        }
//
//        double Y = coordinateBasedOnInitialPoint(currX,currY,sumOfRotation)[1];
//
//        val = Y - selfLocation[0];
//
//        return val;
//    }*/
//
//
//    //given a rotation angle and moving distance, return the xy- coordinates of where agent is based on the previous point.
//    public double[] getXandYAfterRotationMove(double degree, double distance){
//
//        double[] xy = new double[2];
//
//        if (degree >0){
//            //x value
//            xy[0] = distance * Math.sin(Math.toRadians(degree));
//
//            //y value
//            xy[1] = distance * Math.cos(Math.toRadians(degree));
//        }else {
//
//            xy[0] = -distance * Math.sin(Math.toRadians(-degree));
//
//            xy[1] = distance * Math.cos(Math.toRadians(-degree));
//        }
//
//
//        return xy;
//    }

//    /**
//     * Based on the current map situation, change the size of the map in order to achieve a better map.
//     * @param lastSituation
//     * @return a map after changed by a proper size.
//     */
//    public double[][] changeGridMapSize(double[][] lastSituation){
//        double[][] newState = lastSituation.clone();
//
//        return newState;
//    }