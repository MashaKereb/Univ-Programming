/**
 * Created by Masha Kereb on 18-Dec-16.
 */
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestJunit {
    @Test
    public void test1() throws InterruptedException {
        Score cc = new Score(0);
        System.out.println(cc);

        Thread increment = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                cc.addValue(i);
            }
        });
        Thread decrement = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                cc.subValue(i);
            }
        });

        increment.start();
        decrement.start();

        increment.join();
        decrement.join();

        assertEquals(0, cc.getResult(), 0.0001);
    }

    @Test
    public void test2() throws InterruptedException {
        Score cc = new Score(10000);
        System.out.println(cc);

        Thread increment = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                cc.addValue(i);
            }
        });
        Thread decrement = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                cc.subValue(i);
            }
        });

        increment.start();
        decrement.start();

        increment.join();
        decrement.join();

       assertEquals(10000, cc.getResult(), 0.0001);
    }
}
