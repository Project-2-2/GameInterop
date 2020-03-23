package Group5.GameController;

public class Door extends Area {

    private static double slowDownModifier;

    private boolean closed;
    public Door(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4){
        super(x1, y1, x2, y2,x3, y3, x4, y4);
        closed = true;
    }

    public boolean doorClosed(){
        return  closed;
    }

    /**
     * TODO ADD NOISE WHEN OPENING
     */
    public void openDoor(Hearing hearing){
        closed = false;
        hearing.doorSound(this);
    }

    public void closeDoor(Hearing hearing){
        closed = true;
        hearing.doorSound(this);
    }


    public static double getSlowDownModifier() {
        return slowDownModifier;
    }

    protected static void setSlowDownModifier(double slowDownModifier) {
        Door.slowDownModifier = slowDownModifier;
    }


}
