package command.factory;

import command.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommandFactoryImpl implements CommandFactory {

    private CommandFactoryImpl() {
    }

    private static CommandFactoryImpl factory = new CommandFactoryImpl();

    public static synchronized CommandFactory getFactory() {
        return factory;
    }

    @Override
    public Command getCommand(String name, HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be NULL");
        } else {
            Command command = null;

            //----------------------------//
            if (name.equals("NewUser")) {
                command = new NewUserCommand();
            }
            if (name.equals("NewAccount")) {
                command = new NewAccountCommand();
            }
            if (name.equals("NewPayment")) {
                command = new NewPaymentCommand();
            }
            if (name.equals("UnblockAccount")) {
                command = new UnblockAccountCommand();
            }
            if (name.equals("BlockAccount")) {
                command = new BlockAccountCommand();
            }
            if (name.equals("Authorization")) {
                command = new AuthorizationCommand();
            }
            //---------------------------//


            if (command == null) {
                throw new IllegalArgumentException("Wrong command name");
            }

            command.setResp(response);
            command.setReq(request);
            return command;
        }


    }

}
