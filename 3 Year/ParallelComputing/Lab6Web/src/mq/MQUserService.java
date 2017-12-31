package mq;

import entities.User;
import service.IUserService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Masha Kereb on 28-May-17.
 */
public class MQUserService implements IUserService {
    MQClient client;

    public MQUserService(MQClient client){
        this.client = client;
    }

    public MQUserService() {
        client = StaticMQClient.getInstance();
    }

    @Override
    public List<User> getAllElems() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.SELECT_ALL_USERS);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return (List<User>)ois.readObject();
    }

    @Override
    public User getElem(int id) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.SELECT_USER);
        oos.writeInt(id);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return (User)ois.readObject();
    }

    @Override
    public User getElem(Object id) throws SQLException, Exception {
        return getElem((int)id);
    }

    @Override
    public int addElem(User elem) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.ADD_NEW_USER);
        oos.writeObject(elem);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return ois.readInt();
    }

    @Override
    public boolean updateElem(User elem) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.UPDATE_USER);
        oos.writeObject(elem);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return ois.readBoolean();
    }

    @Override
    public boolean deleteElem(int id) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.DEL_USER);
        oos.writeInt(id);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return ois.readBoolean();
    }

    @Override
    public User getRandomElem() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.RANDOM_USER);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return (User)ois.readObject();
    }

    @Override
    public List<User> searchUsersByName(String name) throws SQLException, Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.SEARCH_USERS);
        oos.writeObject(name);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return (List<User>)ois.readObject();
    }
}
