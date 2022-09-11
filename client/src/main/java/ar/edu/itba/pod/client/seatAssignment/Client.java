package ar.edu.itba.pod.client.seatAssignment;

import ar.edu.itba.pod.client.seatAssignment.actions.*;
import ar.edu.itba.pod.services.SeatAssignmentService;
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
        logger.info("seatAssignmentClient Starting ...");
        var cli = new CliParser().parse(args);
        if (cli.isEmpty())
            return;
        var arguments = cli.get();

        try {
            final Registry registry = LocateRegistry.getRegistry(arguments.getHost(), arguments.getPort());
            final SeatAssignmentService service = (SeatAssignmentService) registry.lookup("SeatAssignmentService");

            switch (arguments.getAction()) {
                case STATUS -> new StatusAction(service, arguments).run();
                case ASSIGN -> new AssignAction(service, arguments).run();
                case MOVE -> new MoveAction(service, arguments).run();
                case ALTERNATIVES -> new AlternativesAction(service, arguments).run();
                case CHANGETICKET -> new ChangeTicketAction(service, arguments).run();
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
