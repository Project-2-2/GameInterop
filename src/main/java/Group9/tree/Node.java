package Group9.tree;

import Group9.math.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Node<T> {

    private final short NW = 0, NE = 1, SW = 2, SE = 3;

    private List<Content<T>> content = new ArrayList<>();
    private Node<T>[] children;

    private final Vector2 center;
    private final double width, height;
    private final int maxSize;
    private final int maxDepth;

    private PointContainer.Polygon quadrilateral;

    public Node(final Vector2 center, double width, double height, int maxSize, final int maxDepth)
    {
        this.center = center;
        this.width = width;
        this.height = height;
        this.maxSize = maxSize;
        this.maxDepth = maxDepth;

        this.quadrilateral = new PointContainer.Polygon(
                center.add(width / 2, height / 2),
                center.add(width / 2, -height / 2),
                center.add(-width / 2, -height / 2),
                center.add(-width / 2, height / 2)
        );
    }

    private boolean hasChildren()
    {
        return this.children != null;
    }

    public void add(Content<T> container, int depth)
    {
        if(this.hasChildren())
        {
            for(short index : divide(container.getContainer(), this.children))
            {
                this.children[index].add(container, depth + 1);
            }
        }
        else if(this.content.size() + 1 <= maxSize || this.maxDepth == depth)
        {
            this.content.add(container);
        }
        else
        {
            this.split(container, depth);
        }

    }

    private void split(Content<T> container, int depth)
    {
        this.children = new Node[4];
        final double w = width / 2D;
        final double h = height / 2D;
        this.children[NE] = new Node<>(center.add(w, h), width, height, maxSize, maxDepth);
        this.children[SE] = new Node<>(center.add(w, -h), width, height, maxSize, maxDepth);
        this.children[SW] = new Node<>(center.add(-w, -h), width, height, maxSize, maxDepth);
        this.children[NW] = new Node<>(center.add(-w, h), width, height, maxSize, maxDepth);

        this.content.add(container);
        for(Content<T> c : this.content)
        {
            for(short index : divide(c.getContainer(), this.children))
            {
                this.children[index].add(c, depth + 1);
            }
        }
        this.content = null;

    }

    private Set<Short> divide(PointContainer container, Node<T>[] c)
    {
        Set<Short> divisions = new HashSet<>();

        if(PointContainer.intersect(container, c[NE].quadrilateral))
        {
            divisions.add(NE);
        }

        if(PointContainer.intersect(container, c[SE].quadrilateral))
        {
            divisions.add(SE);
        }

        if(PointContainer.intersect(container, c[SW].quadrilateral))
        {
            divisions.add(SW);
        }

        if(PointContainer.intersect(container, c[NW].quadrilateral))
        {
            divisions.add(NE);
        }

        return divisions;
    }

    static class Content<T> {

        private T content;
        private PointContainer container;

        public Content(T content, PointContainer container)
        {
            this.content = content;
            this.container = container;
        }

        public T getContent() {
            return content;
        }

        public PointContainer getContainer() {
            return container;
        }

    }

}
