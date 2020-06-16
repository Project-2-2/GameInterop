package Group7.agent.Intruder;

import Interop.Geometry.Distance;
import Interop.Geometry.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AsSearch {

  /* public static void main2(String[] args){
       MindMap mindmap = new MindMap();
       int[][] matrix = {{0,0,2,0,0,0},{0,0,0,0,0,0},{0,0,2,0,2,0},{0,0,2,0,2,0},{0,0,2,0,2,0}}; // where 2 = walls, 0 is nothing special
       mindmap.setMapData(matrix);
       Point targetPoint = new Point(5,5);
       System.out.println("value "+matrix[5-1][5-1]);
       mindmap.setTargetPos(targetPoint);
       Point initialPoint = new Point(0,0);
       Angle initialAngle = new Angle(0.0);
       AgentState agentState = new AgentState(initialPoint, Direction.fromRadians(initialAngle.getRadians()));
       System.out.println("Initial map: ");
       printMatrix(matrix);
       mindmap.setState(agentState);
      // computePath(mindmap);
       List<Integer> listPositions =  AsSearch.computePath(mindmap);
       ArrayList<Integer> directions = AsSearch.getListOfActionsDirections(listPositions);
       List<int[]> listConsecutivesActions = AsSearch.getNumberConsecutiveMoves(directions);
       int move = listConsecutivesActions.get(0)[0];
       System.out.println("move = " + move);
       int numberConsecutiveMove = listConsecutivesActions.get(0)[1];
       System.out.println("numberConsecutiveMove = " + numberConsecutiveMove);
   }
   */

//   public static void main(String [] args){
//       int a= 1;
//       test(a);
//       System.out.println(a);
//   }
//
//   public static int test(int a){
//       int out = a;
//       a = 2;
//       return out;
//   }
//   private static ArrayList<Integer> listMoves = new ArrayList<>();

    private static final int[][] moves =  // list of all the possible moves
            {{-1, 0},
            {1,0},
            {0,1},
            {0,-1}
    };

    /**
     *
     * @param mindMap with the data of the map previously created
     * @return a list of (x,y) coordinates from first to last move
     */

   public static ArrayList<Integer> computePath(MindMap mindMap){
       int[][] searchStates = mindMap.walkable();

       List<int[]> listOfPositions = new ArrayList<>();

       int[] initialState = {(int)mindMap.getState().getPos().getX(),(int)mindMap.getState().getPos().getY()};
       System.out.println("initialState = " + initialState[0]+", "+initialState[1]);
       searchStates[initialState[0]][initialState[1]] = 1; // the robot is there at the start so no need to explore it again

//       printMatrix(searchStates);
       int[] target = {(int)mindMap.getTargetPos().getX(),(int)mindMap.getTargetPos().getY()}; // position of the target area
       System.out.println("target: x = " + target[0]+", y = " +target[1]);
       // add the 4 actions
       ArrayList<int []> states = new ArrayList<>();

       Point a = new Point(initialState[0],initialState[1]);
       Point b = new Point(target[0], target[1]);
       Distance dist = new Distance(a,b);

       states.add(new int[] {(int)dist.getValue(),1,initialState[0],initialState[1]});

       // create a matrix of actions
       int[][] actions = new int[searchStates.length][searchStates[0].length];
       int count = 0;
       boolean stop = false;

       while(states.size()>0 && !stop) {
            count++;
           // to sort the state list regarding their cost. The cost is the first value
           states.sort(new Comparator<int[]>() {
               @Override
               public int compare(int[] o1, int[] o2) {
                   return Integer.compare(o1[0], o2[0]);
               }
           });
        /*   System.out.println("States: ");
           for (int i = 0; i <states.size() ; i++) {
                   System.out.print(i+1 + ")" + " Cost = " + states.get(i)[0] + ", " );
                   System.out.print( "x = " + states.get(i)[1]  + ", ");
                   System.out.print("y = " + states.get(i)[2]);
               System.out.println();
           }
           System.out.println("---------");
         */

           int[] checkedState = states.remove(0);
          // System.out.println("Checked State: ");
          // System.out.print( "Cost = " + checkedState[0] + ", " );
          // System.out.print( "x = " + checkedState[2]  + ", ");
          // System.out.print("y = " + checkedState[3]);
          // System.out.println();

           for (int i = 0; i < moves.length; i++) { // to check the 4 different moves

               int possibleNewX = checkedState[2] + moves[i][0]; // new x coordinate after one of the 4 moves
               int possibleNewY = checkedState[3] + moves[i][1]; // new y coordinate after one of the 4 moves

               int instantxdiff = 0;
               int instantydiff = 0;
               int xdiff = mindMap.getState().getX() - initialState[0];
               int ydiff = mindMap.getState().getY() - initialState[1];
              // System.out.println("ydiff = " + ydiff);
              // System.out.println("xdiff = " + xdiff);

               possibleNewX += xdiff; // new x coordinate after one of the 4 moves
               possibleNewY += ydiff; // new y coordinate after one of the 4 moves
              // System.out.println("possibleNewX = " + possibleNewX);
              // System.out.println("possibleNewY = " + possibleNewY);
              // System.out.println();

               // expand in the x direction if needed  to the bottom
               if (searchStates.length <= possibleNewX) {
                   actions = expandBottom(actions);
                   searchStates = expandBottom(searchStates);
                   mindMap.expandBottom(1);
//                   System.out.println("Expended");
//                   printMatrix(searchStates);
               }

               // expand in the x direction if needed  to the top
               if (possibleNewX < 0) {
                   actions = expandTop(actions);
                   searchStates = expandTop(searchStates);
                   mindMap.expandTop(1);
                   possibleNewX = 0;
                   instantxdiff = 1;
//                   System.out.println("Expended");
//                   printMatrix(searchStates);
               }

               // expand in the y direction if needed to the right
               if (searchStates[0].length <= possibleNewY) {
                   actions = expandRight(actions);
                   searchStates = expandRight(searchStates);
                   mindMap.expandRight(1);
//                   System.out.println("Expended");
//                   printMatrix(searchStates);
               }

               // expand in the y direction if needed to the left
               if (possibleNewY < 0) {
                   actions = expandLeft(actions);
                   searchStates = expandLeft(searchStates);
                   mindMap.expandLeft(1);
                   possibleNewY = 0;
                   instantydiff = 1;
//                   System.out.println("Expended");
//                   printMatrix(searchStates);
               }

               if (target[0] == possibleNewX - xdiff && target[1] == possibleNewY - ydiff) { // checks if the agent is in the target area
                   actions[possibleNewX][possibleNewY] = i + 1;
                   /*
                   i = 1 is going to the top
                   i = 2 is going to the bottom
                   i = 3 is going to the right
                   i = 4 is going to the left
                    */
                   stop = true;
               }
               if (!stop) {
                   if (searchStates[possibleNewX][possibleNewY] == 0) { // if unvisited and walkable
                       Point pointA = new Point(target[0],target[1]);
                       Point pointB = new Point(possibleNewX-xdiff-instantxdiff, possibleNewY-ydiff-instantydiff);
                       Distance euclDistance = new Distance(pointA,pointB);
                       int cost = (int) Math.floor(euclDistance.getValue()); // cost of this move, equal to 1 here since only move of 1 case
                       int newCost = cost+checkedState[1]; // new cost = addition of the previous cost (previous moves) and this move
                       //System.out.println("newCost = " + newCost);
                       int[] newState = {newCost,checkedState[1]+1, possibleNewX - xdiff - instantxdiff, possibleNewY - ydiff - instantydiff}; // potential new state
                       searchStates[possibleNewX][possibleNewY] = 1; // no need to explore anymore
                       states.add(newState);
                       actions[possibleNewX][possibleNewY] = i + 1;
                       //System.out.println("add " + newCost + " " + (possibleNewX - xdiff - instantxdiff) + " " + (possibleNewY - ydiff - instantydiff));
                     //  printMatrix(searchStates);
                   /*
                   i = 1 is going to the top
                   i = 2 is going to the bottom
                   i = 3 is going to the right
                   i = 4 is going to the left
                    */
                   }
               }
           }
       }

     // printMatrix(actions);

               int xdiff = mindMap.getState().getX() - initialState[0];
       System.out.println("xdiff = " + xdiff);
               int ydiff = mindMap.getState().getY() - initialState[1];
       System.out.println("ydiff = " + ydiff);

               //int xdiff = mindMap.getState().getX() - target[0];
               //int ydiff = mindMap.getState().getY() - target[1];

               int xCoorTarget = target[0] +xdiff;
               int yCoorTarget = target[1] +ydiff;
             //  actions[xCoorTarget][yCoorTarget] = 9; // 9 is an arbitrary value set to identify the target point
               int x = xCoorTarget;
               int y = yCoorTarget;
               //int x = mindMap.getState().getX();
               //int y = mindMap.getState().getY();
               //actions[xCoorTarget][yCoorTarget] = 9; // 9 is an arbitrary value set to identify the target point
               int[] lastPosition = {x,y};

               listOfPositions.add(lastPosition);

               ArrayList<Integer> list_of_moves = new ArrayList<>();

               while( x != (initialState[0]+xdiff) || y != (initialState[1]+ydiff)){
                   list_of_moves.add(actions[x][y]);

                 //  System.out.println("action coord x = " + (moves[actions[x][y]-1][0]));
                  // System.out.println("action coord y = " + (moves[actions[x][y]-1][1]));

                   int x2 = x - moves[actions[x][y]-1][0];
                   int y2 = y - moves[actions[x][y]-1][1];

                   int[] position = {x2,y2};
                   listOfPositions.add(position);

                   x = x2;
                   y = y2;
                   // System.out.println("x = " + x);
                   // System.out.println("y = " + y);
               }


               //Collections.reverse(listOfPositions); // inverse the position order so the first index of the array is the first position
               for (int j = 0; j < listOfPositions.size(); j++) {
                //   System.out.println("x position of " + j + "th move = " + listOfPositions.get(j)[0]);
                 //  System.out.println("y position of " + j + "th move = " + listOfPositions.get(j)[1]);
                 //  System.out.println(" ----------- ");
               }
//       return listOfPositions;
       Collections.reverse(list_of_moves); // inverse the position order so the first index of the array is the first position
       return list_of_moves;
    }


    /**
     * This method translates the coordinates of the path into a list of moves: "right", "left", "up" and "down
     * @param listPositions from the path finding algo (just above)
     * @return the list of moves in string
     */
    public static ArrayList<Integer> getListOfActionsDirections(List<int[]> listPositions) {
        int length = listPositions.size();
       // ArrayList<String> listOfMoves = new ArrayList<>();
        ArrayList<Integer> listOfMoves = new ArrayList<>();
       // System.out.println("size = " + length);
        System.out.println(listPositions.get(1)[0]);


      /*  for (int i = 0; i < listPositions.size(); i++) {
            System.out.println("listPositions = " + Arrays.toString(listPositions.get(i)));
        }
       */


        for (int i = 0; i < length-1; i++) {
           // System.out.println("i =" + i);
            if (listPositions.get(i)[0] == listPositions.get(i + 1)[0]) {
                if (listPositions.get(i)[1] == listPositions.get(i + 1)[1]) {
                    System.out.println("smthg not normal in the path finding");
                } else if (listPositions.get(i)[1] == listPositions.get(i + 1)[1] + 1) {
                    //String tmp = "right"; // 3 = right
                    int tmp = 3; // 3 = right
                    listOfMoves.add(tmp);
                } else if (listPositions.get(i)[1] == listPositions.get(i + 1)[1] - 1) {
                    // String tmp = "left"; // 4 = left
                    int tmp = 4; // 4 = left
                    listOfMoves.add(tmp);
                }
            }
            else if(listPositions.get(i)[0] == listPositions.get(i + 1)[0]+1){
                // String tmp = "down"; // 2 = down
                int tmp = 2; // 2 = down
                listOfMoves.add(tmp);
            }
            else if(listPositions.get(i)[0] == listPositions.get(i + 1)[0]-1){
               // String tmp = "up"; // 1 = up
                int tmp = 1; // 1 = up
                listOfMoves.add(tmp);
            }
        }
        return listOfMoves;
    }


    public static ArrayList<int[]> toConsecutiveMoves(ArrayList<Integer> listActions) {
        ArrayList<int[]> out = new ArrayList<>();

        int ac = -1; //keeps
        for (Integer action : listActions) {
            if (ac == -1) {
                int[] plus = new int[]{action, 1};
                out.add(plus);
                ac = action;
            } else {
                if (action == ac) {
                    out.get(out.size()-1)[1]++;
                } else {
                    ac = action;
                    int[] plus = new int[]{action, 1};
                    out.add(plus);
                }
            }
        }
        return out;
    }

    /*
    public static List<int[]> getNumberConsecutiveMoves(ArrayList listPositions){
        List<int[]> listOfmoves = new ArrayList<int[]>();
        int length = listPositions.size();
        int numberAdded = 0;

        int i = 0;
        do {
            System.out.println("do1");
            // int counter = 0;
            int indexSame = 1;
            boolean same = true;
            while (same){
                System.out.println("while2");
                System.out.println("i = " + i);
                System.out.println("ind = " + indexSame);
                System.out.println("i+ind = " + (i+indexSame));
                System.out.println("---");
                if ((i+indexSame)<length) {
                    if ((int) listPositions.get(i) == (int) listPositions.get(i + indexSame)) {
                        indexSame++;
                    } else {
                        same = false;
                        int move = (int) listPositions.get(i);
                        int[] moveAndNumberConsecutiveTimes = {move, indexSame};
                        listOfmoves.add(numberAdded, moveAndNumberConsecutiveTimes);
                        numberAdded++;
                    }
                }
                else{
                    same = false;
                }
            }
            System.out.println("i before = " + i);
            i = i + indexSame;
            System.out.println("i after = " + i);
            System.out.println("-");
        }
        while((i)<(length-2));
        return listOfmoves;
    }
    */



    /**
     * @param matrix to expand
     * @return new matrix (expanded) with 1 extra bottom row
     */
    public static int[][] expandBottom(int[][] matrix){
        /*System.out.println("before expansion: ");
        printMatrix(matrix);
        System.out.println();*/
//        System.out.println("expanded bottom");
        // expand in the x direction if needed  to the bottom
            int[][] tmp = new int[matrix.length + 1][matrix[0].length];
            for(int i = 0; i < matrix.length; i++){
                System.arraycopy(matrix[i], 0, tmp[i], 0, matrix[0].length);
            }
        matrix = tmp;
    //    System.out.println("after expansion: ");
    //    printMatrix(matrix);
        return matrix;
    }


    /**
     * @param matrix to expand
     * @return new matrix (expanded) with 1 extra top row
     */
    public static int[][] expandTop(int[][] matrix){
        //System.out.println("before expansion: ");
        // printMatrix(matrix);
        // System.out.println();
//        System.out.println("expanded top");
        // expand in the x direction if needed  to the top
        int[][] tmp = new int[matrix.length + 1][matrix[0].length];
        for(int i = 0; i < matrix.length; i++){
            System.arraycopy(matrix[i], 0, tmp[i + 1], 0, matrix[0].length);
        }
        matrix = tmp;
        //  System.out.println("after expansion: ");
        // printMatrix(matrix);
        return matrix;
    }

    /**
     * @param matrix to expand
     * @return new matrix (expanded) with 1 extra right column
     */
    public static int[][] expandRight(int[][] matrix){
//        System.out.println("expanded right");
        // expand in the y direction if needed to the right
            int[][] tmp = new int[matrix.length][matrix[0].length + 1];
            for(int i = 0; i < matrix.length; i++){
                System.arraycopy(matrix[i], 0, tmp[i], 0, matrix[0].length);
            }
        matrix = tmp;
        // printMatrix(matrix);

        return matrix;
    }

    /**
     * @param matrix to expand
     * @return new matrix (expanded) with 1 extra Left column
     */
    public static int[][] expandLeft(int[][] matrix) {
//        System.out.println("expanded left");
        // expand in the y direction if needed to the left
        int diff = 1;
        int[][] tmp = new int[matrix.length][matrix[0].length + 1];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                tmp[i][j + 1] = matrix[i][j];
             }
        }
        matrix = tmp;
       // printMatrix(matrix);
        return matrix;
    }


    }
