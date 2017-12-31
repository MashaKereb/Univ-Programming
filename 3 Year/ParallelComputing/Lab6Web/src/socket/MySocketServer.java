package socket;

import db.DBGroupService;
import db.DBUserService;
import entities.Group;
import entities.User;
import service.IGroupService;
import service.IUserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public class MySocketServer extends Thread {
    ServerSocket serverSocket = null;


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

    public MySocketServer(String port) {
        try {
            serverSocket = new ServerSocket(Integer.parseInt(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            Socket socket = null;
            System.out.println("Waiting for a client...");
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(new MainServer(socket)).start();
            System.out.println("Client connected.");
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MainServer implements Runnable{
        IGroupService groupService;
        IUserService userService;
        Socket clientSocket = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;

        MainServer(Socket clSocket) {
            this.clientSocket = clSocket;
            userService = new DBUserService();
            groupService = new DBGroupService();
            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    int command = in.readInt();
                    System.out.println("command " + command);

                    switch (command) {
                        case ADD_NEW_USER:
                            addNewUser();
                            break;
                        case DEL_USER:
                            delUser();
                            break;
                        case UPDATE_USER:
                            updateUser();
                            break;
                        case SELECT_USER:
                            selectUser();
                            break;
                        case SELECT_ALL_USERS:
                            selectAllUsers();
                            break;
                        case SEARCH_USERS:
                            searchUsers();
                            break;
                        case RANDOM_USER:
                            randomUser();
                            break;


                        case ADD_NEW_GROUP:
                            addNewGroup();
                            break;
                        case DEL_GROUP:
                            delGroup();
                            break;
                        case UPDATE_GROUP:
                            updateGroup();
                            break;
                        case SELECT_GROUP:
                            selectGroup();
                            break;
                        case SELECT_ALL_GROUPS:
                            selectAllGroups();
                            break;
                        case SEARCH_GROUPS:
                            searchGroups();
                            break;
                        case RANDOM_GROUP:
                            randomGroup();
                            break;
                        case CLOSE:
                            clientSocket.close();
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private int addNewUser() throws IOException {
            try {
                int res = userService.addElem((User) in.readObject());
                out.writeInt(res);
                return res;

            } catch (Exception e) {
                e.printStackTrace();
                out.writeInt(-1);
                out.flush();
                return -1;
            }

        }

        private boolean delUser() throws IOException {
            try {
                out.writeBoolean(userService.deleteElem(in.readInt()));
                out.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                out.writeBoolean(false);
                out.flush();
                return false;
            }

        }

        private User randomUser() throws IOException {
            User u = null;
            try {
                u = userService.getRandomElem();
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.writeObject(u);
            out.flush();
            return u;
        }

        private Group randomGroup() throws IOException {
            Group g = null;
            try {
                g = groupService.getRandomElem();
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.writeObject(g);
            out.flush();
            return g;
        }

        private boolean updateUser() throws IOException {
            try {
                boolean res = userService.updateElem((User) in.readObject());
                out.writeBoolean(res);
                out.flush();
                return res;

            } catch (Exception e) {
                e.printStackTrace();
                out.writeBoolean(false);
                out.flush();
                return false;
            }

        }

        private User selectUser() throws IOException {
            int id = in.readInt();
            User u = null;
            try {
                u = userService.getElem(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.writeObject(u);
            out.flush();
            return u;
        }

        private List<User> selectAllUsers() throws IOException {
            List<User> res;
            try {
                 res = userService.getAllElems();
                out.writeObject(res);
            } catch (Exception e) {
                e.printStackTrace();
                res = new ArrayList<>();
                out.writeObject(res);
            }
            out.flush();
            return res;
        }

        private int addNewGroup() throws IOException {
            try {
                int res = groupService.addElem((Group) in.readObject());
                out.writeInt(res);
                out.flush();
                return res;

            } catch (Exception e) {
                e.printStackTrace();
                out.writeInt(-1);
                out.flush();
                return -1;
            }
        }

        private boolean delGroup() throws IOException {
            try {
                Boolean res = groupService.deleteElem(in.readInt());
                out.writeBoolean(res);
                out.flush();
                return res;

            } catch (Exception e) {
                e.printStackTrace();
                out.writeBoolean(false);
                out.flush();
                return false;
            }

        }

        private boolean updateGroup() throws IOException {
            try {
                boolean res = groupService.updateElem((Group) in.readObject());
                out.writeBoolean(res);
                out.flush();
                return res;

            } catch (Exception e) {
                e.printStackTrace();
                out.writeBoolean(false);
                out.flush();
                return false;
            }
        }

        private Group selectGroup() throws IOException {
            int id = in.readInt();
            Group g = null;
            try {
                g = groupService.getElem(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.writeObject(g);
            out.flush();
            return g;
        }

        private List<Group> selectAllGroups() throws IOException {
            List<Group> res;
            try {
                res = groupService.getAllElems();
                out.writeObject(res);
            } catch (Exception e) {
                res = new ArrayList<>();
                e.printStackTrace();
                out.writeObject(res);
            }
            out.flush();
            return res;

        }
        private List<Group> searchGroups() throws IOException{
            List <Group> g = new ArrayList<>();
            try {
            String name = (String)in.readObject();
                g = groupService.searchGroupByNameAndDescription(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.writeObject(g);
            out.flush();
            return g;

        }
        private List<User> searchUsers() throws IOException{
            List <User> u = new ArrayList<>();
            try {
                String name = (String)in.readObject();
                u = userService.searchUsersByName(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.writeObject(u);
            out.flush();
            return u;

        }
    }


}


