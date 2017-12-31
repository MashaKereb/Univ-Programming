package command;

/**
 * Created by Masha Kereb on 12-Jun-17.
 */
public class MessageFactory {
    public enum MessageType {
        NoSuchUser, IncorrectLogin, IncorrectPassword, NoSuchAccount, AccessError, InvalidParameters,
        PaymentCreationError, UserCreationError, AccountCreationError, InsufficientFunds,
        SuccessPayment, SuccessAccountCreation, SuccessUserCreation, AccountOperationError,
        UserOperationError, Success, AccountBlockedError
    }

    private static MessageFactory instance;

    public static synchronized MessageFactory getInstance() {
        if (instance == null) {
            instance = new MessageFactory();
        }
        return instance;
    }

    protected MessageFactory() {
    }

    public String getMessage(MessageFactory.MessageType type) {
        if (type == MessageType.AccessError) {
            return "You have not access for this operation";
        } else if (type == MessageType.NoSuchUser) {
            return "The user with such id does not exist";
        } else if (type == MessageType.IncorrectLogin) {
            return "The user with such login does not exists";
        } else if (type == MessageType.IncorrectPassword) {
            return "Incorrect password";
        } else if (type == MessageType.NoSuchAccount) {
            return "The account with such id does not exists";
        } else if (type == MessageType.InvalidParameters) {
            return "The parameters of the request are not valid";
        } else if (type == MessageType.InsufficientFunds) {
            return "Insufficient funds for withdrawal";
        } else if (type == MessageType.UserCreationError) {
            return "Error during user creation";
        } else if (type == MessageType.PaymentCreationError) {
            return "Error during payment creation";
        } else if (type == MessageType.AccountCreationError) {
            return "Error during account creation";
        } else if (type == MessageType.SuccessAccountCreation) {
            return "The account was successfully created";
        } else if (type == MessageType.SuccessPayment) {
            return "The payment was successfully created";
        } else if (type == MessageType.SuccessUserCreation) {
            return "The user was successfully created";
        } else if (type == MessageType.AccountOperationError) {
            return "Error during operation with account";
        } else if (type == MessageType.UserOperationError) {
            return "Error during operation with users";
        } else if (type == MessageType.Success) {
            return "The operation was successful";
        } else if (type == MessageType.AccountBlockedError) {
            return "Cannot provide operations with an blocked account";
        }
        return null;
    }
}
