package beans;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    int id;
    private List dishes=new ArrayList<Dish>();
    private int table;
    private int price;
    private boolean confirm;
    private boolean paid;
    User user;

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

    public List getDishes() {
        return dishes;
    }

    public void setDishes(List dishes) {
        this.dishes = dishes;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void addDish(Dish dish){
        dishes.add(dish);
    }
}
