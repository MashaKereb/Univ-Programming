package operations;

import java.math.BigInteger;

@FunctionalInterface
public interface PrimeTest {
    boolean isPrime(BigInteger n, int k);
}
