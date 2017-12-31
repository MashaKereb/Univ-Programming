package rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Masha Kereb on 28-May-17.
 */
public class RMIServerRunner {
    static IServer serverInstance;

    public static IServer getServerInstance() {
        if (serverInstance != null)
            return serverInstance;
        try {
            serverInstance = (IServer) Naming.lookup("//127.0.0.1:4040/RMIServer");
            return serverInstance;
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(4040);
            RMIServer ms = new RMIServer();
            registry.rebind("RMIServer", ms);

            System.out.println("Server started! ");
            //System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
