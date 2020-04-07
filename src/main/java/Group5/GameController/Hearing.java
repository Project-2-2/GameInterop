package Group5.GameController;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Sound.SoundPercepts;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Hearing {

    private MapInfo mapInfo;
    private Set<Door> doorActions;
    private Set<Window> windowActions;
    private Set<Point> yells;

    public Hearing(MapInfo map){
        this.mapInfo = map;
    }

    public void doorSound(Door door){
        doorActions.add(door);
    }
    public void windowSound(Window window){
        windowActions.add(window);
    }
    public void yellSound(Point yell){
        yells.add(yell);
    }
    public void clearSounds(){
        doorActions.clear();
        windowActions.clear();
        yells.clear();
    }
    public SoundPercepts getSounds(AgentController perceptor){
        Point position = perceptor.getPosition();
        Set<SoundPercept> sounds= new HashSet<SoundPercept>();
        ArrayList<AgentController> agents = mapInfo.getAgents();
        ArrayList<Door> doors= mapInfo.doors;
        ArrayList<Window> windows= mapInfo.windows;
        for(int i=0; i<agents.size(); i++){
            AgentController agent = agents.get(i);
            Point pointRelation = new Point(agent.getPosition().getX()-position.getX(), agent.getPosition().getY()-position.getY());
            Distance distance = new Distance(position, agent.getPosition());
            if (!agent.getIsMoving() && distance.getValue() < mapInfo.getMaxMoveSoundRadius()) {
                sounds.add(new SoundPercept(SoundPerceptType.Noise, pointRelation.getClockDirection())); //work out this
            }
        }
        for(Door door : doorActions){
            Point pointRelation = new Point(door.getCentre().getX()-position.getX(), door.getCentre().getY()-position.getY());
            Distance distance = new Distance(position, door.getClosest(position));
            if (distance.getValue() < mapInfo.getDoorSoundRadius()) {
                sounds.add(new SoundPercept(SoundPerceptType.Noise, pointRelation.getClockDirection()));
            }
        }
        for(Window window : windowActions){
            Point pointRelation = new Point(window.getCentre().getX()-position.getX(), window.getCentre().getY()-position.getY());
            Distance distance = new Distance(position, window.getClosest(position));
            if (distance.getValue() < mapInfo.getWindowSoundRadius()) {
                sounds.add(new SoundPercept(SoundPerceptType.Noise, pointRelation.getClockDirection())); //work out this
            }
        }
        for(Point yell : yells){
            Point pointRelation = new Point(yell.getX()-position.getX(), yell.getY()-position.getY());
            Distance distance = new Distance(position, yell);
            if(distance.getValue() < mapInfo.getYellSoundRadius()) {
                sounds.add(new SoundPercept(SoundPerceptType.Yell, pointRelation.getClockDirection()));
            }
        }
        return new SoundPercepts(sounds);
    }
}