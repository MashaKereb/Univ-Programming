package db;

import entities.User;
import entities.UserList;
import staticservice.UserService;

import java.sql.SQLException;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public class TestMain {
    public static void main(String[] args) throws SQLException {
        DBGroupService db = new DBGroupService();
        UserService us = new UserService(UserList.getInstance());
        for (User user: us.getAllElems()
                ) {
            System.out.println(user.getId());
            System.out.println(user.getName());
            System.out.println(user.getGroup());
        }

    }
}
