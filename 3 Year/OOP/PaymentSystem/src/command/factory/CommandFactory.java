package command.factory;


import command.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface CommandFactory {

    public Command getCommand(String name, HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException;

}
