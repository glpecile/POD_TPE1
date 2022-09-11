package ar.edu.itba.pod.server.services;

import ar.edu.itba.pod.exceptions.*;
import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.server.notifications.EventsManagerImpl;
import ar.edu.itba.pod.services.AdminService;
import ar.edu.itba.pod.utils.Pair;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public class AdminServiceImpl implements AdminService {

    private final Map<String, Plane> planes;
    private final Map<String, Flight> flights;
    private final EventsManagerImpl eventsManager;

    public AdminServiceImpl(Map<String, Plane> planes, Map<String, Flight> flights, EventsManagerImpl eventsManager) {
        this.planes = planes;
        this.flights = flights;
        this.eventsManager = eventsManager;
    }

    @Override
    public void addPlane(String planeModelName, TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory) throws RemoteException {
        if (planes.containsKey(planeModelName)) {
            throw new PlaneModelAlreadyExistsException();
        }
        final Plane plane = new Plane(planeModelName, seatsPerCategory);
        planes.put(planeModelName, plane);
    }

    @Override
    public void addFlight(String planeModelName, String flightCode, String airportCode, List<Ticket> tickets) throws RemoteException {
        if (flights.containsKey(flightCode)) {
            throw new FlightCodeAlreadyExistsException();
        }

        Plane plane = Optional.ofNullable(planes.get(planeModelName)).orElseThrow(PlaneModelNotExistException::new);
        Flight flight = new Flight(FlightStatus.SCHEDULED, airportCode, flightCode, plane, tickets);
        flights.put(flightCode, flight);
    }

    @Override
    public FlightStatus getFlightStatus(String flightCode) throws RemoteException {
        Flight flight = Optional.ofNullable(flights.get(flightCode)).orElseThrow(FlightCodeNotExistException::new);
        return flight.getStatus();
    }

    @Override
    public void confirmFlight(String flightCode) throws RemoteException {
        Flight flight = setFlightStatus(flightCode, FlightStatus.CONFIRMED);
        this.eventsManager.notifyFlightConfirmation(flight);
    }

    @Override
    public void cancelFlight(String flightCode) throws RemoteException {
        Flight flight = setFlightStatus(flightCode, FlightStatus.CANCELLED);
        this.eventsManager.notifyFlightCancellation(flight);
    }

    private Flight setFlightStatus(String flightCode, FlightStatus status) {
        Flight flight = Optional.ofNullable(flights.get(flightCode)).orElseThrow(FlightCodeNotExistException::new);
        if (!flight.getStatus().equals(FlightStatus.SCHEDULED)) {
            throw new FlightStatusNotPendingException();
        }
        flight.setStatus(status);
        return flight;
    }

    @Override
    public ReticketingReport rescheduleTickets() throws RemoteException {
        final List<Pair<Flight, Ticket>> tickets = this.flights.values().stream()
                .filter(f -> f.getStatus().equals(FlightStatus.CANCELLED))
                .sorted(Comparator.comparing(Flight::getFlightCode))
                .flatMap(f -> f.getTickets().stream()
                        .sorted(Comparator.comparing(Ticket::getPassengerName))
                        .map(t -> new Pair<>(f, t)))
                .toList();

        List<Pair<Flight, Ticket>> successfulTickets = new ArrayList<>();
        List<Pair<Flight, Ticket>> failedTickets = new ArrayList<>();
        tickets.forEach(pair -> {
            List<Flight> alternativeFlights = getAlternativeFlights(pair.getSecond(), pair.getFirst());
            if (alternativeFlights.isEmpty()) {
                failedTickets.add(pair);
            } else {
                Flight flight = alternativeFlights.get(0);
                Ticket ticket = pair.getSecond();
                ticket.setSeatLocation(null);
                flight.getTickets().add(ticket);
                pair.getFirst().getTickets().remove(ticket);
                successfulTickets.add(pair);
                try {
                    eventsManager.notifyFlightChange(pair.getFirst(), flight, ticket.getPassengerName());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return new ReticketingReport(successfulTickets.size(),
                failedTickets.stream()
                        .map(pair -> new ReticketingReport.FailureTicket(pair.getSecond().getPassengerName(), pair.getFirst().getFlightCode()))
                        .collect(Collectors.toList()));
    }

    private List<Flight> getAlternativeFlights(Ticket ticket, Flight flight) {
        return AlternativeFlights.getAlternativeFlights(flights.values().stream().toList(), ticket, flight);

    }
}
