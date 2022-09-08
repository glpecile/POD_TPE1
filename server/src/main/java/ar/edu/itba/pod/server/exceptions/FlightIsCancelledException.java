package ar.edu.itba.pod.server.exceptions;

public class FlightIsCancelledException extends IllegalArgumentException{
    public FlightIsCancelledException(){
        super("FlightIsCancelledException");
    }
}