package ar.edu.itba.pod.models;

import ar.edu.itba.pod.exceptions.EmptySeatDistributionException;
import ar.edu.itba.pod.utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlaneTest {

    @Test
    public void createPlane() {
        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(10, 6));
        seatsPerCategory.put(SeatCategory.PREMIUM_ECONOMY, new Pair<>(10, 4));
        seatsPerCategory.put(SeatCategory.BUSINESS, new Pair<>(4, 4));

        Plane plane = new Plane("Boeing 747", seatsPerCategory);

        assertEquals(plane.getModelName(), "Boeing 747");
        assertEquals(plane.getRows().size(), 24);
    }

    @Test
    public void createPlaneWithEmptySeatDistribution() {
        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();

        assertThrows(EmptySeatDistributionException.class, () -> new Plane("Boeing 747", seatsPerCategory));
    }

    @Test
    public void createPlaneWithNegativeSeats() {
        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(10, -6));
        seatsPerCategory.put(SeatCategory.PREMIUM_ECONOMY, new Pair<>(10, 4));
        seatsPerCategory.put(SeatCategory.BUSINESS, new Pair<>(4, 4));

        assertThrows(IllegalArgumentException.class, () -> new Plane("Boeing 747", seatsPerCategory));
    }

    @Test
    public void createPlaneWithNegativeColumns() {
        TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory = new TreeMap<>();
        seatsPerCategory.put(SeatCategory.ECONOMY, new Pair<>(10, -6));
        seatsPerCategory.put(SeatCategory.PREMIUM_ECONOMY, new Pair<>(10, 4));
        seatsPerCategory.put(SeatCategory.BUSINESS, new Pair<>(4, 4));

        assertThrows(IllegalArgumentException.class, () -> new Plane("Boeing 747", seatsPerCategory));
    }
}
