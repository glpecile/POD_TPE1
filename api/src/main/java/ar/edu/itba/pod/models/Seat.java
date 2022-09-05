package ar.edu.itba.pod.models;

import ar.edu.itba.pod.utils.Pair;

public class Seat {
    private Boolean IsTaken;
    private String Passenger;
    private Pair<Integer,Character> Location;
    private SeatCategory Category;

    public Seat(Boolean isTaken, String passenger, Pair<Integer, Character> location, SeatCategory category) {
        IsTaken = isTaken;
        Passenger = passenger;
        Location = location;
        Category = category;
    }

    public Seat(Boolean isTaken, String passenger, int row, char col, SeatCategory category) {
        IsTaken = isTaken;
        Passenger = passenger;
        Location = new Pair<>(row, col);
        Category = category;
    }

    public Boolean getTaken() {
        return IsTaken;
    }

    public void setTaken(Boolean taken) {
        IsTaken = taken;
    }

    public String getPassenger() {
        return Passenger;
    }

    public void setPassenger(String passenger) {
        Passenger = passenger;
    }

    public Pair<Integer, Character> getLocation() {
        return Location;
    }

    public void setLocation(Pair<Integer, Character> location) {
        Location = location;
    }

    public SeatCategory getCategory() {
        return Category;
    }

    public void setCategory(SeatCategory category) {
        Category = category;
    }
}
