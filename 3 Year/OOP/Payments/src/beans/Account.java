package beans;

/**
 * Created by Masha Kereb on 07-Jun-17.
 */
public class Account implements Entity{
    public enum Status{
        Active, Block
    }
    private int id;
    private String number;
    private int balance;
    private Status status;
    private User user;

    public Account(int id, String number, int balance, Status status, User user) {
        this.id = id;
        this.number = number;
        this.balance = balance;
        this.status = status;
        this.user = user;
    }
    public Account(String number, int balance, Status status, User user) {
        this(-1, number, balance, status, user);
    }


        public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
