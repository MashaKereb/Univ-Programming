package PointFindingTree;

import java.awt.*;
import java.util.ArrayList;


class PointFindingNode {
    private ArrayList<Point> bHull;
    private ArrayList<Point> pointsRemoved;
    private int height;
    private int id;
    private PointFindingNode parent;
    private PointFindingNode leftChild;
    private PointFindingNode rightChild;

    PointFindingNode(PointFindingNode parent) {
        this.parent = parent;
        if (parent != null)
            this.height = parent.height + 1;
        else
            this.height = 0;
        leftChild = null;
        rightChild = null;
        bHull = new ArrayList<>();
        pointsRemoved = new ArrayList<>();
    }

    PointFindingNode getLeftChild() {
        return leftChild;
    }

    PointFindingNode getRightChild() {
        return rightChild;
    }

    PointFindingNode getParent() {
        return parent;
    }

    public PointFindingNode getGrandparent() {
        if (parent == null)
            return null;
        return parent.getParent();
    }

    boolean isLeaf() {
        return (leftChild == null) && (rightChild == null);
    }

    void setLeftChild(PointFindingNode node) {
        leftChild = node;
    }

    void setRightChild(PointFindingNode node) {
        rightChild = node;
    }

    ArrayList<Point> getbHull() {
        return bHull;
    }

    void setbHull(ArrayList<Point> points) {
        bHull = points;
    }

    public ArrayList<Point> getPointsRemoved() {
        return pointsRemoved;
    }

    public void setPointsRemoved(ArrayList<Point> pointsRemoved) {
        this.pointsRemoved = pointsRemoved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
