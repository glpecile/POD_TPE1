package ar.edu.itba.pod.exceptions;

public class PlaneModelAlreadyExistsException extends IllegalArgumentException {
    public PlaneModelAlreadyExistsException() {
        super("The plane model already exists");
    }
}

