package creditcard;

import beans.Bill;


public class CreaditCardSimulator implements CreditCard {
    @Override
    public boolean getMoney(Bill bill) {
        return true;
    }
}
