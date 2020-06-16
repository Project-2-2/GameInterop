package Group3.Intruder;

import java.util.Random;
import java.util.Set;
import java.util.Stack;



import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

import Group3.Agent.Action;
import Group3.DiscreteMap.BFS;
import Group3.DiscreteMap.DirectedEdge;
import Group3.DiscreteMap.DiscreteMap;
import Group3.DiscreteMap.ObjectType;
import Group3.DiscreteMap.Vertice;
import Interop.Action.GuardAction;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.VisionPrecepts;


public class Intruder implements Interop.Agent.Intruder {
	
	Action lastAction = null;
	DiscreteMap map;
	Vertice currentPosition;
	double viewAngle;
	double radius;
	Angle angle;
	Queue<Action> actionList = new LinkedList<Action>();
	Queue<Double> distanceCounter = new LinkedList<Double>();
	boolean foundTarget = false;
	boolean escape = false;
	
	@Override
	public IntruderAction getAction(IntruderPercepts percepts) {
		// TODO Auto-generated method stub
		
		if(this.map == null || percepts.getAreaPercepts().isJustTeleported()) {
			viewAngle = percepts.getVision().getFieldOfView().getViewAngle().getDegrees();
			radius = Math.sqrt(Math.pow(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue(),2)/2)/2;
			angle = Angle.fromDegrees(0);
			this.map = new DiscreteMap();
			currentPosition = new Vertice(ObjectType.None, new Point(0, 0), radius, new Integer[] {0,0});
			map.addVertice(currentPosition);
			lastAction = null;
			if(percepts.wasLastActionExecuted()) {
				actionList.offer(Action.Move);
			}
			else {
				actionList.offer(Action.Right);
			}
		}
		if(distanceCounter.size() != 0) {
			return new Move(new Distance(distanceCounter.poll()));
		}
		if(percepts.wasLastActionExecuted()) {
			updateState();
		}
		else {
			//System.out.println("Illegal Move!!!");
			actionList = new LinkedList<Action>();
			actionList.offer(Action.Left);
			actionList.offer(Action.Move);
		}
		if(percepts.getAreaPercepts().isInSentryTower()) {
			System.out.println("In Sentry");
		}
		createNewVerticesInSight(percepts.getVision());
		//System.out.println(map.toString(currentPosition.getCoordinate()));
		evaluateVision(percepts.getVision());
		
		//System.out.println("");
		//System.out.println(map.toString(currentPosition.getCoordinate()));
		
		if(actionList.size() == 0) {
			getNextAction(percepts);
			if(actionList.size() == 0) {
				actionList.offer(Action.Move);
			}
			return returnAction(actionList.poll(), percepts);
		}
		else {
			return returnAction(actionList.poll(), percepts);
		}
	}
	
	private IntruderAction returnAction(Action action, IntruderPercepts percepts) {
		switch(action) {
		case Left:
			//System.out.println("Left");
			return turnLeft(); 
		case Right:
			//System.out.println("Right");
			return turnRight();
		case Move:
			//System.out.println("Move");
			double maxMoveDistance = percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue();
			if(percepts.getAreaPercepts().isInSentryTower()) {
				maxMoveDistance = maxMoveDistance * percepts.getScenarioIntruderPercepts().getScenarioPercepts().getSlowDownModifiers().getInSentryTower();
			}
			else if(percepts.getAreaPercepts().isInDoor()) {
				maxMoveDistance = maxMoveDistance * percepts.getScenarioIntruderPercepts().getScenarioPercepts().getSlowDownModifiers().getInDoor();
			}
			else if(percepts.getAreaPercepts().isInWindow()) {
				maxMoveDistance = maxMoveDistance * percepts.getScenarioIntruderPercepts().getScenarioPercepts().getSlowDownModifiers().getInWindow();
			}
			else {
				maxMoveDistance = percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue(); 
			}		
			return forward(maxMoveDistance);
		default:
			return new NoAction();
		}
	}
	
