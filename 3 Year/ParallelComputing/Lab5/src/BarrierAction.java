import java.util.ArrayList;

/**
 * Created by Masha Kereb on 19-Mar-17.
 */
public class BarrierAction implements Runnable {
    private ArrayList<ArrayThread> threads = new ArrayList<>();

    public void addThread(ArrayThread thread) {
        this.threads.add(thread);
    }

    @Override
    public void run() {
        int mean = this.calculateMean();
        System.out.printf("The mean = %d\n", mean);
        if(this.checkIfEnd(mean)){
            this.interruptAll();
            System.out.println("The barrier is done!");
        } else{
            this.setActions(mean);
            System.out.println("Next iteration!");
            System.out.println("----------------------------------------------------");
        }

    }
    private void interruptAll(){
        for (ArrayThread thread : threads) {
            thread.interrupt();
        }
    }
    private boolean checkIfEnd(int mean){
        for (ArrayThread thread : threads) {
            if(mean != thread.getCurrentSum()){
                return false;
            }
        }
        return true;
    }

    private int calculateMean() {
        int sum = 0;
        for (ArrayThread thread : threads) {
            sum += thread.getCurrentSum();
        }
        return sum / threads.size();
    }

    private void setActions(int mean){
        for (ArrayThread thread : threads) {
            if (mean > thread.getCurrentSum()){
                thread.setNexAction(Action.INCREMENT);
            } else if (mean < thread.getCurrentSum()) {
                thread.setNexAction(Action.DECREMENT);
            } else {
                thread.setNexAction(Action.NOACTION);
            }
        }
    }
}
