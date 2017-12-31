package operations;

import java.math.BigInteger;
import java.util.Arrays;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class EuclidEx {

    public static BigInteger[] exEuclid(BigInteger a, BigInteger b) {

        BigInteger[] answer = new BigInteger[3];

        if (b.equals(ZERO)) {
            answer[0] = a;
            answer[1] = ONE;
            answer[2] = ZERO;
        } else {
            BigInteger quotient = a.divide(b);
            answer = exEuclid(b, a.remainder(b));

            BigInteger temp = answer[1].subtract(answer[2].multiply(quotient));
            answer[1] = answer[2];
            answer[2] = temp;
        }

        return answer;
    }

    public static void main(String[] args) {

        BigInteger a = BigInteger.valueOf(13);
        BigInteger b = BigInteger.valueOf(-17);

        System.out.println(Arrays.toString(exEuclid(a, b)));
    }
}
