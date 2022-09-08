package ar.edu.itba.pod.models.criteria;

import ar.edu.itba.pod.models.SeatCategory;
import ar.edu.itba.pod.models.SeatRow;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryCriteria implements Criteria{

    private final SeatCategory category;

    public CategoryCriteria(SeatCategory category) {
        this.category = category;
    }

    @Override
    public List<SeatRow> filter(List<SeatRow> seatRows) {
        return seatRows.stream().filter( s -> s.getCategory().equals(category)).collect(Collectors.toList());
    }
}
