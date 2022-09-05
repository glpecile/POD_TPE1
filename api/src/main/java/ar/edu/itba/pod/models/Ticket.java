package ar.edu.itba.pod.models;

public class Ticket {
    private String flightCode;
    private String passengerName;
    private Category category;

    public Ticket(String flightCode, String passengerName, Category category) {
        this.flightCode = flightCode;
        this.passengerName = passengerName;
        this.category = category;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public Category getCategory() {
        return category;
    }
}
