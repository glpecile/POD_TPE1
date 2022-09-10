package ar.edu.itba.pod.exceptions;

public class PassengerNotExistException extends IllegalArgumentException {
    public PassengerNotExistException() {
        super("Passenger does not exist");
    }
}

