package command;

import beans.User;
import database.UserDAO;

import java.io.IOException;

public class AuthorizationCommand extends Command {

    @Override
    public void execute() throws IOException {

        UserDAO userDao = (UserDAO) daoFactory.getDataAccessObject("user");

        User user = null;
        try {
            user = userDao.getElemsForCriteria("email", req.getParameter("email")).get(0);
            System.out.println("autho command");
            if (user != null) {
                if (user.getPassword().equals(req.getParameter("password"))) {

                    req.getSession().setAttribute("User", user);
                    resp.sendRedirect("/Main?command=Menu");
                }
                else {
                    throw new Exception();
                    // TODO: add new exception for incorrect email or password
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
