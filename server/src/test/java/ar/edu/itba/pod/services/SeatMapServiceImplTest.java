package ar.edu.itba.pod.services;

import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.models.criteria.CategoryCriteria;
import ar.edu.itba.pod.models.criteria.Criteria;
import ar.edu.itba.pod.models.criteria.IdentityCriteria;
import ar.edu.itba.pod.models.criteria.RowCriteria;
import ar.edu.itba.pod.server.services.SeatMapServiceImpl;
import ar.edu.itba.pod.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeatMapServiceImplTest {

    private List<Plane> planes;
    private List<Flight> flights;
    private SeatMapService seatMapService;
    private List<Ticket> tickets;

    @BeforeEach
    public void setUp(){
        this.flights = new ArrayList<>();
        this.seatMapService = new SeatMapServiceImpl(flights);
        this.planes = new ArrayList<>();
        this.tickets = new ArrayList<>();

        String planeModelName = "Boeing 787";
        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.BUSINESS, new Pair<>(2, 2));
        seatsPerCategory.put(SeatCategory.PREMIUM_ECONOMY, new Pair<>(2, 2));
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(2, 2));
        Plane plane = new Plane(planeModelName, seatsPerCategory);
        this.planes.add(plane);


        String flightCode = "AR1235";
        String airportCode = "EZE";
        Flight flight = new Flight(FlightStatus.SCHEDULED, airportCode, flightCode, plane, tickets);
        this.flights.add(flight);

    }

    @Test
    public void requestAllSeats() throws RemoteException {
        final Criteria criteria = new IdentityCriteria();
        String passenger = "juan";
        Ticket juanTicket = new Ticket("juan", SeatCategory.BUSINESS);
        int row = 1;
        char col = 'A';
        juanTicket.setSeatLocation(new Ticket.SeatLocation(row,col));
        this.tickets.add(juanTicket);

        var result = seatMapService.getSeatMap(flights.get(0).getFlightCode(),criteria);

        assertEquals(passenger,result.get(0).getSeats()[col - 'A'].getPassenger());

    }

    @Test
    public void requestPerCategory() throws RemoteException {
        final Criteria criteria = new CategoryCriteria(SeatCategory.BUSINESS);
        String passenger = "juan";
        Ticket juanTicket = new Ticket("juan", SeatCategory.BUSINESS);
        int row = 1;
        char col = 'A';
        juanTicket.setSeatLocation(new Ticket.SeatLocation(row,col));
        this.tickets.add(juanTicket);

        var result = seatMapService.getSeatMap(flights.get(0).getFlightCode(),criteria);

        assertEquals(passenger,result.get(0).getSeats()[col - 'A'].getPassenger());
    }

    @Test
    public void requestByRow() throws RemoteException {
        int row = 1;
        char col = 'A';
        String passenger = "juan";
        final Criteria criteria = new RowCriteria(row);
        Ticket juanTicket = new Ticket("juan", SeatCategory.BUSINESS);

        juanTicket.setSeatLocation(new Ticket.SeatLocation(row,col));
        this.tickets.add(juanTicket);

        var result = seatMapService.getSeatMap(flights.get(0).getFlightCode(),criteria);

        assertEquals(passenger,result.get(0).getSeats()[col - 'A'].getPassenger());

    }
}
