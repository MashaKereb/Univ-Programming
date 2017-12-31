/**
 * Created by Masha Kereb on 04-Dec-16.
 */

public class Pollard {

    private final int n;
    private final int N;
    private final int alpha;
    private final int beta;


    private Pollard(int n, int alpha, int beta) {

        this.n = n;
        this.N = n + 1;
        this.alpha = alpha;
        this.beta = beta;

    }



    private int discreteLog() throws InterruptedException {
        SeqStep singler = new SeqStep(1, 0, 0);
        SeqStep doubler = new SeqStep(1, 0, 0);

        for (int i = 0; i < n; i++) {
            Thread thread = new Thread(singler::nextIter);
            thread.run();

            doubler.nextIter();
            doubler.nextIter();

            thread.join();

            if (doubler.x == singler.x)
                return (doubler.a - singler.a) / (singler.b - doubler.b);
        }

        return 0;
    }

    private  class SeqStep{
        int a, b, x;

        SeqStep(int x, int a, int b){
            this.a = a;
            this.b = b;
            this.x = x;
        }

        private void nextIter() {
            switch (x % 3) {
                case 0:
                    x = x * x % N;
                    a = a * 2 % n;
                    b = b * 2 % n;
                    break;

                case 1:
                    x = x * alpha % N;
                    a = (a + 1) % n;
                    break;

                case 2:
                    x = x * beta % N;
                    b = (b + 1) % n;
                    break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Pollard pollard = new Pollard(1018, 2, 5);
        System.out.println(pollard.discreteLog());
    }
}
