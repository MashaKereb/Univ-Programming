import java.math.*;

public class ExtendedEuclid {

    public static void main(String[] args) {
        BigInteger a = new BigInteger("612");
        BigInteger b = new BigInteger("342");
        BigInteger[] res = ExtendedEuclid(a, b);
        System.out.println("GCD(" + a.toString()
                            + ", " + b.toString()
                            + ") = " + res[0].toString());

        System.out.println(res[0].toString() + " = "
                + res[1].toString() + "*" + a.toString()
                + " + " + res[2].toString() + "*" + a.toString());

    }

    public static BigInteger[] ExtendedEuclid(BigInteger a, BigInteger b) {
    /*  This function will perform the Extended Euclidean algorithm
        to find the GCD of a and b.  We assume here that a and b
        are non-negative (and not both zero).  This function also
        will return numbers j and k such that
               d = j*a + k*b
        where d is the GCD of a and b.
    */
        BigInteger[] ans = new BigInteger[3];


        if (b.intValue() == 0)  {  /*  If b = 0, then we're done...  */
            ans[0] = a;
            ans[1] = BigInteger.valueOf(1);
            ans[2] = BigInteger.valueOf(0);
        }
        else {
            /*  Otherwise, make a recursive function call  */
            BigInteger q = a.divide(b);
            ans = ExtendedEuclid (b, a.remainder(b));
            BigInteger temp = ans[1].subtract(ans[2].multiply(q));
            ans[1] = ans[2];
            ans[2] = temp;
        }

        return ans;
    }

}

