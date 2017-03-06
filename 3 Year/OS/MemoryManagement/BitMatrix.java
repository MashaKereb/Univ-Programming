import org.junit.jupiter.api.Test;

import java.util.BitSet;

/**
 * Created by masha on 05.03.17.
 */
public class BitMatrix {
    private BitSet[] columns;

    public BitMatrix(int matrixSize) {
        this.columns = new BitSet[matrixSize];
        for (int i = 0; i < matrixSize; i++){
            this.columns[i] = new BitSet(matrixSize);
        }
    }

    public void setZeroesInColumn(int index){
        this.columns[index].clear();
    }

    public void setOnesInRow(int index){
        for(int i = 0; i < this.columns.length; i++)
            this.columns[i].set(index, true);
    }

    private  long bitRowToLong(int index){
        long bitLong = 0L;
        for(int i = 0 ; i < columns.length; i++)
            if(columns[i].get(index))
                bitLong |= (1L << i);

        return bitLong;
    }

    public int findMinimumRow(){
        long temp;
        for (int i = 0; i < this.columns.length; i++){
            temp = this.bitRowToLong(i);
            if (temp == 0L){
               return  i;
            }
        }
        return  -1;
    }

    public void clear(){
        int matrixSize = columns.length;
        for (int i = 0; i < matrixSize; i++){
            this.columns[i].clear();
        }
    }

}
