package ar.edu.itba.pod.models;

import lombok.Getter;

public class SeatRow {
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
}
