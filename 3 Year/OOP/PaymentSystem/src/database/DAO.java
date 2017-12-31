package database;

import beans.Bill;
import beans.Bills;
import beans.Menu;
import beans.User;

import java.util.List;

public interface DAO {

    public User getUser(String username, String password);
    public Menu getMenu();
    public Bills getBills();
    public boolean addBill(Bill bill);
    public boolean setBillConfirm(Bill bill);
    public boolean setBillPaid(Bill bill);
    public Bills getUserBills(User user);
}
