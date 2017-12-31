package operations;

import java.math.BigInteger;
import java.util.stream.IntStream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class MillerRabin {

    private final static BigInteger TWO = BigInteger.valueOf(2);

    private static BigInteger oddFactor(BigInteger n, int binPow) {
        return n.divide(BigInteger.valueOf((long) Math.pow(2, binPow)));
    }

    private static BigInteger binPowMod(BigInteger x, int e, BigInteger n) {
        return x.pow(2 * e).mod(n);
    }

    private static int maxBinFactor(BigInteger n) {
        return IntStream.range(0, n.bitLength())
                .filter(i -> !n.mod(TWO.pow(i + 1)).equals(ZERO))
                .findFirst()
                .orElse(0);
    }

    private static boolean passMillerTrial(BigInteger n, int s, BigInteger d) {

        BigInteger a = Fermat.generateRandomRemainder(n).add(ONE);
        BigInteger x = a.modPow(d, n);

        return x.equals(ONE) || x.equals(n.subtract(ONE)) ||
                (s != 1 && IntStream.range(1, s).allMatch(__ -> millerTest(n, x, s)));
    }

    private static boolean millerTest(BigInteger n, BigInteger x, int s) {
        return  IntStream.range(1, s).noneMatch(i -> binPowMod(x, i, n).equals(ONE)) &&
                IntStream.range(1, s).anyMatch( i -> binPowMod(x, i, n).equals(n.subtract(ONE)));
    }

    public static boolean isPrime(BigInteger numberToCheck, int timesToCheck) {

        if (isEven(numberToCheck)) return false;

        int s = maxBinFactor(numberToCheck.subtract(ONE));
        BigInteger d = oddFactor(numberToCheck.subtract(ONE), s);

        return IntStream.range(0, timesToCheck)
                .allMatch(__ -> passMillerTrial(numberToCheck, s, d));
    }

    private static boolean isEven(BigInteger numberToCheck) {
        return !numberToCheck.testBit(0);
    }

    public static void main(String[] args) {
        BigInteger n = BigInteger.valueOf(293);
        System.out.println(isPrime(n, 10));
    }
}
