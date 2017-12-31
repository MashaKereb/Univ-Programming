package rmi;

import db.DBGroupService;
import db.DBUserService;
import entities.Group;
import entities.User;
import service.IGroupService;
import service.IUserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by Masha Kereb on 28-May-17.
 */
public class RMIServer  extends UnicastRemoteObject implements IServer {
    IGroupService dbGroupService = new DBGroupService();
    IUserService dbUserService = new DBUserService();

    RMIServer() throws RemoteException {
        super();
    }

    @Override
    public int addNewUser(User user) throws Exception {
        return dbUserService.addElem(user);
    }

    @Override
    public boolean delUser(int id) throws Exception {
        return dbUserService.deleteElem(id);
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        return dbUserService.updateElem(user);
    }

    @Override
    public User selectUser(int id) throws Exception {
        return dbUserService.getElem(id);
    }

    @Override
    public List<User> selectAllUsers() throws Exception {
        return dbUserService.getAllElems();
    }

    @Override
    public List<User> searchUsers(String name) throws Exception {
        return dbUserService.searchUsersByName(name);
    }

    @Override
    public User randomUser() throws Exception {
        return dbUserService.getRandomElem();
    }

    @Override
    public int addNewGroup(Group group) throws Exception {
        return dbGroupService.addElem(group);
    }

    @Override
    public boolean delGroup(int id) throws Exception {
        return dbGroupService.deleteElem(id);
    }

    @Override
    public boolean updateGroup(Group group) throws Exception {
        return dbGroupService.updateElem(group);
    }

    @Override
    public Group selectGroup(int id) throws Exception {
        return dbGroupService.getElem(id);
    }

    @Override
    public List<Group> selectAllGroups() throws Exception {
        return dbGroupService.getAllElems();
    }

    @Override
    public List<Group> searchGroups(String name) throws Exception {
        return dbGroupService.searchGroupByNameAndDescription(name);
    }

    @Override
    public Group randomGroup() throws Exception {
        return dbGroupService.getRandomElem();
    }
}
