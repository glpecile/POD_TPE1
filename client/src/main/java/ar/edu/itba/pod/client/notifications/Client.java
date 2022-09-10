package ar.edu.itba.pod.client.notifications;

import ar.edu.itba.pod.interfaces.PassengerNotifier;
import ar.edu.itba.pod.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {

        var cli = new CliParser().parse(args);

        if (cli.isEmpty())
            return;
        var arguments = cli.get();

        try
        {
            final Registry registry = LocateRegistry.getRegistry(arguments.getHost(),arguments.getPort());
            final NotificationService service = (NotificationService) registry.lookup("NotificationService");

            PassengerNotifier remote = (PassengerNotifier) UnicastRemoteObject
                    .exportObject(new PassengerNotifierImpl(arguments.getPassenger()), 0);

            service.registerPassenger(arguments.getFlight(),arguments.getPassenger(),remote);


        } catch (AccessException | NotBoundException e) {
            logger.error("Cannot access service on server");
        } catch (RemoteException e) {
            logger.error("Cannot connect to server");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
