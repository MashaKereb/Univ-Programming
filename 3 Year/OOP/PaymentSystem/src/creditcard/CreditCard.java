package creditcard;

import beans.Bill;
import beans.User;


public interface CreditCard {
    public boolean getMoney(Bill bill);
}
