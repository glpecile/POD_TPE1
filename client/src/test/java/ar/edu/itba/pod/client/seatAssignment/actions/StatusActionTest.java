package ar.edu.itba.pod.client.seatAssignment.actions;

import ar.edu.itba.pod.client.seatAssignment.CliParser;
import ar.edu.itba.pod.services.SeatAssignmentService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.rmi.RemoteException;

import static org.mockito.Mockito.*;

public class StatusActionTest {
    private final SeatAssignmentService seatAssignmentService = mock(SeatAssignmentService.class);
    private final Logger logger = mock(Logger.class);

    @Test
    public void getStatus_ShouldSucceed() throws Exception {
        // Arrange
        String flightCode = "AA1234";
        int row = 1;
        String col = "A";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(flightCode);
        arguments.setRow(row);
        arguments.setCol(col);

        when(seatAssignmentService.isSeatTaken(eq(flightCode), eq(row), eq(col.charAt(0)))).thenReturn(true);

        // Act
        new StatusAction(seatAssignmentService, arguments, logger).run();

        // Assert
        verify(logger).info("Seat {}{} is {}.", row, col, "TAKEN");
    }

    @Test
    public void getStatus_SeatDoesNotExist_ShouldFail() throws Exception {
        // Arrange
        String flightCode = "AA1234";
        int row = 1;
        String col = "A";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(flightCode);
        arguments.setRow(row);
        arguments.setCol(col);

        when(seatAssignmentService.isSeatTaken(flightCode, row, col.charAt(0))).thenThrow(RemoteException.class);

        // Act
        new StatusAction(seatAssignmentService, arguments, logger).run();

        // Assert
        verify(logger).error("Cannot get the status of seat {}{} in flight {}", row, col, flightCode);
    }

}
