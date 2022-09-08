package ar.edu.itba.pod.services;

import ar.edu.itba.pod.exceptions.PlaneModelAlreadyExistsException;
import ar.edu.itba.pod.models.Flight;
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

}
