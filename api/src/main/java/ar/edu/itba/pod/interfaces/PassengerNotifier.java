package ar.edu.itba.pod.interfaces;

import ar.edu.itba.pod.models.Seat;
import ar.edu.itba.pod.models.SeatCategory;
import ar.edu.itba.pod.models.Ticket;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PassengerNotifier extends Remote {

    // You are following Flight AA100 with destination JFK.
    void notifySuccessfulRegistration(String flightCode, String destination) throws RemoteException;

    // Your seat is BUSINESS 1B for Flight AA100 with destination JFK.
    void notifySeatAssignment(String flightCode, String destination, Ticket.SeatLocation seat, SeatCategory category) throws RemoteException;

    // Your seat changed to BUSINESS 2C from BUSINESS 1B for Flight AA100 with destination JFK.
    void notifySeatChange(String flightCode, String destination, Ticket.SeatLocation oldSeat,SeatCategory oldCategory, Ticket.SeatLocation newSeat, SeatCategory newCategory) throws RemoteException;

    // Your Flight AA100 with destination JFK was cancelled and your seat is BUSINESS 2C.
    void notifyFlightCancellation(String flightCode, String destination, Ticket.SeatLocation seat, SeatCategory category) throws RemoteException;

    // Your ticket changed to Flight AA101 with destination JFK from Flight AA100 with destination JFK.
    void notifyFlightChange(String oldFlightCode, String newFlightCode, String destination) throws RemoteException;

    // Your Flight AA100 with destination JFK was confirmed and your seat is PREMIUM_ECONOMY 15D.
    void notifyFlightConfirmation(String flightCode, String destination, Ticket.SeatLocation seat, SeatCategory category) throws RemoteException;
}
