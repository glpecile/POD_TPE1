package ar.edu.itba.pod.exceptions;

public class FlightDoesNotExistException extends IllegalArgumentException {
    public FlightDoesNotExistException() {
        super("The flight does not exists.");
    }
}
