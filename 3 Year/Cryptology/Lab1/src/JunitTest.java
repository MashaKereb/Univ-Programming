/**
 * Created by Masha Kereb on 20-Dec-16.
 */
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.locks.Lock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JunitTest {

    @Test
    public void testMontgomery1()  {
        BigInteger x = new BigInteger("125487");
        BigInteger y = new BigInteger("647339");
        BigInteger mod = new BigInteger("546353");

        MontgomeryReducer red = new MontgomeryReducer(mod);

        BigInteger xm = red.convertIn(x);
        BigInteger zm = red.multiply(xm, red.convertIn(y));
        BigInteger z = x.multiply(y).mod(mod);

        assertTrue(red.convertOut(zm).equals(z));
        assertTrue(z.equals(new BigInteger("318700")));


    }

    @Test
    public void testMontgomery2()  {
        BigInteger x = new BigInteger("125487");
        BigInteger y = new BigInteger("647339");
        BigInteger mod = new BigInteger("546353");

        MontgomeryReducer red = new MontgomeryReducer(mod);

        BigInteger xm = red.convertIn(x);
        BigInteger zm = red.pow(xm, y);
        BigInteger z = x.modPow(y, mod);

        assertTrue(red.convertOut(zm).equals(z));
        assertTrue(z.equals(new BigInteger("208360")));


    }

    @Test
    public void testMillerRabin()  {
        MillerRabin mr = new MillerRabin();

        // run with -ea to enable assertions
        String[] primes = { "1", "3", "3613", "7297",
                "226673591177742970257407", "2932031007403" };
        String[] nonPrimes = { "3341", "2932021007403",
                "226673591177742970257405" };
        int k = 40;
        for (String p : primes)
            assertTrue(mr.isProbablePrime(new BigInteger(p), k));
        for (String n : nonPrimes)
            assertFalse(mr.isProbablePrime(new BigInteger(n), k));


    }
    @Test
    public void testKaratsuba(){

        BigInteger a = new BigInteger("1836254326565253566245263526");
        BigInteger b = new BigInteger("3762656356546356543656735765");
        BigInteger c = KaratsubaMult.karatsuba(a, b);
        assertTrue(c.equals(new BigInteger("6909194014086500546921326753262446549436411937374207390")));


        BigInteger a2 = new BigInteger("6545448465146517484515489985415158485");
        BigInteger b2 = new BigInteger("558488784654848541654156849874984165416");
        BigInteger c2 = KaratsubaMult.karatsuba(a2, b2);
        assertTrue(c2.equals(new BigInteger("3655559558320622313635431106063944269847883039083894124396002224273095954760")));
    }

    @Test
    public void tesFerma()  {

        String[] primes = { "3", "3613", "7297",
                 "2932031007403" };
        String[] nonPrimes = { "3341", "2932021007403",
                "226673591177742970257405" };
        int k = 40;
        for (String p : primes)
            assertTrue(FermaTest.checkPrime(new BigInteger(p), k));
        for (String n : nonPrimes)
            assertFalse(FermaTest.checkPrime(new BigInteger(n), k));


    }

    @Test
    public void tesEuclid() {

        BigInteger a = new BigInteger("612");
        BigInteger b = new BigInteger("342");
        BigInteger[] res = ExtendedEuclid.ExtendedEuclid(a, b);
        assertTrue(res[0].equals(res[1].multiply(a).add(res[2].multiply(b))));


        BigInteger a1 = new BigInteger("54778");
        BigInteger b1 = new BigInteger("58882");
        BigInteger[] res1 = ExtendedEuclid.ExtendedEuclid(a1, b1);
        assertTrue(res1[0].equals(res1[1].multiply(a1).add(res1[2].multiply(b1))));

    }

    @Test
    public void testBinPower(){
        assertTrue(BinaryPower.binpow(new BigInteger("512447"), new BigInteger("2254")).mod(new BigInteger("25447")).equals(new BigInteger("557")));
        assertTrue(BinaryPower.binpow(new BigInteger("5654994"), new BigInteger("54554")).mod(new BigInteger("66597")).equals(new BigInteger("26064")));
        assertTrue(BinaryPower.binpow(new BigInteger("6564785612"), new BigInteger("857474")).mod(new BigInteger("95989")).equals(new BigInteger("79654")));
    }

}

