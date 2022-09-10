package ar.edu.itba.pod.services;

import ar.edu.itba.pod.models.Flight;

import java.rmi.Remote;
import java.util.List;

public interface SeatAssignmentService extends Remote {

    Boolean isSeatTaken(String flightCode, int row, char column) throws Exception;
    void assignSeat(String flightCode, String passenger, int row, char column) throws Exception;
    void changeSeat(String flightCode, String passenger, int row, char column) throws Exception;
    List<Flight> getAlternativeFlights(String flightCode, String passenger) throws Exception;
    void changeTicket(String passenger, String flightCode, String newFlightCode) throws Exception;

}
