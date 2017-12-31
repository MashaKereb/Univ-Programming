package command;

import beans.Bill;

import javax.servlet.ServletException;
import java.io.IOException;

public class PayCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException {
        Bill bill=new Bill();
        bill.setId(Integer.parseInt(req.getParameter("billID")));
        dao.setBillPaid(bill);
        req.getRequestDispatcher("Main?command=Confirm").forward(req,resp);

    }
}