	private void evaluateVision(VisionPrecepts vision) {
		Set<ObjectPercept> objectPercepts = vision.getObjects().getAll();
		for(ObjectPercept percept : objectPercepts) {
			//double radianAngle = Angle.fromDegrees(angle.getDegrees() + 90).getRadians();
			//double x=percept.getPoint().getX()*Math.cos(radianAngle) + currentPosition.getCenter().getX();
			//double y=percept.getPoint().getY()*Math.sin(radianAngle) + currentPosition.getCenter().getY();
			double addX = currentPosition.getCenter().getX();
			double addY = currentPosition.getCenter().getY();
			Point rotatePoint = truePoint(percept.getPoint());
			Point truePoint = new Point(rotatePoint.getX() + addX, rotatePoint.getY() + addY);
			int l = 1;
			// TODO: Add x and y
			Vertice inVertice = getRelativeVertice(truePoint);
			switch(percept.getType()) {
			case Door:
				inVertice.setType(ObjectType.Door);
				break;
			case EmptySpace:
				inVertice.setType(ObjectType.None);
				break;
			case SentryTower:
				inVertice.setType(ObjectType.SentryTower);
				break;
			case ShadedArea:
				inVertice.setType(ObjectType.ShadedArea);
				break;
			case TargetArea:
				inVertice.setType(ObjectType.TargetArea);
				foundTarget = true;
				break;
			case Teleport:
				inVertice.setType(ObjectType.Teleport);
				break;
			case Wall:
				inVertice.setType(ObjectType.Wall);
				break;
			case Window:
				inVertice.setType(ObjectType.Window);
				break;
			case Guard:
				map.removeDanger();
				System.out.println("Danger");
				escape = true;
				inVertice.setType(ObjectType.Danger);
				break;
			case Intruder:
				Random rand = new Random();
				int r = rand.nextInt(2);
				if(r == 0) {
					actionList.add(Action.Left);
					actionList.add(Action.Move);
				}
				else {
					actionList.add(Action.Right);
					actionList.add(Action.Move);
				}
				break;
			default:
				inVertice.setType(ObjectType.Unknown);
				break;
			}
		}
	}
	
	private Vertice findEscape() {
		Vertice candidate = map.getVertice(new Integer[] {0,0});
		double maxDistance = 0;
		int x = currentPosition.getCoordinate()[0];
		int y = currentPosition.getCoordinate()[1];
		
		for(Vertice v : BFS.getReachableVertices(currentPosition)) {
			if(BFS.checkVertice2(v) && v.getEdges().size() < 8) {
				int xp = v.getCoordinate()[0];
				int yp = v.getCoordinate()[1];
				double distance = Math.sqrt(Math.pow(xp-x, 2)+Math.pow(yp-y, 2));
				if(distance > maxDistance) {
					maxDistance = distance;
					candidate = v;
				}
			}
		}
		return candidate;
	}
	
