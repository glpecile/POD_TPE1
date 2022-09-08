package ar.edu.itba.pod.models.criteria;

import ar.edu.itba.pod.models.SeatRow;
import ar.edu.itba.pod.models.criteria.Criteria;

import java.util.List;

public class IdentityCriteria implements Criteria {
    @Override
    public List<SeatRow> filter(List<SeatRow> seatRows) {
        return seatRows;
    }
}
