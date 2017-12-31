package command;

import beans.Bill;
import beans.Bills;
import database.ConcreteDAO;

import javax.servlet.ServletException;
import java.io.IOException;

public class BillCommand extends Command {


    @Override
    public void execute() throws ServletException, IOException {
        Bill bill=new Bill();
        boolean confirm=true;
        try {
            bill.setId(Integer.parseInt(req.getParameter("billID")));
            new ConcreteDAO().setBillConfirm(bill);
        } catch (NumberFormatException e){
            confirm=false;
        }
        if(confirm)
            new ConcreteDAO().setBillConfirm(bill);

        Bills bills=new ConcreteDAO().getBills();
        req.setAttribute("Bills",bills);
        if (bills!=null)
            req.getRequestDispatcher("/bills.jsp").forward(req,resp);
        else
            req.getRequestDispatcher("/dataError.jsp").forward(req,resp);


    }
}
