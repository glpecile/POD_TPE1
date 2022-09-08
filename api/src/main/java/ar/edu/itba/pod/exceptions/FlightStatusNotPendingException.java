package ar.edu.itba.pod.exceptions;

public class FlightStatusNotPendingException extends IllegalArgumentException {
    public FlightStatusNotPendingException() {
        super("Flight status is not pending");
    }
}
