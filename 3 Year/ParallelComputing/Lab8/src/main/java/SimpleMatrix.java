/**
 * Created by Masha Kereb on 10-Apr-17.
 */

import mpi.MPI;

public class SimpleMatrix {

    public static void calculate(String[] args, int matSize) {
        MPI.Init(args);
        int procRank = MPI.COMM_WORLD.Rank();

        Matrix matrixA = new Matrix(matSize, "A");
        Matrix matrixB = new Matrix(matSize, "B");

        long startTime = 0L;
        if(procRank == 0) {
            matrixA.fillRandom(3);
            matrixB.fillRandom(3);
            startTime = System.currentTimeMillis();

        }


        Matrix result = matrixA.multiply(matrixB);
        if (procRank == 0) {
            System.out.print("Simple matrix multiplication. Size " + matSize + "; Cores: "+ args[1] + "; Time: ");
            System.out.println(System.currentTimeMillis() - startTime);
//            System.out.println(matrixA.toString());
//            System.out.println(matrixB.toString());
//            System.out.println(result.toString());
        }
        MPI.Finalize();
    }
}
