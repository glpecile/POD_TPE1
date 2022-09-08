package ar.edu.itba.pod.server.exceptions;

public class FlightIsConfirmedException extends IllegalArgumentException{
    public FlightIsConfirmedException(){
        super("FlightIsConfirmedException");
    }
}
