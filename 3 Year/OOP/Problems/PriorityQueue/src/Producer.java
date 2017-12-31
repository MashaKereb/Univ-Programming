import javafx.util.Pair;

/**
 * Created by Masha Kereb on 04-Dec-16.
 */
public class Producer extends Thread{
    private int n1, n2;
    private PriorityQueue q;
    public Producer(PriorityQueue q, int start, int end){
        this.n1 = start;
        this.n2 = end;
        this.q = q;
    }
    @Override
    public void run() {
        for (int i = n1; i < n2; i++) {
            Pair<Integer, Integer> p = new Pair<>(i, i-n1);
            System.out.println("Producer " + ThreadId.get() + " puts " + p);
            q.put(p);
        }
    }
}
