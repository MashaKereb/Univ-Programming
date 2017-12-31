package command;

import javax.servlet.ServletException;
import java.io.IOException;

public class MenuCommand extends Command {
    @Override
    public void execute() {
        beans.Menu menu = dao.getMenu();
        req.getSession().setAttribute("Menu", menu);

        try {

            if (menu == null) {
                req.getRequestDispatcher("web/dataError.jsp").forward(req, resp);
            } else
                req.getRequestDispatcher("web/menu.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
