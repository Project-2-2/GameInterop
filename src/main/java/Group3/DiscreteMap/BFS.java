package Group3.DiscreteMap;
import java.util.Queue;
import java.util.Stack;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BFS {
	public static Stack<Vertice> findNonCompleteVertice(Vertice start){
		Queue<Vertice> queue = new LinkedList<Vertice>();
		start.marked = true;
		queue.offer(start);
		while(queue.size() != 0) {
			Vertice v = queue.poll();
			if(v.getEdges().size() < 8) {
				Stack<Vertice> s = new Stack<Vertice>();
				while(v != null) {
					s.push(v);
					v = v.getParent();
				}
				return s;
			}
			for(DirectedEdge edge : v.getEdges()) {
				if(!edge.endVertice.isMarked()) {
					Vertice w = edge.endVertice;
					if(checkVertice(w)) {
						w.setMarked(true);
						w.setParent(v);
						queue.offer(w);
					}
				}
			}
		}
		return null;
	}
	
	public static boolean checkVertice(Vertice v) {
		if((v.getType() != ObjectType.Wall) && (v.getType() != ObjectType.Teleport)) {
			for(DirectedEdge e : v.getEdges()) {
				Vertice w = e.getEndVertice();
				if(!((w.getType() != ObjectType.Wall) && (w.getType() != ObjectType.Teleport))) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean checkVertice2(Vertice v) {
		if((v.getType() != ObjectType.Wall) && (v.getType() != ObjectType.Danger)) {
			for(DirectedEdge e : v.getEdges()) {
				Vertice w = e.getEndVertice();
				if((w.getType() == ObjectType.Wall) || (w.getType() == ObjectType.Danger)) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	public static Stack<Vertice> findPath(Vertice start, ObjectType type){
		Queue<Vertice> queue = new LinkedList<Vertice>();
		start.marked = true;
		queue.offer(start);
		while(queue.size() != 0) {
			Vertice v = queue.poll();
			if(v.getType() == type) {
				Stack<Vertice> s = new Stack<Vertice>();
				while(v != null) {
					s.push(v);
					v = v.getParent();
				}
				return s;
			}
			for(DirectedEdge edge : v.getEdges()) {
				if(!edge.endVertice.isMarked()) {
					Vertice w = edge.endVertice;
					if(checkVertice2(w)) {
						w.setMarked(true);
						w.setParent(v);
						queue.offer(w);
					}
				}
			}
		}
		return null;
	}
	
	public static List<Vertice> getReachableVertices(Vertice start){
		ArrayList<Vertice> vertices = new ArrayList<Vertice>();
		
		Queue<Vertice> queue = new LinkedList<Vertice>();
		start.marked = true;
		queue.offer(start);
		while(queue.size() != 0) {
			Vertice v = queue.poll();
			for(DirectedEdge edge : v.getEdges()) {
				if(!edge.endVertice.isMarked()) {
					Vertice w = edge.endVertice;
					if(checkVertice2(w)) {
						w.setMarked(true);
						vertices.add(w);
						queue.offer(w);
					}
				}
			}
		}
		
		return vertices;
	}
	
	public static Stack<Vertice> findPath(Vertice start, Vertice end){
		Queue<Vertice> queue = new LinkedList<Vertice>();
		start.marked = true;
		queue.offer(start);
		while(queue.size() != 0) {
			Vertice v = queue.poll();
			if(v == end) {
				Stack<Vertice> s = new Stack<Vertice>();
				while(v != null) {
					s.push(v);
					v = v.getParent();
				}
				return s;
			}
			for(DirectedEdge edge : v.getEdges()) {
				if(!edge.endVertice.isMarked()) {
					Vertice w = edge.endVertice;
					if(checkVertice2(w)) {
						w.setMarked(true);
						w.setParent(v);
						queue.offer(w);
					}
				}
			}
		}
		return null;
	}
}