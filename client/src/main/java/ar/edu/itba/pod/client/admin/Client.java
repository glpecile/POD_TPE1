package ar.edu.itba.pod.client.admin;

import ar.edu.itba.pod.client.admin.actions.*;
import ar.edu.itba.pod.services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        logger.info("AdminClient Starting ...");

        var cli = new CliParser().parse(args);

        if (cli.isEmpty())
            return;
        var arguments = cli.get();

        try
        {
            final Registry registry = LocateRegistry.getRegistry(arguments.getHost(),arguments.getPort());
            final AdminService service = (AdminService) registry.lookup("AdminService");

            switch (arguments.getAction()){
                case MODELS -> new ModelsAction(service,arguments).run();
                case STATUS -> new StatusAction(service,arguments).run();
                case CONFIRM -> new ConfirmAction(service,arguments).run();
                case CANCEL -> new CancelAction(service,arguments).run();
                case FLIGHTS -> new FlightsAction(service,arguments).run();
                case RETICKETING -> new ReticketingAction(service,arguments).run();
            }


        } catch (AccessException | NotBoundException e) {
            logger.error("Cannot access service on server");
        } catch (RemoteException e) {
            logger.error("Cannot connect to server");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
