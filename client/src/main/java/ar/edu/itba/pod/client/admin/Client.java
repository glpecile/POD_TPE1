package ar.edu.itba.pod.client.admin;

import ar.edu.itba.pod.client.admin.actions.CancelAction;
import ar.edu.itba.pod.client.admin.actions.ConfirmAction;
import ar.edu.itba.pod.client.admin.actions.ModelsAction;
import ar.edu.itba.pod.client.admin.actions.StatusAction;
import ar.edu.itba.pod.services.AdminService;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(ar.edu.itba.pod.client.Client.class);

    public static void main(String[] args) {
        logger.info("AdminClient Starting ...");

        var cli = new CliParser().parse(args);

        if (cli.isEmpty())
            return;
        var arguments = cli.get();

        try
        {
            final Registry registry = LocateRegistry.getRegistry(arguments.getServerAddress());
            final AdminService service = (AdminService) registry.lookup(AdminService.getServiceName());

            switch (arguments.getAction()){
                case MODELS -> new ModelsAction(service,arguments).run();
                case STATUS -> new StatusAction(service,arguments).run();
                case CONFIRM -> new ConfirmAction(service,arguments).run();
                case CANCEL -> new CancelAction(service,arguments).run();
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
