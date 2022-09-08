package ar.edu.itba.pod.services;
import ar.edu.itba.pod.models.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

import ar.edu.itba.pod.utils.Pair;


public interface AdminService extends Remote {

    void addPlane(String planeModelName, TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory)
            throws RemoteException;

    void addFlight(String planeModelName, String flightCode, String airportCode, List<Ticket> tickets)
            throws RemoteException;

    FlightStatus getFlightStatus(String flightCode) throws RemoteException;

    void confirmFlight(String flightCode) throws RemoteException;

    void cancelFlight(String flightCode) throws RemoteException;

    ReticketingReport rescheduleTickets() throws RemoteException;
}
