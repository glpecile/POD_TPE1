package ar.edu.itba.pod.server.notifications;

import ar.edu.itba.pod.exceptions.PassengerNotExistException;
import ar.edu.itba.pod.interfaces.PassengerNotifier;
import ar.edu.itba.pod.models.Flight;
import ar.edu.itba.pod.models.SeatCategory;
import ar.edu.itba.pod.models.Ticket;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventsManagerImpl implements EventsManager {

    private final List<PassengerSubscriber> passengerSubscribers;

    public EventsManagerImpl() {
        this.passengerSubscribers = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public void addPassengerSubscriber(String passengerName, String flightCode, String destination, PassengerNotifier passengerNotifier) throws RemoteException {
        passengerSubscribers.add(new PassengerSubscriber(passengerName, flightCode, passengerNotifier));
        passengerNotifier.notifySuccessfulRegistration(flightCode, destination);
    }

    @Override
    public void notifySeatAssignment(Flight flight, Ticket ticket) throws RemoteException {
        synchronized (passengerSubscribers) {
            for (PassengerSubscriber passengerSubscriber : passengerSubscribers) {
                if (passengerSubscriber.getFlightCode().equals(flight.getFlightCode()) &&
                        passengerSubscriber.getPassengerName().equals(ticket.getPassengerName()))
                {
                    passengerSubscriber
                            .getPassengerNotifier()
                            .notifySeatAssignment(flight.getFlightCode(), flight.getAirportCode(), ticket.getSeatLocation()
                                    .orElse(null), ticket.getSeatCategory());
                }
            }
        }
    }

    @Override
    public void notifySeatChange(Flight flight, Ticket.SeatLocation oldSeat, SeatCategory oldCategory, Ticket ticket) throws RemoteException {
        synchronized (passengerSubscribers) {
            for (PassengerSubscriber passengerSubscriber : passengerSubscribers) {
                if (passengerSubscriber.getFlightCode().equals(flight.getFlightCode()) && passengerSubscriber.getPassengerName().equals(ticket.getPassengerName())) {
                    passengerSubscriber.getPassengerNotifier()
                            .notifySeatChange(flight.getFlightCode(), flight.getAirportCode(), oldSeat, oldCategory, ticket.getSeatLocation().orElse(null), ticket.getSeatCategory());
                }
            }
        }
    }

    @Override
    public void notifyFlightCancellation(Flight flight) throws RemoteException {
        synchronized (passengerSubscribers) {
            for (PassengerSubscriber p : this.passengerSubscribers) {
                if (p.getFlightCode().equals(flight.getFlightCode())) {
                    Ticket ticket = flight.getTickets().stream()
                            .filter(t -> t.getPassengerName().equals(p.getPassengerName()))
                            .findFirst().orElseThrow(PassengerNotExistException::new);
                    p.getPassengerNotifier()
                            .notifyFlightCancellation(flight.getFlightCode(), flight.getAirportCode(), ticket.getSeatLocation()
                                    .orElse(null), ticket.getSeatCategory());
                }
            }
        }
    }

    @Override
    public void notifyFlightChange(Flight oldFlight, Flight newFlight, String passengerName) throws RemoteException {
        synchronized (passengerSubscribers) {
            for (PassengerSubscriber p : this.passengerSubscribers) {
                if (p.getFlightCode().equals(oldFlight.getFlightCode()) && p.getPassengerName().equals(passengerName)) {
                    p.getPassengerNotifier().notifyFlightChange(oldFlight.getFlightCode(), newFlight.getFlightCode(), newFlight.getAirportCode());
                    p.setFlightCode(newFlight.getFlightCode());
                }
            }
        }
    }

    @Override
    public void notifyFlightConfirmation(Flight flight) throws RemoteException {
        synchronized (passengerSubscribers) {
            var toRemove = new ArrayList<PassengerSubscriber>();

            for (PassengerSubscriber p : this.passengerSubscribers) {
                if (p.getFlightCode().equals(flight.getFlightCode())) {
                    Ticket ticket = flight.getTickets().stream()
                            .filter(t -> t.getPassengerName().equals(p.getPassengerName()))
                            .findFirst().orElseThrow(PassengerNotExistException::new);

                    var notifier = p.getPassengerNotifier();
                    notifier.notifyFlightConfirmation(flight.getFlightCode(), flight.getAirportCode(), ticket.getSeatLocation().orElse(null), ticket.getSeatCategory());
                    toRemove.add(p);
                }
            }
            this.passengerSubscribers.removeAll(toRemove);
        }
    }
}
