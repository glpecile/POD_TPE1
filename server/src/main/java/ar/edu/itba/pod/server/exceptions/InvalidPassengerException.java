package ar.edu.itba.pod.server.exceptions;

public class InvalidPassengerException extends IllegalArgumentException{
    public InvalidPassengerException(){
        super("InvalidPassengerException");
    }
}
