package ar.edu.itba.pod.services;

import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.server.notifications.EventsManagerImpl;
import ar.edu.itba.pod.server.services.SeatAssignmentServiceImpl;
import ar.edu.itba.pod.utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SeatAssignmentServiceTest {
    SeatAssignmentServiceImpl seatAssignmentService;

    @Test
    public void AssignSeat_WithWeirdFlightCode_ShouldThrowFlightDoesNotExist() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.assignSeat("23232", "test-name", 1, 'a');
//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("FlightDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_WithEmptyFlightCode_ShouldThrowFlightDoesNotExist() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.assignSeat("", "test-name", 1, 'a');
//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("FlightDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_ShouldThrowFlightIsConfirmedException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.CONFIRMED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());
//        Act
        try {
            seatAssignmentService.assignSeat(flightCode, "test-name", 1, 'a');
//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("FlightIsConfirmedException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_ShouldThrowFlightIsCancelledException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.CANCELLED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());
//        Act
        try {
            seatAssignmentService.assignSeat(flightCode, "test-name", 1, 'a');
//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("FlightIsCancelledException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_WithEmptyString_ShouldThrowInvalidPassengerException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());
//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "", 1, 'a');
//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("InvalidPassengerException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_WithNonExistingPassenger_ShouldThrowInvalidPassengerException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());
//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "different-name", 1, 'a');
//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("InvalidPassengerException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_ShouldThrowPassengerAlreadyAssignedException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        ticket.setSeatLocation(new Ticket.SeatLocation(1, 'a'));
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());
//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 1, 'b');
//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("PassengerAlreadyAssignedException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_ShouldThrowSeatAlreadyAssignedException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        Ticket ticket2 = new Ticket("test2-name", SeatCategory.ECONOMY);
        ticket2.setSeatLocation(new Ticket.SeatLocation(1, 'a'));
        tickets.add(ticket);
        tickets.add(ticket2);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());
//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 1, 'a');
//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("SeatAlreadyAssignedException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_ShouldThrowSeatCategoryIsToHighException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.BUSINESS, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 1, 'a');

//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("SeatCategoryIsToHighException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_WithBigColumn_ShouldThrowSeatDoesNotExistException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 1, 'c');

//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_WithBigRow_ShouldThrowSeatDoesNotExistException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 5, 'b');

//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_WithNotLetterColumn_ShouldThrowSeatDoesNotExistException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 4, ';');

//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_WithSmallRow_ShouldThrowSeatDoesNotExistException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 0, 'a');

//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void isSeatTaken_WithSmallRow_ShouldThrowSeatDoesNotExistException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.isSeatTaken("FC1234", 0, 'a');

//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void isSeatTaken_WithBigRow_ShouldThrowSeatDoesNotExistException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.isSeatTaken("FC1234", 6, 'a');

//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void isSeatTaken_WithInvalidColumn_ShouldThrowSeatDoesNotExistException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.isSeatTaken("FC1234", 4, '0');

//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void isSeatTaken_WithBigColumn_ShouldThrowSeatDoesNotExistException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.isSeatTaken("FC1234", 4, 'c');

//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void isSeatTaken_WithEmptyFlightCode_ShouldThrowFlightDoesNotExistException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.isSeatTaken("", 4, 'b');

//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("FlightDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void isSeatTaken_WithNonExistentFlightCode_ShouldThrowFlightDoesNotExistException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.isSeatTaken("FC-different", 4, 'b');

//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("FlightDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void isSeatTaken_ShouldReturnFalse() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            boolean isSeatTaken = seatAssignmentService.isSeatTaken("FC1234", 4, 'b');

//            Assert
            assertFalse(isSeatTaken);
        } catch (Exception e) {
            fail();
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void isSeatTaken_ShouldReturnTrue() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        ticket.setSeatLocation(new Ticket.SeatLocation(4, 'b'));
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            boolean isSeatTaken = seatAssignmentService.isSeatTaken("FC1234", 4, 'b');

//            Assert
            assertTrue(isSeatTaken);
        } catch (Exception e) {
            fail();
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void changeSeat_WithBigRow_ShouldThrowSeatDoesNotExistException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        ticket.setSeatLocation(new Ticket.SeatLocation(4, 'b'));
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.changeSeat("FC1234", "test-name", 5, 'b');

//            Assert
            fail();
        } catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void changeSeat_WithSameLocation_ShouldSucceed() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        ticket.setSeatLocation(new Ticket.SeatLocation(4, 'b'));
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.changeSeat("FC1234", "test-name", 4, 'b');

