import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    int width;
    int height;

    private ImageIcon background = new ImageIcon("resources/sky.jpg");
    private int maxAlienPlanesCount = 5;
    ArrayList<AlienPlane> alienPlanes = new ArrayList<>();
    private Hero hero = null;
    private MainFrame main = null;

    public GamePanel(MainFrame main, int width, int height) {
        setBackground(Color.WHITE);
        this.main = main;

        this.width = width;
        this.height = height;

        setLayout(null);
        setSize(width, height);

        addMouseListener(new MouseClickManager());

        GameThread game = new GameThread();
        game.start();
    }

    class GameThread extends Thread {
        @Override
        public void run() {
            if (hero == null) {
                hero = new Hero(main, GamePanel.this);
                hero.start();
            }
            while (!isInterrupted()) {
                if (alienPlanes.size() < maxAlienPlanesCount) {

                    AlienPlane alienPlane = new AlienPlane(width, height, GamePanel.this);
                    alienPlanes.add(alienPlane);
                    alienPlane.start();
                }
                try {
                    sleep(200);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    class MouseClickManager extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            for (AlienPlane alienPlane : alienPlanes) {
                if (alienPlane.checkIfInside(e.getX(), e.getY()))
                    alienPlane.interrupt();
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, width, height, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(width, height);
    }
}
