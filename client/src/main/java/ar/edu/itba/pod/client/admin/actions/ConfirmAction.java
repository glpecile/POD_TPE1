package ar.edu.itba.pod.client.admin.actions;

import ar.edu.itba.pod.client.admin.CliParser;
import ar.edu.itba.pod.services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

public class ConfirmAction implements Runnable {

    private final AdminService service;
    private final CliParser.Arguments arguments;
    private final Logger logger;
    public ConfirmAction(AdminService service, CliParser.Arguments arguments) {
        logger = LoggerFactory.getLogger(ConfirmAction.class);
        this.arguments = arguments;
        this.service = service;
    }

    public ConfirmAction(AdminService service, CliParser.Arguments arguments, Logger logger) {
        this.service = service;
        this.arguments = arguments;
        this.logger = logger;
    }

    @Override
    public void run() {
        var flightCode = arguments.getFlightCode().get();

        try {
            service.confirmFlight(flightCode);
            logger.info("The flight {} is confirmed", flightCode);
        } catch (RemoteException e) {
            logger.error("Error confirming flight {}", flightCode);
        }
    }
}
