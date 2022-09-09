package ar.edu.itba.pod.server.services;

import ar.edu.itba.pod.exceptions.*;
import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.services.AdminService;
import ar.edu.itba.pod.utils.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class AdminServiceImpl implements AdminService {

    private final List<Plane> planes;
    private final List<Flight> flights;

    public AdminServiceImpl(List<Plane> planes, List<Flight> flights) {
        this.planes = planes;
        this.flights = flights;
    }

    @Override
    public void addPlane(String planeModelName, TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory) throws RemoteException {
        if (planes.stream().anyMatch(plane -> plane.getModelName().equals(planeModelName))) {
            throw new PlaneModelAlreadyExistsException();
        }
        final Plane plane = new Plane(planeModelName, seatsPerCategory);
        planes.add(plane);
    }

    @Override
    public void addFlight(String planeModelName, String flightCode, String airportCode, List<Ticket> tickets) throws RemoteException {
        if (flights.stream().anyMatch(flight -> flight.getFlightCode().equals(flightCode))) {
            throw new FlightCodeAlreadyExistsException();
        }

        Plane plane = planes.stream().filter(p -> p.getModelName().equals(planeModelName)).findFirst().orElseThrow(PlaneModelNotExistException::new);
        Flight flight = new Flight(FlightStatus.SCHEDULED, airportCode, flightCode, plane, tickets);
        flights.add(flight);
    }

    @Override
    public FlightStatus getFlightStatus(String flightCode) throws RemoteException {
        Flight flight = this.flights.stream().filter(f -> f.getFlightCode().equals(flightCode)).findFirst().orElseThrow(FlightCodeNotExistException::new);
        return flight.getStatus();
    }

    @Override
    public void confirmFlight(String flightCode) throws RemoteException {
        setFlightStatus(flightCode, FlightStatus.CONFIRMED);
    }

    @Override
    public void cancelFlight(String flightCode) throws RemoteException {
        setFlightStatus(flightCode, FlightStatus.CANCELLED);
    }

    private void setFlightStatus(String flightCode, FlightStatus status) {
        Flight flight = this.flights.stream().filter(f -> f.getFlightCode().equals(flightCode)).findFirst().orElseThrow(FlightCodeNotExistException::new);
        if (!flight.getStatus().equals(FlightStatus.SCHEDULED)) {
            throw new FlightStatusNotPendingException();
        }
        flight.setStatus(status);
    }

    @Override
    public ReticketingReport rescheduleTickets() throws RemoteException {
        final List<Pair<Flight, Ticket>> tickets = this.flights.stream()
                .filter(f -> f.getStatus().equals(FlightStatus.CANCELLED))
                .sorted(Comparator.comparing(Flight::getFlightCode))
                .flatMap(f -> f.getTickets().stream()
                        .sorted(Comparator.comparing(Ticket::getPassengerName))
                        .map(t -> new Pair<>(f, t)))
                .collect(Collectors.toList());

        List<Pair<Flight, Ticket>> successfulTickets = new ArrayList<>();
        List<Pair<Flight, Ticket>> failedTickets = new ArrayList<>();
        tickets.forEach(pair -> {
            List<Flight> alternativeFlights = getAlternativeFlights(pair.getSecond(), pair.getFirst().getAirportCode());
            if (alternativeFlights.isEmpty()) {
                failedTickets.add(pair);
            } else {
                Flight flight = alternativeFlights.get(0);
                Ticket ticket = pair.getSecond();
                ticket.setSeatLocation(null);
                flight.getTickets().add(ticket);
                pair.getFirst().getTickets().remove(ticket);
                successfulTickets.add(pair);
            }
        });

        return new ReticketingReport(successfulTickets.size(),
                failedTickets.stream()
                        .map(pair -> new ReticketingReport.FailureTicket(pair.getSecond().getPassengerName(), pair.getFirst().getFlightCode()))
                        .collect(Collectors.toList()));
    }

    private List<Flight> getAlternativeFlights(Ticket ticket, String airportCode) {
        return this.flights.stream()
                .filter(f -> f.getAirportCode().equals(airportCode))
                .filter(f -> f.getStatus().equals(FlightStatus.SCHEDULED))
                .filter(f -> f.getMaxCategoryAvailable(ticket) != null)
                .sorted((o1, o2) -> {
                    if (o1.getMaxCategoryAvailable(ticket) != o2.getMaxCategoryAvailable(ticket)) {
                        return o1.getMaxCategoryAvailable(ticket).ordinal() - o2.getMaxCategoryAvailable(ticket).ordinal(); //we want the highest category (BUSINESS = 0)
                    }
                    if (o1.getFreeSeats(ticket.getSeatCategory()) != o2.getFreeSeats(ticket.getSeatCategory())) {
                        return o2.getFreeSeats(ticket.getSeatCategory()) - o1.getFreeSeats(ticket.getSeatCategory()); //reversed because we want the most free seats
                    }
                    return o1.getFlightCode().compareTo(o2.getFlightCode());

                })
                .collect(Collectors.toList());
    }
}
