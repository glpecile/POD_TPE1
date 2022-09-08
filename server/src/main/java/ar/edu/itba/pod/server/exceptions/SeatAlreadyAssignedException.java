package ar.edu.itba.pod.server.exceptions;

import ar.edu.itba.pod.models.Seat;

public class SeatAlreadyAssignedException extends IllegalArgumentException{
    public SeatAlreadyAssignedException(){
        super("SeatAlreadyAssignedException");
    }
}
