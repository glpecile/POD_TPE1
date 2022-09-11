package ar.edu.itba.pod.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class Seat implements Serializable {
    @Getter @Setter
    private String passenger;
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

    private String printPassenger(){
        if(passenger != null)
            return passenger.charAt(0) + "";
        return " * ";
    }

    @Override
    public String toString() {
        return column + " " + printPassenger() + " ";
    }
}