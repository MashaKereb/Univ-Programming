package socket;

import entities.User;
import service.IUserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public class SocketUserService implements IUserService {
    MySocketClient client = MySocketClient.getInstance();
    @Override
    public List<User> getAllElems() throws Exception {
        System.out.println("get all");
        client.out.writeInt(MySocketServer.SELECT_ALL_USERS);
        client.out.flush();
        return (ArrayList<User>) client.in.readObject();
    }

    @Override
    public User getElem(int id) throws Exception {
        client.out.writeInt(MySocketServer.SELECT_USER);
        client.out.writeInt(id);
        client.out.flush();
        return (User)client.in.readObject();
    }

    @Override
    public User getElem(Object id) throws SQLException, Exception {
        return this.getElem((int)id);
    }

    @Override
    public int addElem(User elem) throws Exception {
        client.out.writeInt(MySocketServer.ADD_NEW_USER);
        client.out.writeObject(elem);
        client.out.flush();
        return client.in.readInt();
    }

    @Override
    public boolean updateElem(User elem) throws Exception {
        client.out.writeInt(MySocketServer.UPDATE_USER);
        client.out.writeObject(elem);
        return client.in.readBoolean();
    }

    @Override
    public boolean deleteElem(int id) throws Exception {
        client.out.writeInt(MySocketServer.DEL_USER);
        client.out.writeInt(id);
        client.out.flush();
        return client.in.readBoolean();
    }

    @Override
    public User getRandomElem() throws Exception {
        client.out.writeInt(MySocketServer.RANDOM_USER);
        client.out.flush();
        return (User)client.in.readObject();
    }

    @Override
    public List<User> searchUsersByName(String name) throws SQLException {
        try {
            client.out.writeInt(MySocketServer.SEARCH_USERS);
            client.out.writeObject(name);
            client.out.flush();
            return (ArrayList<User>)client.in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
