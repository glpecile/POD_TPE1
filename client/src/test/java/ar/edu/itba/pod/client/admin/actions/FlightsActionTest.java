package ar.edu.itba.pod.client.admin.actions;

import ar.edu.itba.pod.client.admin.CliParser;
import ar.edu.itba.pod.services.AdminService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.rmi.RemoteException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class FlightsActionTest {
    private final AdminService adminService = mock(AdminService.class);
    private final Logger logger = mock(Logger.class);
    private final ClassLoader classloader = Thread.currentThread().getContextClassLoader();


    @Test
    public void loadValidFlights_ShouldSucceed() throws RemoteException {
        // Arrange
        var arguments = new CliParser.Arguments();

        arguments.setFilePath(GetPath("Flights.csv"));

        // Act
        try {
            new FlightsAction(adminService,arguments,logger).run();

            // Assert
            verify(adminService, times(2)).addFlight(any(),any(),any(),any());
            verify(logger, times(1)).info("{} flights added", 2);
        }
        catch (Exception e){
            fail("");
        }
    }

    @Test
    public void loadPartiallyValidModels_ShouldSucceed() throws RemoteException {
        // Arrange
        var arguments = new CliParser.Arguments();
        arguments.setFilePath(GetPath("Flights.csv"));


        doThrow(RemoteException.class).when(adminService).addFlight(any(),eq("AA100"),any(),any());

        // Act
        try {
            new FlightsAction(adminService, arguments,logger).run();
        }
        catch (Exception e){
            fail("");
        }

        verify(adminService, times(2)).addFlight(any(),any(),any(),any());
        verify(logger, times(1)).info("{} flights added",1);
        verify(logger, times(1)).error("Cannot add flight {}.","AA100");

    }


    private String GetPath(String fileName)
    {
        return Objects.requireNonNull(classloader.getResource(fileName)).getPath();
    }
}
