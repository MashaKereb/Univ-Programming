import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SimulationThread extends Thread {
    volatile FieldManager fieldManager;

    int generationsNumber;
    int start;
    int finish;

    CyclicBarrier barrier;
    ReentrantReadWriteLock lock;

    SimulationThread(FieldManager fieldManager,
                     CyclicBarrier barrier,
                     ReentrantReadWriteLock lock,
                     int generationsNumber,
                     int start,
                     int finish) {

        this.barrier = barrier;
        this.lock = lock;

        this.fieldManager = fieldManager;
        this.generationsNumber = generationsNumber;

        this.start = start;
        this.finish = finish;

    }

    @Override
    public void run() {
        while (true) {
            if (this.isInterrupted()) break;
            lock.readLock().lock();

            fieldManager.simulate(generationsNumber, start, finish);

            lock.readLock().unlock();
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                currentThread().interrupt();
            }
        }
    }
}
