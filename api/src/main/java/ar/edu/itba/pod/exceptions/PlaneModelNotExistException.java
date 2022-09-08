package ar.edu.itba.pod.exceptions;

public class PlaneModelNotExistException extends IllegalArgumentException {
    public PlaneModelNotExistException() {
        super("The plane model does not exist");
    }
}
