package ar.edu.itba.pod.server.services;

import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.server.exceptions.*;

import java.util.List;
import java.util.Optional;

public class SeatAssignmentService implements ar.edu.itba.pod.services.SeatAssignmentService {

    List<Flight> flightList;

    public SeatAssignmentService(List<Flight> flightList) {
        this.flightList = flightList;
    }
    private Ticket validatePassenger(String passenger, Flight flight) throws Exception{
        if(passenger.isEmpty())
            throw new InvalidPassengerException();
        return flight.getTickets().stream()
                .filter(t -> t.getPassengerName().equals(passenger))
                .findAny().orElseThrow(InvalidPassengerException::new);
    }
    private Plane validateSeatLocation(int row, char column, Flight flight) throws Exception{
        if(row <= 0 || !Character.isLetter(column))
            throw new SeatDoesNotExistException();

        Plane plane = flight.getPlane();
        if( ((long) plane.getRows().size() < row) || (plane.getRows().get(row-1).getColumns() < (column-'a' + 1)) )
            throw new SeatDoesNotExistException();
        return plane;
    }
    private Flight validateFlight(String flightCode)throws Exception{
        if(flightCode.isEmpty())
            throw new FlightDoesNotExistException();

        Flight flight = flightList.stream()
                .filter(f -> f.getFlightCode().equals(flightCode))
                .findFirst().orElseThrow(FlightDoesNotExistException::new);

        if(flight.getStatus() == FlightStatus.CONFIRMED)
            throw new FlightIsConfirmedException();
        return flight;
    }
    private Optional<Ticket> ticketOnSeatLocation(Flight flight, int row, char column){
        Ticket.SeatLocation seatLocation = new Ticket.SeatLocation(row, column);
        return flight.getTickets().stream()
                .filter(t -> t.getSeatLocation().isPresent() && t.getSeatLocation().get().equals(seatLocation))
                .findAny();
    }

    @Override
    public Boolean isSeatTaken(String flightCode, int row, char column) throws Exception {

        Flight flight = validateFlight(flightCode);
        if(flight.getStatus() == FlightStatus.CANCELLED)
            throw new FlightIsCancelledException();
        validateSeatLocation(row, column, flight);

        Optional<Ticket> ticketOnLocation = ticketOnSeatLocation(flight, row, column);

        return ticketOnLocation.isPresent();
    }
    @Override
    public void assignSeat(String flightCode, String passenger, int row, char column) throws Exception {

        Flight flight = validateFlight(flightCode);
        if(flight.getStatus() == FlightStatus.CANCELLED)
            throw new FlightIsCancelledException();
        Plane plane = validateSeatLocation(row, column, flight);
        Ticket ticket = validatePassenger(passenger, flight);

        if(ticket.getSeatLocation().isPresent())
            throw new PassengerAlreadyAssignedException();

        Optional<Ticket> ticketOnLocation = ticketOnSeatLocation(flight, row, column);

        if(ticketOnLocation.isPresent())
            throw new SeatAlreadyAssignedException();

        //        seat - tiket
        if(ticket.getSeatCategory().compareTo(plane.getRows().get(row-1).getCategory()) > 0 )
            throw new SeatCategoryIsToHighException();

        ticket.setSeatLocation(new Ticket.SeatLocation(row, column));

    }

    @Override
    public void changeSeat(String flightCode, String passenger, int row, char column) throws Exception{

        Flight flight = validateFlight(flightCode);
        if(flight.getStatus() == FlightStatus.CANCELLED)
            throw new FlightIsCancelledException();
        validateSeatLocation(row, column, flight);
        Ticket ticket = validatePassenger(passenger, flight);

        if(ticket.getSeatLocation().isEmpty())
            throw new PassengerNotAssignedException();

        if(ticket.getSeatLocation().isPresent() && ticket.getSeatLocation().get().equals(new Ticket.SeatLocation(row, column))){
//            se quiere cambiar a su mismo lugar
            return;
        }

        Optional<Ticket> ticketOnLocation = ticketOnSeatLocation(flight, row, column);

        if(ticketOnLocation.isPresent())
            throw new SeatAlreadyAssignedException();

        ticket.setSeatLocation(new Ticket.SeatLocation(row, column));

    }


    @Override
    public List<Flight> getAlternativeFlights(String flightCode, String passenger) throws Exception {
        Flight flight = validateFlight(flightCode);
        if(flight.getStatus().equals(FlightStatus.CONFIRMED))
            throw new FlightIsConfirmedException();
        Ticket ticket = validatePassenger(passenger, flight);
        return AlternativeFlights.getAlternativeFlights(flightList, ticket , flight);
    }

    @Override
    public void changeTicket(String passenger, String flightCode, String newFlightCode) throws Exception {
        Flight oldFlight = validateFlight(flightCode);
        List<Flight> alternativeFlights = getAlternativeFlights(flightCode, passenger);
//        nueva excepcion
        Flight newFlight = alternativeFlights.stream()
                .findFirst()
                .filter(f-> f.getFlightCode().equals(newFlightCode))
                .orElseThrow(FlightDoesNotExistException::new);

        validateFlight(newFlightCode);
        Ticket ticket = validatePassenger(passenger, oldFlight);

        ticket.setSeatLocation(null);

        oldFlight.getTickets().remove(ticket);
        newFlight.getTickets().add(ticket);

    }
}
