package ar.edu.itba.pod.client.seatmap.actions;

import ar.edu.itba.pod.client.seatmap.CliParser;
import ar.edu.itba.pod.services.SeatMapService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.rmi.RemoteException;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SeatMapActionTests {
    private final SeatMapService seatMapService = mock(SeatMapService.class);
    private final Logger logger = mock(Logger.class);
    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    @Test
    public void loadValidFlights_ShouldSucceed() throws RemoteException {
        // Arrange
        var arguments = new CliParser.Arguments();

        arguments.setAction(ActionType.IDENTITY);
        arguments.setOutputPath("out.csv");
        arguments.setFlight("AA123");
        arguments.setServerAddress("10.23.34.55:9999");

        // Act
        try {
            new SeatMapAction(seatMapService,arguments,logger).run();

            // Assert
            verify(seatMapService, times(1)).getSeatMap(any(),any());
            verify(logger, times(1)).info("Seat map exported...");
        }
        catch (Exception e){
            fail("");
        }
    }


}
