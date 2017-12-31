package service;

/**
 * Created by Masha Kereb on 26-Jun-17.
 */
public class ServiceFactory {
    private static ServiceFactory instance;

    public static synchronized ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public Object getService(String serviceName) {
        switch (serviceName) {
            case "user":
                return UserService.getInstance();
            case "account":
                return AccountService.getInstance();
            case "payment":
                return PaymentService.getInstance();
            case "auth":
                return AuthService.getInstance();
            default:
                return null;
        }
    }

    protected ServiceFactory() {
    }

}
