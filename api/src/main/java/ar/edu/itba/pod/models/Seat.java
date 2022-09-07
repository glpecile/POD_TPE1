package ar.edu.itba.pod.models;

import lombok.Getter;

public class Seat {
    @Getter
    private final String passenger;
    @Getter
    private final char column;

    public Seat(char column) {
        this.column = column;
        this.passenger = null;
    }

    public Seat(String passenger, char column) {
        this.passenger = passenger;
        this.column = column;
    }


}