package lucifer;

import java.io.*;

/**
 * This is the main program that implements our swanjr.lucifer code.
 * This program can either encrypt or decrypt text depending on the command
 * line arguments.
 * When encrypting, the program takes in plaintext via an
 * ASCII file. The program then writes to a file with Hex Strings. When
 * decrypting a file, it assumes the file with the CipherText was generated
 * from this program, that is, it is represented in Hex Strings.
 * The key used in this program must be a 128-bit key represented
 * in a hex string.
 */
public class WeLoveLucy {

    public static final int ENCRYPT = 0;
    public static final int DECRYPT = 1;

    /**
     * Main method for running the Lucifer cipher.
     *
     * @param args -	option - e for encrypt, d for decrypt
     *             infile - the file with the input text
     *             outfile - the file to write to
     *             key - the 128 bit hex string key
     */
    public static void main(String args[]) {
        Lucifer lucifer = new Lucifer();
        String[] result;
        byte[][] result2;

        if (args.length != 4)
            usage();

        int choice = 0;
        if (args[0].charAt(0) == 'e')
            choice = ENCRYPT;
        else if (args[0].charAt(0) == 'd')
            choice = DECRYPT;
        else
            usage();

        File infile = new File(args[1]);
        File outfile = new File(args[2]);
        String sKey = args[3];
        byte[] key = Hex.toByteArray(sKey);
        BufferedReader br;
        PrintWriter pw;

        try {

            br = new BufferedReader(new FileReader(infile));
            String line;
            String plaintext = "";

            while ((line = br.readLine()) != null)
                plaintext = plaintext + line + '\n';

            String[] padStrings = StringPadding.padString(plaintext);

            result = new String[padStrings.length];
            result2 = new byte[padStrings.length][16];

            //for every string array, send it to be encrypted, store in result array

            if (choice == ENCRYPT)
                for (int x = 0; x < padStrings.length; x++) {
                    byte[] pb = lucifer.stringToByteArray(padStrings[x]);
                    byte[] cb = lucifer.encrypt(pb, key, 16);
                    result[x] = Hex.toString(cb);
                }
            else
                for (int x = 0; x < padStrings.length - 1; x = x + 2) {
                    //since 2 characters of hex is 1 plaintext character...
                    //concat two strings together.
                    String concatted = padStrings[x] + padStrings[x + 1];
                    byte[] pb = Hex.toByteArray(concatted);
                    byte[] cb = lucifer.decrypt(pb, key, 16);
                    result2[x] = cb;
                }


            //write it all to the outfile
            if (choice == ENCRYPT) {
                pw = new PrintWriter(new FileWriter(outfile));
                for (String aResult : result) {
                    pw.print(aResult);
                    pw.flush();
                }
            } else {
                String tempString = "";
                String overString = "";
                pw = new PrintWriter(new FileWriter(outfile));

                for (byte[] aResult2 : result2) {
                    for (int y = 0; y < 16; y++)
                        if (aResult2[y] != 0)
                            tempString = tempString + (char) aResult2[y];

                    overString = overString + tempString;
                    tempString = "";
                }

                overString = StringPadding.unpadString(overString);
                pw.println(overString);
                pw.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void usage() {
        System.err.println("Usage: java WeLoveLucy <option> <infile> " +
                "<outfile> <key>");
        System.err.println("    option   - e for encryption, " +
                "d for decryption");
        System.err.println("    infile   - the file with the initial text");
        System.err.println("    outfile  - the file to store output");
        System.err.println("    key      - 128-bit hex key string");
        System.exit(1);
    }
}

