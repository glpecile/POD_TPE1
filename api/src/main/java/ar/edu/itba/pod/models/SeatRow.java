package ar.edu.itba.pod.models;

import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class SeatRow implements Serializable {
    @Getter
    private final int row;
    @Getter
    private final Seat[] seats;
    @Getter
    private final SeatCategory category;

    public SeatRow(int row, Seat[] seats, SeatCategory category) {
        this.row = row;
        this.seats = seats;
        this.category = category;
    }

    @Override
    public String toString() {
        var seatsStrings = Arrays.stream(seats).filter(Objects::nonNull).map(Seat::toString).collect(Collectors.toList());
        return seatsStrings.stream().collect(Collectors.joining("|", "| ", " | " + category.toString())) ;
    }
}
