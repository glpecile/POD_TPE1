package ar.edu.itba.pod.models.criteria;

import ar.edu.itba.pod.models.SeatRow;

import java.util.List;

public interface Criteria {

    List<SeatRow> filter(List<SeatRow> seatRows);

}
