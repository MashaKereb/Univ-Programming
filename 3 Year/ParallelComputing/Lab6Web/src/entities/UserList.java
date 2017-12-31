package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class UserList {
    private static final List<User> userList = new ArrayList();

    private UserList(){
    }

    static{
        List<Group> gr = GroupList.getInstance();
        Random rand = new Random();

        try {
            userList.add(new User("John Smith", "john.smith@abc.com",
                    User.Status.Staff, User.Role.Manager, gr.get(rand.nextInt(gr.size()))));

        userList.add(new User("Laura Adams","laura.adams@abc.com",
                User.Status.New, User.Role.User, gr.get(rand.nextInt(gr.size()))));

        userList.add(new User("Peter Williams","peter.williams@abc.com",
                User.Status.Active, User.Role.PremiumUser, gr.get(rand.nextInt(gr.size()))));

        userList.add(new User("Joana Sanders","joana.sanders@abc.com",
                User.Status.Blocked, User.Role.User));

        userList.add(new User("John Drake","john.drake@abc.com",
                User.Status.Staff, User.Role.Manager, gr.get(rand.nextInt(gr.size()))));

        userList.add(new User("Samuel Williams","samuel.williams@abc.com",
                User.Status.Staff, User.Role.Moderator, gr.get(rand.nextInt(gr.size()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List <User> getInstance(){
        return userList;
    }
}