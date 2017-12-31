/**
 * Created by Masha Kereb on 05-Dec-16.
 */


import java.util.concurrent.locks.Lock;

public class lock {

    public static void main(String[] args) throws InterruptedException {
        Score cc = new Score(50000);
        System.out.println(cc);

        Thread subThread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                cc.subValue(100);
                System.out.println(cc);
            }
        });
        Thread addThread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                cc.addValue(100);
                System.out.println(cc);
            }
        });

        subThread.start();
        addThread.start();

        subThread.join();
        addThread.join();

        System.out.println(cc);
    }
}


