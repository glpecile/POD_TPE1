package ar.edu.itba.pod.client.seatAssignment.actions;

import ar.edu.itba.pod.client.seatAssignment.CliParser;
import ar.edu.itba.pod.services.SeatAssignmentService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.rmi.RemoteException;

import static org.mockito.Mockito.*;

public class AssignActionTest {
    private final SeatAssignmentService seatAssignmentService = mock(SeatAssignmentService.class);
    private final Logger logger = mock(Logger.class);

    @Test
    public void assignSeat_ShouldSucceed() throws Exception {
        // Arrange
        String flightCode = "AA1234";
        int row = 1;
        String col = "A";
        String passengerName = "John Doe";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(flightCode);
        arguments.setRow(row);
        arguments.setCol(col);
        arguments.setPassengerName(passengerName);

        // Act
        new AssignAction(seatAssignmentService, arguments, logger).run();

        // Assert
        verify(seatAssignmentService).assignSeat(flightCode, passengerName, row, col.charAt(0));
        verify(logger).info("Seat {}{} successfully assigned to passenger {} in flight {}.", row, col.charAt(0), passengerName, flightCode);
    }

    @Test
    public void assignSeat_SeatDoesNotExist_ShouldFail() throws Exception {
        // Arrange
        String flightCode = "AA1234";
        int row = 1;
        String col = "A";
        String passengerName = "Jane Doe";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(flightCode);
        arguments.setRow(row);
        arguments.setCol(col);
        arguments.setPassengerName(passengerName);

        doThrow(RemoteException.class).when(seatAssignmentService).assignSeat(flightCode, passengerName, row, col.charAt(0));

        // Act
        new AssignAction(seatAssignmentService, arguments, logger).run();

        // Assert
        verify(seatAssignmentService).assignSeat(flightCode, passengerName, row, col.charAt(0));
        verify(logger).error("Cannot assign seat {}{} for flight {} and passenger {}.", row, col.charAt(0), flightCode, passengerName);
    }
}
