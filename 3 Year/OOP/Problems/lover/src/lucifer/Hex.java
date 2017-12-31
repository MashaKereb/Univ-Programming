package lucifer;

public class Hex {

    private static final char[] int2hex = new char[]
            {'0', '1', '2', '3', '4', '5', '6', '7',
                    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static String toString(byte[] val) {
        return toString (val, 0, val.length);
    }


    public static String toString(byte[] val, int off, int len) {
        if (off < 0 || len < 0 || off + len > val.length)
            throw new IndexOutOfBoundsException();

        StringBuilder buf = new StringBuilder (2 * len);
        while (len > 0) {
            buf.append (int2hex[(val[off] >> 4) & 0xF]);
            buf.append (int2hex[(val[off]     ) & 0xF]);
            ++off;
            --len;
        }
        return buf.toString();
    }

    public static byte[] toByteArray(String str) {
        int n = (str.length() + 1) / 2;
        byte[] val = new byte [n];
        toByteArray (str, val, 0, val.length);
        return val;
    }

    public static byte toByte(String str) {
        return (byte) toInt (str);
    }

    public static char toChar(String str) {
        return (char) toInt (str);
    }

    public static short toShort(String str) {
        return (short) toInt (str);
    }

    public static int toInt(String str) {
        int result = 0;
        int n = str.length();
        for (int i = 0; i < n; ++ i)
            result = (result << 4) + hex2int (str.charAt (i));

        return result;
    }


    public static void toByteArray(String str, byte[] val, int off, int len) {

        if (off < 0 || len < 0 || off + len > val.length)
            throw new IndexOutOfBoundsException();

        int stroff = str.length() - 1;
        int valoff = off + len - 1;
        int result;

        while (len > 0 && stroff >= 0) {
            result = hex2int (str.charAt (stroff));
            --stroff;

            if (stroff >= 0)
                result += hex2int (str.charAt (stroff)) << 4;

            --stroff;
            val[valoff] = (byte) result;
            --valoff;
            --len;
        }

        while (len > 0) {
            val[valoff] = (byte) 0;
            --valoff;
            --len;
        }
    }

    private static int hex2int(char digit) {
        switch (digit) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return digit - '0';
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
                return digit - 'a' + 10;
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
                return digit - 'A' + 10;
            default:
                throw new IllegalArgumentException
                        ("Not a hexadecimal digit: '" + digit + "'");
        }
    }
}

