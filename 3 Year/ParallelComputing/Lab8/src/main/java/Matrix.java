import java.util.Random;

/**
 * Created by Masha Kereb on 10-Apr-17.
 */
public class Matrix {
    int[] matrix;
    int[] transpose;
    int height, width;
    final int MAX_NUMBER = 100;
    String name;

    public Matrix(int height, int width, String name) {
        this.height = height;
        this.width = width;
        this.matrix = new int[width*height];
        this.name = name;
    }

    public Matrix(int size, String name) {
        this.height = size;
        this.width = size;
        this.matrix = new int[width*height];
        this.name = name;
    }

    public void fillRandom(){
        this.fillRandom(MAX_NUMBER);
    }

    public void fillRandom(int maxNumber) {
        Random rand = new Random();
        for(int i = 0; i < height*width; i++){
            this.matrix[i] = rand.nextInt(maxNumber);

        }
    }

    public void fillWithNumber(int number){
        for(int i = 0; i < height*width; i++){
            this.matrix[i] = number;
        }
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                res.append(matrix[i * height + j]);
                res.append("  ");
            }
            res.append("\n");
        }
        res.append("Matrix:  ").append(name);
        return res.toString();
    }

    public Matrix multiply(Matrix other){
        assert this.height == other.width;
        Matrix res = new Matrix(this.width, other.height, this.name + "*" + other.name);
        res.fillWithNumber(0);
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < other.height; j++)
                for (int k = 0; k < this.height; k++)
                    res.matrix[i * this.width + j] += this.matrix[i * this.width + k] * other.matrix[k * other.width + j];

        }
        return res;
    }
}
