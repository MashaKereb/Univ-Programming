import PointFindingTree.PointFindingTree;
import common.Computations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Drawling and mouse handling implementations
 * Created by anastasia on 4/12/17.
 */
@SuppressWarnings("ALL")
public class DrawArea extends JComponent{
    //.............................................VARIABLES..........................................................//

    private ArrayList<Point> points;
    private ArrayList<Point> convexHull;
    private PointFindingTree tree;

    private Image image;
    private Graphics2D graphics2D;

    private final int PAINT_RADIUS = 10;

    private enum Mode {
            ADD_POINT, REMOVE_POINT
    }
    private Mode drawingMode;

    //.............................................PUBLIC..METHODS....................................................//

    DrawArea() {
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (drawingMode == Mode.ADD_POINT) {
                   addPoint(e.getPoint());
                } else {
                    removePoint(e.getPoint());
                }
            }
        });
    }

    void setAddingPointsMode() {
        drawingMode = Mode.ADD_POINT;
    }

    void setRemovingPointsMode() {
        drawingMode = Mode.REMOVE_POINT;
    }

    void clearData() {
        clear();
        initialize();
    }

    void createConvexHull() {
        if (points.isEmpty()) {
            System.out.println("There are no points");
            return;
        }

        if (points.size() < 4) {
            convexHull = points;
            drawConvexHull();
            return;
        }

        buildTree(points);
        convexHull = tree.getConvexHull();
        clear();
        tree.drawTree(graphics2D);
        drawConvexHull();
    }

    //.............................................PRIVATE..METHODS...................................................//

    @Override
    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clearData();
        }
        g.drawImage(image, 0, 0, null);
    }

    private void initialize() {
        points = new ArrayList<>();
        graphics2D.setPaint(Color.black);
        drawingMode = Mode.ADD_POINT;
        tree = new PointFindingTree();
    }

    private void drawPoint(Point e) {
        graphics2D.fillOval(e.x - PAINT_RADIUS / 2, e.y - PAINT_RADIUS / 2, PAINT_RADIUS, PAINT_RADIUS);
        repaint();
    }

    private void drawPoint(Integer i) {
        graphics2D.fillOval(points.get(i).x - PAINT_RADIUS / 2, points.get(i).y - PAINT_RADIUS / 2, PAINT_RADIUS, PAINT_RADIUS);
        graphics2D.drawString(Integer.toString(i), points.get(i).x, points.get(i).y);
        repaint();
    }

    private void drawLine(Point from, Point to) {
        graphics2D.drawLine(from.x, from.y, to.x, to.y);
        repaint();
    }

    private void drawPoints() {
        for (int i = 0; i < points.size(); i++) {
            drawPoint(i);
        }
    }

    private void drawConvexHull() {
        drawPoints();
        graphics2D.setPaint(Color.GREEN);
        for (int i = 0; i < convexHull.size() - 1; i++) {
            drawPoint(convexHull.get(i));
            drawLine(convexHull.get(i), convexHull.get((i + 1) % convexHull.size()));
        }
        drawPoint(convexHull.get(convexHull.size() - 1));
        graphics2D.setPaint(Color.BLACK);
        repaint();
    }

    private void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        graphics2D.setPaint(Color.BLACK);
        repaint();
    }

    private void addPoint(Point e) {
        for (int i = 0; i < points.size(); i++) {
            Point current = points.get(i);
            if (Computations.isPointInsideRectangle(e, current.x - PAINT_RADIUS, current.y - PAINT_RADIUS,
                    2 * PAINT_RADIUS, 2 * PAINT_RADIUS)) {
                return;
            }
        }
        points.add(e);
        drawPoint(points.indexOf(e));
    }

    private void removePoint(Point e) {
        for (int i = 0; i < points.size(); i++) {
            Point current = points.get(i);
            if (Computations.isPointInsideRectangle(e, current.x - PAINT_RADIUS, current.y - PAINT_RADIUS,
                    2 * PAINT_RADIUS, 2 * PAINT_RADIUS)) {
                points.remove(i);
                break;
            }
        }
        clear();
        drawPoints();
    }

    private void buildTree(ArrayList<Point> points) {
        tree = new PointFindingTree();
        tree.build(points);
    }
}
