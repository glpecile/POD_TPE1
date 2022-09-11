package ar.edu.itba.pod.server.services;

import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.server.exceptions.*;
import ar.edu.itba.pod.server.notifications.EventsManager;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SeatAssignmentServiceImpl implements ar.edu.itba.pod.services.SeatAssignmentService {

    private final Map<String, Flight> flights;
    private final EventsManager eventsManager;

    public SeatAssignmentServiceImpl(Map<String, Flight> flights, EventsManager eventsManager) {
        this.flights = flights;
        this.eventsManager = eventsManager;
    }

    private Ticket validatePassenger(String passenger, Flight flight) {
        if (passenger.isEmpty())
            throw new InvalidPassengerException();

        synchronized (flight.getTickets()) {
            return flight.getTickets().stream()
                    .filter(t -> t.getPassengerName().equals(passenger))
                    .findAny().orElseThrow(InvalidPassengerException::new);
        }
    }

    private Plane validateSeatLocation(int row, char column, Flight flight) {
        if (row <= 0 || !Character.isLetter(column))
            throw new SeatDoesNotExistException();

        Plane plane = flight.getPlane();
        if (((long) plane.getRows().size() < row) || (plane.getRows().get(row - 1).getColumns() < (column - 'a' + 1)))
            throw new SeatDoesNotExistException();
        return plane;
    }

    private Flight validateFlight(String flightCode) {
        if (flightCode.isEmpty())
            throw new FlightDoesNotExistException();

        Flight flight = Optional.ofNullable(flights.get(flightCode)).orElseThrow(FlightDoesNotExistException::new);

        if (flight.isStatus(FlightStatus.CONFIRMED))
            throw new FlightIsConfirmedException();

        return flight;
    }

    private Optional<Ticket> ticketOnSeatLocation(Flight flight, int row, char column) {
        Ticket.SeatLocation seatLocation = new Ticket.SeatLocation(row, column);
        synchronized (flight.getTickets()) {
            return flight.getTickets().stream()
                    .filter(t -> t.getSeatLocation().isPresent() && t.getSeatLocation().get().equals(seatLocation))
                    .findAny();
        }
    }

    @Override
    public Boolean isSeatTaken(String flightCode, int row, char column) throws RemoteException {
        Flight flight = validateFlight(flightCode);

        if (flight.isStatus(FlightStatus.CANCELLED))
            throw new FlightIsCancelledException();

        validateSeatLocation(row, column, flight);
        Optional<Ticket> ticketOnLocation = ticketOnSeatLocation(flight, row, column);

        return ticketOnLocation.isPresent();
    }

    @Override
    public void assignSeat(String flightCode, String passenger, int row, char column) throws RemoteException {
        Flight flight = validateFlight(flightCode);

        if (flight.isStatus(FlightStatus.CANCELLED))
            throw new FlightIsCancelledException();

        Plane plane = validateSeatLocation(row, column, flight);

        Ticket ticket = validatePassenger(passenger, flight);
        synchronized (flights.get(flightCode)) {
            if (ticket.getSeatLocation().isPresent())
                throw new PassengerAlreadyAssignedException();

            Optional<Ticket> ticketOnLocation = ticketOnSeatLocation(flight, row, column);
            if (ticketOnLocation.isPresent())
                throw new SeatAlreadyAssignedException();

            //        seat - tiket
            if (ticket.getSeatCategory().compareTo(plane.getRows().get(row - 1).getCategory()) > 0)
                throw new SeatCategoryIsToHighException();

            ticket.setSeatLocation(new Ticket.SeatLocation(row, column));
            eventsManager.notifySeatAssignment(flight, ticket);
        }
    }

    @Override
    public void changeSeat(String flightCode, String passenger, int row, char column) throws RemoteException {
        Flight flight = validateFlight(flightCode);

        if (flight.isStatus(FlightStatus.CANCELLED))
            throw new FlightIsCancelledException();

        validateSeatLocation(row, column, flight);

        Ticket ticket = validatePassenger(passenger, flight);

        synchronized (flights.get(flightCode)) {
            Ticket.SeatLocation oldSeatLocation = new Ticket.SeatLocation(ticket.getSeatLocation()
                    .orElseThrow(PassengerNotAssignedException::new)
            );
            SeatCategory oldSeatCategory = flight.getPlane().getRows().get(oldSeatLocation.getRow() - 1).getCategory();
            ;
            Optional<Ticket> ticketOnLocation = ticketOnSeatLocation(flight, row, column);

            if (ticketOnLocation.isPresent() && !oldSeatLocation.equals(new Ticket.SeatLocation(row, column)))
                throw new SeatAlreadyAssignedException();

            ticket.setSeatLocation(new Ticket.SeatLocation(row, column));
            eventsManager.notifySeatChange(flight, oldSeatLocation, oldSeatCategory, ticket);
        }
    }

    @Override
    public List<AlternativeFlight> getAlternativeFlights(String flightCode, String passenger) throws RemoteException {
        Flight flight = validateFlight(flightCode);

        if (flight.isStatus(FlightStatus.CONFIRMED))
            throw new FlightIsConfirmedException();

        Ticket ticket = validatePassenger(passenger, flight);

        synchronized (flights.values()) {
            return AlternativeFlight.getAlternativeFlights(flights.values(), ticket, flight).stream()
                    .map(f -> new AlternativeFlight(f, f.getMaxCategoryAvailable(ticket.getSeatCategory()), f.getFreeSeatsInCategory(f.getMaxCategoryAvailable(ticket.getSeatCategory()))))
                    .toList();
        }
    }

    @Override
    public void changeTicket(String passenger, String flightCode, String newFlightCode) throws RemoteException {
        Flight oldFlight = validateFlight(flightCode);
        List<AlternativeFlight> alternativeFlights = null;
        try {
            alternativeFlights = getAlternativeFlights(flightCode, passenger);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // nueva excepcion
        Flight newFlight = alternativeFlights.stream()
                .map(AlternativeFlight::getFlight)
                .filter(flight -> flight.getFlightCode().equals(newFlightCode))
                .findFirst()
                .orElseThrow(FlightDoesNotExistException::new);

        validateFlight(newFlightCode);
        Ticket ticket = validatePassenger(passenger, oldFlight);

        ticket.setSeatLocation(null);
        oldFlight.getTickets().remove(ticket);
        newFlight.getTickets().add(ticket);
        eventsManager.notifyFlightChange(oldFlight, newFlight, passenger);
    }
}
