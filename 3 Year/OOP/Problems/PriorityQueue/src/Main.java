

import javafx.util.Pair;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Ігор on 13.11.16.
 */

public class Main {

    public  void main(String[] args) throws InterruptedException {
        testQueue();
    }

    @Test
    public  void testQueue() throws InterruptedException{
        PriorityQueue q = new PriorityQueue(120000);
        final int n = 50;
        Runnable producer1 = () -> {
            for (int i = 0; i < n; i++) {
                Pair<Integer, Integer> p = new Pair<>(i, n - i);
                System.out.println("Producer " + ThreadId.get() + " puts " + p);
                q.put(p);
            }
        };
        Runnable producer2 = () -> {
            for (int i = 1; i < 2*n; i++) {
                Pair<Integer, Integer> p = new Pair<>( 2*n - i, i);
                System.out.println("Producer " + ThreadId.get() + " puts " + p);
                q.put(p);
            }
        };

        Runnable consumer = () -> {
            int pr = q.pop().getKey();
            for (int i = 1; i < 3*n-1; i++) {
                Pair<Integer, Integer> val = q.pop();
                System.out.println("Consumer " + ThreadId.get() + " pops " + val);

                assertTrue(pr >= val.getKey());
                pr = val.getKey();
                Thread.yield();
            }
        };
        Thread prod1 = new Thread(producer1);
        Thread prod2 = new Thread(producer2);
        Thread cons1 = new Thread(consumer);
        prod1.start();
        prod2.start();
        prod1.join();
        prod2.join();
        cons1.start();

        cons1.join();

    }
}