	private void getNextAction(IntruderPercepts percepts) {
		if(escape) {
			map.unMark();
			escape = false;
			Vertice escape = findEscape();
			map.unMark();
			Stack<Vertice> target = BFS.findPath(currentPosition, escape);
			if(target != null) {
				actionList = new LinkedList<Action>();
				generateActionList(target);
				return;
			}
		}
		if(foundTarget) {
			map.unMark();
			Stack<Vertice> target = BFS.findPath(currentPosition, ObjectType.TargetArea);
			if(target != null) {
				generateActionList(target);
				return;
			}
		}
		
		List<Action> actionSpace = new ArrayList<Action>();
		actionSpace.add(Action.Left);
		actionSpace.add(Action.Right);
		Integer[] nextPosition = DiscreteMap.getCoordinate((int)angle.getDegrees(), currentPosition.getCoordinate());
		Vertice nextVertice = map.getVertice(nextPosition);
		if(BFS.checkVertice(nextVertice)) {
			actionSpace.add(Action.Move);
		}
		
		double actionValue = 0;
		Action selectedAction = null;
		for(Action action : actionSpace) {
			Angle angle = Angle.fromRadians(this.angle.getRadians());
			Vertice currentPosition = this.currentPosition;
			switch(action) {
			case Left:
				angle = Angle.fromDegrees(getTrueAngle(this.angle.getDegrees() +45));
				break;
			case Right:
				angle = Angle.fromDegrees(getTrueAngle(this.angle.getDegrees() -45));
				break;
			case Move:
				Integer[] newPosition = DiscreteMap.getCoordinate((int)angle.getDegrees(), currentPosition.getCoordinate());
				currentPosition = map.getVertice(newPosition);
				break;
			}
			double value = simulateAction(percepts.getVision(), angle, currentPosition);
			if(value > actionValue) {
				actionValue = value;
				selectedAction = action;
			}
		}
		//selectedAction = null;
		if(selectedAction == null) {
			map.unMark();
			Stack<Vertice> path = BFS.findNonCompleteVertice(currentPosition);
			if(path == null) {
				map.unMark();
				path = BFS.findPath(currentPosition, ObjectType.Teleport);
			}
			generateActionList(path);
		}
		else {
			actionList.offer(selectedAction);
		}
	}
	
	private void generateActionList(Stack<Vertice> path) {
		if(path == null) {
			actionList.add(Action.Move);
			return;
		}
		Vertice start = path.pop();
		int currentDegrees = (int)angle.getDegrees();
		while(path.size() != 0) {
			Vertice end = path.pop();
			for(DirectedEdge e : start.getEdges()) {
				if(e.getEndVertice() == end) {
					int degrees = e.getDegrees();
					// Some inaccuracies
					double add;
					if(currentDegrees > 180) {
						add = 360 - currentDegrees;
					}
					else {
						add = -currentDegrees;
					}
					while(Math.abs(currentDegrees - degrees)>2) {
						if(getTrueAngle(degrees + add) < 180) {
							actionList.add(Action.Left);
							currentDegrees = (int)getTrueAngle(currentDegrees +45);
						}
						else {
							actionList.add(Action.Right);
							currentDegrees = (int)getTrueAngle(currentDegrees -45);
						}
					}
					break;
				}
			}
			start = end;
			actionList.add(Action.Move);
		}
	}

	/*
	private IntruderAction getNextAction(IntruderPercepts percepts) {
		if(!percepts.wasLastActionExecuted()) {
			int r = new Random().nextInt(2);
			if(r == 0) {
				return turnLeft();
			}
			else {
				return turnRight();
			}
		}
		
		int steps = 3;
		int[] counter = new int[steps];
		double[] result = new double[3];
		while(!checkCounter(counter)) {
			Angle angle = Angle.fromRadians(this.angle.getRadians());
			Vertice currentPosition = this.currentPosition;
			for(int i = 0; i < steps; i++) {
				switch(counter[i]) {
				case 0:
					angle = Angle.fromDegrees(getTrueAngle(this.angle.getDegrees() +45));
					break;
				case 1:
					angle = Angle.fromDegrees(getTrueAngle(this.angle.getDegrees() -45));
					break;
				case 2:
					Integer[] newPosition = DiscreteMap.getCoordinate((int)angle.getDegrees(), currentPosition.getCoordinate());
					currentPosition = new Vertice(null, new Point(2*radius*newPosition[0], 2*radius*newPosition[1]), 0, newPosition);
					break;
				}
				result[counter[0]] += simulateAction(percepts.getVision(), angle, currentPosition);
			}
			increaseCounter(counter);
		}
		
		int max = 0;
		for(int i = 1; i < 3; i++) {
			if(result[i] > result[max]) {
				max = i;
			}
		}

		switch(max) {
		case 0:
			return turnLeft();
		case 1:
			return turnRight();
		case 2:
			return forward();
		default:
			return new NoAction();
		}
	}
	
	private void increaseCounter(int[] counter) {
		int increase = 1;
		for(int i = 0; i < counter.length; i++) {
			if(counter[i] + increase > 2) {
				counter[i] = 0;
			}
			else {
				counter[i] += increase;
				increase = 0;
			}
		}
	}
	
	private boolean checkCounter(int[] counter) {
		for(int i = 0; i < counter.length; i++) {
			if(counter[i] != 2) return false;
		}
		return true;
	}
	*/
	
