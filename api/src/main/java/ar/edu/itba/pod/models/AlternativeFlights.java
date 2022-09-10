package ar.edu.itba.pod.models;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class AlternativeFlights implements Serializable {


    public static List<Flight> getAlternativeFlights(List<Flight> flights,  Ticket ticket, Flight flight) {
        return flights.stream()
                .filter(f -> f.getAirportCode().equals(flight.getAirportCode()))
                .filter(f -> f.getStatus().equals(FlightStatus.SCHEDULED))
                .filter(f -> f.getMaxCategoryAvailable(ticket) != null)
                .filter(f-> !f.equals(flight))
                .sorted((o1, o2) -> {
                    if (o1.getMaxCategoryAvailable(ticket) != o2.getMaxCategoryAvailable(ticket)) {
                        return o1.getMaxCategoryAvailable(ticket).ordinal() - o2.getMaxCategoryAvailable(ticket).ordinal(); //we want the highest category (BUSINESS = 0)
                    }
                    if (o1.getFreeSeats(ticket.getSeatCategory()) != o2.getFreeSeats(ticket.getSeatCategory())) {
                        return o2.getFreeSeats(ticket.getSeatCategory()) - o1.getFreeSeats(ticket.getSeatCategory()); //reversed because we want the most free seats
                    }
                    return o1.getFlightCode().compareTo(o2.getFlightCode());

                })
                .collect(Collectors.toList());
    }

}
