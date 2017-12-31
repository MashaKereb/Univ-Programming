package beans;

import java.util.List;

public class Bills {
    private List bills;

    public Bills(){}
    public Bills(List bills){

        this.bills = bills;
    }

    public List getBills() {
        return bills;
    }

    public void setBills(List bills) {
        this.bills = bills;
    }
}
