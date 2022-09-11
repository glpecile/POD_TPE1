package ar.edu.itba.pod.client.admin.actions;

import ar.edu.itba.pod.client.admin.CliParser;
import ar.edu.itba.pod.models.FlightStatus;
import ar.edu.itba.pod.services.AdminService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.rmi.RemoteException;

import static org.mockito.Mockito.*;

public class StatusActionTest {
    private final AdminService adminService = mock(AdminService.class);
    private final Logger logger = mock(Logger.class);

    @Test
    public void getStatus_ShouldSucceed() throws RemoteException {
        // Arrange
        String flightCode = "AA1234";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(flightCode);

        var status = FlightStatus.CONFIRMED;
        when(adminService.getFlightStatus(eq(flightCode))).thenReturn(status);

        // Act
        new StatusAction(adminService, arguments, logger).run();

        // Assert
        verify(logger).info("Flight {} status: {}", flightCode, status);
    }

    @Test
    public void getStatus_FlightDoesNoExist_ShouldFail() throws RemoteException {
        // Arrange
        String flightCode = "AA1234";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(flightCode);

        when(adminService.getFlightStatus(eq(flightCode))).thenThrow(RemoteException.class);

        // Act
        new StatusAction(adminService, arguments, logger).run();

        // Assert
        verify(logger).error("Cannot get status of flight {}", flightCode);
    }
}
