package socket;

import entities.Group;
import service.IGroupService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public class SocketGroupService implements IGroupService {
    MySocketClient client = MySocketClient.getInstance();

    @Override
    public List<Group> getAllElems() throws Exception {
        client.out.writeInt(MySocketServer.SELECT_ALL_GROUPS);
        client.out.flush();
        return (ArrayList<Group>) client.in.readObject();

    }

    @Override
    public Group getElem(int id) throws Exception {
        client.out.writeInt(MySocketServer.SELECT_GROUP);
        client.out.writeInt(id);
        client.out.flush();
        return (Group)client.in.readObject();
    }

    @Override
    public Group getElem(Object id) throws Exception {
        return this.getElem((int)id);
    }

    @Override
    public int addElem(Group elem) throws Exception {
        client.out.writeInt(MySocketServer.ADD_NEW_GROUP);
        client.out.writeObject(elem);
        client.out.flush();
        return client.in.readInt();
    }

    @Override
    public boolean updateElem(Group elem) throws Exception {
        client.out.writeInt(MySocketServer.UPDATE_GROUP);
        client.out.writeObject(elem);
        client.out.flush();
        return client.in.readBoolean();
    }

    @Override
    public boolean deleteElem(int id) throws Exception {
        client.out.writeInt(MySocketServer.DEL_GROUP);
        client.out.writeInt(id);
        client.out.flush();
        return client.in.readBoolean();
    }

    @Override
    public Group getRandomElem() throws Exception {
        client.out.writeInt(MySocketServer.RANDOM_GROUP);
        client.out.flush();
        return (Group)client.in.readObject();
    }

    @Override
    public List<Group> searchGroupByNameAndDescription(String name) throws SQLException {
        try {
            client.out.writeInt(MySocketServer.SEARCH_GROUPS);
            client.out.writeObject(name);
            client.out.flush();
            return (ArrayList<Group>)client.in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
