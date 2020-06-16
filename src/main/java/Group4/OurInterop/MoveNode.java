package Group4.OurInterop;

import Interop.Action.Action;
import Interop.Action.Move;

public class MoveNode implements Action {

    private Action move;

    public MoveNode(Action move) {
        this.move = move;
    }
}
