package ar.edu.itba.pod.client.seatAssignment.actions;

import ar.edu.itba.pod.client.seatAssignment.CliParser;
import ar.edu.itba.pod.services.SeatAssignmentService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.rmi.RemoteException;

import static org.mockito.Mockito.*;

public class ChangeTicketActionTest {
    private final SeatAssignmentService seatAssignmentService = mock(SeatAssignmentService.class);
    private final Logger logger = mock(Logger.class);

    @Test
    public void changeTicket_ShouldSucceed() throws Exception {
        // Arrange
        String flightCode = "AA1234";
        String newFlightCode = "AA1235";
        String passengerName = "John Doe";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(newFlightCode);
        arguments.setOriginalFlightCode(flightCode);
        arguments.setPassengerName(passengerName);

        // Act
        new ChangeTicketAction(seatAssignmentService, arguments, logger).run();

        // Assert
        verify(seatAssignmentService).changeTicket(passengerName, flightCode, newFlightCode);
        verify(logger).info("Ticket for new flight {} from {} changed successfully.", newFlightCode, flightCode);
    }

    @Test
    public void changeTicket_ShouldFail() throws Exception {
        // Arrange
        String flightCode = "AA1234";
        String newFlightCode = "AA1235";
        String passengerName = "John Doe";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(newFlightCode);
        arguments.setOriginalFlightCode(flightCode);
        arguments.setPassengerName(passengerName);

        doThrow(RemoteException.class).when(seatAssignmentService).changeTicket(passengerName, flightCode, newFlightCode);

        // Act
        new ChangeTicketAction(seatAssignmentService, arguments, logger).run();

        // Assert
        verify(seatAssignmentService).changeTicket(passengerName, flightCode, newFlightCode);
        verify(logger).error("Error while changing ticket from flight {} to {}.", flightCode, newFlightCode);
    }
}
