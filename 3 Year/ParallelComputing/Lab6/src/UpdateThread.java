import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Andrey on 03/26/2017.
 */
public class UpdateThread implements Runnable {
    public SimulationPanel simulationPanel;
    public volatile FieldManager fieldManager;
    public ReentrantReadWriteLock lock;


    public UpdateThread(SimulationPanel simulationPanel,
                        FieldManager fieldManager,
                        ReentrantReadWriteLock lock){
        this.simulationPanel = simulationPanel;
        this.fieldManager = fieldManager;
        this.lock = lock;
    }

    @Override
    public void run() {

        lock.writeLock().lock();
        fieldManager.swapFields();
        lock.writeLock().unlock();

        simulationPanel.repaint();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
