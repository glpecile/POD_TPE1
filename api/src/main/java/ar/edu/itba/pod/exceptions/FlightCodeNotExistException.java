package ar.edu.itba.pod.exceptions;

public class FlightCodeNotExistException extends IllegalArgumentException {
    public FlightCodeNotExistException() {
        super("Flight code does not exist");
    }
}
