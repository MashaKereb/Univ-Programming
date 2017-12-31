import java.util.concurrent.RecursiveTask;

/**
 * Created by Masha Kereb on 26-Feb-17.
 */
public class Competition extends RecursiveTask<Monk> {
    private Monk[] monks;
    int end;
    int start;

    public Competition(Monk[] monks) {
        this.monks = monks;
        this.start = 0;
        this.end = monks.length - 1;
    }

    protected Competition(Monk[] monks, int end, int start) {
        this.monks = monks;
        this.end = end;
        this.start = start;
    }

    @Override
    public Monk compute() {
        int length = end - start;

        if (length == 0) {
            return monks[end];
        }
        else if (length == 1) {
            return Monk.max(monks[end], monks[start]);
        } else {
        Competition left = new Competition(monks, start, (end + start)/2);
        left.fork();
        Competition right = new Competition(monks, (end + start)/2 + 1, end);
        return Monk.max(right.compute(), left.join());
    }
}

}
