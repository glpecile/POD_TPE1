package ar.edu.itba.pod.server.exceptions;

public class PassengerAlreadyAssignedException extends IllegalArgumentException{
    public PassengerAlreadyAssignedException(){
        super("PassengerAlreadyAssignedException");
    }
}
