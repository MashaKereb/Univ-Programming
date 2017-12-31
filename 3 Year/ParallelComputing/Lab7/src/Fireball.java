import javax.swing.*;
import java.awt.*;


public class Fireball extends Thread {
    private int x;
    private int y;


    private GamePanel panel;
    private Hero hero;

    private final int delta = 5;
    private final int labelWidth = 60;
    private final int labelHeight = 60;

    private final JLabel label = new JLabel(new ImageIcon("resources/fireball.png"));

    Fireball(GamePanel panel, Hero hero, int x, int y){
        this.panel = panel;
        this.hero = hero;
        this.x = x;
        this.y = y;
        label.setSize(new Dimension(labelWidth, labelHeight));
        label.setLocation(x - labelWidth/2,y - labelHeight/2);
    }

    @Override
    public void run() {
        hero.addBullet();
        panel.add(label);

        while (!isInterrupted()){
            if (y < 0) break;
            y -= delta;
            label.setLocation(x - labelWidth/2,y - labelHeight/2);
            for (AlienPlane alienPlane : panel.alienPlanes){
                if (alienPlane.checkIfInside(x, y)){
                    alienPlane.interrupt();
                    this.interrupt();
                    break;
                }
            }
            try {
                sleep(10);
            } catch (InterruptedException e) { break; }
        }
        panel.remove(label);
        panel.repaint();
        hero.removeBullet();
    }
}
