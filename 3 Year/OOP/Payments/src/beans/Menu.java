package beans;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private int id;
    private String name;
    private List dishes=new ArrayList<Dish>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getDishes() {
        return dishes;
    }

    public void setDishes(List dishes) {
        this.dishes = dishes;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
