public class Cell {
     private int value;

    public Cell(int value) {
        this.value = value;
    }

     Cell() {
        this.value = 0;
    }

    synchronized public void setValue(int value) {
        this.value = value;
    }

    synchronized public int getValue() {
        return value;
    }
}
