package Group9.tree;

import Group9.math.Vector2;

import java.util.*;

public class Node<T extends PointContainer> {

    private final short NW = 0, NE = 1, SW = 2, SE = 3;

    private List<T> content = new ArrayList<>();
    private Node<T>[] children;

    private final Vector2 center;
    private final double width, height;
    private final int maxSize;


    public Node(Vector2 center, double width, double height, int maxSize)
    {
        this.center = center;
        this.width = width;
        this.height = height;
        this.maxSize = maxSize;
    }

    private boolean hasChildren()
    {
        return this.children != null;
    }

    public void add(T container)
    {
        // if |content| + 1 >= maxSize
        //  --> split()

        if(this.hasChildren())
        {
            this.children[getIndexBasedOnCenter(container)].add(container);
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
        this.children[NE] = new Node<>(center.add(w, h), w, h, maxSize);
        this.children[SE] = new Node<>(center.add(w, -h), w, h, maxSize);
        this.children[SW] = new Node<>(center.add(-w, -h), w, h, maxSize);
        this.children[NW] = new Node<>(center.add(-w, h), w, h, maxSize);

        for(T c : this.content)
        {
            for(short index : divide(c))
            {
                this.children[index].add(c);
            }
        }

        this.children[getIndexBasedOnCenter(container)].add(container);

        this.content = null;
    }

    private Short[] divide(T container)
    {
        Vector2 relative = container.getCenter().sub(center);
        Set<Short> subcontainers = new HashSet<>();

        if(container instanceof PointContainer.Rectangle)
        {
            PointContainer.Quadrilateral rec = (PointContainer.Quadrilateral) container;
            /*Vector2 ne = new Vector2(relative.getX() + rec.getHeight(), relative.getY() + relative.getY());
            Vector2 se = new Vector2(relative.getX() + rec.getHeight(), relative.getY() - relative.getY());
            Vector2 sw = new Vector2(relative.getX() - rec.getHeight(), relative.getY() - relative.getY());
            Vector2 nw = new Vector2(relative.getX() - rec.getHeight(), relative.getY() + relative.getY());

            subcontainers.add(getIndexBasedOnCenter(ne));
            subcontainers.add(getIndexBasedOnCenter(se));
            subcontainers.add(getIndexBasedOnCenter(sw));
            subcontainers.add(getIndexBasedOnCenter(nw));*/

        }
        else if(container instanceof PointContainer.Circle)
        {
            PointContainer.Circle cir = (PointContainer.Circle) container;


        }
        else
        {
            throw new IllegalStateException("Container type not implemented.");
        }

        if (subcontainers.isEmpty()) {
            throw new IllegalStateException("Failed to locate at least one container for the circle.");
        }


        return subcontainers.toArray(new Short[subcontainers.size()]);
    }

    private short getIndexBasedOnCenter(PointContainer container)
    {
        return this.getIndexBasedOnCenter(container.getCenter());
    }

    private short getIndexBasedOnCenter(Vector2 center)
    {
        Vector2 relative = center.sub(this.center);

        if(relative.getX() > 0)
        {
            if(relative.getY() > 0)
            {
                return NE;
            }
            else
            {
                return SE;
            }
        }
        else
        {
            if(relative.getY() > 0)
            {
                return NW;
            }
            else
            {
                return SW;
            }
        }
    }

}
