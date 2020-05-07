package Group9.math.graph;

public class Vertex<T> {

    private final T content;

    public Vertex(T content)
    {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "content=" + content +
                '}';
    }
}
