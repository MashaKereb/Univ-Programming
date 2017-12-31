package command;

import beans.Bill;
import beans.Bills;
import beans.Dish;
import beans.User;
import util.InputValidation;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfirmCommand extends Command {
    @Override
    public void execute() throws IOException, ServletException {
        if (InputValidation.dataValidation(req)){
            List<Dish> dishses=((beans.Menu)req.getSession().getAttribute("Menu")).getDishes();
            User user=(User)req.getSession().getAttribute("User");
            Bill newBill=new Bill();
            List<Integer> dishesID= (List<Integer>) req.getAttribute("dishesID");

            newBill.setUser(user);
            newBill.setTable(Integer.parseInt((String)req.getAttribute("table")));
            newBill.setConfirm(false);
            newBill.setPaid(false);

            int totalPrice = 0;
            List<Dish> checkedDish=new ArrayList<Dish>();
            for(Dish dish:dishses){
                for(int dishID:dishesID){
                    if (dish.getId()==dishID)     {
                        checkedDish.add(dish);
                        totalPrice+=dish.getPrice();
                    }
                }
            }

            newBill.setPrice(totalPrice);
            newBill.setDishes(checkedDish);
            dao.addBill(newBill);
        }

        Bills bills=dao.getUserBills((User) req.getSession().getAttribute("User"));
        req.setAttribute("Bills",bills);
        req.getRequestDispatcher("web/confirmPage.jsp").forward(req,resp);
    }
}
