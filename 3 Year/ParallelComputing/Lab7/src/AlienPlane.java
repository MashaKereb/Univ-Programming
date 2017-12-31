import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AlienPlane extends Thread {
    private int x;
    private int y;
    private int deltaX;
    private int deltaY;
    private int skyWidth;
    private int skyHeight;
    private int heroSize = 250;

    private JLabel plane;
    private GamePanel panel;

    final private Random random = new Random();

    final int labelWidth = 140;
    final int labelHeight = 90;
    final ReentrantReadWriteLock rwlockX = new ReentrantReadWriteLock(true);
    final ReentrantReadWriteLock rwlockY = new ReentrantReadWriteLock(true);

    AlienPlane(int width, int height, GamePanel panel) {
        super();
        this.skyWidth = width;
        this.skyHeight = height - heroSize;
        this.panel = panel;


        plane = new JLabel(new ImageIcon("resources/plane.png"));
        plane.setSize(new Dimension(labelWidth, labelHeight));
        int type = random.nextInt(4);
        if (type == 0) {
            x = -labelWidth;
            y = Math.abs(random.nextInt()) % skyHeight;
        } else if (type == 1) {
            y = -labelHeight;
            x = Math.abs(random.nextInt()) % skyWidth;
        } else if (type == 2) {
            y = Math.abs(random.nextInt()) % skyHeight;
            x = width + labelWidth;
        } else {
            y = height - heroSize;
            x = Math.abs(random.nextInt()) % skyWidth;
        }

        if (y > height / 2) deltaY = -Math.abs(random.nextInt(4)) - 1;
        else deltaY = Math.abs(random.nextInt(4)) + 1;
        if (x > width / 2) deltaX = -Math.abs(random.nextInt(4)) - 1;
        else deltaX = Math.abs(random.nextInt(4)) + 1;
    }

    void changeDirectionX() {
        deltaX = -deltaX;
    }

    void changeDirectionY() {
        deltaY = -deltaY;
    }

    int getX() {
        int n_x;
        rwlockX.readLock().lock();
        n_x = x;
        rwlockX.readLock().unlock();
        return n_x;
    }

    int getY() {
        int n_y;
        rwlockY.readLock().lock();
        n_y = y;
        rwlockY.readLock().unlock();
        return n_y;
    }

    void setX(int x) {
        rwlockX.writeLock().lock();
        this.x = x;
        rwlockX.writeLock().unlock();
    }

    void setY(int y) {
        rwlockY.writeLock().lock();
        this.y = y;
        rwlockY.writeLock().unlock();
    }

    boolean checkIfInside(int x, int y) {
        int planeX = getX();
        int planeY = getY();
        return (x <= planeX + labelWidth && x >= planeX && y >= planeY && y <= planeY + labelHeight);
    }

    @Override
    public void run() {
        panel.add(plane);
        boolean isAlive = true;
        while (!isInterrupted() && isAlive) {
            int nextX = x + deltaX;
            int nextY = y + deltaY;

            setX(nextX);
            setY(nextY);

            if (deltaX > 0 && nextX > skyWidth - labelWidth / 2 || deltaX < 0 && nextX < -labelWidth / 2)
                if (random.nextInt(10) % 5 == 0)
                    isAlive = false;
                else changeDirectionX();

            if (deltaY > 0 && nextY > skyHeight - labelHeight / 2 || deltaY < 0 && nextY < -labelHeight / 2)
                if (random.nextInt(10) % 5 == 0)
                    isAlive = false;
                else changeDirectionY();

            plane.setLocation(nextX, nextY);
            try {
                sleep(Math.abs(random.nextInt(5)) + 20);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
        panel.remove(plane);
        panel.repaint();
        panel.alienPlanes.remove(this);
    }
}
