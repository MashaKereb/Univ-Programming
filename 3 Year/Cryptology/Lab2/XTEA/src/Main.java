/**
 * Created by Masha Kereb on 03-Dec-16.
 */
public class Main {
    public static void main(String[] args) {
        int[] key = {34563, 9835575, 8635552, 957352};
        XTEA xtea = new XTEA(key);
        String data = "Hello World!";
        byte[] encrypted = xtea.encrypt(data.getBytes());
        System.out.println(encrypted);
        byte[] decrypted = xtea.decrypt(encrypted);

        System.out.println(new String(decrypted));
    }
}
