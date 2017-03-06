import java.util.Vector;

/**
 * Created by masha on 05.03.17.
 */
public class BitMatrixPageFault {
    BitMatrix bm;

    public BitMatrixPageFault(int virtPageNum) {

        this.bm = new BitMatrix(virtPageNum);
    }
    public  void processPhysicalPage (int index){

        bm.setOnesInRow(index);
        bm.setZeroesInColumn(index);

    }

    public  void replacePage (Vector mem, int virtPageNum, int replacePageNum, ControlPanel controlPanel)
    {
        int oldestPage = bm.findMinimumRow();

        Page oldPage = (Page) mem.elementAt(0);
        int i = 0;
        for(; i < mem.size(); i++){
            oldPage = (Page) mem.elementAt(i);
            if(oldPage.physical == oldestPage){
                break;
            }
        }

        Page nextPage = (Page) mem.elementAt(replacePageNum);
        controlPanel.removePhysicalPage(i);

        nextPage.physical = oldPage.physical;
        controlPanel.addPhysicalPage(nextPage.physical, replacePageNum);


        bm.setOnesInRow(nextPage.physical);
        bm.setZeroesInColumn(nextPage.physical);

        oldPage.inMemTime = 0;
        oldPage.lastTouchTime = 0;
        oldPage.R = 0;
        oldPage.M = 0;
        oldPage.physical = -1;
    }

    public void clearMatrix(){
        bm.clear();
    }
}
