package ar.edu.itba.pod.server.notifications;

import ar.edu.itba.pod.interfaces.PassengerNotifier;
import ar.edu.itba.pod.models.Flight;
import ar.edu.itba.pod.models.SeatCategory;
import ar.edu.itba.pod.models.Ticket;

import java.rmi.RemoteException;

public interface EventsManager {
    void addPassengerSubscriber(String passengerName, String flightCode, String destination, PassengerNotifier passengerNotifier) throws RemoteException;

    void notifySeatAssignment(Flight flight, Ticket ticket) throws RemoteException; //TODO define parameters

    void notifySeatChange(Flight flight,Ticket.SeatLocation oldSeat, SeatCategory oldCategory, Ticket ticket) throws RemoteException; //TODO define parameters

    void notifyFlightCancellation(Flight flight) throws RemoteException;

    void notifyFlightChange(Flight oldFlight, Flight newFlight, String passengerName) throws RemoteException;

    void notifyFlightConfirmation(Flight flight) throws RemoteException;
}
