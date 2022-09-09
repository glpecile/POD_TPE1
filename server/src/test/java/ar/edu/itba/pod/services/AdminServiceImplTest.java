package ar.edu.itba.pod.services;

import ar.edu.itba.pod.exceptions.*;
import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.server.services.AdminServiceImpl;
import ar.edu.itba.pod.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceImplTest {

    private List<Plane> planes;
    private List<Flight> flights;
    private AdminService adminService;

    @BeforeEach
    public void setUp() {
        this.planes = new ArrayList<>();
        this.flights = new ArrayList<>();
        this.adminService = new AdminServiceImpl(planes, flights);

        String planeModelName = "Boeing 787";
        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.BUSINESS, new Pair<>(2, 2));
        seatsPerCategory.put(SeatCategory.PREMIUM_ECONOMY, new Pair<>(2, 2));
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(2, 2));
        Plane plane = new Plane(planeModelName, seatsPerCategory);
        this.planes.add(plane);

        String flightCode = "AR1235";
        String airportCode = "EZE";
        Flight flight = new Flight(FlightStatus.SCHEDULED, airportCode, flightCode, plane, new ArrayList<>());
        this.flights.add(flight);

    }

    @Test
    public void addPlaneTest() throws RemoteException {
        String planeModelName = "Boeing 747";
        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.BUSINESS, new Pair<>(2, 2));
        seatsPerCategory.put(SeatCategory.PREMIUM_ECONOMY, new Pair<>(2, 2));
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(2, 2));

        adminService.addPlane(planeModelName, seatsPerCategory);

        assertEquals(planes.size(), 2);
        assertEquals(planes.get(1).getModelName(), planeModelName);
    }

    @Test
    public void addAlreadyExistsPlane() {
        String planeModelName = "Boeing 787";
        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.BUSINESS, new Pair<>(2, 2));
        seatsPerCategory.put(SeatCategory.PREMIUM_ECONOMY, new Pair<>(2, 2));
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(2, 2));

        assertThrows(PlaneModelAlreadyExistsException.class, () -> adminService.addPlane(planeModelName, seatsPerCategory));
    }

    @Test
    public void addFlightTest() throws RemoteException {
        String planeModelName = "Boeing 787";
        String flightCode = "AR1234";
        String airportCode = "EZE";

        adminService.addFlight(planeModelName, flightCode, airportCode, new ArrayList<>());

        assertEquals(flights.size(), 2);
        assertEquals(flights.get(1).getFlightCode(), flightCode);
    }

    @Test
    public void addFlightWithNoPlaneTest() throws RemoteException {
        String planeModelName = "Boeing 747";
        String flightCode = "AR1234";
        String airportCode = "EZE";

        assertThrows(PlaneModelNotExistException.class, () -> adminService.addFlight(planeModelName, flightCode, airportCode, new ArrayList<>()));
    }

    @Test
    public void flightCodeAlreadyExistsTest() throws RemoteException {
        String planeModelName = "Boeing 787";
        String flightCode = "AR1235";
        String airportCode = "EZE";

        assertThrows(FlightCodeAlreadyExistsException.class, () -> adminService.addFlight(planeModelName, flightCode, airportCode, new ArrayList<>()));
    }

    @Test
    public void getFlightStatusTest() throws RemoteException {
        String flightCode = "AR1235";

        assertEquals(adminService.getFlightStatus(flightCode), FlightStatus.SCHEDULED);
    }

    @Test
    public void getFlightStatusWithNoFlightTest() throws RemoteException {
        String flightCode = "AR1234";

        assertThrows(FlightCodeNotExistException.class, () -> adminService.getFlightStatus(flightCode));
    }

    @Test
    public void confirmFlightTest() throws RemoteException {
        String flightCode = "AR1235";

        adminService.confirmFlight(flightCode);

        assertEquals(adminService.getFlightStatus(flightCode), FlightStatus.CONFIRMED);
    }

    @Test
    public void confirmFlightWithNoFlightTest() {
        String flightCode = "AR1236";
        Flight confirmedFlight = new Flight(FlightStatus.CONFIRMED, "EZE", flightCode, planes.get(0), new ArrayList<>());
        flights.add(confirmedFlight);

        assertThrows(FlightStatusNotPendingException.class, () -> adminService.confirmFlight(flightCode));
    }

    @Test
    public void cancelFlightTest() throws RemoteException {
        String flightCode = "AR1235";

        adminService.cancelFlight(flightCode);

        assertEquals(adminService.getFlightStatus(flightCode), FlightStatus.CANCELLED);
    }

    @Test
    public void cancelFlightWithNoFlightTest() {
        String flightCode = "AR1236";
        Flight cancelledFlight = new Flight(FlightStatus.CANCELLED, "EZE", flightCode, planes.get(0), new ArrayList<>());
        flights.add(cancelledFlight);

        assertThrows(FlightStatusNotPendingException.class, () -> adminService.cancelFlight(flightCode));
    }

    @Test
    void rescheduleTickets_RescheduleSingleTicketOnSameCategory_ShouldSucceed() throws RemoteException {
        // Arrange
        String planeModelName = "Boeing 787";
        var plane = planes.stream().filter(p -> p.getModelName().equals(planeModelName)).findFirst().orElseThrow();

        String cancelledFlightCode = "CAN123";
        String newFlightCode = "NEW123";
        String airportCode = "EZE";

        var tickets = new ArrayList<Ticket>();
        tickets.add(new Ticket("Carlos",SeatCategory.ECONOMY, 5,'a'));

        this.flights.clear();
        this.flights.add(new Flight(FlightStatus.CANCELLED, airportCode, cancelledFlightCode, plane, tickets));
        this.flights.add(new Flight(FlightStatus.SCHEDULED, airportCode, newFlightCode, plane, new ArrayList<>()));

        try {
            // Act
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(List.of(),report.getFailure());
            assertEquals(1,report.getSuccess());
            assertEquals(1,flights.stream().filter(f -> f.getFlightCode().equals(newFlightCode)).findFirst().orElseThrow().getTickets().size());
            assertEquals(0,flights.stream().filter(f -> f.getFlightCode().equals(cancelledFlightCode)).findFirst().orElseThrow().getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rescheduleTickets_NoAlternativeFlights_ShouldFail() {
        // Arrange
        String planeModelName = "Boeing 787";
        var plane = planes.stream().filter(p -> p.getModelName().equals(planeModelName)).findFirst().orElseThrow();

        String cancelledFlightCode = "CAN123";
        String newFlightCode = "NEW123";

        var tickets = new ArrayList<Ticket>();
        tickets.add(new Ticket("Carlos",SeatCategory.ECONOMY, 5,'a'));

        this.flights.clear();
        this.flights.add(new Flight(FlightStatus.CANCELLED, "JFK", cancelledFlightCode, plane, tickets));
        this.flights.add(new Flight(FlightStatus.SCHEDULED, "EE", newFlightCode, plane, new ArrayList<>()));

        try {
            // Act
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(1,report.getFailure().size());
            assertEquals(0,report.getSuccess());
            assertEquals(1,flights.stream().filter(f -> f.getFlightCode().equals(cancelledFlightCode)).findFirst().orElseThrow().getTickets().size());
            assertEquals(0,flights.stream().filter(f -> f.getFlightCode().equals(newFlightCode)).findFirst().orElseThrow().getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rescheduleTickets_RescheduleSingleTicketOnDifferentCategory_ShouldSucceed() throws RemoteException {
        // Arrange
        var privateJet = new Plane("Private Jet",new TreeMap<>(Map.of(SeatCategory.BUSINESS, new Pair<>(1, 1))));
        var cheapPlane = new Plane("Private Jet",new TreeMap<>(Map.of(SeatCategory.ECONOMY, new Pair<>(1, 1))));
        planes.addAll(List.of(privateJet,cheapPlane));

        var tickets = new ArrayList<Ticket>();
        tickets.add(new Ticket("Carlos", SeatCategory.BUSINESS, 1, 'a'));

        this.flights.clear();
        this.flights.add(new Flight(FlightStatus.CANCELLED, "EZE", "CAN123", privateJet, tickets));
        this.flights.add(new Flight(FlightStatus.SCHEDULED, "EZE","NEW123" , cheapPlane, new ArrayList<>()));

        try {
            // Act
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(List.of(),report.getFailure());
            assertEquals(1,report.getSuccess());
            assertEquals(1,flights.stream().filter(f -> f.getFlightCode().equals("NEW123")).findFirst().orElseThrow().getTickets().size());
            assertEquals(0,flights.stream().filter(f -> f.getFlightCode().equals("CAN123")).findFirst().orElseThrow().getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rescheduleTickets_FlightExistsButNoTicketsOnSimilarCategory_ShouldFail(){
        // Arrange
        var privateJet = new Plane("Private Jet",new TreeMap<>(Map.of(SeatCategory.BUSINESS, new Pair<>(1, 1))));
        var cheapPlane = planes.stream().filter(p -> p.getModelName().equals("Boeing 787")).findFirst().orElseThrow();
        planes.add(privateJet);

        var tickets = new ArrayList<Ticket>();
        tickets.add(new Ticket("Carlos", SeatCategory.ECONOMY, 5, 'a'));

        this.flights.clear();
        this.flights.add(new Flight(FlightStatus.CANCELLED, "EZE", "AR111", cheapPlane, tickets));
        this.flights.add(new Flight(FlightStatus.SCHEDULED, "EZE", "EX123", privateJet, new ArrayList<>()));

        // Act
        try {
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(1, report.getFailure().size());
            assertEquals(0, report.getSuccess());
            assertEquals(1, flights.stream().filter(f -> f.getFlightCode().equals("AR111")).findFirst().orElseThrow().getTickets().size());
            assertEquals(0, flights.stream().filter(f -> f.getFlightCode().equals("EX123")).findFirst().orElseThrow().getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rescheduleTickets_MultipleTickets_ShouldSucceed(){
        // Arrange
        var privateJet = new Plane("Private Jet",new TreeMap<>(Map.of(SeatCategory.BUSINESS, new Pair<>(1, 2))));
        planes.add(privateJet);

        var tickets = new ArrayList<Ticket>();
        tickets.add(new Ticket("Carlos", SeatCategory.BUSINESS, 1, 'a'));
        tickets.add(new Ticket("Carlos Segundo", SeatCategory.BUSINESS, 1, 'b'));

        this.flights.clear();
        this.flights.add(new Flight(FlightStatus.CANCELLED, "EZE", "AR111", privateJet, tickets));
        this.flights.add(new Flight(FlightStatus.SCHEDULED, "EZE", "EX123", privateJet, new ArrayList<>()));

        try{
            // act
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(0, report.getFailure().size());
            assertEquals(2, report.getSuccess());
            assertEquals(2, flights.stream().filter(f -> f.getFlightCode().equals("EX123")).findFirst().orElseThrow().getTickets().size());
            assertEquals(0, flights.stream().filter(f -> f.getFlightCode().equals("AR111")).findFirst().orElseThrow().getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rescheduleTickets_MultipleTickets_ShouldPreferBusinessSucceed(){
        // Arrange
        var privateJet = new Plane("Private Jet",new TreeMap<>(Map.of(SeatCategory.BUSINESS, new Pair<>(1, 2))));
        planes.add(privateJet);
        var cheapJet = new Plane("Cheap Jet",new TreeMap<>(Map.of(SeatCategory.PREMIUM_ECONOMY, new Pair<>(1, 2))));
        planes.add(cheapJet);

        var cancelledTickets = new ArrayList<Ticket>();
        cancelledTickets.add(new Ticket("Carlos", SeatCategory.BUSINESS, 1, 'a'));
        cancelledTickets.add(new Ticket("Carlos Segundo", SeatCategory.BUSINESS, 1, 'b'));

//        var scheduledTickets = new ArrayList<Ticket>();
//        scheduledTickets.add(new Ticket("Carla", SeatCategory.BUSINESS, 1, 'a'));

        this.flights.clear();
        this.flights.add(new Flight(FlightStatus.CANCELLED, "EZE", "AR111", privateJet, cancelledTickets));
        this.flights.add(new Flight(FlightStatus.SCHEDULED, "EZE", "EX123", privateJet, new ArrayList<>()));
        this.flights.add(new Flight(FlightStatus.SCHEDULED, "EZE", "EX124", cheapJet, new ArrayList<>()));

        try{
            // act
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(0, report.getFailure().size());
            assertEquals(2, report.getSuccess());
            assertEquals(2, flights.stream().filter(f -> f.getFlightCode().equals("EX123")).findFirst().orElseThrow().getTickets().size());
            assertEquals(0, flights.stream().filter(f -> f.getFlightCode().equals("EX124")).findFirst().orElseThrow().getTickets().size());
            assertEquals(0, flights.stream().filter(f -> f.getFlightCode().equals("AR111")).findFirst().orElseThrow().getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rescheduleTickets_MultipleTicketsWithoutSpots_ShouldFail(){
        // Arrange
        var privateJet = new Plane("Private Jet",new TreeMap<>(Map.of(SeatCategory.BUSINESS, new Pair<>(1, 2))));
        planes.add(privateJet);

        var cancelledTickets = new ArrayList<Ticket>();
        cancelledTickets.add(new Ticket("Carlos", SeatCategory.BUSINESS, 1, 'a'));
        cancelledTickets.add(new Ticket("Carlos Segundo", SeatCategory.BUSINESS, 1, 'b'));

        var scheduledTickets = new ArrayList<Ticket>();
        scheduledTickets.add(new Ticket("Carla", SeatCategory.BUSINESS, 1, 'a'));

        this.flights.clear();
        this.flights.add(new Flight(FlightStatus.CANCELLED, "EZE", "AR111", privateJet, cancelledTickets));
        this.flights.add(new Flight(FlightStatus.SCHEDULED, "JFK", "EX123", privateJet, new ArrayList<>()));

        try{
            // act
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(2, report.getFailure().size());
            assertEquals(0, report.getSuccess());
            assertEquals(0, flights.stream().filter(f -> f.getFlightCode().equals("EX123")).findFirst().orElseThrow().getTickets().size());
            assertEquals(2, flights.stream().filter(f -> f.getFlightCode().equals("AR111")).findFirst().orElseThrow().getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


}
