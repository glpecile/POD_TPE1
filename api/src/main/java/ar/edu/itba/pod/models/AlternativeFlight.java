package ar.edu.itba.pod.models;

import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AlternativeFlight implements Serializable {
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

    public static List<Flight> getAlternativeFlights(Collection<Flight> flights, Ticket ticket, Flight flight) {
            return flights.stream()
                    .filter(f -> f.getAirportCode().equals(flight.getAirportCode()))
                    .filter(f -> f.isStatus(FlightStatus.SCHEDULED))
                    .filter(f -> f.getMaxCategoryAvailable(ticket.getSeatCategory()) != null)
                    .filter(f -> !f.equals(flight))
                    .sorted((o1, o2) -> {
                        if (o1.getMaxCategoryAvailable(ticket.getSeatCategory()) != o2.getMaxCategoryAvailable(ticket.getSeatCategory())) {
                            return o1.getMaxCategoryAvailable(ticket.getSeatCategory()).ordinal() - o2.getMaxCategoryAvailable(ticket.getSeatCategory()).ordinal(); //we want the highest category (BUSINESS = 0)
                        }
                        if (o1.getFreeSeatsInMaxCategory(ticket.getSeatCategory()) != o2.getFreeSeatsInMaxCategory(ticket.getSeatCategory())) {
                            return o2.getFreeSeatsInMaxCategory(ticket.getSeatCategory()) - o1.getFreeSeatsInMaxCategory(ticket.getSeatCategory()); //reversed because we want the most free seats
                        }
                        return o1.getFlightCode().compareTo(o2.getFlightCode());

                    })
                    .collect(Collectors.toList());

    }

}
