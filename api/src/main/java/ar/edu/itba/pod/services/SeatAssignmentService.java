package ar.edu.itba.pod.services;

import ar.edu.itba.pod.models.AlternativeFlight;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SeatAssignmentService extends Remote {

    Boolean isSeatTaken(String flightCode, int row, char column) throws RemoteException;
    void assignSeat(String flightCode, String passenger, int row, char column) throws RemoteException;
    void changeSeat(String flightCode, String passenger, int row, char column) throws RemoteException;
    List<AlternativeFlight> getAlternativeFlights(String flightCode, String passenger) throws RemoteException;
    void changeTicket(String passenger, String flightCode, String newFlightCode) throws RemoteException;

}
