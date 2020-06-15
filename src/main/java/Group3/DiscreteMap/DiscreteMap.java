package Group3.DiscreteMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;;

public class DiscreteMap {
	private static final String ArrayList = null;

	HashMap<String, Vertice> vertices;
	
	int xMin = 0;
	int xMax = 0;
	int yMin = 0;
	int yMax = 0;
	
	public DiscreteMap() {
		this.vertices = new HashMap<String, Vertice>();
	}

	public Vertice getVertice(Integer[] position) {
		String key = position[0] + " " + position[1];
		return vertices.get(key);
	}
	
	public List<Vertice> getAllVertices() {
		ArrayList<Vertice> verticeList = new ArrayList<Vertice>();
		for(String key: vertices.keySet()) {
			verticeList.add(vertices.get(key));
		}
		return verticeList;
	}

	public void addVertice(Vertice vertice) {
		checkNewVertice(vertice);
		
		Integer[] xy = vertice.getCoordinate();
		if(xy[0] < xMin) {
			xMin = xy[0];
		}
		if(xy[0] > xMax) {
			xMax = xy[0];
		}
		if(xy[1] < yMin) {
			yMin = xy[1];
		}
		if(xy[1] > yMax) {
			yMax = xy[1];
		}
		
		Integer[] position =  vertice.getCoordinate();
		String key = position[0] + " " + position[1];
		this.vertices.put(key,vertice);
	}
	
	public boolean verticeExists(Integer[] coordinate) {
		String key = coordinate[0] + " " + coordinate[1];
		return vertices.containsKey(key);
	}
	
	public static Integer[] getCoordinate(int degrees, Integer[] currentPosition) {
		int x = currentPosition[0];
		int y = currentPosition[1];
		
		switch(degrees) {
		case 0:
			return new Integer[] {x, y+1};
		case 45:
			return new Integer[] {x-1, y+1};
		case 90:
			return new Integer[] {x-1, y};
		case 135:
			return new Integer[] {x-1, y-1};
		case 180:
			return new Integer[] {x, y-1};
		case 225:
			return new Integer[] {x+1, y-1};
		case 270:
			return new Integer[] {x+1, y};
		case 315:
			return new Integer[] {x+1, y+1};
		default: 
			return null;
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int y = yMax; y >= yMin; y--) {
			for(int x = xMin; x <= xMax; x++) {
				String key = x + " " + y;
				if(vertices.containsKey(key)) {
					Vertice vertice = vertices.get(key);
					switch (vertice.type) {
					case Wall:
						sb.append("W");
						break;
					case None:
						sb.append("_");
						break;
					case SentryTower:
						sb.append("S");
						break;
					case Teleport:
						sb.append("T");
						break;
					case TargetArea:
						sb.append("!");
						break;
					case Unknown:
						sb.append("?");
						break;
					default:
						sb.append("O");
					}
				}
				else {
					sb.append("X");
				}
			}
			sb.append('\n');
		}
		return sb.toString();
	}
	
	public String toString(Integer[] agentPosition) {
		StringBuilder sb = new StringBuilder();
		for(int y = yMax; y >= yMin; y--) {
			for(int x = xMin; x <= xMax; x++) {
				String key = x + " " + y;
				
				if(x == agentPosition[0] && y == agentPosition[1]) {
					sb.append("#");
				}
				else if(vertices.containsKey(key)) {
					Vertice vertice = vertices.get(key);
					switch (vertice.type) {
					case Wall:
						sb.append("W");
						break;
					case None:
						sb.append("_");
						break;
					case SentryTower:
						sb.append("S");
						break;
					case Teleport:
						sb.append("T");
						break;
					case TargetArea:
						sb.append("!");
						break;
					case Unknown:
						sb.append("?");
						break;
					default:
						sb.append("O");
					}
				}
				else {
					sb.append("X");
				}
			}
			sb.append('\n');
		}
		return sb.toString();
	}
	
	public void unMark() {
		for(String key: vertices.keySet()) {
			Vertice vertice = vertices.get(key);
			vertice.setMarked(false);
			vertice.setParent(null);
		}
	}
	
	public void reset() {
		for(String key: vertices.keySet()) {
			vertices.get(key).setType(ObjectType.None);
		}
	}
	
	private void checkNewVertice(Vertice vertice) {
		for(int i = 0; i <= 315; i+=45) {
			Integer[] position = getCoordinate(i, vertice.coordinate);
			if(verticeExists(position)) {
				vertice.addEdge(getVertice(position), i);
			}
			
		}
			
	}

	public void removeDanger() {
		for(String key: vertices.keySet()) {
			if(vertices.get(key).getType() == ObjectType.Danger) {
				vertices.get(key).setType(ObjectType.None);
			}
		}
	}
	
	public void removeIntruder() {
		for(String key: vertices.keySet()) {
			if(vertices.get(key).getType() == ObjectType.Intruder) {
				vertices.get(key).setType(ObjectType.None);
			}
		}
	}
}
