package Group6.Graph;

public class Edge {

    private Node node1;
    private Node node2;

    public Edge(Node n1, Node n2) {
        node1 = n1;
        node2 = n2;
    }

    public Node[] getNodes() {
        Node[] nodes = {node1, node2};
        return nodes;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }
}