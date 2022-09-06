package ar.edu.itba.pod.client.admin.actions;

import ar.edu.itba.pod.client.admin.CliParser;
import ar.edu.itba.pod.services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CancelAction {
    private final AdminService service;
    private final CliParser.Arguments arguments;
    private final Logger logger;

    public CancelAction(AdminService service, CliParser.Arguments arguments) {
        this.service = service;
        this.arguments = arguments;
        logger = LoggerFactory.getLogger(CancelAction.class);
    }

    public CancelAction(AdminService service, CliParser.Arguments arguments, Logger logger) {
        this.service = service;
        this.arguments = arguments;
        this.logger = logger;
    }

    public void run() {
        var flightCode = arguments.getFlightCode().get();

        try{
            service.cancelFlight(flightCode);
            logger.info("The flight {} is cancelled", flightCode);
        } catch (Exception e) {
            logger.error("Error canceling flight {}", flightCode);
        }
    }
}
