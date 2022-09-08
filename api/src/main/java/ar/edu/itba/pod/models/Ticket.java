package ar.edu.itba.pod.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Optional;

public class Ticket {
    @Setter
    private SeatLocation seatLocation;
    @Getter
    private final String passengerName;
    @Getter
    private final SeatCategory seatCategory;

    public Ticket(String passengerName, SeatCategory seatCategory) {
        this.passengerName = passengerName;
        this.seatCategory = seatCategory;
    }

    public Optional<SeatLocation> getSeatLocation() {
        return Optional.ofNullable(seatLocation);
    }

    public static class SeatLocation {
        @Getter
        private final int row;
        @Getter
        private final char column;

        public SeatLocation(int row, char column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SeatLocation that = (SeatLocation) o;
            return row == that.row && column == that.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }
}
