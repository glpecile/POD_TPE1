package ar.edu.itba.pod.server.services;

import ar.edu.itba.pod.exceptions.FlightDoesNotExistException;
import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.models.criteria.Criteria;
import ar.edu.itba.pod.services.SeatMapService;

import java.rmi.RemoteException;
import java.util.*;

public class SeatMapServiceImpl implements SeatMapService {

    private final List<Flight> flights;


    public SeatMapServiceImpl(List<Flight> flights) {
        this.flights = flights;
    }

    private Seat[] createNewSeats(int columns) {
        Seat[] toReturn = new Seat[columns + 1];
        for (int i = 0; i < columns; i++) {
            toReturn[i] = new Seat((char) ('A' + i));
        }
        return toReturn;
    }

    @Override
    public List<SeatRow> getSeatMap(String flightCode, Criteria criteria) throws RemoteException {

        //Primero encontrar el vuelo segun el string dado.
        Flight flight = flights.stream().filter(f -> f.getFlightCode().equals(flightCode)).findFirst().orElseThrow(FlightDoesNotExistException::new);

        //Filtrar dentro de los asientos de acuerdo al criterio recibido.

        //Pedimos los seats que ya estan asignados


        final Map<Ticket.SeatLocation, String> assignees = new HashMap<>();

        flight.getTickets().stream()
                .filter( t -> t.getSeatLocation().isPresent()).
                forEach( t -> assignees.put(t.getSeatLocation().orElse(null),t.getPassengerName()));

        //Creamos las rows

        final List<Plane.RowDescription> rawRows = flight.getPlane().getRows();

        //Creamos la coleccion de filas con los seats, chequeando cuando corresponde que esten asignados

        List<SeatRow> seatRows = new ArrayList<>();
        rawRows.forEach(r -> seatRows.add(new SeatRow(r.getRow(), createNewSeats(r.getColumns()), r.getCategory())));

        assignees.forEach( (k,v)-> seatRows.get(k.getRow() - 1).getSeats()[(int) k.getColumn()-'A'].setPassenger(v));

        return criteria.filter(seatRows);
    }
}
