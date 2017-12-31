package schnorr;

import schnorr.blake2b.Blake2bHasher;

import java.math.BigInteger;

public class SchnorrSignature {

    private BigInteger privateKey;
    private BigInteger publicKey;

    private byte[] message;

    // Parameters
    private BigInteger prime;
    private BigInteger factorPrime;
    private BigInteger witness;

    // Null byte
    private byte[] zero = new byte[1];

    public void config(BigInteger prime, BigInteger factor, BigInteger witness) {
        this.prime = prime;
        this.factorPrime = factor;
        this.witness = witness;
    }

    public void update(String message) {
        this.message = message.getBytes();
    }

    public BigInteger keyPairGen(String pw) {

        byte[] pwHash = computeHash(pw.getBytes());

        privateKey = new BigInteger(append(zero, pwHash)).mod(factorPrime);
        publicKey = witness.modPow(privateKey, prime);

        return publicKey;
    }

    public BigInteger[] sign() {

        byte[] xm = append(privateKey.toByteArray(), message);
        byte[] xmHash = computeHash(xm);
        BigInteger e = new BigInteger(append(zero, xmHash)).mod(factorPrime);

        BigInteger u = witness.modPow(e, prime);

        byte[] mu = append(message, u.toByteArray());
        byte[] muHash = computeHash(mu);

        BigInteger h = new BigInteger(append(zero, muHash));
        BigInteger xh = privateKey.multiply(h);
        BigInteger s = e.subtract(xh).mod(factorPrime);

        return new BigInteger[] {h, s};
    }

    public boolean verify(BigInteger[] sigma, BigInteger key) {

        BigInteger h = sigma[0];
        BigInteger s = sigma[1];

        // u = g^privateKey publicKey^h mod prime
        BigInteger as = witness.modPow(s, prime);
        BigInteger yh = key.modPow(h, prime);
        BigInteger u = as.multiply(yh).mod(prime);


        byte[] mu = append(message, u.toByteArray());
        byte[] muHash = computeHash(mu);
        BigInteger h1 = new BigInteger(append(zero, muHash));

        return h1.equals(h);
    }

    public static byte[] computeHash(byte[] data) {
        return computeHash(data, data.length);
    }

    private static byte[] computeHash(byte[] data, int count) {
        Blake2bHasher hasher = new Blake2bHasher(null);
        hasher.Update(data, 0, count);
        return hasher.Finish();
    }

    public static byte[] append ( byte[] a, byte[] b ) {

        byte[] c = new byte[a.length + b.length];

        System.arraycopy ( a, 0, c, 0, a.length );
        System.arraycopy ( b, 0, c, a.length, b.length );

        return c;
    }
}
