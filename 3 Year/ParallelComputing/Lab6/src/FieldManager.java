import java.util.Arrays;
import java.util.Random;

public class FieldManager {

    private Cell[][] visibleField;
    private Cell[][] hiddenField;

    private int	width, height;

    private int[][]	neighbors = new int[][] {
            {-1, -1}, {0, -1}, {1, -1},
            {-1, 0},           {1, 0},
            {-1, 1},  {0, 1},  {1, 1}};


    FieldManager(int width, int height) {
        this.width = width;
        this.height = height;

        visibleField = new Cell[height][width];
        hiddenField = new Cell[height][width];

        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++) {
                hiddenField[i][j] = new Cell();
                visibleField[i][j] = new Cell();
            }
    }
    void swapFields(){
        visibleField =  hiddenField;
        hiddenField = new Cell[height][width];
        for(int i = 0; i < width; i++)
            for(int j = 0; j < width; j++) {
                hiddenField[i][j] = new Cell();
            }
    }

     int getWidth() {
        return width;
    }

     int getHeight() {
        return height;
    }

     void clear() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                visibleField[i][j].setValue(0);
    }

    public void setCell(int x, int y, Cell c) {
        visibleField[x][y] = c;
    }

     Cell getCell(int x, int y) {
        return visibleField[x][y];
    }

     void simulate(int generationsNumber, int start, int finish) {
        for (int gen = 1; gen <= generationsNumber; gen++) {
            for (int x = start; x < finish; x++) {
                for (int y = 0; y < width; y++) {
                     hiddenField[x][y].setValue(
                            simulateCell(
                                    visibleField[x][y].getValue(),
                                    hiddenField[x][y].getValue(),
                                    countNeighborsSameGeneration(x, y, gen),
                                    countAllNeighbors(x, y), gen
                            )
                    );
                }
            }
        }
    }
    private int simulateCell(int cell, int inNewField, int neighbors, int allNeighbors, int type) {
        if (cell == type && neighbors < 2) return 0;
        if (cell == type && (neighbors == 2 || neighbors == 3)) return type;
        if (cell == type && allNeighbors > 3) return 0;
        if (cell != type && neighbors == 3) return type;
        return inNewField;
    }

    public void generate(int civAmount, float density) {
        Random rand = new Random();
        int cellAmount = (int) (width * height * density / civAmount);

        for (int i = 1; i <= civAmount; i++) {
            int setCount = 0;
            while (setCount < cellAmount){
                int randomX = rand.nextInt(width);
                int randomY = rand.nextInt(height);

                if (visibleField[randomX][randomY].getValue() == 0) {
                    visibleField[randomX][randomY].setValue(i);
                    setCount ++;
                }
            }
        }
    }

    private int countNeighborsSameGeneration(int cellX, int cellY, int generationId) {
        return (int)Arrays.stream(neighbors).filter((neighbor) -> {
            int x = cellX + neighbor[0];
            int y = cellY + neighbor[1];
            if (y >= 0 && y < width && x >= 0 && x < height){
                return (visibleField[x][y].getValue() == generationId);
            }
            return false;
        }).count();
    }

    private int countAllNeighbors(int cellX, int cellY) {
        return (int)Arrays.stream(neighbors).filter((neighbor) -> {
            int x = cellX + neighbor[0];
            int y = cellY + neighbor[1];
            if (y >= 0 && y < width && x >= 0 && x < height){
                return (visibleField[x][y].getValue() > 0);
            }
            return false;
        }).count();
    }
}
