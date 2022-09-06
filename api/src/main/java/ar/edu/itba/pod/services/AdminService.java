package ar.edu.itba.pod.services;

import ar.edu.itba.pod.models.*;

import java.rmi.RemoteException;
import java.util.*;
import ar.edu.itba.pod.utils.Pair;



public interface AdminService {

    static String getServiceName(){
        return "adminService";
    }

    void addPlane(String planeModelName, Map<SeatCategory, Pair<Integer, Integer> > seatsPerCategory)
            throws RemoteException;
    void addFlight(String planeModelName, String flightCode, String airportCode, List<Ticket> tickets);
    FlightStatus getFlightStatus(String flightCode) throws RemoteException;
    void confirmFlight(String flightCode) throws RemoteException;
    void cancelFlight(String flightCode);
    void rescheduleTickets();
}
