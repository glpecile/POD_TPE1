package ar.edu.itba.pod.client.seatmap;


import ar.edu.itba.pod.client.seatmap.actions.SeatMapAction;
import ar.edu.itba.pod.exceptions.FlightDoesNotExistException;
import ar.edu.itba.pod.services.SeatMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        logger.info("SeatMapCLient starting ... ");

        var cli = new CliParser().parse(args);

        if(cli.isEmpty())
            return;

        var arguments = cli.get();

        try{
            final Registry registry = LocateRegistry.getRegistry(arguments.getHost(),arguments.getPort());
            final SeatMapService service = (SeatMapService) registry.lookup("SeatMapService");

            new SeatMapAction(service,arguments).run();


        }catch (AccessException e) {
            logger.error("Cannot access service on server");
        } catch (RemoteException e) {
            logger.error("Cannot connect to server");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