	public double simulateAction(VisionPrecepts percepts, Angle angle, Vertice currentPosition) {
		int vertexMultiply = 1;
		double result = vertexMultiply*numberNewVerticesInSight(angle,currentPosition, percepts);
		return result;
	}
	
	private int numberNewVerticesInSight(Angle angle, Vertice currentPosition, VisionPrecepts percepts) {
		int count = 0;
		int samples = 10;
		double viewRange = percepts.getFieldOfView().getRange().getValue();
		double step = viewRange / samples;
		if(step > radius) {
			samples = (int)(viewRange / radius);
			step = radius;
		}
		double currentAngle = (viewAngle+4)/2;
		while(currentAngle >= -viewAngle/2) {
			double finalAngle = 0;
			if(angle.getDegrees() > 180) finalAngle = getTrueAngle(currentAngle + (angle.getDegrees()-360) + 90);
			else finalAngle = getTrueAngle(currentAngle + angle.getDegrees() + 90);
			
			Angle pAngle = Angle.fromDegrees(finalAngle);
			for(int i=1; i < samples+2; i++) {
				double x=i*step*Math.cos(pAngle.getRadians());
				double y=i*step*Math.sin(pAngle.getRadians());
				x += currentPosition.getCenter().getX();
				y += currentPosition.getCenter().getY();
				Integer[] position = getRelativeVerticeCoordinate(new Point(x,y));
				if(!map.verticeExists(position)) {
					count ++;
				}
			}
			currentAngle = currentAngle -1;
		}
		return count;
	}
	
	private Rotate turnLeft() {
		lastAction = Action.Left;
		return new Rotate(Angle.fromDegrees(-45));
	}
	
	private Rotate turnRight() {
		lastAction = Action.Right;
		return new Rotate(Angle.fromDegrees(+45));
	}
	
	private Move forward(double maxDistance) {
		this.lastAction = Action.Move;
		double distance;
		
		if(this.angle.getDegrees() % 90 == 0) {
			distance = 2*radius;
		}
		else {
			distance = Math.sqrt(2*Math.pow(2*radius, 2));
		}
		while(distance > maxDistance) {
			distanceCounter.offer(maxDistance);
			distance = distance - maxDistance;
		}
		return new Move(new Distance(distance));
	}
	
	private void updateState() {
		if(lastAction != null) {
			switch(lastAction) {
			case Left:
				this.angle = Angle.fromDegrees(getTrueAngle(this.angle.getDegrees() +45));
				break;
			case Right:
				this.angle = Angle.fromDegrees(getTrueAngle(this.angle.getDegrees() -45));
				break;
			case Move:
				Integer[] newPosition = DiscreteMap.getCoordinate((int)angle.getDegrees(), currentPosition.getCoordinate());
				currentPosition = map.getVertice(newPosition);
				break;
			}
		}
	}
	
