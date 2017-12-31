package servlets;

import entities.User;
import rmi.RMIGroupService;
import rmi.RMIUserService;
import service.IGroupService;
import service.IUserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(
        name = "UserServlet",
        urlPatterns = {"/user"}
)
public class UserServlet extends HttpServlet {

    //    // socket service
    //    IUserService userService = new SocketUserService();
    //    IGroupService groupService = new SocketGroupService();

    //    // static service
    //    IGroupService groupService = new GroupService(GroupList.getInstance());
    //    IUserService userService = new UserService(UserList.getInstance());


    //    // db service
    //    IUserService userService = new DBUserService();
    //    IGroupService groupService = new DBGroupService();

        // mq service
      //  IUserService userService = new MQUserService();
        //IGroupService groupService = new MQGroupService();

        // rmi service
        IUserService userService = new RMIUserService();
        IGroupService groupService = new RMIGroupService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("searchAction");
        if (action!=null){
            switch (action) {
                case "searchById":
                    searchUserById(req, resp);
                    break;
                case "searchByName":
                    searchUserByName(req, resp);
                    break;
                case "newUser":
                    newUserPage(req, resp);
                    break;
            }
        }else{
            List<User> result = null;
            try {
                result = userService.getAllElems();
            } catch (Exception e) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, e);
            }
            forwardListUsers(req, resp, result);
        }
    }
    private void newUserPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("groupList", groupService.getAllElems());
        } catch (Exception e) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        String nextJSP = "/jsp/new-user.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        dispatcher.forward(req, resp);
    }

    private void searchUserById(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int idUser = Integer.valueOf(req.getParameter("idUser"));
        User user = null;
        try {
            user = userService.getElem(idUser);
        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setAttribute("user", user);
        try {
            req.setAttribute("groupList", groupService.getAllElems());
        } catch (Exception e) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        req.setAttribute("action", "edit");
        String nextJSP = "/jsp/new-user.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        dispatcher.forward(req, resp);
    }

    private void searchUserByName(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String userName = req.getParameter("userName");
        List<User> result = null;
        try {
            result = userService.searchUsersByName(userName);
        } catch (Exception e) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        req.setAttribute("searchVal", userName);
        forwardListUsers(req, resp, result);

    }

    private void forwardListUsers(HttpServletRequest req, HttpServletResponse resp, List userList)
            throws ServletException, IOException {
        String nextJSP = "/jsp/list-users.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("userList", userList);

        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "add":
                addUserAction(req, resp);
                break;
            case "edit":
                editUserAction(req, resp);
                break;
            case "remove":
                removeUserByName(req, resp);
                break;
        }


    }

    private void addUserAction(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String status = req.getParameter("status");
        String role = req.getParameter("role");
        String email = req.getParameter("email");
        String group = req.getParameter("idGroup");
        User user = null;
        try {
            user = new User(name, email, User.Status.valueOf(status), User.Role.valueOf(role),
                    groupService.getElem(Integer.valueOf(group)));
            userService.addElem(user);
        } catch (Exception e) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, e);
        }

        resp.sendRedirect("user");
    }

    private void editUserAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String status = req.getParameter("status");
        String role = req.getParameter("role");
        String email = req.getParameter("email");
        String group = req.getParameter("idGroup");
        int idUser = Integer.valueOf(req.getParameter("idUser"));
        User user = null;
        try {
            User prevUserVersion = userService.getElem(idUser);
            user = new User(name, email, User.Status.valueOf(status), User.Role.valueOf(role),
                    groupService.getElem(Integer.valueOf(group)), prevUserVersion.getCreated(), idUser);
        } catch (Exception e) {
            try {
                user = new User(name, email, User.Status.valueOf(status), User.Role.valueOf(role),
                        groupService.getElem(Integer.valueOf(group)));
            } catch (Exception e1) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, e1);
                resp.sendRedirect("user");
                return;
            }
        }
        try {
            userService.updateElem(user);
        } catch (Exception e) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        resp.sendRedirect("user");
    }

    private void removeUserByName(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int idUser = Integer.valueOf(req.getParameter("idUser"));
        try {
            userService.deleteElem(idUser);
        } catch (Exception e) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        resp.sendRedirect("user");
    }

}
