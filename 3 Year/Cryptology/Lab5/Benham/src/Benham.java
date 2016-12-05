/**
 * Created by Masha Kereb on 05-Dec-16.
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Benham {

    private static DCT dct = new DCT();


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

                int [][] dcp = dct.ForwardDCT(a);
                double k = Math.abs(dcp[1][2]) - Math.abs(dcp[2][1]);
                if (getBit(msg[i], j) == 1) {
                    if (k <= 25) {
                        dcp[1][2] = (dcp[2][1]>=0) ? Math.abs(dcp[2][1]) + 100 : -1*(Math.abs(dcp[2][1]) + 100);
                    }
                } else if (k >= -25) {
                    dcp[2][1] = (dcp[2][1]>=0) ? Math.abs(dcp[1][2]) + 100 : -1*(Math.abs(dcp[1][2]) + 100);
                }

                setBlueColorArray(img, i * 8 + j, dct.InverseDCT(dcp));
            }
        }
    }

    public static void getCipherMessage(BufferedImage img) {
        int i = 0;
        byte[] b = new byte[1];
        while (true) {
            byte o;
            int[][] a = getBlueColorArray(img, i);
            int[][] dcp = dct.ForwardDCT(a);
            double k = Math.abs(dcp[1][2]) - Math.abs(dcp[2][1]);
            if (k >= 25) {
                o = 1;
            } else if (k <= -25) {
                o = 0;
            } else {
                o = -1;
            }

            b[0] = setBit(b[0], o, i % 8);

            if ((int)(o) == -1)
                break;
            if (i % 8 == 7) {
                System.out.print(new String(b));
                b[0] = 0;
            }
            i++;
        }
        System.out.println();
    }
    public static int[][] getBlueColorArray(BufferedImage img, int numOfBlock) {
        int numOfRows = img.getHeight() / 8;
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
        int numOfRows = img.getHeight() / 8;
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
        getCipherPicture(img, "Hello world!");
        ImageIO.write(img, "bmp", new File("secret.bmp"));


                BufferedImage img2 = ImageIO.read(new File("secret.bmp"));
                System.out.print("Cipher text: ");
                getCipherMessage(img2);

    }
}

