import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Masha Kereb on 05-Dec-16.
 */
public class Main {

    static final int MAX_ELEM=100000;
    static final int THREAD_NUM=10;

    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<Integer> hashMap=new ConcurrentHashMap(29);

        ArrayList<Integer> arrayList=new ArrayList<>();
        Thread[] threads=new Thread[THREAD_NUM];

        for(int i=0;i<MAX_ELEM;i++)
            arrayList.add(i);

        for(int num=0;num<THREAD_NUM;num++){
            threads[num]=new HashMapUser(num, hashMap, arrayList);
            threads[num].start();
        }
        for(int i=0;i<THREAD_NUM;i++)
            threads[i].join();
        System.out.print(hashMap);
    }

}

class HashMapUser extends  Thread{
    private final int threadNum;
    private ConcurrentHashMap hashMap;
    private ArrayList arrayList;

    HashMapUser(int num, ConcurrentHashMap hashMap, ArrayList arrayList){
        this.threadNum = num;
        this.arrayList =  arrayList;
        this.hashMap = hashMap;
    }
    @Override
    public void run(){
        Random r = new Random();

        for(int t=0;t<9;t++) {
            try {
                Thread.sleep(r.nextInt(1000));
            } catch(Exception e){}
            if (t % 2 == 0)
                hashMap.add(arrayList.get(t + 10 * threadNum));

            else
                hashMap.remove(arrayList.get(t + 10 * threadNum - 1));
        }
    }

}
