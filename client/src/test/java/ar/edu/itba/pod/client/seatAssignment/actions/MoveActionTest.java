package ar.edu.itba.pod.client.seatAssignment.actions;

import ar.edu.itba.pod.client.seatAssignment.CliParser;
import ar.edu.itba.pod.services.SeatAssignmentService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.rmi.RemoteException;

import static org.mockito.Mockito.*;

public class MoveActionTest {
    private final SeatAssignmentService seatAssignmentService = mock(SeatAssignmentService.class);
    private final Logger logger = mock(Logger.class);

    @Test
    public void move_ShouldSucceed() throws Exception {
        // Arrange
        String flightCode = "AA1234";
        int row = 1;
        String col = "A";
        String passengerName = "Janice Doe";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(flightCode);
        arguments.setPassengerName(passengerName);
        arguments.setRow(row);
        arguments.setCol(col);

        // Act
        new MoveAction(seatAssignmentService, arguments, logger).run();

        // Assert
        verify(seatAssignmentService).changeSeat(flightCode, passengerName, row, col.charAt(0));
        verify(logger).info("Seat changed successfully to {}{}.", row, col.charAt(0));
    }

    @Test
    public void move_SeatDoesNotExist_ShouldFail() throws Exception {
        // Arrange
        String flightCode = "AA1234";
        int row = 1;
        String col = "A";
        String passengerName = "Janice Doe";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(flightCode);
        arguments.setPassengerName(passengerName);
        arguments.setRow(row);
        arguments.setCol(col);

        doThrow(RemoteException.class).when(seatAssignmentService).changeSeat(flightCode, passengerName, row, col.charAt(0));

        // Act
        new MoveAction(seatAssignmentService, arguments, logger).run();

        // Assert
        verify(logger).error("Cannot change seat to {}{} in flight {}.", row, col.charAt(0), flightCode);
    }
}
