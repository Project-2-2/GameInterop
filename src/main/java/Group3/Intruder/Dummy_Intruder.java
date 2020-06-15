package Group3.Intruder;
import Interop.Action.IntruderAction;
import Interop.Action.NoAction;
import Interop.Percept.IntruderPercepts;

public class Dummy_Intruder implements Interop.Agent.Intruder {

	@Override
	public IntruderAction getAction(IntruderPercepts percepts) {
		return new NoAction();
	}
	
	
}
