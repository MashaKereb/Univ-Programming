import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ImprovedBakeryLock implements Lock {

    private AtomicInteger lastUsed, lastDone;


    ImprovedBakeryLock() {
        lastUsed = new AtomicInteger(0);
        lastDone = new AtomicInteger(0);
    }

    public void lock() {
        int ticket = lastUsed.getAndIncrement();
        while (ticket != lastDone.get()){}
    }

    /*
    * Оскільки в нас є лише порівняння на рівність, то конструкція (1) не потрібна
    * і не несе жодної користі. Навіть коли LastUsed переходить у від'ємні значення,
    * інкремент і порівняння повинні працювати коректно. Проблема може виникнути лише тоді,
    * коли LastUsed як переповнена змінна досягає значень непереповненої LastDone.
    * (тобто 2^32 потоки одночасно намагаються зайти в критичну зону, яка зайнята і не звільняється)
    * І для такої проблеми навряд чи можна знайти рішення.
    * */

    @Override
    public void unlock() {
        int temp = lastDone.incrementAndGet();
//        if(lastDone.get() == lastUsed.get() && temp > 100){ // (1)
//            if(lastUsed.compareAndSet(temp, 0))
//                lastDone.compareAndSet(temp, 0);
//            System.out.println("overflow check ---"); // for testing
//        }
    }

    @Override
    public boolean tryLock() {
        int expected = lastDone.get();
        return lastUsed.compareAndSet(expected, expected+1);
    }




    @Override
    public Condition newCondition() {
        return new Condition() {
            @Override
            public void await() throws InterruptedException {

            }

            @Override
            public void awaitUninterruptibly() {

            }

            @Override
            public long awaitNanos(long nanosTimeout) throws InterruptedException {
                return 0;
            }

            @Override
            public boolean await(long time, TimeUnit unit) throws InterruptedException {
                return false;
            }

            @Override
            public boolean awaitUntil(Date deadline) throws InterruptedException {
                return false;
            }

            @Override
            public void signal() {

            }

            @Override
            public void signalAll() {

            }
        };
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }
}
