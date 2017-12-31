package command.factory;

import command.*;
import database.ConcreteDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommandFactoryImpl implements CommandFactory {

    private CommandFactoryImpl(){};
    private static CommandFactoryImpl factory=new CommandFactoryImpl();
    public static CommandFactory getFactory(){
        return factory;
    }
    @Override
    public Command getCommand(String name, HttpServletRequest request,HttpServletResponse response) throws IllegalArgumentException {
        if (name==null){
            throw new IllegalArgumentException("Name cannot be NULL") ;
        }
        else{
            Command command=null;

            //----------------------------//
            if (name.equals("Menu")) {
                command=new MenuCommand();
            }
            if (name.equals("Bills")){
                command=new BillCommand();
            }
            if(name.equals("Pay")){
                command=new PayCommand();
            }
            if (name.equals("Confirm")){
                command=new ConfirmCommand();
            }
            if (name.equals("Authorization")){
                command=new AuthorizationCommand();
            }
            //---------------------------//


            if (command==null){
                throw new IllegalArgumentException("Wrong command name");
            }

            command.setResp(response);
            command.setReq(request);
            command.setDao(new ConcreteDAO());
            return command;
        }


    }

}
