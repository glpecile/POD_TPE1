package ar.edu.itba.pod.services;

import ar.edu.itba.pod.models.Flight;

import java.util.List;

public interface SeatAssignmentService {

    Boolean isSeatTaken(String flightCode, int row, char column);
    void assignSeat(String flightCode, String passenger, int row, char column);
    void changeSeat(String flightCode, String passenger, int row, char column);
    List<Flight> getAlternativeFlights(String flightCode, String passenger);
    void changeTicket(String passenger, String flightCode, String newFlightCode);

}
