package lucifer;

public class Lucifer {

    private KeyHandler keyhandler = new KeyHandler();
    private Permutator permutator = new Permutator();
    private SBoxHandler sboxhandler = new SBoxHandler();

    public byte[] encrypt(byte[] plaintext, byte[] skey, int rounds) {

        byte[] cipherText = plaintext;
        byte[] key = skey;

        byte[] rightHalf = new byte[8];
        byte[] leftHalf = new byte[8];
        byte[] encRight;
        byte[] encLeft;

        for (int round = 1; round <= rounds; round++) {

            //---KEY OPERATIONS---
            // grab the subKey
            byte[] subkey = keyhandler.generateSubkey(key);

            //do a shift on the key
            key = keyhandler.shiftEncrKey(key);

            //---GET THE LEFT AND RIGHT HALVES---
            System.arraycopy(cipherText, 0, leftHalf, 0, cipherText.length);
            System.arraycopy(cipherText, 8, rightHalf, 0, cipherText.length);

            //---RUN F FUNCTION---
            //run the function and get the cipherText
            encRight = theFunction(rightHalf, subkey);

            //---XOR ENCRYPTED RIGHT HALF WITH LEFT HALF---
            encLeft = xorHalves(encRight, leftHalf);

            if (round == 16)
                cipherText = concatHalves(encLeft, rightHalf);
            else
                cipherText = concatHalves(rightHalf, encLeft);
        }

        return cipherText;
    }

    public byte[] decrypt(byte[] ciphertext, byte[] skey, int rounds) {

        byte[] plaintext = ciphertext;
        byte[] key = skey;
        key = keyhandler.reverseInit(key);

        byte[] rightHalf = new byte[8];
        byte[] leftHalf = new byte[8];
        byte[] encRight;
        byte[] encLeft;

        //PERFORM 16 ROUNDS OF LUCIFER ON THE PLAINTEXT!
        for (int round = 1; round <= rounds; round++) {

            byte[] subkey = keyhandler.generateSubkey(key);
            key = keyhandler.shiftDecrKey(key);

            //---GET THE LEFT HALF---
            System.arraycopy(plaintext, 0, leftHalf, 0, plaintext.length);
            System.arraycopy(plaintext, 8, rightHalf, 0, plaintext.length);

            //---RUN F FUNCTION---
            //run the function and get the ciphertext
            encRight = theFunction(rightHalf, subkey);

            //---XOR ENCRYPTED RIGHT HALF WITH LEFT HALF---
            encLeft = xorHalves(encRight, leftHalf);

            if (round == 16)
                plaintext = concatHalves(encLeft, rightHalf);
            else
                plaintext = concatHalves(rightHalf, encLeft);
        }

        return plaintext;
    }

    public byte[] theFunction(byte[] rightHalf, byte[] subkey) {

        //grab last 8 bytes of the subKey
        byte[] lastEight = new byte[8];
        for (int i = 0; i < lastEight.length; i++)
            lastEight[i] = subkey[subkey.length - lastEight.length - i];

        //XOR the right half with the last 8 bytes of the subKey
        byte[] xorResults = new byte[8];
        for (int i = 0; i < xorResults.length; i++)
            xorResults[i] = (byte) (rightHalf[i] ^ lastEight[i]);

        //get the first byte of the subKey for this round
        byte firstByte = subkey[0];

        //check each bit of the first byte. If it is a 1 bit,
        //then swap nibbles~!

        //turn the byte into a bit array
        int[] firstByteArray = new int[8];
        for (int i = 0; i < firstByteArray.length; i++)
            firstByteArray[i] = (firstByte >> firstByteArray.length - 1 - i) & 1;

        //check for nibble-swapping!
        for (int i = 0; i < firstByteArray.length; i++)
            if (firstByteArray[i] == 1)
                xorResults[i] = nibbleSwap(xorResults[i]);

        //time to S-box the xor result bytes
        byte[] postbox = new byte[8];
        for (int i = 0; i < 8; i++) {

            //get left nibble
            byte leftNibble = (byte) ((xorResults[i] & 0xf0) >>> 4);
            leftNibble = sboxhandler.Convert(0, leftNibble);

            //get right nibble
            byte rightNibble = (byte) (xorResults[i] & 0x0f);
            rightNibble = sboxhandler.Convert(1, rightNibble);

            postbox[i] = (byte) (rightNibble ^ (leftNibble << 4));
        }


        //now we need to permute all the bits in the postbox array...
        //we wrote a method that turns an int array, where each int represents
        //a 1 or 0, and permutes that. Therefore, we must convert our
        //postbox array of 8 bytes to an int array with 64 1's or 0's.
        //
        //To do this, i use an incemental mask!

        int[] bitarray = new int[64];
        int index = 0;

        for (int i = 0; i < 8; i++)
            for (int j = 7; j >= 0; j--) {
                bitarray[index] = (postbox[i] >> j) & 1;
                index++;
            }

        //turn this back into the byte array...

        bitarray = permutator.permute(bitarray);

        byte[] postperm = {0, 0, 0, 0, 0, 0, 0, 0};
        index = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (bitarray[index] == 1)
                    postperm[i] = (byte) (postperm[i] + (byte) Math.pow(2, (7 - j)));

                index++;
            }
        }

        return postperm;
    }

    /**
     * New operation: (byte)(left ^ (right << 4));
     * Swap the first 4 bits of a byte with the last 4 bits.
     *
     * @param swapByte The byte with the 2 nibbles to swap
     * @return byte with the nibbles swapped
     */
    public byte nibbleSwap(Byte swapByte) {
        byte right = (byte) (swapByte & 0x0f);
        byte left = (byte) ((swapByte & 0xf0) >>> 4);

        return (byte) (left ^ (right << 4));
    }


    public byte[] xorHalves(byte[] right, byte[] left) {

        byte[] xorResults = new byte[8];
        for (int i = 0; i < xorResults.length; i++)
            xorResults[i] = (byte) (right[i] ^ left[i]);

        return xorResults;
    }

    public byte[] stringToByteArray(String given) {

        byte[] plainbytes = new byte[16];
        for (int i = 0; i < 16; i++)
            plainbytes[i] = (byte) given.charAt(i);

        return plainbytes;
    }


    public byte[] concatHalves(byte[] left, byte[] right) {

        byte[] retarr = new byte[16];

        System.arraycopy(left, 0, retarr, 0, left.length);
        System.arraycopy(right, 0, retarr, left.length, right.length);

        return retarr;
    }
}