	private void createNewVerticesInSight(VisionPrecepts percepts) {
		int samples = 10;
		double viewRange = percepts.getFieldOfView().getRange().getValue();
		double step = viewRange / samples;
		if(step > radius) {
			samples = (int)(viewRange / radius);
			step = radius;
		}
		double currentAngle = (viewAngle+4)/2;
		while(currentAngle >= -viewAngle/2) {
			double finalAngle = 0;
			if(angle.getDegrees() > 180) finalAngle = getTrueAngle(currentAngle + (angle.getDegrees()-360) + 90);
			else finalAngle = getTrueAngle(currentAngle + angle.getDegrees() + 90);
			
			Angle pAngle = Angle.fromDegrees(finalAngle);
			for(int i=1; i < samples+2; i++) {
				double x=i*step*Math.cos(pAngle.getRadians());
				double y=i*step*Math.sin(pAngle.getRadians());
				x += currentPosition.getCenter().getX();
				y += currentPosition.getCenter().getY();
				Integer[] position = getRelativeVerticeCoordinate(new Point(x,y));
				if(!map.verticeExists(position)) {
					map.addVertice(new Vertice(ObjectType.None, new Point(2*radius*position[0], 2*radius*position[1]), radius, position));
				}
			}
			if(radius < 0.05) {
				currentAngle = currentAngle -.1;
			}
			else {
				currentAngle = currentAngle -1;
			}			
		}
	}
	
	private double getTrueAngle(double angle) {
		if(angle < 0) {
			angle = 360 + angle;
		}
		else if(angle > 360) {
			angle = 0 + (angle - 360);
		}
		else if(angle == 360) {
			angle = 0;
		}
		return angle;
	}
	
	private Vertice getRelativeVertice(Point point) {
		Integer[] position = getRelativeVerticeCoordinate(point);
		//System.out.println(position[0] + ";" + position[1]);
		return map.getVertice(position);
	}
	
	
	private Integer[] getRelativeVerticeCoordinate(Point point) {
		//Point truePoint = truePoint(point);
		//double x = truePoint.getX();
		//double y = truePoint.getY();
		double x = point.getX();
		double y = point.getY();
		int xVertice = 0;
		int yVertice = 0;
		if(!(Math.abs(x)<=radius)) {
			if(x<0) {
				xVertice--;
				x = x + radius;
			}
			else {
				xVertice++;
				x = x - radius;
			}
			while(Math.abs(x) >= 2*radius) {
				if(x<0) {
					xVertice--;
					x = x + 2*radius;
				}
				else {
					xVertice++;
					x = x - 2*radius;
				}
			}
		}
		if(!(Math.abs(y)<=radius)) {
			if(y<0) {
				yVertice--;
				y = y + radius;
			}
			else {
				yVertice++;
				y = y - radius;
			}
			while(Math.abs(y) >= 2*radius) {
				if(y<0) {
					yVertice--;
					y = y + 2*radius;
				}
				else {
					yVertice++;
					y = y - 2*radius;
				}
			}
		}
		return new Integer[]{xVertice, yVertice};
	}
	
	
	private Point truePoint(Point point) {
		double x = -point.getX();
		double y = point.getY();
		/*
		if((int)angle.getDegrees() == 180) {
			x = -x;
		}
		else if((int)angle.getDegrees() == 0) {
			x = -x;
		}
		*/
		double x_ = x*Math.cos(angle.getRadians()) - y * Math.sin(angle.getRadians());
		double y_ = y*Math.cos(angle.getRadians()) + x * Math.sin(angle.getRadians());
		return new Point(x_, y_);
	}
	
	private Point getCenterPoint(int degrees) {
		double x = currentPosition.getCenter().getX();
		double y = currentPosition.getCenter().getY();
		
		switch(degrees) {
		case 0:
			return new Point(x, y+2*radius);
		case 45:
			return new Point(x+2*radius, y+2*radius);
		case 90:
			return new Point(x+2*radius, y);
		case 135:
			return new Point(x+2*radius, y-2*radius);
		case 180:
			return new Point(x, y-2*radius);
		case 225:
			return new Point(x-2*radius, y-2*radius);
		case 270:
			return new Point(x-2*radius, y);
		case 315:
			return new Point(x-2*radius, y+2*radius);
		default: 
			return null;
		}
	}
}
