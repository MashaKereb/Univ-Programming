
/**
 * Created by Masha Kereb on 03-Dec-16.
 */
import java.nio.charset.Charset;

public class
Main {
    public static void main(String[] args) {
        Charset cs = Charset.forName("UTF-8");
        String value = "Hello";
        byte[] data = value.getBytes(cs);
        byte[] hash = Sha256.encode(data);

        StringBuilder hex = new StringBuilder(hash.length * 2);
        int len = hash.length;
        for (int i = 0 ; i < len ; i++) {
            hex.append(String.format("%02X", hash[i]));
        }
        System.out.println( hex.toString());
    }
}
