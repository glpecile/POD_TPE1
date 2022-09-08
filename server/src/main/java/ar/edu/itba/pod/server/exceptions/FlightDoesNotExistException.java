package ar.edu.itba.pod.server.exceptions;

public class FlightDoesNotExistException extends IllegalArgumentException{
    public FlightDoesNotExistException(){
        super("FlightDoesNotExistException");
    }
}
