package Group5.GameController;

public class Window extends Area {

    private boolean closed;

    public Window(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4){
        super(x1, y1, x2, y2,x3, y3, x4, y4);
        closed = true;
    }

    public boolean windowClosed(){
        return  closed;
    }

    public void openWindow(){
        closed = false;
    }

    public void closeWindow(){
        closed = true;
    }
}
