/**
 * Created by Masha Kereb on 11-Apr-17.
 */

public class MainRunner {
    public static void main(String[] args) {
        int[]sizes = {1000, 100};

        for (int matSize: sizes) {
        //    SimpleMatrix.calculate(args, matSize);
//            StringMatrix.calculate(args, matSize);
            CannonMatrix.calculate(args, matSize);
//            FoxMatrix.calculate(args, matSize);
        }
    }
}
