package mq;

import db.DBGroupService;
import db.DBUserService;
import entities.Group;
import entities.User;
import service.IGroupService;
import service.IUserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masha Kereb on 28-May-17.
 */
public class MQServer {
    public static final int ADD_NEW_USER = 1;
    public static final int DEL_USER = 2;
    public static final int UPDATE_USER = 3;
    public static final int SELECT_USER = 4;
    public static final int SELECT_ALL_USERS = 5;
    public static final int SEARCH_USERS = 6;
    public static final int RANDOM_USER = 7;

    public static final int ADD_NEW_GROUP = 11;
    public static final int DEL_GROUP = 12;
    public static final int UPDATE_GROUP = 13;
    public static final int SELECT_GROUP = 14;
    public static final int SELECT_ALL_GROUPS = 15;
    public static final int SEARCH_GROUPS = 16;
    public static final int RANDOM_GROUP = 17;

    public static final int CLOSE = 21;

    IUserService userService = new DBUserService();
    IGroupService groupService = new DBGroupService();

    public void pocessCommand(int command, ObjectInputStream in, ObjectOutputStream out) {
        try {
            switch (command) {
                case ADD_NEW_USER:
                    addNewUser(in, out);
                    break;
                case DEL_USER:
                    delUser(in, out);
                    break;
                case UPDATE_USER:
                    updateUser(in, out);
                    break;
                case SELECT_USER:
                    selectUser(in, out);
                    break;
                case SELECT_ALL_USERS:
                    selectAllUsers(out);
                    break;
                case SEARCH_USERS:
                    searchUsers(in, out);
                    break;
                case RANDOM_USER:
                    randomUser(out);
                    break;


                case ADD_NEW_GROUP:
                    addNewGroup(in, out);
                    break;
                case DEL_GROUP:
                    delGroup(in, out);
                    break;
                case UPDATE_GROUP:
                    updateGroup(in, out);
                    break;
                case SELECT_GROUP:
                    selectGroup(in, out);
                    break;
                case SELECT_ALL_GROUPS:
                    selectAllGroups(out);
                    break;
                case SEARCH_GROUPS:
                    searchGroups(in, out);
                    break;
                case RANDOM_GROUP:
                    randomGroup(out);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int addNewUser(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        try {
            int res = userService.addElem((User) in.readObject());
            out.writeInt(res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            out.writeInt(-1);
            return -1;
        }

    }

    private boolean delUser(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        try {
            out.writeBoolean(userService.deleteElem(in.readInt()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            out.writeBoolean(false);
            return false;
        }
    }

    private User randomUser(ObjectOutputStream out) throws IOException {
        User u = null;
        try {
            u = userService.getRandomElem();
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeObject(u);
        return u;
    }

    private Group randomGroup(ObjectOutputStream out) throws IOException {
        Group g = null;
        try {
            g = groupService.getRandomElem();
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeObject(g);
        return g;
    }

    private boolean updateUser(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        try {
            boolean res = userService.updateElem((User) in.readObject());
            out.writeBoolean(res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            out.writeBoolean(false);
            return false;
        }

    }

    private User selectUser(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        int id = in.readInt();
        User u = null;
        try {
            u = userService.getElem(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeObject(u);
        return u;
    }

    private List<User> selectAllUsers(ObjectOutputStream out) throws IOException {
        List<User> res;
        try {
            res = userService.getAllElems();
            out.writeObject(res);
        } catch (Exception e) {
            res = new ArrayList<>();
            out.writeObject(res);
            e.printStackTrace();
        }
        return res;
    }

    private int addNewGroup(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        try {
            int res = groupService.addElem((Group) in.readObject());
            out.writeInt(res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            out.writeInt(-1);
            return -1;
        }
    }

    private boolean delGroup(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        try {
            Boolean res = groupService.deleteElem(in.readInt());
            out.writeBoolean(res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            out.writeBoolean(false);
            return false;
        }

    }

    private boolean updateGroup(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        try {
            boolean res = groupService.updateElem((Group) in.readObject());
            out.writeBoolean(res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            out.writeBoolean(false);
            return false;
        }
    }

    private Group selectGroup(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        int id = in.readInt();
        Group g = null;
        try {
            g = groupService.getElem(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeObject(g);
        return g;
    }

    private List<Group> selectAllGroups(ObjectOutputStream out) throws IOException {
        List<Group> res;
        try {
            res = groupService.getAllElems();
            out.writeObject(res);
        } catch (Exception e) {
            res = new ArrayList<>();
            out.writeObject(res);
        }
        return res;

    }

    private List<Group> searchGroups(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        List<Group> g = new ArrayList<>();
        try {
            String name = (String) in.readObject();
            g = groupService.searchGroupByNameAndDescription(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeObject(g);
        return g;

    }

    private List<User> searchUsers(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        List<User> u = new ArrayList<>();
        try {
            String name = (String) in.readObject();
            u = userService.searchUsersByName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeObject(u);
        return u;

    }
}
