package Group9.agent.Intruder;

public class Score{

    // scores for cells with certain features
    private final double TARGET_SCORE = 1000;
    private final double WALL_SCORE = 10;
    private final double TOWER_SCORE = -100;
    private final double DOOR_SCORE = 2;
    private final double WINDOW_SCORE = -500;
    private final double GUARD_SCORE = -500;
    private final double TELEPORT_SCORE = 2;


    // Method to calculate score **might have to add evaluation function adjustments
    public double getScoreCell(Cell cell){
        double scoreOfCell = 0;

        if(cell.hasWall()){scoreOfCell+= WALL_SCORE;}
        if(cell.hasWindow()){scoreOfCell+= WINDOW_SCORE;}
        if(cell.hasDoor()){scoreOfCell+= DOOR_SCORE;}
        if(cell.hasTower()){scoreOfCell+= TOWER_SCORE;}
        if(cell.hasGuard()){scoreOfCell+= GUARD_SCORE;}
        if(cell.hasTarget()){scoreOfCell+= TARGET_SCORE;}
        if(cell.hasTeleport()){scoreOfCell+= TELEPORT_SCORE;}

        //TODO: redefine getCount() in cell
        scoreOfCell+= (-2) * cell.getCount();
        return scoreOfCell;
    }

    // Method to choose best of the available cells regarding the score
    public Cell chooseBestCell(Cell[] cellsToCompare){
        Cell bestCell = cellsToCompare[0];

        for (int i = 1; i < cellsToCompare.length; i++){
            if(getScoreCell(cellsToCompare[i]) > getScoreCell(bestCell)) {
                bestCell = cellsToCompare[i];
            }
        }
        return bestCell;
    }

}
