package ar.edu.itba.pod.client.admin.actions;

import ar.edu.itba.pod.client.admin.CliParser;
import ar.edu.itba.pod.services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

public class StatusAction {
    private final AdminService service;
    private final CliParser.Arguments arguments;
    private final Logger logger;

    public StatusAction(AdminService service, CliParser.Arguments arguments) {
        this.service = service;
        this.arguments = arguments;
        this.logger = LoggerFactory.getLogger(StatusAction.class);
    }

    public StatusAction(AdminService service, CliParser.Arguments arguments, Logger logger) {
        this.service = service;
        this.arguments = arguments;
        this.logger = logger;
    }

    public void run() {
        var flightCode = arguments.getFlightCode().get();

        try {
            var status = service.getFlightStatus(flightCode);
            logger.info("Flight {} status: {}", flightCode, status);
        }
        catch (RemoteException e){
            logger.error("Cannot get status of flight {}",flightCode);
        }
    }
}
