/**
 * Created by Masha Kereb on 20-Dec-16.
 */
import java.time.Duration;
import java.time.Instant;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.locks.Lock;

import static org.junit.Assert.assertEquals;

public class JunitTest {

    @Test
    public void test1() throws InterruptedException {
        ValueWrapper val = new ValueWrapper(0);
        int ThreadNum = 20;
        Thread[] t = new TestThread[ThreadNum];

        for (int i = 0; i < ThreadNum; i++) {
            t[i] = new TestThread(val);
            t[i].start();
        }

        for (int i = 0; i < ThreadNum; i++) {

            t[i].join();
        }
        assertEquals(ThreadNum, val.getValue());
        System.out.println("---------------------------------------------");
    }

    @Test
    public void test2() throws InterruptedException {
        ValueWrapper test1 = new ValueWrapper(0);
        ValueWrapper test2 = new ValueWrapper(0);

        ValueWrapper val = new ValueWrapper(0);

        int ThreadNum = 20;
        Thread[] t = new Thread[ThreadNum];

        for (int i = 0; i < ThreadNum; i++) {
            if(i == 5){
                t[i] = new TestValThread(val, test1);
                t[i].start();
            } else if(i == 10){
                t[i] = new TestValThread(val, test2);
                t[i].start();
            }
            else {
                t[i] = new TestThread(val);
                t[i].start();
            }
        }

        for (int i = 0; i < ThreadNum; i++) {
            t[i].join();
        }
        assertEquals(ThreadNum, val.getValue());
        assertEquals(6, test1.getValue());
        assertEquals(11, test2.getValue());

        System.out.println("---------------------------------------------");
    }
    @Test
    public void timeTest(){
        for (int ThreadNum=2; ThreadNum < 52; ThreadNum+=5){
            ValueWrapper val = new ValueWrapper(0);
            ValueWrapper timeval = new ValueWrapper(0);
            Thread[] t = new TestTimeThread[ThreadNum];

            for (int i = 0; i < ThreadNum; i++) {
                t[i] = new TestTimeThread(val, timeval);
                t[i].start();
            }

            for (int i = 0; i < ThreadNum; i++) {
                try {
                    t[i].join();
                } catch (InterruptedException e){

                }
            }
            // в середньому на потік
            System.out.println("Time taken: "+ timeval.getValue()/ThreadNum +" milliseconds ("+ThreadNum+" threads)");

            // З розрахунку, що на кожен потік витрачаеться час для очікування завершення попередніх
            int threadAcumulated = (ThreadNum*(ThreadNum + 1))/2;
            System.out.println("Time taken: "+ timeval.getValue()/threadAcumulated +" milliseconds ("+ThreadNum+" threads)");
            System.out.println("---------------------------------------------");
            assertEquals(ThreadNum, val.getValue());

            // помітно, що зі збільшенням пооків час роботи відчутно росте
        }
    }

    @Test
    public void overflowTest() {
        int overfVal = 0;
        boolean flag = false;
        int tNum = 1;
        ValueWrapper val = new ValueWrapper(0);
        Thread t;
        while(true) {
            t = new TestThread(val);
            t.start();
            overfVal++;
//            for(int i = 0; i < tNum; i++){
//                Thread[] t = new TestThread[tNum];
//
//                for (int j = 0; j < tNum; j++) {
//                    t[j] = new TestThread(val);
//                    t[j].start();
//                    overfVal++;
//                }
//
//                for (int j = 0; j < tNum; j++) {
//                    try {
//                        t[j].join();
//                    } catch (InterruptedException e) {
//
//                    }
//                }

                System.out.println(overfVal);

                if(overfVal == 0 && !flag) {
                    flag = true;
                }
                if(overfVal > 30 && flag){
                    System.out.println("------- ok");
                    assertEquals(val.getValue(), overfVal);
                }
            }
        //}
    }
}

class ValueWrapper{

    int value;
    public Lock lock;
    Random r;
    ValueWrapper(int value){
        this.value = value;
        lock = new ImprovedBakeryLock();
        r = new Random();
    }
    int getValue(){
        return value;
    }
    void setValue(int value){
        this.value = value;
    }

    void increment(){
        lock.lock();
        try {
            Thread.sleep((100));
        } catch (InterruptedException e){}
        value ++;
        lock.unlock();
    }

    synchronized void add(int val){
        value +=val;

    }
    int incrementAndGet(){
        lock.lock();
        value ++;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e){}
        lock.unlock();
        return value;
    }

}

class TestThread extends Thread{
    ValueWrapper val;
    TestThread(ValueWrapper v) {
        this.val = v;
    }
    @Override
    public void run(){
        val.increment();
    }
}

class TestTimeThread extends Thread{
    ValueWrapper val;
    ValueWrapper timeVal;
    TestTimeThread(ValueWrapper v, ValueWrapper tval) {
        this.val = v;
        this.timeVal = tval;
    }
    @Override
    public void run(){
        Instant start = Instant.now();
        val.increment();
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        timeVal.add((int)timeElapsed.toMillis());
    }
}

class TestValThread extends Thread{
    ValueWrapper val;
    ValueWrapper test;
    TestValThread(ValueWrapper v, ValueWrapper test) {
        this.val = v;
        this.test = test;
    }
    @Override
    public void run(){
        test.setValue(val.incrementAndGet());
    }
}