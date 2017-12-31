import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class SimulationPanel extends JPanel {

    private ReentrantReadWriteLock lock;
    private CyclicBarrier barrier;
    private UpdateThread updater;


    private volatile FieldManager field;
    private int cellSize = 8;
    private int cellGap = 1;

    private int civAmount = 0;

    private int linesPerWorker = 4;
    private int workersNumber = 4;
    private Thread[] workers;
    private final Color[] Colors = {
            Color.DARK_GRAY, Color.CYAN, Color.GREEN,
            Color.RED, Color.YELLOW, Color.BLUE};

    public SimulationPanel(int width, int height, int cSize) {
        setBackground(Color.WHITE);
        cellSize = cSize;
        lock = new ReentrantReadWriteLock();
        field = new FieldManager(width, height);
    }

    void init(int civAmount, float density) {
        this.civAmount = civAmount;
        workers = null;
        updater = new UpdateThread(this, field, lock);
        barrier = new CyclicBarrier(workersNumber, updater);
        field.clear();
        field.generate(civAmount, density);
    }

    public void startSimulation() {

        int quarterSize = field.getHeight() / linesPerWorker;
        workers = new SimulationThread[workersNumber];
        for (int i = 0; i < workersNumber; i++) {
            workers[i] = new SimulationThread(field, barrier, lock, civAmount,
                    quarterSize * i, quarterSize * (i + 1));
        }
        for (int i = 0; i < workersNumber; i++) workers[i].start();
    }


    void stopSimulation() {

        if (workers != null)
            for (int i = 0; i < workers.length; i++) {
                workers[i].interrupt();
            }
        workers = null;

    }

    @Override
    protected void paintComponent(Graphics g) {
        if (field != null) {
            lock.readLock().lock();
            super.paintComponent(g);

            Insets b = getInsets();
            int height = field.getHeight();
            int width = field.getHeight();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    g.setColor(Colors[field.getCell(x, y).getValue()]);
                    g.fillRect(b.left + cellGap + x * (cellSize + cellGap),
                            b.top + cellGap + y * (cellSize + cellGap),
                            cellSize,
                            cellSize);
                }
            }
            lock.readLock().unlock();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (field != null) {
            Insets b = getInsets();
            return new Dimension((cellSize + cellGap) * field.getWidth() + cellGap + b.left + b.right,
                    (cellSize + cellGap) * field.getHeight() + cellGap + b.top + b.bottom + 30);
        } else
            return new Dimension(300, 300);
    }
}
