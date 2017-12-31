package common;

import java.awt.*;
import java.util.ArrayList;


public class Computations {
    public static boolean isOnTheLeftSide(Point vectorA, Point vectorB) {
        int res = vectorA.x * vectorB.y - vectorA.y * vectorB.x;
        return res > 0;
    }

    public static boolean isBetween(int value, int from, int to) {
        return ((from <= value) && (value < to)) || ((to <= value) && (value < from));
    }

    public static boolean isPointInsideRectangle(Point point, int x, int y, int width, int height) {
        return isBetween(point.x, x, x + width) && isBetween(point.y, y, y + height);
    }

    public static ArrayList<Point> sortByAxis(ArrayList<Point> points) {
        ArrayList<Point> result = clone(points);
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                if (result.get(i).x > result.get(j).x) {
                    Point temp = result.get(i);
                    result.set(i, result.get(j));
                    result.set(j, temp);
                }
            }
        }
        return result;
    }

    public static ArrayList<Point> clone(ArrayList<Point> points) {
        ArrayList<Point> result = new ArrayList<>();
        for (Point point :
                points) {
            result.add(point);
        }
        return result;
    }

    public static Point findTheHighestPoint(ArrayList<Point> points) {
        Point highest = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).y < highest.y) {
                highest = points.get(i);
            }
        }
        return highest;
    }

    public static Edge findBaseLine(ArrayList<Point> fromArray, ArrayList<Point> toArray) {
        Edge baseLine = createEdge(findTheHighestPoint(fromArray), findTheHighestPoint(toArray));

        for (;;) {
            ArrayList<Point> pointsOnTheRightSideInFromArray = findPointsOnTheRightSide(baseLine, fromArray);
            ArrayList<Point> pointsOnTheRightSideInToArray = findPointsOnTheRightSide(baseLine, toArray);
            if (!pointsOnTheRightSideInFromArray.isEmpty()) {
                baseLine.setFrom(pointsOnTheRightSideInFromArray.get(0));
            }
            if (!pointsOnTheRightSideInToArray.isEmpty()) {
                baseLine.setTo(pointsOnTheRightSideInToArray.get(0));
            }
            if (pointsOnTheRightSideInFromArray.isEmpty() && pointsOnTheRightSideInToArray.isEmpty()) {
                break;
            }
        }
        return baseLine;
    }

    private static ArrayList<Point> findPointsOnTheRightSide(Edge baseLine, ArrayList<Point> array) {
        ArrayList<Point> pointsOnTheRightSide = new ArrayList<>();
        for (Point point : array) {
            if ((baseLine.getFrom() != point) && (baseLine.getTo() != point) && !isOnTheLeftSide (
                    createVector(baseLine.getFrom(), point), createVector(baseLine.getFrom(), baseLine.getTo()))) {
                pointsOnTheRightSide.add(point);
            }
        }
        return pointsOnTheRightSide;
    }

    private static Point createVector(Point from, Point to) {
        return new Point(to.x - from.x, from.y - to.y);
    }

    private static Edge createEdge(Point from, Point to) {
        Edge edge = new Edge();
        edge.setFrom(from);
        edge.setTo(to);
        return edge;
    }

    public static void removeAllPointsOnRightSide(ArrayList<Point> points, Point splittingPoint) {
        int splittingPointIndex = points.indexOf(splittingPoint);
        for (int i = splittingPointIndex + 1; i < points.size(); i++) {
            points.remove(points.size() - 1);
        }
    }

    public static void removeAllPointsOnLeftSide(ArrayList<Point> points, Point splittingPoint) {
        int splittingPointIndex = points.indexOf(splittingPoint);
        for (int i = 0; i < splittingPointIndex; i++) {
            points.remove(0);
        }
    }

    public static ArrayList<Point> merge(ArrayList<Point> first, ArrayList<Point> second) {
        ArrayList<Point> result = new ArrayList<>(first);
        for (Point aSecond : second) {
            result.add(aSecond);
        }
        return result;
    }

    public static void print(ArrayList<Point> points) {
        for (Point point : points) {
            System.out.print(point + "\t");
        }
        System.out.println();
    }
}
