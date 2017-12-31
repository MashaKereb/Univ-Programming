
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        final Integer monksNumber = 55;
        Monk[] monks = new Monk[monksNumber];
        for(int i = 0; i< monksNumber; i++){
            monks[i] = new Monk();
        }
        Competition comp = new Competition(monks);
        ForkJoinPool pool = new ForkJoinPool();
        Monk winner = pool.invoke(comp);
        System.out.println(winner.getQiEnergy());
    }
}

