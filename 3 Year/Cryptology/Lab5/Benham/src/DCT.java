/**
 * Created by Masha Kereb on 05-Dec-16.
 */
public class DCT {
    private final int n;

    private final double c[][];

    public DCT() {
        this(8);
    }

    public DCT(int N) {
        this.n = N;

        // c[p][q] = 1 / sqrt(M) if p = 0 else
        // c[p][q] = sqrt(2/M) * cos(PI(2q+1)p / 2M)

        c = new double[this.n][this.n];

        final double pi = Math.atan(1.0) * 4.0;
        for (int j = 0; j < N; j++) {
            this.c[0][j] = 1.0 / Math.sqrt(N);
        }

        for (int i = 1; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.c[i][j] = Math.sqrt(2.0 / N) * Math.cos(pi * (2 * j + 1) * i / (2.0 * N));
            }
        }
    }

    public int[][] ForwardDCT(final int input[][]) {
        final double temp[][] = new double[this.n][this.n];
        int output[][] = new int[this.n][this.n];
        double temp1;


        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                temp[i][j] = 0.0;
                for (int k = 0; k < this.n; k++) {
                    temp[i][j] += (input[i][k] - 128) * this.c[j][k];
                }
            }
        }

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                temp1 = 0.0;
                for (int k = 0; k < this.n; k++) {
                    temp1 += this.c[i][k] * temp[k][j];
                }
                output[i][j] = (int) Math.round(temp1);
            }
        }

        return output;
    }

    public int[][] InverseDCT(final int input[][]) {
        final double temp[][] = new double[this.n][this.n];
        double temp1;
        int output[][] = new int[this.n][this.n];

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                temp[i][j] = 0.0;
                for (int k = 0; k < this.n; k++) {
                    temp[i][j] += input[i][k] * this.c[k][j];
                }
            }
        }

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                temp1 = 0.0;
                for (int k = 0; k < this.n; k++) {
                    temp1 += this.c[k][i] * temp[k][j];
                }
                temp1 += 128.0;
                if (temp1 < 0) {
                    output[i][j] = 0;
                } else if (temp1 > 255) {
                    output[i][j] = 255;
                } else {
                    output[i][j] = (int) Math.round(temp1);
                }
            }
        }
        return output;
    }
}
