package Group9.tree;

import Group9.math.Vector2;

import java.util.*;

public class Node<T extends Container> {

    private final short NW = 0, NE = 1, SW = 2, SE = 3;

    private List<T> content = new ArrayList<>();
    private Node<T>[] children;

    private final Vector2 center;
    private final double width, height;
    private final int maxSize;

    private PointContainer.Quadrilateral quadrilateral;


    public Node(Vector2 center, double width, double height, int maxSize)
    {
        this.center = center;
        this.width = width;
        this.height = height;
        this.maxSize = maxSize;


        this.quadrilateral = new PointContainer.Quadrilateral(
                center.add(width / 2, height / 2),
                center.add(width / 2, -height / 2),
                center.add(-width / 2, -height / 2),
                center.add(-width / 2, height / 2)
        );
        System.out.println(PointContainer.intersect(quadrilateral, new PointContainer.Circle(new Vector2(10, 10), 10)));
        System.out.println();
    }

    private boolean hasChildren()
    {
        return this.children != null;
    }

    public void add(T container)
    {
        if(this.hasChildren())
        {
            for(short index : divide(container.getContainer(), this.children))
            {
                this.children[index].add(container);
            }
        }
        else if(this.content.size() + 1 <= maxSize)
        {
            this.content.add(container);
        }
        else
        {
            this.split(container);
        }

    }

    private void split(T container)
    {
        this.children = new Node[4];
        final double w = width / 2D;
        final double h = height / 2D;
        this.children[NE] = new Node<>(center.add(w, h), width, height, maxSize);
        this.children[SE] = new Node<>(center.add(w, -h), width, height, maxSize);
        this.children[SW] = new Node<>(center.add(-w, -h), width, height, maxSize);
        this.children[NW] = new Node<>(center.add(-w, h), width, height, maxSize);

        for(T c : this.content)
        {
            for(short index : divide(c.getContainer(), this.children))
            {
                this.children[index].add(c);
            }
        }
        this.content = null;

        this.add(container);

    }

    private Set<Short> divide(PointContainer container, Node[] c)
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

}
