package ar.edu.itba.pod.services;

import ar.edu.itba.pod.exceptions.*;
import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.server.notifications.EventsManagerImpl;
import ar.edu.itba.pod.server.services.AdminServiceImpl;
import ar.edu.itba.pod.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceImplTest {

    private Map<String, Plane> planes;
    private Map<String, Flight> flights;
    private AdminService adminService;

    @BeforeEach
    public void setUp() {
        this.planes = Collections.synchronizedMap(new HashMap<>());
        this.flights = Collections.synchronizedMap(new HashMap<>());
        this.adminService = new AdminServiceImpl(planes, flights, new EventsManagerImpl());

        String planeModelName = "Boeing 787";
        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.BUSINESS, new Pair<>(2, 2));
        seatsPerCategory.put(SeatCategory.PREMIUM_ECONOMY, new Pair<>(2, 2));
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(2, 2));
        Plane plane = new Plane(planeModelName, seatsPerCategory);
        this.planes.put(planeModelName, plane);

        String flightCode = "AR1235";
        String airportCode = "EZE";
        Flight flight = new Flight(FlightStatus.SCHEDULED, airportCode, flightCode, plane, new ArrayList<>());
        this.flights.put(flightCode, flight);
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
        assertEquals(planes.get(planeModelName).getModelName(), planeModelName);
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
        assertEquals(flights.get(flightCode).getFlightCode(), flightCode);
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
        Flight confirmedFlight = new Flight(FlightStatus.CONFIRMED, "EZE", flightCode, planes.values().stream().toList().get(0), new ArrayList<>());
        flights.put(flightCode, confirmedFlight);

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
        Flight cancelledFlight = new Flight(FlightStatus.CANCELLED, "EZE", flightCode, planes.values().stream().toList().get(0), new ArrayList<>());
        flights.put(flightCode, cancelledFlight);

        assertThrows(FlightStatusNotPendingException.class, () -> adminService.cancelFlight(flightCode));
    }

    @Test
    void rescheduleTickets_RescheduleSingleTicketOnSameCategory_ShouldSucceed() throws RemoteException {
        // Arrange
        String planeModelName = "Boeing 787";
        var plane = planes.get(planeModelName);

        String cancelledFlightCode = "CAN123";
        String newFlightCode = "NEW123";
        String airportCode = "EZE";

        var tickets = new ArrayList<Ticket>();
        tickets.add(new Ticket("Carlos", SeatCategory.ECONOMY, 5, 'a'));

        this.flights.clear();
        this.flights.put(cancelledFlightCode, new Flight(FlightStatus.CANCELLED, airportCode, cancelledFlightCode, plane, tickets));
        this.flights.put(newFlightCode, new Flight(FlightStatus.SCHEDULED, airportCode, newFlightCode, plane, new ArrayList<>()));

        try {
            // Act
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(List.of(), report.getFailure());
            assertEquals(1, report.getSuccess());
            assertEquals(1, flights.get(newFlightCode).getTickets().size());
            assertEquals(0, flights.get(cancelledFlightCode).getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rescheduleTickets_NoAlternativeFlights_ShouldFail() {
        // Arrange
        String planeModelName = "Boeing 787";
        var plane = planes.get(planeModelName);

        String cancelledFlightCode = "CAN123";
        String newFlightCode = "NEW123";

        var tickets = new ArrayList<Ticket>();
        tickets.add(new Ticket("Carlos", SeatCategory.ECONOMY, 5, 'a'));

        this.flights.clear();
        this.flights.put(cancelledFlightCode, new Flight(FlightStatus.CANCELLED, "JFK", cancelledFlightCode, plane, tickets));
        this.flights.put(newFlightCode ,new Flight(FlightStatus.SCHEDULED, "EE", newFlightCode, plane, new ArrayList<>()));

        try {
            // Act
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(1, report.getFailure().size());
            assertEquals(0, report.getSuccess());
            assertEquals(1, flights.get(cancelledFlightCode).getTickets().size());
            assertEquals(0, flights.get(newFlightCode).getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rescheduleTickets_RescheduleSingleTicketOnDifferentCategory_ShouldSucceed() throws RemoteException {
        // Arrange
        var privateJet = new Plane("Private Jet 1", new TreeMap<>(Map.of(SeatCategory.BUSINESS, new Pair<>(1, 1))));
        var cheapPlane = new Plane("Private Jet 2", new TreeMap<>(Map.of(SeatCategory.ECONOMY, new Pair<>(1, 1))));
        planes.put("Private Jet 1", privateJet);
        planes.put("Private Jet 2", cheapPlane);

        var tickets = new ArrayList<Ticket>();
        tickets.add(new Ticket("Carlos", SeatCategory.BUSINESS, 1, 'a'));

        this.flights.clear();
        String cancelledFlightCode = "CAN123";
        String newFlightCode = "NEW123";
        this.flights.put(cancelledFlightCode, new Flight(FlightStatus.CANCELLED, "EZE", cancelledFlightCode, privateJet, tickets));
        this.flights.put(newFlightCode ,new Flight(FlightStatus.SCHEDULED, "EZE", newFlightCode, cheapPlane, new ArrayList<>()));

        try {
            // Act
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(List.of(), report.getFailure());
            assertEquals(1, report.getSuccess());
            assertEquals(1, flights.get(newFlightCode).getTickets().size());
            assertEquals(0, flights.get(cancelledFlightCode).getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rescheduleTickets_FlightExistsButNoTicketsOnSimilarCategory_ShouldFail() {
        // Arrange
        var privateJet = new Plane("Private Jet", new TreeMap<>(Map.of(SeatCategory.BUSINESS, new Pair<>(1, 1))));
        var cheapPlane = planes.get("Boeing 787");
        planes.put("Private Jet" ,privateJet);

        var tickets = new ArrayList<Ticket>();
        tickets.add(new Ticket("Carlos", SeatCategory.ECONOMY, 5, 'a'));

        this.flights.clear();
        String ar111 = "AR111";
        String ex123 = "EX123";
        this.flights.put(ar111, new Flight(FlightStatus.CANCELLED, "EZE", ar111, cheapPlane, tickets));
        this.flights.put(ex123, new Flight(FlightStatus.SCHEDULED, "EZE", ex123, privateJet, new ArrayList<>()));

        // Act
        try {
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(1, report.getFailure().size());
            assertEquals(0, report.getSuccess());
            assertEquals(1, flights.get(ar111).getTickets().size());
            assertEquals(0, flights.get(ex123).getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rescheduleTickets_MultipleTickets_ShouldSucceed() {
        // Arrange
        var privateJet = new Plane("Private Jet", new TreeMap<>(Map.of(SeatCategory.BUSINESS, new Pair<>(1, 2))));
        planes.put("Private Jet", privateJet);

        var tickets = new ArrayList<Ticket>();
        tickets.add(new Ticket("Carlos", SeatCategory.BUSINESS, 1, 'a'));
        tickets.add(new Ticket("Carlos Segundo", SeatCategory.BUSINESS, 1, 'b'));

        this.flights.clear();
        String ar111 = "AR111";
        String ex123 = "EX123";
        this.flights.put(ar111, new Flight(FlightStatus.CANCELLED, "EZE", ar111, privateJet, tickets));
        this.flights.put(ex123, new Flight(FlightStatus.SCHEDULED, "EZE", ex123, privateJet, new ArrayList<>()));

        try {
            // act
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(0, report.getFailure().size());
            assertEquals(2, report.getSuccess());
            assertEquals(2, flights.get(ex123).getTickets().size());
            assertEquals(0, flights.get(ar111).getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rescheduleTickets_MultipleTickets_ShouldPreferBusinessSucceed() {
        // Arrange
        String privateJetModelName = "Private Jet";
        String cheapJetModelName = "Cheap Jet";
        var privateJet = new Plane(privateJetModelName, new TreeMap<>(Map.of(SeatCategory.BUSINESS, new Pair<>(1, 2))));
        planes.put(privateJetModelName ,privateJet);
        var cheapJet = new Plane(cheapJetModelName, new TreeMap<>(Map.of(SeatCategory.PREMIUM_ECONOMY, new Pair<>(1, 2))));
        planes.put(cheapJetModelName, cheapJet);

        var cancelledTickets = new ArrayList<Ticket>();
        cancelledTickets.add(new Ticket("Carlos", SeatCategory.BUSINESS, 1, 'a'));
        cancelledTickets.add(new Ticket("Carlos Segundo", SeatCategory.BUSINESS, 1, 'b'));

//        var scheduledTickets = new ArrayList<Ticket>();
//        scheduledTickets.add(new Ticket("Carla", SeatCategory.BUSINESS, 1, 'a'));

        this.flights.clear();
        String ar111 = "AR111";
        String ex123 = "EX123";
        String ex124 = "EX124";
        this.flights.put(ar111, new Flight(FlightStatus.CANCELLED, "EZE", ar111, privateJet, cancelledTickets));
        this.flights.put(ex123, new Flight(FlightStatus.SCHEDULED, "EZE", ex123, privateJet, new ArrayList<>()));
        this.flights.put(ex124, new Flight(FlightStatus.SCHEDULED, "EZE", ex124, cheapJet, new ArrayList<>()));

        try {
            // act
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(0, report.getFailure().size());
            assertEquals(2, report.getSuccess());
            assertEquals(2, flights.get(ex123).getTickets().size());
            assertEquals(0, flights.get(ex124).getTickets().size());
            assertEquals(0, flights.get(ar111).getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rescheduleTickets_MultipleTicketsWithoutSpots_ShouldFail() {
        // Arrange
        String privateJetModelName = "Private Jet";
        var privateJet = new Plane(privateJetModelName, new TreeMap<>(Map.of(SeatCategory.BUSINESS, new Pair<>(1, 2))));
        planes.put(privateJetModelName, privateJet);

        var cancelledTickets = new ArrayList<Ticket>();
        cancelledTickets.add(new Ticket("Carlos", SeatCategory.BUSINESS, 1, 'a'));
        cancelledTickets.add(new Ticket("Carlos Segundo", SeatCategory.BUSINESS, 1, 'b'));

        var scheduledTickets = new ArrayList<Ticket>();
        scheduledTickets.add(new Ticket("Carla", SeatCategory.BUSINESS, 1, 'a'));

        this.flights.clear();
        String ar111 = "AR111";
        String ex123 = "EX123";
        this.flights.put(ar111, new Flight(FlightStatus.CANCELLED, "EZE", ar111, privateJet, cancelledTickets));
        this.flights.put(ex123, new Flight(FlightStatus.SCHEDULED, "JFK", ex123, privateJet, new ArrayList<>()));

        try {
            // act
            var report = adminService.rescheduleTickets();

            // Assert
            assertEquals(2, report.getFailure().size());
            assertEquals(0, report.getSuccess());
            assertEquals(0, flights.get(ex123).getTickets().size());
            assertEquals(2, flights.get(ar111).getTickets().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
