import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Masha Kereb on 19-Mar-17.
 */
public class ArrayThread extends Thread {

    private int[] array;
    private int currentSum = 0;

    private Action nexAction;
    private CyclicBarrier barrier;

    final private Random rand = new Random();

    public ArrayThread(CyclicBarrier barrier, int[] array, Action nexAction) {
        this.array = array;
        this.nexAction = nexAction;
        this.barrier = barrier;
    }

    public ArrayThread(CyclicBarrier barrier, int[] array) {
        this.array = array;
        this.barrier = barrier;
        this.nexAction = rand.nextInt(2) == 1 ? Action.DECREMENT : Action.INCREMENT;
    }

    public ArrayThread(CyclicBarrier barrier) {
        this(barrier, null);
        int len = 15; // rand.nextInt(100);
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = rand.nextInt(100);
        }
        this.array = arr;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {

            this.applyAction();
            int sum = this.calculateSum();

            System.out.printf("Applied action - %s, sum - %d\n", this.nexAction, sum);

            this.setCurrentSum(sum);
            try {
                Thread.sleep(100);
                barrier.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        }

    }

    private int calculateSum() {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    public void setNexAction(Action nexAction) {
        this.nexAction = nexAction;
    }

    synchronized private void setCurrentSum(int currentSum) {
        this.currentSum = currentSum;
    }

    synchronized public int getCurrentSum() {
        return currentSum;
    }

    private void applyAction() {
        if (nexAction.equals(Action.DECREMENT)) {
            this.decrement();
        } else if (nexAction.equals(Action.INCREMENT)) {
            this.increment();
        } else {

        }
    }

    private void decrement() {
        int index = rand.nextInt(array.length);
        array[index]--;
    }

    private void increment() {
        int index = rand.nextInt(array.length);
        array[index]++;
    }
}
