package ar.edu.itba.pod.models;

import lombok.Getter;

import java.io.Serializable;
import java.util.*;

public class Flight implements Serializable {
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
        this.tickets = Collections.synchronizedList(tickets);
    }

    public int getFreeSeatsInCategory(SeatCategory category) {
        int freeSeats = 0;
        for (SeatCategory c : SeatCategory.values()) {
            if (c.compareTo(category) == 0) {
                freeSeats += plane.getSeatsDistribution().getOrDefault(c, 0);
            }
        }

        synchronized (tickets) {
            freeSeats -= tickets.stream()
                    .filter(t -> t.getSeatLocation().isPresent())
                    .filter(t -> t.getSeatCategory().compareTo(category) == 0)
                    .count();
        }

        return freeSeats;
    }

    public List<AlternativeFlight> getAlternativeFlight(Ticket ticket) {
        List<AlternativeFlight> alternatives = new ArrayList<>();
        for (SeatCategory category : SeatCategory.values()) {
            if (category.compareTo(ticket.getSeatCategory()) >= 0) {
                int freeSeats = getFreeSeatsInCategory(category);
                if (freeSeats > 0) {
                    alternatives.add(new AlternativeFlight(this, category, freeSeats));
                }
            }
        }
        return alternatives;
    }

    public synchronized FlightStatus getStatus() {
        return status;
    }

    public synchronized void setStatus(FlightStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Flight flight = (Flight) o;
        return flightCode.equals(flight.flightCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightCode);
    }

    public synchronized boolean isStatus(FlightStatus confirmed) {
        return this.status == confirmed;
    }
}
