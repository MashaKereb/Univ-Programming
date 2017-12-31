package operations;

import java.math.BigInteger;
import java.util.Random;
import java.util.stream.IntStream;

public class Fermat {

    private static boolean isPrime(BigInteger numberToCheck, int timesToCheck) {
        return IntStream.range(0, timesToCheck)
                .allMatch(__ -> passFermatTest(numberToCheck));
    }

    private static boolean passFermatTest(BigInteger numberToCheck) {
        BigInteger randomRemainder = generateRandomRemainder(numberToCheck);
        return littleFermat(numberToCheck, randomRemainder);
    }
    
    private static boolean littleFermat(BigInteger n, BigInteger a) {
        return a.modPow(n, n).compareTo(a) == 0;
    }

    static BigInteger generateRandomRemainder(BigInteger n) {

        Random random = new Random();

        BigInteger a = new BigInteger(n.bitLength(), random);
        a = a.mod(n.subtract(BigInteger.ONE));
        a = a.add(BigInteger.ONE);

        return a;
    }

    public static void main(String[] args) {
        BigInteger number = BigInteger.valueOf(937);
        System.out.println(isPrime(number, 1));
    }
}
