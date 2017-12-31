package service;

import database.DAOFactory;
import database.UserDAO;
import entity.User;
import exceptions.ElementCreationException;
import exceptions.ElementRemoveException;

import java.util.List;

/**
 * Created by Masha Kereb on 26-Jun-17.
 */
public class UserService {
    static UserService instance;

    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDataAccessObject("user");

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    protected UserService() {
    }

    public List<User> getAllUsers() {
        return userDAO.getAllElems();
    }

    public User getUser(int userId) {
        return userDAO.getElem(userId);
    }

    public int createUser(User user) throws ElementCreationException {
        return userDAO.addElem(user);
    }

    public boolean deleteUser(int userId) throws ElementRemoveException {
        return userDAO.deleteElem(userId);
    }
}
