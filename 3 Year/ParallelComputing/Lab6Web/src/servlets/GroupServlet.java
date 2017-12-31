package servlets;

import entities.Group;
import rmi.RMIGroupService;
import service.IGroupService;

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

/**
 * Created by Masha Kereb on 20-May-17.
 */

@WebServlet(
        name = "GroupServlet",
        urlPatterns = {"/group"}
)
public class GroupServlet extends HttpServlet {

    // // static service
    // IGroupService groupService = new GroupService(GroupList.getInstance());

    // // db service
    // IGroupService groupService = new DBGroupService();

    // // socket service
    // IGroupService groupService = new SocketGroupService();

    //    // rmi service
        IGroupService groupService = new RMIGroupService();

    // mq service
    //IGroupService groupService = new MQGroupService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("searchAction");
        if (action!=null){
            switch (action) {
                case "searchById":
                    searchGroupById(req, resp);
                    break;
                case "searchByName":
                    searchGroupByName(req, resp);
                    break;
            }
        }else{
            List<Group> result = null;
            try {
                result = groupService.getAllElems();
            } catch (Exception e) {
                Logger.getLogger(GroupServlet.class.getName()).log(Level.SEVERE, null, e);
            }
            forwardListGroups(req, resp, result);
        }
    }

    private void searchGroupById(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int idGroup = Integer.valueOf(req.getParameter("idGroup"));
        Group group = null;
        try {
            group = groupService.getElem(idGroup);
        } catch (Exception ex) {
            Logger.getLogger(GroupServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setAttribute("group", group);
        req.setAttribute("action", "edit");
        String nextJSP = "/jsp/new-group.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        dispatcher.forward(req, resp);
    }

    private void searchGroupByName(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String groupName = req.getParameter("groupName");
        List<Group> result = null;
        try {
            result = groupService.searchGroupByNameAndDescription(groupName);
        } catch (Exception e) {
            Logger.getLogger(GroupServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        req.setAttribute("searchVal", groupName);
        forwardListGroups(req, resp, result);

    }

    private void forwardListGroups(HttpServletRequest req, HttpServletResponse resp, List groupList)
            throws ServletException, IOException {
        String nextJSP = "/jsp/list-groups.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("groupList", groupList);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "add":
                addGroupAction(req, resp);
                break;
            case "edit":
                editGroupAction(req, resp);
                break;
            case "remove":
                removeGroupByName(req, resp);
                break;
        }

    }

    private void addGroupAction(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");

        Group group = new Group(name, description);
        try {
            long idGroup = groupService.addElem(group);
        } catch (Exception e) {
            Logger.getLogger(GroupServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        resp.sendRedirect("group");
    }

    private void editGroupAction(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        int idGroup = Integer.valueOf(req.getParameter("idGroup"));
        Group group;
        try {
            Group prevGroupVersion = groupService.getElem(idGroup);
            group = new Group(name, description, prevGroupVersion.getCreated(), idGroup);
        } catch (Exception e) {
            group = new Group(name, description);
        }

        try {
            groupService.updateElem(group);
        } catch (Exception e) {
            Logger.getLogger(GroupServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        resp.sendRedirect("group");
    }

    private void removeGroupByName(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int idGroup = Integer.valueOf(req.getParameter("idGroup"));
        try {
            groupService.deleteElem(idGroup);
        } catch (Exception e) {
            Logger.getLogger(GroupServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        resp.sendRedirect("group");
    }

}
