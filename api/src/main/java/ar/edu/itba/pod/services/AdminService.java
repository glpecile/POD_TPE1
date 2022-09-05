package ar.edu.itba.pod.services;

import ar.edu.itba.pod.models.*;
import java.util.*;
import ar.edu.itba.pod.utils.Pair;



public interface AdminService {
    void addPlane(String planeModelName, Map<SeatCategory, Pair<Integer, Integer> > seatsPerCategory);
    void addFlight(String planeModelName, String flightCode, String airportCode, List<Ticket> tickets);
    FlightStatus getFlightStatus(String flightCode);
    void confirmFlight(String flightCode);
    void cancelFlight(String flightCode);
    void rescheduleTickets();
}
