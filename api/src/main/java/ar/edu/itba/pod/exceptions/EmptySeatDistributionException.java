package ar.edu.itba.pod.exceptions;

public class EmptySeatDistributionException extends IllegalArgumentException {
    public EmptySeatDistributionException() {
        super("Seat distribution cannot be empty");
    }
}

