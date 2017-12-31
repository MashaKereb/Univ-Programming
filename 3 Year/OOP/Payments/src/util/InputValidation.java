package util;

import beans.Menu;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class InputValidation {
    public static boolean dataValidation(HttpServletRequest req) {


        List<Integer> dishesID = new ArrayList<Integer>();
        HttpServletRequest request = (HttpServletRequest) req;
        Menu menu = (Menu) request.getSession().getAttribute("Menu");
        int menuSize = menu.getDishes().size();
        for (int i = 0; i < menuSize; i++) {
            if (req.getParameter(String.valueOf(i)) != null)
                dishesID.add(Integer.parseInt(req.getParameter(String.valueOf(i))));
        }
        if (dishesID.size() == 0) {
            req.setAttribute("Error", "No dishes have been checked");
            return false;
        }
        String tableNumber = (req.getParameter("table"));
        if (tableNumber == null || tableNumber.trim().length() == 0) {
            req.setAttribute("Error", "Wrong table number");
            // req.getRequestDispatcher("dataError.jsp").forward(req,resp);
            return false;
        }

        try {
            Integer.parseInt(tableNumber);
        } catch (NumberFormatException e) {
            req.setAttribute("Error", "Wrong table number");
            //req.getRequestDispatcher("dataError.jsp").forward(req,resp);
            return false;
        }
        req.setAttribute("dishesID", dishesID);
        req.setAttribute("table", tableNumber);
        return true;
    }
}
