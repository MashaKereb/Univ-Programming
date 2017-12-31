package rmi;

import entities.User;
import service.IUserService;

import java.util.List;

/**
 * Created by Masha Kereb on 28-May-17.
 */
public class RMIUserService implements IUserService {

    IServer server = RMIServerRunner.getServerInstance();

    @Override
    public List<User> getAllElems() throws Exception {
        return server.selectAllUsers();
    }

    @Override
    public User getElem(int id) throws Exception {
        return server.selectUser(id);
    }

    @Override
    public User getElem(Object id) throws Exception {
        return server.selectUser((int)id);
    }

    @Override
    public int addElem(User elem) throws Exception {
        return server.addNewUser(elem);
    }

    @Override
    public boolean updateElem(User elem) throws Exception {
        return server.updateUser(elem);
    }

    @Override
    public boolean deleteElem(int id) throws Exception {
        return server.delUser(id);
    }

    @Override
    public User getRandomElem() throws Exception {
        return server.randomUser();
    }

    @Override
    public List<User> searchUsersByName(String name) throws Exception {
        return server.searchUsers(name);
    }
}
