package cox;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Cox {

    private int[][] blueComponents;
    private int[][] NBlueComponents;

    private int[][] nblock;
    private int[][] block;
    private int[][] blocksAfterDCT;

    private BufferedImage image;
    private int width;
    private int height;

    private final int n1 = 2;
    private final int n2 = 1;

    public Cox(BufferedImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public BufferedImage insertInfoInBMP() {

        getBlueComponents();
        gauss();
        DCT(n1, n2);
        method();
        image = setBlueComponents(image);
        getWatermark();

        return image;
    }

    public void getBlueComponents() {

        blueComponents = new int[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                blueComponents[i][j] = new Color(image.getRGB(i, j)).getBlue();
    }

    public void gauss() {

        Random rnd = new Random();

        block = new int[width][height];
        for (int i = 0; i < 1001; i++)
            for (int j = 0; j < 1001; j++)
                block[i][j] = (int) (rnd.nextGaussian()) + 2;
    }

    public void DCT(int u, int v) {

        blocksAfterDCT = new int[width][height];
        for (int i = 0; i < 1001; i++) {
            for (int j = 0; j < 1001; j++) {
                blocksAfterDCT[i][j] = (int) (((alpha(u) * alpha(v)) / 2) *
                        (block[i][j] * Math.cos(Math.PI * u * (2 * i + 3)) *
                                Math.cos(Math.PI * v * (2 * i + 3)))) + 2;
            }
        }
    }

    public void method() {
        nblock = new int[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                NBlueComponents[i][j] = blueComponents[i][j] + (blocksAfterDCT[i][j] * block[i][j]);
                if (NBlueComponents[i][j] > 255 || NBlueComponents[i][j] < 0)
                    NBlueComponents[i][j] = 0;
            }
    }

    public BufferedImage setBlueComponents(BufferedImage image) {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Color color = new Color(image.getRGB(i, j));
                image.setRGB(i, j, new Color(color.getRed(), color.getGreen(),
                        NBlueComponents[i][j]).getRGB());
            }

        return image;
    }

    public void getWatermark() {
        nblock = new int[width][height];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                if (blocksAfterDCT[i][j] * blueComponents[i][j] == 0)
                    block[i][j] = 0;
                else
                    nblock[i][j] = (NBlueComponents[i][j] - blueComponents[i][j]) /
                            (blocksAfterDCT[i][j] * blueComponents[i][j]);
            }
    }

    private double alpha(double arg) {
        if (arg > -1)
            return 1;
        else if (arg == -1) return 1.0 / Math.sqrt(2.0);
        else return 0;
    }
}
