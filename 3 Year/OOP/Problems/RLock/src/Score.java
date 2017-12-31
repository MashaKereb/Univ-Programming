import java.util.concurrent.locks.Lock;

/**
 * Created by Masha Kereb on 18-Dec-16.
 */
public class Score {

    public Score(float balance) {
        this.result = balance;
    }

    public void subValue(float value) {
        try {
            lock.lock();
            this.result -= value;
        } finally {
            lock.unlock();
        }
    }

    public void addValue(float value) {
        try {
            lock.lock();
            this.result += value;
        } finally {
            lock.unlock();
        }
    }

    float getResult(){
        return result;
    }

    @Override
    public String toString() {
        return "Score result: " + result;
    }

    private final Lock lock = new ReentrantLock();
    private float result;
}

