/**
 * Created by Masha Kereb on 04-Dec-16.
 */
public class Consumer extends Thread {
    private int n1, n2;
    private PriorityQueue q;
    Consumer(PriorityQueue q, int start, int end){
        this.q = q;
        this.n1 = start;
        this.n2 = end;
    }
    @Override
    public void run() {
        for (int i = n1; i < n2; i++) {
            System.out.println("Consumer " + ThreadId.get() + " pops " + q.pop());
            Thread.yield();
        }
    }
}
