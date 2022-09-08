package ar.edu.itba.pod.models.criteria;

import ar.edu.itba.pod.models.SeatRow;
import ar.edu.itba.pod.models.criteria.Criteria;

import java.util.List;
import java.util.stream.Collectors;

public class RowCriteria implements Criteria {
    private final int row;

    public RowCriteria(int row) {
        this.row = row;
    }

    @Override
    public List<SeatRow> filter(List<SeatRow> seatRows) {
        return seatRows.stream().filter( s-> s.getRow() == row).collect(Collectors.toList());
    }
}
