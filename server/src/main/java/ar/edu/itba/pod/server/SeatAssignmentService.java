package ar.edu.itba.pod.server;

import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.server.exceptions.*;
import ar.edu.itba.pod.utils.Pair;

import java.util.List;
import java.util.Optional;

public class SeatAssignmentService implements ar.edu.itba.pod.services.SeatAssignmentService {

    List<Flight> flightList;

    public SeatAssignmentService(List<Flight> flightList) {
        this.flightList = flightList;
    }

    @Override
    public Boolean isSeatTaken(String flightCode, int row, char column) {
        if(row <= 0 || !Character.isLetter(column))
            throw new SeatDoesNotExistException();

        if(flightCode.isEmpty())
            throw new FlightDoesNotExistException();

        Flight flight = flightList.stream()
                .filter(f -> f.getFlightCode().equals(flightCode))
                .findFirst().orElseThrow(FlightDoesNotExistException::new);

        Plane plane = flight.getPlane();
        if( ((long) plane.getRows().size() < row) || (plane.getRows().get(row-1).getColumns() < (column-'a' + 1)) )
            throw new SeatDoesNotExistException();

        Ticket.SeatLocation seatLocation = new Ticket.SeatLocation(row, column);
        Optional<Ticket> ticketOnLocation = flight.getTickets().stream()
                .filter(t -> t.getSeatLocation().isPresent() && t.getSeatLocation().get().equals(seatLocation))
                .findAny();

        return ticketOnLocation.isPresent();
    }
    @Override
    public void assignSeat(String flightCode, String passenger, int row, char column) throws Exception {
        if(passenger.isEmpty())
            throw new InvalidPassengerException();
//        TODO: asegurarse que row 1 es el 0
        if(row <= 0 || !Character.isLetter(column))
            throw new SeatDoesNotExistException();

        if(flightCode.isEmpty())
            throw new FlightDoesNotExistException();

        Flight flight = flightList.stream()
                .filter(f -> f.getFlightCode().equals(flightCode))
                .findFirst().orElseThrow(FlightDoesNotExistException::new);

        if(flight.getStatus() == FlightStatus.CONFIRMED)
            throw new FlightIsConfirmedException();

        if(flight.getStatus() == FlightStatus.CANCELLED)
            throw new FlightIsCancelledException();

        Ticket ticket = flight.getTickets().stream()
                .filter(t -> t.getPassengerName().equals(passenger))
                .findAny().orElseThrow(InvalidPassengerException::new);

        if(ticket.getSeatLocation().isPresent())
            throw new PassengerAlreadyAssignedException();

        Ticket.SeatLocation seatLocation = new Ticket.SeatLocation(row, column);
        Optional<Ticket> ticketOnLocation = flight.getTickets().stream()
                .filter(t -> t.getSeatLocation().isPresent() && t.getSeatLocation().get().equals(seatLocation))
                .findAny();

        if(ticketOnLocation.isPresent())
            throw new SeatAlreadyAssignedException();
        Plane plane = flight.getPlane();
        if( ((long) plane.getRows().size() < row) || (plane.getRows().get(row-1).getColumns() < (column-'a' + 1)) )
            throw new SeatDoesNotExistException();

        //        seat - tiket
//        TODO: asegurarse que row 1 es el 0
        if(ticket.getSeatCategory().compareTo(plane.getRows().get(row-1).getCategory()) > 0 )
            throw new SeatCategoryIsToHighException();

        ticket.setSeatLocation(new Ticket.SeatLocation(row, column));

    }

    @Override
    public void changeSeat(String flightCode, String passenger, int row, char column) {

    }

    @Override
    public List<Flight> getAlternativeFlights(String flightCode, String passenger) {
        return null;
    }

    @Override
    public void changeTicket(String passenger, String flightCode, String newFlightCode) {

    }
}
