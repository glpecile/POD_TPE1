package ar.edu.itba.pod.client.seatAssignment.actions;

import ar.edu.itba.pod.client.seatAssignment.CliParser;
import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.services.SeatAssignmentService;
import ar.edu.itba.pod.utils.Pair;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.rmi.RemoteException;
import java.util.List;
import java.util.TreeMap;

import static org.mockito.Mockito.*;

public class AlternativesActionTest {
    private final SeatAssignmentService seatAssignmentService = mock(SeatAssignmentService.class);
    private final Logger logger = mock(Logger.class);

    @Test
    public void getAlternatives_ShouldSucceed() throws Exception {
        // Arrange
        var flightCode = "AA1234";
        String passengerName = "John Doe";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(flightCode);
        arguments.setPassengerName(passengerName);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(10, 6));
        seatsPerCategory.put(SeatCategory.PREMIUM_ECONOMY, new Pair<>(10, 4));
        seatsPerCategory.put(SeatCategory.BUSINESS, new Pair<>(4, 4));
        Plane plane = new Plane("Boeing 747", seatsPerCategory);
        var tickets = List.of(
                new Ticket(passengerName, SeatCategory.BUSINESS)
        );
        var flight = new Flight(FlightStatus.CONFIRMED, "JFK", "AA101", plane, tickets);

        List<AlternativeFlight> alternatives = List.of(
                new AlternativeFlight(flight, SeatCategory.BUSINESS, 1)
        );

        when(seatAssignmentService.getAlternativeFlights(eq(flightCode), eq(passengerName))).thenReturn(alternatives);

        // Act
        new AlternativesAction(seatAssignmentService, arguments, logger).run();

        // Assert
        verify(logger).info("{} | {} | {} {}", flight.getAirportCode(), flight.getFlightCode(), alternatives.get(0).getFreeSeats(), alternatives.get(0).getCategory());
    }

    @Test
    public void getAlternatives_EmptyAlternatives_ShouldSucceed() throws Exception {
        // Arrange
        String flightCode = "AA1234";
        String passengerName = "John Doe";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(flightCode);
        arguments.setPassengerName(passengerName);

        // Act
        new AlternativesAction(seatAssignmentService, arguments, logger).run();

        // Assert
        verify(seatAssignmentService).getAlternativeFlights(flightCode, passengerName);
        verify(logger).info("No alternatives found for flight {}.", flightCode);
    }

    @Test
    public void getAlternatives_ShouldFail() throws Exception {
        // Arrange
        String flightCode = "AA1234";
        String passengerName = "John Doe";
        var arguments = new CliParser.Arguments();
        arguments.setFlightCode(flightCode);
        arguments.setPassengerName(passengerName);

        doThrow(RemoteException.class).when(seatAssignmentService).getAlternativeFlights(eq(flightCode), eq(passengerName));

        // Act
        new AlternativesAction(seatAssignmentService, arguments, logger).run();

        // Assert
        verify(seatAssignmentService).getAlternativeFlights(flightCode, passengerName);
        verify(logger).error("Cannot get alternatives for flight {}.", flightCode);
    }
}
