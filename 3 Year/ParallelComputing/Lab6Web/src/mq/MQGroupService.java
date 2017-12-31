package mq;

import entities.Group;
import service.IGroupService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Masha Kereb on 28-May-17.
 */
public class MQGroupService implements IGroupService{
    MQClient client;
    public MQGroupService(){
        client = StaticMQClient.getInstance();
    }
    public MQGroupService(MQClient client){
        this.client = client;
    }


    @Override
    public List<Group> getAllElems() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.SELECT_ALL_GROUPS);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return (List<Group>)ois.readObject();
    }

    @Override
    public Group getElem(int id) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.SELECT_GROUP);
        oos.writeInt(id);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return (Group)ois.readObject();
    }

    @Override
    public Group getElem(Object id) throws SQLException, Exception {
        return getElem((int)id);
    }

    @Override
    public int addElem(Group elem) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.ADD_NEW_GROUP);
        oos.writeObject(elem);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return ois.readInt();
    }

    @Override
    public boolean updateElem(Group elem) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.UPDATE_GROUP);
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
        oos.writeInt(MQServer.DEL_GROUP);
        oos.writeInt(id);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return ois.readBoolean();
    }

    @Override
    public Group getRandomElem() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.RANDOM_GROUP);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return (Group)ois.readObject();
    }

    @Override
    public List<Group> searchGroupByNameAndDescription(String name) throws SQLException, Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeInt(MQServer.SEARCH_GROUPS);
        oos.writeObject(name);
        oos.close();
        byte[] res = client.call(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(res));
        return (List<Group>)ois.readObject();
    }
}
