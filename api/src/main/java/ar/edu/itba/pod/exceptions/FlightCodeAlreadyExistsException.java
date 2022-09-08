package ar.edu.itba.pod.exceptions;

public class FlightCodeAlreadyExistsException extends IllegalArgumentException {
    public FlightCodeAlreadyExistsException() {
        super("The flight code already exists");
    }
}
