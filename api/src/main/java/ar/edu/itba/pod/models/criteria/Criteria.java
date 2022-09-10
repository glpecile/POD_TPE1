package ar.edu.itba.pod.models.criteria;

import ar.edu.itba.pod.models.SeatRow;

import java.io.Serializable;
import java.util.List;

public interface Criteria extends Serializable {

    List<SeatRow> filter(List<SeatRow> seatRows);

}
