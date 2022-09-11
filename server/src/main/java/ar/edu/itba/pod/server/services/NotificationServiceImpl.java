package ar.edu.itba.pod.server.services;

import ar.edu.itba.pod.exceptions.FlightCodeNotExistException;
import ar.edu.itba.pod.exceptions.PassengerNotExistException;
import ar.edu.itba.pod.interfaces.PassengerNotifier;
import ar.edu.itba.pod.models.Flight;
import ar.edu.itba.pod.server.notifications.EventsManager;
import ar.edu.itba.pod.services.NotificationService;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Optional;

public class NotificationServiceImpl implements NotificationService {

    private final Map<String, Flight> flights;
    private final EventsManager eventsManager;

    public NotificationServiceImpl(Map<String, Flight> flights, EventsManager eventsManager) {
        this.flights = flights;
        this.eventsManager = eventsManager;
    }

    @Override
    public void registerPassenger(String flightCode, String passenger, PassengerNotifier remote) throws RemoteException {
        Flight flight = Optional.ofNullable(flights.get(flightCode)).orElseThrow(FlightCodeNotExistException::new);
        synchronized (flight.getTickets()) {
            flight.getTickets().stream()
                    .filter(t -> t.getPassengerName().equals(passenger))
                    .findFirst().orElseThrow(PassengerNotExistException::new);
        }
        eventsManager.addPassengerSubscriber(passenger, flightCode, flight.getAirportCode(), remote);
    }
}
