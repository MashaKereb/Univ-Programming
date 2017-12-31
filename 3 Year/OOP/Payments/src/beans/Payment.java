package beans;

/**
 * Created by Masha Kereb on 07-Jun-17.
 */
public class Payment implements Entity{
    public enum Type {
        Withdrawal,
        Deposit
    }
    private int id;
    private int amount;
    private Type type;
    private Account account;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Payment(int id, int amount, Type type, Account account) {

        this.id = id;
        this.amount = amount;
        this.type = type;
        this.account = account;
    }

    public Payment(int amount, Type type, Account account){
        this(-1, amount, type, account);
    }
}
