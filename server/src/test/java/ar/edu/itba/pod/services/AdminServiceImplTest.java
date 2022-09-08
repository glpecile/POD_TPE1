package ar.edu.itba.pod.services;

import ar.edu.itba.pod.exceptions.*;
import ar.edu.itba.pod.models.Flight;
import ar.edu.itba.pod.models.FlightStatus;
import ar.edu.itba.pod.models.Plane;
import ar.edu.itba.pod.models.SeatCategory;
import ar.edu.itba.pod.server.services.AdminServiceImpl;
import ar.edu.itba.pod.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

}
