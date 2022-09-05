package ar.edu.itba.pod.models;

import java.util.List;

public class Flight {
    private FlightStatus Status;
    private String AirportCode;
    private String FlightCode;
    private String PlaneModelName;
    private List<Ticket> Tickets;

    public Flight(FlightStatus status, String airportCode, String flightCode, String planeModelName, List<Ticket> tickets) {
        Status = status;
        AirportCode = airportCode;
        FlightCode = flightCode;
        PlaneModelName = planeModelName;
        Tickets = tickets;
    }

    public FlightStatus getStatus() {
        return Status;
    }

    public void setStatus(FlightStatus status) {
        Status = status;
    }

    public String getAirportCode() {
        return AirportCode;
    }

    public void setAirportCode(String airportCode) {
        AirportCode = airportCode;
    }

    public String getFlightCode() {
        return FlightCode;
    }

    public void setFlightCode(String flightCode) {
        FlightCode = flightCode;
    }

    public String getPlaneModelName() {
        return PlaneModelName;
    }

    public void setPlaneModelName(String planeModelName) {
        PlaneModelName = planeModelName;
    }

    public List<Ticket> getTickets() {
        return Tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        Tickets = tickets;
    }
}
