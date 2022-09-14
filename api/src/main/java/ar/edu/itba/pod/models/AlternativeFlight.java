package ar.edu.itba.pod.models;

import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AlternativeFlight implements Serializable, Comparable<AlternativeFlight> {
    @Getter
    private final Flight flight;
    @Getter
    private final SeatCategory category;
    @Getter
    private final int freeSeats;

    public AlternativeFlight(Flight flight, SeatCategory category, int freeSeats) {
        this.flight = flight;
        this.category = category;
        this.freeSeats = freeSeats;
    }

    @Override
    public int compareTo(AlternativeFlight other) {
        if (!this.category.equals(other.category)) {
            return this.category.compareTo(other.category); //we want the highest category (BUSINESS = 0)
        }
        if (this.freeSeats != other.freeSeats) {
            return Integer.compare(other.freeSeats, this.freeSeats); //reversed because we want the most free seats
        }
        return this.flight.getFlightCode().compareTo(other.flight.getFlightCode());
    }

    public static List<AlternativeFlight> getAlternativeFlights(Collection<Flight> flights, Ticket ticket, Flight flight) {
            return flights.stream()
                    .filter(f -> f.getAirportCode().equals(flight.getAirportCode()))
                    .filter(f -> f.isStatus(FlightStatus.SCHEDULED))
                    .filter(f -> !f.equals(flight))
                    .flatMap(f -> f.getAlternativeFlight(ticket).stream())
                    .sorted()
                    .collect(Collectors.toList());
    }

}
