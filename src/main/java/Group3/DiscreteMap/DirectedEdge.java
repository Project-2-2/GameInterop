package Group3.DiscreteMap;

public class DirectedEdge {
	Vertice startVertice;
	Vertice endVertice;
	int degrees;
	public DirectedEdge(Vertice startVertice, Vertice endVertice, int degrees) {
		this.startVertice = startVertice;
		this.endVertice = endVertice;
		this.degrees = degrees;
	}
	public int getDegrees() {
		return degrees;
	}
	public Vertice getStartVertice() {
		return startVertice;
	}
	public void setStartVertice(Vertice startVertice) {
		this.startVertice = startVertice;
	}
	public Vertice getEndVertice() {
		return endVertice;
	}
	public void setEndVertice(Vertice endVertice) {
		this.endVertice = endVertice;
	}
}
