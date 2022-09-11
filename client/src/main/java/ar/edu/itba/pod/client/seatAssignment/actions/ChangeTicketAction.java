package ar.edu.itba.pod.client.seatAssignment.actions;

import ar.edu.itba.pod.client.seatAssignment.CliParser;
import ar.edu.itba.pod.services.SeatAssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangeTicketAction implements Runnable {
    private final Logger logger;
    private final SeatAssignmentService service;
    private final CliParser.Arguments arguments;

    public ChangeTicketAction(SeatAssignmentService service, CliParser.Arguments arguments) {
        this.service = service;
        this.arguments = arguments;
        this.logger = LoggerFactory.getLogger(ChangeTicketAction.class);
    }

    public ChangeTicketAction(SeatAssignmentService service, CliParser.Arguments arguments, Logger logger) {
        this.service = service;
        this.arguments = arguments;
        this.logger = logger;
    }

    @Override
    public void run() {
        var flightCode = arguments.getFlightCode();
        var originalFlightCode = arguments.getOriginalFlightCode();
        try {
            service.changeTicket(arguments.getPassengerName(), originalFlightCode, arguments.getFlightCode());
            logger.info("Ticket for new flight {} from {} changed successfully.", flightCode, originalFlightCode);
        } catch (Exception e) {
            logger.error("Error while changing ticket from flight {} to {}.", originalFlightCode, flightCode);
        }
    }
}
