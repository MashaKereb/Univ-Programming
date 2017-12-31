package command.factory;

import command.*;
import command.account.*;
import command.auth.AuthorizationCommand;
import command.payment.AllPaymentsCommand;
import command.payment.NewPaymentCommand;
import command.payment.PaymentCreationCommand;
import command.user.AllUsersCommand;
import command.user.NewUserCommand;
import command.user.RemoveUserCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommandFactory implements ICommandFactory {

    private CommandFactory() {
    }

    private static CommandFactory factory = new CommandFactory();

    public static synchronized ICommandFactory getFactory() {
        return factory;
    }

    @Override
    public Command getCommand(String name, HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be NULL");
        } else {
            Command command = null;

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
            if (name.equals("AllAccounts")) {
                command = new AllAccountsCommand();
            }
            if (name.equals("AllUsers")) {
                command = new AllUsersCommand();
            }
            if (name.equals("AllPayments")) {
                command = new AllPaymentsCommand();
            }
            if (name.equals("RemoveUser")) {
                command = new RemoveUserCommand();
            }
            if (name.equals("RemoveAccount")) {
                command = new RemoveAccountCommand();
            }
            if(name.equals("AccountCreation")){
                command = new AccountCreationCommand();
            }
            if(name.equals("PaymentCreation")){
                command = new PaymentCreationCommand();
            }

            if (command == null) {
                throw new IllegalArgumentException("Wrong command name");
            }

            command.setResp(response);
            command.setReq(request);
            return command;
        }


    }

}
