package Group9.tree;

import Group9.math.Vector2;

public class QuadTree<T> {

    private final Node<T> root;
    private final Vector2 translateToTreeOrigin;
    private final Vector2 translateToRealOrigin;

    private TransferFunction<T> transferFunction;

    public QuadTree(int width, int height, int maxDepth, TransferFunction<T> transferFunction)
    {
        this.translateToTreeOrigin = new Vector2(-width / 2D, -height / 2D);
        this.translateToRealOrigin = this.translateToTreeOrigin.mul(-1, -1);

        this.root = new Node<>(new Vector2(0, 0), width, height, 5, maxDepth);
        this.transferFunction = transferFunction;
    }

    public void add(T value)
    {
        PointContainer pointContainer = null;
        try {
            pointContainer = transferFunction.transfer(value).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        pointContainer.translate(this.translateToTreeOrigin);
        this.root.add(new Node.Content<>(value, pointContainer), 1);
    }

    public interface TransferFunction<T> {

        PointContainer transfer(T o);

    }

}
