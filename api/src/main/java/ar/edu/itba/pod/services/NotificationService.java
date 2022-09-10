package ar.edu.itba.pod.services;

import ar.edu.itba.pod.interfaces.PassengerNotifier;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationService extends Remote {
    void registerPassenger(String flightCode, String passenger, PassengerNotifier remote) throws RemoteException;
}