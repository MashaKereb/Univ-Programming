package database;

/**
 * Created by Masha Kereb on 08-Jun-17.
 */
public class DAOFactory {
    private static DAOFactory instance;


    protected DAOFactory() {
    }

    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    public DAO getDataAccessObject(String entityName) {

        if (entityName.toLowerCase().equals("payment")) {

            return PaymentDAO.getInstance();

        } else if (entityName.toLowerCase().equals("user")) {

            return UserDAO.getInstance();

        } else if (entityName.toLowerCase().equals("account")) {

            return PaymentDAO.getInstance();

        } else return null;
    }
}
