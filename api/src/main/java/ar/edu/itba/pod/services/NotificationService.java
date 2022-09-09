package ar.edu.itba.pod.services;

import ar.edu.itba.pod.interfaces.PassengerNotifier;

public interface NotificationService {
    void registerPassenger(String flightCode, String passenger, PassengerNotifier remote);
}