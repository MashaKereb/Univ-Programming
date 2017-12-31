import java.util.Random;

/**
 * Created by Masha Kereb on 26-Feb-17.
 */
public class Monk implements Comparable{
    Integer QiEnergy;

    public Monk(Integer qiEnergy) {
        QiEnergy = qiEnergy;
    }

    public Monk() {
        Random r = new Random();
        QiEnergy = r.nextInt(10000);
    }

    public Integer getQiEnergy() {
        return QiEnergy;
    }

    @Override
    public int compareTo(Object o) {
        Monk other = (Monk)o;
        if(this.QiEnergy > other.QiEnergy) {
            return 1;
        } else if (this.QiEnergy < other.QiEnergy){
            return -1;
        } else {
            return 0;
        }
    }
    public static Monk max(Object first, Object second) {
        return Monk.max((Monk)first, (Monk)second);
    }

    public static Monk max(Monk first, Monk second){
            if(first.QiEnergy > second.QiEnergy){
                return first;
            } else {
                return second;
            }
    }
}
