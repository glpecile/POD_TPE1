package ar.edu.itba.pod.models;

import ar.edu.itba.pod.exceptions.EmptySeatDistributionException;
import ar.edu.itba.pod.utils.Pair;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Plane implements Serializable {
    @Getter
    private final String modelName;
    @Getter
    private final List<RowDescription> rows;
    @Getter
    private final Map<SeatCategory, Integer> seatsDistribution;

    public Plane(String modelName, TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory) {
        this.modelName = modelName;

        int rows = seatsPerCategory.values().stream().mapToInt(Pair::getFirst).sum();
        if (rows == 0) {
            throw new EmptySeatDistributionException();
        }
        this.rows = new ArrayList<>(rows);

        //row counter
        int j = 1;

        for (Map.Entry<SeatCategory, Pair<Integer, Integer>> entry : seatsPerCategory.entrySet()) {
            if (entry.getValue().getFirst() <= 0 || entry.getValue().getSecond() <= 0) {
                throw new EmptySeatDistributionException();
            }
            for (int i = 0; i < entry.getValue().getFirst(); i++) {
                this.rows.add(new RowDescription(j++,entry.getValue().getSecond(), entry.getKey()));
            }
        }

        this.seatsDistribution = seatsPerCategory.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getFirst() * entry.getValue().getSecond()));
    }



    @Override
    public int hashCode() {
        return modelName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plane plane = (Plane) o;
        return modelName.equals(plane.modelName);
    }

    public static class RowDescription {
        @Getter
        private final int row;
        @Getter
        private final int columns;
        @Getter
        private final SeatCategory category;

        public RowDescription(int row, int columns, SeatCategory category) {
            this.row = row;
            this.columns = columns;
            this.category = category;
        }
    }
}
