package ar.edu.itba.pod.server.exceptions;

public class SeatDoesNotExistException extends IllegalArgumentException{
    public SeatDoesNotExistException(){
        super("SeatDoesNotExistException");
    }
}
