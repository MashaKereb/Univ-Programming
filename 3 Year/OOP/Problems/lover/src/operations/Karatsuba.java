package operations;

import java.math.BigInteger;

public class Karatsuba {

    private static BigInteger getLeftPart(BigInteger n, int N) {
        return n.shiftRight(N);
    }

    private static BigInteger getRightPart(BigInteger n, BigInteger rightPart, int N) {
        return n.subtract(rightPart.shiftLeft(N));
    }

    public static BigInteger karatsuba(BigInteger x, BigInteger y) {

        int N = Math.max(x.bitLength(), y.bitLength());
        if (N <= 2000) return x.multiply(y);

        N = (N / 2) + (N % 2);

        BigInteger b = getLeftPart(x, N);
        BigInteger a = getRightPart(x, b, N);
        BigInteger d = getLeftPart(y, N);
        BigInteger c = getRightPart(y, d, N);

        BigInteger ac =   karatsuba(a, c);
        BigInteger bd =   karatsuba(b, d);
        BigInteger abcd = karatsuba(a.add(b), c.add(d));

        return ac.add(abcd.subtract(ac).subtract(bd).shiftLeft(N))
                 .add(bd.shiftLeft(2*N));
    }

    public static void main(String[] args) {

        BigInteger x = BigInteger.valueOf(12345);
        BigInteger y = BigInteger.valueOf(6789);

        System.out.println(karatsuba(x, y));
    }
}
