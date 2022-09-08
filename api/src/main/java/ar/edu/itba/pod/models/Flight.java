package ar.edu.itba.pod.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

public class Flight implements Serializable {
    @Getter @Setter
    private FlightStatus status;
    @Getter
    private final String airportCode;
    @Getter
    private final String flightCode;
    @Getter
    private final Plane plane;
    @Getter
    private final List<Ticket> tickets;

    public Flight(FlightStatus status, String airportCode, String flightCode, Plane plane, List<Ticket> tickets) {
        this.status = status;
        this.airportCode = airportCode;
        this.flightCode = flightCode;
        this.plane = plane;
        this.tickets = tickets;
    }
}
