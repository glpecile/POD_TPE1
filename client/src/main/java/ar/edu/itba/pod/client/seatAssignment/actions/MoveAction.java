package ar.edu.itba.pod.client.seatAssignment.actions;

import ar.edu.itba.pod.client.seatAssignment.CliParser;
import ar.edu.itba.pod.services.SeatAssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveAction implements Runnable {
    private final Logger logger;
    private final SeatAssignmentService service;
    private final CliParser.Arguments arguments;

    public MoveAction(SeatAssignmentService service, CliParser.Arguments arguments) {
        this.service = service;
        this.arguments = arguments;
        this.logger = LoggerFactory.getLogger(MoveAction.class);
    }

    public MoveAction(SeatAssignmentService service, CliParser.Arguments arguments, Logger logger) {
        this.service = service;
        this.arguments = arguments;
        this.logger = logger;
    }

    @Override
    public void run() {
        var flightCode = arguments.getFlightCode();
        var row = arguments.getRow();
        var col = arguments.getCol().charAt(0);
        try {
            service.changeSeat(flightCode, arguments.getPassengerName(), row, col);
            logger.info("Seat changed successfully to {}{}.", row, col);
        } catch (Exception e) {
            logger.error("Cannot change seat to {}{} in flight {}.", row, col, flightCode);
        }
    }
}
