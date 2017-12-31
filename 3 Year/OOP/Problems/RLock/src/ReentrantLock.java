import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by Masha Kereb on 05-Dec-16.
 */

class ReentrantLock implements Lock {

    public ReentrantLock() {
        sync = new Sync();
    }

    @Override
    public void lock() {
        sync.lock();
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public Condition newCondition() {
        return sync.newCondotion();
    }

    private final Sync sync;

    static final class Sync extends AbstractQueuedSynchronizer {

        final void lock() {
            acquire(1);
        }

        @Override
        protected final boolean tryAcquire(int toAcquire) {
            final Thread curThread = Thread.currentThread();
            int state = getState();
            if (state == 0) {
                if (compareAndSetState(0, toAcquire)) {
                    setExclusiveOwnerThread(curThread);
                    return true;
                }
            } else if (curThread == getExclusiveOwnerThread()) {
                int nextState = state + toAcquire;
                if (nextState < 0) {
                    throw new Error("To many threads!");
                }
                setState(nextState);
                return true;
            }
            return false;
        }

        @Override
        protected final boolean tryRelease(int toRelease) {
            int state = getState() - toRelease;
            boolean free = false;
            if (state == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(state);
            return free;
        }

        final Condition newCondotion() {
            return new AbstractQueuedSynchronizer.ConditionObject();
        }
    }
}
