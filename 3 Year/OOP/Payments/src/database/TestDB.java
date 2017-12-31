package database;

import beans.User;

import java.sql.SQLException;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public class TestDB {
    public static void main(String[] args) throws SQLException {
        UserDAO db = (UserDAO) DAOFactory.getInstance().getDataAccessObject("user");
        try {
            for (User user: db.getAllElems()
                    ) {
                System.out.println(user.getId());
                System.out.println(user.getName());
                System.out.println(user.getSurname());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
