package pollard;

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

    private double discreteLog() throws InterruptedException {

        SeqStep single = new SeqStep(1, 0, 0);
        SeqStep dub = new SeqStep(1, 0, 0);

        for (int i = 0; i < n; i++) {

            Thread thread = new Thread(single::nextIteration);
            thread.run();

            dub.nextIteration();
            dub.nextIteration();
            thread.join();

            if (single.x == dub.x)
                return (dub.a - single.a) /
                        (single.b  - dub.b);
        }

        return 0;
    }

    class SeqStep {
        int x;
        int a;
        int b;

        SeqStep(int x, int a, int b) {
            this.x = x;
            this.a = a;
            this.b = b;
        }

        void nextIteration() {
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

        Pollard pollard = new Pollard(12, 2, 3);
        System.out.println(pollard.discreteLog());
    }
}
