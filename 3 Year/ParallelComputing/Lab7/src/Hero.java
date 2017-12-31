import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Hero extends Thread {
    private JLabel label;
    private GamePanel panel;

    private int x;
    private int y;

    private Direction side = Direction.Left;
    private int delta = 10;
    private int panelWidth = 0;
    private int panelHeight = 0;
    private volatile int fireballCount = 0;

    private boolean leftKeyPressed = false;
    private boolean rightKeyPressed = false;

    private final int labelWidth = 170;
    private final int labelHeight = 180;
    private final int maxFireballsCount = 5;
    private final ImageIcon heroRight = new ImageIcon("resources/hunter_right.png");
    private final ImageIcon heroLeft = new ImageIcon("resources/hunter_left.png");

    Hero(MainFrame main, GamePanel panel) {
        this.panelWidth = panel.width;
        this.panelHeight = panel.height;
        this.panel = panel;

        x = panel.width / 2;
        y = panel.height - labelHeight;

        label = new JLabel(heroLeft);
        label.setSize(new Dimension(labelWidth, labelHeight));
        label.setLocation(x, y);
        label.setVisible(true);
        panel.add(label);

        main.addKeyListener(new HeroKeyListener());
    }

    synchronized public void addBullet(){
        fireballCount += 1;
    }
    synchronized public void removeBullet(){
        fireballCount -= 1;
    }
    public enum Direction {Left, Right}
    public class HeroKeyListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            if (panel != null){
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        if(fireballCount < maxFireballsCount) {
                            Fireball fireball;
                            if (side == Direction.Left) {
                                fireball = new Fireball(panel, Hero.this, x, y + 50);
                            }
                            else {
                                fireball = new Fireball(panel, Hero.this, x + labelWidth, y + 50);
                            }
                            fireball.start();
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        leftKeyPressed = true;
                        if (x > 0){
                            if (side != Direction.Left){
                                label.setIcon(heroLeft);
                                side = Direction.Left;
                            }
                        }
                        break;

                    case KeyEvent.VK_RIGHT:
                        rightKeyPressed = true;
                        if (x < panelWidth - labelWidth){
                            if (side != Direction.Right){
                                label.setIcon(heroRight);
                                side = Direction.Right;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                rightKeyPressed = false;
            else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                leftKeyPressed = false;
        }
        @Override
        public void keyTyped(KeyEvent e) {}
    }


    @Override
    public void run() {
        while (!isInterrupted()) {
            if (leftKeyPressed && x - delta >= 0)
                x -= delta;
            else if (rightKeyPressed && x + delta + labelWidth <= panelWidth)
                x += delta;

            label.setLocation(x, y);
            try {
                sleep(20);
            } catch (InterruptedException e) { break; }
        }
        panel.remove(label);
    }


}
