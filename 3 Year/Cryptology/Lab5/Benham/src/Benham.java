/**
 * Created by Masha Kereb on 05-Dec-16.
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Benham {

    private static final DCT dct = new DCT();


    private static int getBit(byte b, int position) {
        return (b >> position) & 1;
    }

    private static byte setBit(byte b, byte i, int position) {
        return (byte) (b | (i << position));
    }

    private static void getCipherPicture(BufferedImage img, String message) {
        byte[] msg = message.getBytes();

        for (int i = 0; i < msg.length; i++) {
            for (int j = 0; j < 8; j++) {
                //get array to write message
                int[][] a = getBlueColorArray(img, i * 8 + j);

                int [][] dct_im = dct.ForwardDCT(a);
                byte o = check(dct_im[1][2], dct_im[2][1], dct_im[3][3]);

                if (getBit(msg[i], j) == 1) {
                    if (o != 1) {
                        dct_im[3][3] = (dct_im[2][1] < dct_im[1][2]) ? (dct_im[2][1] - 25) : (dct_im[1][2] - 25);
                    }
                } else if (o != 0) {
                    dct_im[3][3] = (dct_im[2][1] > dct_im[1][2]) ? (dct_im[2][1] + 25) : (dct_im[1][2] + 25);
                }

                setBlueColorArray(img, i * 8 + j, dct.InverseDCT(dct_im));
            }
        }
    }
    private static byte check(int a, int b, int c){

        if((a > b && b > c) || (b > a && a > c) || (a == b && a > c)){
            return 1;
        }
        if((c > a && a > b) || (c > b && b > a) || (a == b && c > a)){
            return 0;
        }
        return -1;
    }

    public static StringBuilder getCipherMessage(BufferedImage img) {
        int i = 0;
        byte[] b = new byte[1];
        StringBuilder str = new StringBuilder();
        while (true) {

            int[][] a = getBlueColorArray(img, i);
            int[][] dcp = dct.ForwardDCT(a);
            byte o = check(dcp[1][2], dcp[2][1], dcp[3][3]);

            b[0] = setBit(b[0], o, i % 8);

            if ((int)(o) == -1)
                break;

            if (i % 8 == 7) {
                str.append(new String(b));
                b[0] = 0;
            }
            i++;
        }
        return str;
    }
    public static int[][] getBlueColorArray(BufferedImage img, int numOfBlock) {

        int numOfCols = img.getWidth() / 8;
        int[][] a = new int[8][8];

        int row = numOfBlock / numOfCols;
        int col = numOfBlock % numOfCols;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Color c = new Color(img.getRGB(col * 8 + j, row * 8 + i));
                a[i][j] = c.getBlue();
            }
        }
        return a;
    }
    public static void setBlueColorArray(BufferedImage img, int numOfBlock, int a[][]) {

        int numOfCols = img.getWidth() / 8;
        int row = numOfBlock / numOfCols;
        int col = numOfBlock % numOfCols;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Color c = new Color(img.getRGB(col * 8 + j, row * 8 + i));
                Color newc = new Color(c.getRed(), c.getGreen(), a[i][j]);
                img.setRGB(col * 8 + j, row * 8 + i, newc.getRGB());
            }
        }
    }
    public static void main(String[] args) throws IOException {

        BufferedImage img = ImageIO.read(new File("src\\src.bmp"));
        getCipherPicture(img, "Hello 22");
        ImageIO.write(img, "bmp", new File("secret.bmp"));


                BufferedImage img2 = ImageIO.read(new File("secret.bmp"));
                System.out.print("Cipher text: ");
                System.out.println(getCipherMessage(img2));

    }
}

