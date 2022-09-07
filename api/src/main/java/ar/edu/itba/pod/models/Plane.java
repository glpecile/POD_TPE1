package ar.edu.itba.pod.models;

import ar.edu.itba.pod.utils.Pair;
import lombok.Getter;

import java.util.Map;
import java.util.TreeMap;

public class Plane {
    @Getter
    private final String modelName;
    @Getter
    private final RowDescription[] rows;

    public Plane(String modelName, TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory) {
        this.modelName = modelName;
        int rows = seatsPerCategory.values().stream().mapToInt(Pair::getFirst).sum();
        this.rows = new RowDescription[rows];
        int currentRow = 0;
        for (Map.Entry<SeatCategory, Pair<Integer, Integer>> entry : seatsPerCategory.entrySet()) {
            for (int i = 0; i < entry.getValue().getFirst(); i++) {
                this.rows[currentRow] = new RowDescription(entry.getValue().getSecond(), entry.getKey());
                currentRow++;
            }
        }
    }

    private static class RowDescription {
        @Getter
        private final int columns;
        @Getter
        private final SeatCategory category;

        public RowDescription(int columns, SeatCategory category) {
            this.columns = columns;
            this.category = category;
        }
    }
}