//            Assert

        } catch (Exception e) {
            fail();

        }
    }

    @Test
    public void changeSeat_WithNotTakenSeat_ShouldSucceed() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        ticket.setSeatLocation(new Ticket.SeatLocation(4, 'b'));
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.changeSeat("FC1234", "test-name", 3, 'b');

//            Assert

        } catch (Exception e) {
            fail();

        }
    }

    @Test
    public void changeSeat_WithTakenSeat_ShouldThrowSeatAlreadyAssignedException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        ticket.setSeatLocation(new Ticket.SeatLocation(4, 'b'));
        Ticket ticket2 = new Ticket("test-name2", SeatCategory.ECONOMY);
        ticket2.setSeatLocation(new Ticket.SeatLocation(3, 'b'));
        tickets.add(ticket);
        tickets.add(ticket2);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode = "FC1234";
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode, plane, tickets);
        flights.put(flightCode, flight);
        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.changeSeat("FC1234", "test-name", 3, 'b');
//            Assert
            fail();
        } catch (Exception e) {

            assertEquals("SeatAlreadyAssignedException", e.getMessage());

        }
    }

    @Test
    public void changeTicket_ShouldSucceed() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        List<Ticket> tickets2 = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        ticket.setSeatLocation(new Ticket.SeatLocation(4, 'b'));
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane1 = new Plane("test-model", seatsPerCategory);
        Plane plane2 = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode1 = "FC1234";
        String flightCode2 = "FC1235";
        Flight flight1 = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode1, plane1, tickets);
        Flight flight2 = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode2, plane2, tickets2);
        flights.put(flightCode1, flight1);
        flights.put(flightCode2, flight2);

        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.changeTicket("test-name", "FC1234", "FC1235");
//            Assert

        } catch (Exception e) {
            fail();
        }
        assertEquals(flight1.getTickets().size(), 0);
        assertEquals(flight2.getTickets().size(), 1);
    }

    @Test
    public void changeTicket_WithNotAlternativeFlights_ShouldThrowFlightDoesNotExistException() {

//        Arrange
        List<Ticket> tickets = new ArrayList<>();
        List<Ticket> tickets2 = new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        ticket.setSeatLocation(new Ticket.SeatLocation(4, 'b'));
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane1 = new Plane("test-model", seatsPerCategory);
        Plane plane2 = new Plane("test-model", seatsPerCategory);

        Map<String, Flight> flights = new HashMap<>();
        String flightCode1 = "FC1234";
        String flightCode2 = "FC1235";
        Flight flight1 = new Flight(FlightStatus.SCHEDULED, "AC1234", flightCode1, plane1, tickets);
        Flight flight2 = new Flight(FlightStatus.SCHEDULED, "AC1235", flightCode2, plane2, tickets2);
        flights.put(flightCode1, flight1);
        flights.put(flightCode2, flight2);

        seatAssignmentService = new SeatAssignmentServiceImpl(flights, new EventsManagerImpl());

//        Act
        try {
            seatAssignmentService.changeTicket("test-name", "FC1234", "FC1235");
//            Assert
            fail();
        } catch (Exception e) {

            assertEquals("FlightDoesNotExistException", e.getMessage());
        }
    }
}
