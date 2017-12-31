

import java.math.BigInteger;
import java.util.Scanner;

public class BinaryPower {

    private static final BigInteger ZERO = BigInteger.valueOf(0);
    private static final BigInteger ONE = BigInteger.valueOf(1);
    private static final BigInteger TWO = BigInteger.valueOf(2);

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("Input number: ");
        BigInteger numb = scan.nextBigInteger();

        System.out.println("Input degree: ");
        BigInteger deg = scan.nextBigInteger();

        System.out.println("Input mod: ");
        BigInteger mod = scan.nextBigInteger();

        System.out.println(binpow(numb, deg).mod(mod));
    }

    public static BigInteger binpow(BigInteger a, BigInteger n) {
        if (n.equals(ZERO)) {
            return BigInteger.ONE;
        }
        if (n.mod(TWO).equals(ONE)) {
            return a.multiply(binpow(a, n.subtract(ONE)));
        } else {
            BigInteger b = binpow(a, n.divide(TWO));
            return b.multiply(b);
        }
    }
}
