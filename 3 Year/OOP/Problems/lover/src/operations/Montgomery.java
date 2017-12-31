package operations;

import java.math.BigInteger;

public class Montgomery {

    private BigInteger reducer;
    private BigInteger modulus;
    private BigInteger reciprocal;

    public Montgomery(BigInteger modulus, BigInteger reducer) {

        this.modulus = modulus;
        this.reducer = reducer;

        reciprocal = modulus.negate().modInverse(reducer);
    }

    public BigInteger multiply(BigInteger x, BigInteger y) {

        BigInteger a = convertIn(x);
        BigInteger b = convertIn(y);
        BigInteger T = a.multiply(b);

        return convertOut(reduce(T));
    }

    private BigInteger reduce(BigInteger T) {

        BigInteger m = T.mod(reducer).multiply(reciprocal).mod(reducer);
        BigInteger t = T.add(m.multiply(modulus)).divide(reducer);

        return t.compareTo(modulus) >= 0 ? t.subtract(modulus) : t;
    }

    private BigInteger convertIn(BigInteger n) {
        return n.multiply(reducer).mod(modulus);
    }

    private BigInteger convertOut(BigInteger n) {
        BigInteger R = reducer.modInverse(modulus);
        return n.multiply(R).mod(modulus);
    }

    public static void main(String[] args) {

        BigInteger modulus = BigInteger.valueOf(17);
        BigInteger reducer = BigInteger.valueOf(100);

        BigInteger x = BigInteger.valueOf(7);
        BigInteger y = BigInteger.valueOf(15);

        Montgomery montgomery = new Montgomery(modulus, reducer);
        System.out.println( montgomery.multiply(x, y));
    }
}
