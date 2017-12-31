package operations;

import  java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class BinPow {

    private final static BigInteger TWO = BigInteger.valueOf(2);

    public static BigInteger binModPow(BigInteger x, BigInteger p, BigInteger base) {
        return binModPow(ONE, x, p, base);
    }

    public static BigInteger oneLess(BigInteger n) {
        return n.subtract(ONE);
    }

    private static BigInteger binModPow(BigInteger acc, BigInteger x,
                                        BigInteger p, BigInteger base) {
             if (p.equals(ZERO)) return acc;
        else if (p.equals(ONE))  return multMod(x, acc, base);
        else if (isEven(p))      return binModPow(acc,                   squareMod(x, base), half(p),          base);
        else                     return binModPow(multMod(x, acc, base), squareMod(x, base), half(oneLess(p)), base);
    }

    private static BigInteger half(BigInteger n) {
        return n.shiftRight(1);
    }

    private static BigInteger squareMod(BigInteger n, BigInteger base) {
        return multMod(n, n, base);
    }

    private static BigInteger multMod(BigInteger x, BigInteger y, BigInteger base) {
        return x.multiply(y).mod(base);
    }

    private static boolean isEven(BigInteger n) {
        return !n.testBit(0);
    }

    public static void main(String[] args) {
        BigInteger number = BigInteger.valueOf(2);
        BigInteger power = BigInteger.valueOf(10);
        BigInteger base = BigInteger.valueOf(1000);

        System.out.println(number.modPow(power, base));
        System.out.println(binModPow(number, power, base));
    }
}
