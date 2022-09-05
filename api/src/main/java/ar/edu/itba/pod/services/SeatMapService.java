package ar.edu.itba.pod.services;

import ar.edu.itba.pod.models.Criteria;
import ar.edu.itba.pod.models.Seat;

import java.util.List;

public interface SeatMapService {
    List<Seat> getSeatMap(String flightCode, Criteria criteria);
}
