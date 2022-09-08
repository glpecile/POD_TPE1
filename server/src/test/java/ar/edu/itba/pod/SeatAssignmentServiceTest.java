package ar.edu.itba.pod;

import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.server.SeatAssignmentService;
import ar.edu.itba.pod.utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SeatAssignmentServiceTest {
    SeatAssignmentService seatAssignmentService;
    @Test
    public void AssignSeat_WithWeirdFlighCode_ShouldThrowFlightDoesNotExist(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);

//        Act
        try {
            seatAssignmentService.assignSeat("23232", "test-name", 1, 'a');
//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("FlightDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_WithEmptyFlightCode_ShouldThrowFlightDoesNotExist(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);

//        Act
        try {
            seatAssignmentService.assignSeat("", "test-name", 1, 'a');
//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("FlightDoesNotExistException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_ShouldThrowFlightIsConfirmedException(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.CONFIRMED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);
//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 1, 'a');
//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("FlightIsConfirmedException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_ShouldThrowFlightIsCancelledException(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.CANCELLED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);
//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 1, 'a');
//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("FlightIsCancelledException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_WithEmptyString_ShouldThrowInvalidPassengerException(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);
//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "", 1, 'a');
//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("InvalidPassengerException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_WithNonExistingPassenger_ShouldThrowInvalidPassengerException(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);
//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "different-name", 1, 'a');
//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("InvalidPassengerException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_ShouldThrowPassengerAlreadyAssignedException(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        ticket.setSeatLocation(new Ticket.SeatLocation(1, 'a'));
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);
//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 1, 'b');
//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("PassengerAlreadyAssignedException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_ShouldThrowSeatAlreadyAssignedException(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        Ticket ticket2 = new Ticket("test2-name", SeatCategory.ECONOMY);
        ticket2.setSeatLocation(new Ticket.SeatLocation(1, 'a'));
        tickets.add(ticket);
        tickets.add(ticket2);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);
//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 1, 'a');
//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("SeatAlreadyAssignedException", e.getMessage());
        }
    }

    @Test
    public void AssignSeat_ShouldThrowSeatCategoryIsToHighException(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.BUSINESS, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);

//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 1, 'a');

//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("SeatCategoryIsToHighException", e.getMessage());
        }
    }
    @Test
    public void AssignSeat_WithBigColumn_ShouldThrowSeatDoesNotExistException(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);

//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 1, 'c');

//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }
    @Test
    public void AssignSeat_WithBigRow_ShouldThrowSeatDoesNotExistException(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);

//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 5, 'b');

//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }
    @Test
    public void AssignSeat_WithNotLetterColumn_ShouldThrowSeatDoesNotExistException(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);

//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 4, ';');

//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }
    @Test
    public void AssignSeat_WithSmallRow_ShouldThrowSeatDoesNotExistException(){

//        Arrange
        List<Ticket> tickets =new ArrayList<>();
        Ticket ticket = new Ticket("test-name", SeatCategory.ECONOMY);
        tickets.add(ticket);

        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(4, 2));

        Plane plane = new Plane("test-model" ,seatsPerCategory);

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight(FlightStatus.SCHEDULED, "AC1234", "FC1234", plane, tickets);
        flights.add(flight);
        seatAssignmentService = new SeatAssignmentService(flights);

//        Act
        try {
            seatAssignmentService.assignSeat("FC1234", "test-name", 0, 'a');

//            Assert
            fail();
        }catch (Exception e) {
            assertEquals("SeatDoesNotExistException", e.getMessage());
        }
    }



}
