package ar.edu.itba.pod.client.seatAssignment.actions;

import ar.edu.itba.pod.client.seatAssignment.CliParser;
import ar.edu.itba.pod.services.SeatAssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssignAction implements Runnable {
    private final Logger logger;
    private final SeatAssignmentService service;
    private final CliParser.Arguments arguments;

    public AssignAction(SeatAssignmentService service, CliParser.Arguments arguments) {
        this.service = service;
        this.arguments = arguments;
        this.logger = LoggerFactory.getLogger(AssignAction.class);
    }

    public AssignAction(SeatAssignmentService service, CliParser.Arguments arguments, Logger logger) {
        this.service = service;
        this.arguments = arguments;
        this.logger = logger;
    }

    @Override
    public void run() {
        var flightCode = arguments.getFlightCode();
        var passengerName = arguments.getPassengerName();
        var row = arguments.getRow();
        var col = arguments.getCol().charAt(0);
        try {
            service.assignSeat(flightCode, passengerName, row, col);
            logger.info("Seat {}{} successfully assigned to passenger {} in flight {}.", row, col, passengerName, flightCode);
        } catch (Exception e) {
            logger.error("Cannot assign seat {}{} for flight {} and passenger {}.", row, col, flightCode, passengerName);
        }
    }
}
