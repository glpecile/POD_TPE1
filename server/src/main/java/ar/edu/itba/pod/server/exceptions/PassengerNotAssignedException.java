package ar.edu.itba.pod.server.exceptions;

public class PassengerNotAssignedException extends IllegalArgumentException{
//    TODO: ask si tiene sentido esta excepcion
    public PassengerNotAssignedException(){
        super("PassengerNotAssignedException");
    }
}
