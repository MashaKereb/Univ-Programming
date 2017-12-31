import java.util.Random;

/**
 * Created by Masha Kereb on 05-Dec-16.
 */
class Phaser {
    private int phase;
    private int parties;
    private int arrived;
    private int unarrived;
    private boolean terminated;

    public Phaser(int parties) {
        this.parties = parties;
        this.arrived = 0;
        this.unarrived = parties;
        this.phase = 0;
        terminated = false;
    }

    public synchronized void register() {
        parties++;
    }

    private synchronized boolean putDown() {
        if(unarrived == 0) {
            arrived = 0;
            unarrived = parties;
            phase++;
            notifyAll();
            System.out.println("Barrier triggered. Phase: " + phase);
            return true;
        }
        return false;
    }

    public synchronized void arrive(){
        arrived++;
        unarrived--;
        putDown();
    }

    public synchronized void arriveAndAwaitAdvance() throws InterruptedException {
        if(terminated) return;
        arrived++;
        unarrived--;
        if(!putDown()) {
            this.wait();
        }
    }

    public synchronized void arriveAndDeregister() {
        if(terminated) return;
        parties--;
        unarrived--;
        putDown();
        if(parties == 0) {
            terminated = true;
        }
    }

    public boolean isTerminated() {
        return terminated;
    }

    public int getParties() {
        return parties;
    }

    public int getArrived() {
        return arrived;
    }

    public int getUnarrived() {
        return unarrived;
    }

    public int getPhase() {
        return phase;
    }
}

public class MainPhaser {

    public static Runnable task(int value, Phaser phaser){
        return new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                try {
                    int count = value;
                    for (int i = 1; i < 101; i++) {
                        count++;
                        Thread.sleep(r.nextInt(100));
                        System.out.println(Thread.currentThread().getName() + " Value: " + count);
                        if(i*value > 60){
                            phaser.arriveAndDeregister();
                            break;
                        } else {

                            phaser.arriveAndAwaitAdvance();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void main(String[] args) {
        Phaser phaser = new Phaser(3);
        Runnable task1 = task(5, phaser);
        Runnable task2 = task(7, phaser);
        Runnable task3 = task(3, phaser);

        new Thread(task1, "Thread1").start();
        new Thread(task2, "Thread2").start();
        new Thread(task3, "Thread3").start();
    }
}

