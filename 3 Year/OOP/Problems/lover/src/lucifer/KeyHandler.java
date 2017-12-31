package lucifer;

public class KeyHandler {

    public byte[] reverseInit(byte[] key){

        byte[] postShift = new byte[16];

        // Left shift 7 bytes
        System.arraycopy(key, 9, postShift, 0, key.length / 2 - 1);
        System.arraycopy(key, 0, postShift, 7, key.length / 2 + 1);

        return postShift;
    }

    public int[] toIntArray(String key) {
        int keylength = key.length();
        int arraycounter = 0;
        int[] retval = new int[ (keylength / 2) ];

        for (int i = 0; i < keylength; i += 2) {
            if (i <= keylength - 3)
                retval[arraycounter] = Hex.toInt(key.substring(i, i + 2));
            else
                retval[arraycounter] = Hex.toInt(key.substring(i));

            arraycounter++;
        }

        return retval;
    }

    public byte[] generateSubkey(byte[] key){

        byte[] subKey = new byte[9];

        // Generate the subKey by repeating the first byte twice
        // and then take the next 7 bytes.
        subKey[0] = key[0];
        System.arraycopy(key, 0, subKey, 1, key.length);

        return subKey;
    }

    public byte[] shiftEncrKey(byte[] key){

        byte[] postShift = new byte[16];

        // Left shift 7 bytes
        System.arraycopy(key, 7, postShift, 0, key.length / 2 + 1);
        System.arraycopy(key, 0, postShift, 9, key.length / 2 - 1);

        return postShift;
    }

    public byte[] shiftDecrKey(byte[] key){

        byte[] postShift = new byte[16];

        // right shift 7 bytes
        System.arraycopy(key, 0, postShift, 7, key.length / 2 + 1);
        System.arraycopy(key, 9, postShift, 0, key.length / 2 - 1);

        return postShift;
    }

	public static void main(String args[]) {

        String key = "AB1B41A58289C24F209D1827E99AB3C8";
        KeyHandler keygen = new KeyHandler();
        int[] hexstring = keygen.toIntArray(key);

        for (int aHexstring : hexstring)
            System.out.print(aHexstring + " ");
    }
}

