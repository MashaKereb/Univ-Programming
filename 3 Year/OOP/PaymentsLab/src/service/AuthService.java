package service;

import database.DAOFactory;
import database.UserDAO;
import entity.User;
import exceptions.IncorrectPasswordException;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 26-Jun-17.
 */
public class AuthService {
    static AuthService instance;

    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDataAccessObject("user");

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    protected AuthService() {
    }

    public User authorization(String email, String password) throws IncorrectPasswordException, NoSuchElementException {
        List<User> userList = userDAO.getElemsForCriteria("email", email);
        if (userList.isEmpty()) {
            throw new NoSuchElementException();
        }
        User user = userList.get(0);

        if (!user.checkPassword(password)) {
            throw new IncorrectPasswordException();
        }

        return user;
    }
}
