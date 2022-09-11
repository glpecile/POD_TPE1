package ar.edu.itba.pod.client.notifications;

import ar.edu.itba.pod.interfaces.PassengerNotifier;
import ar.edu.itba.pod.models.Seat;
import ar.edu.itba.pod.models.SeatCategory;
import ar.edu.itba.pod.models.Ticket;
import ar.edu.itba.pod.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PassengerNotifierImpl implements PassengerNotifier {

    private final Logger logger;

    public PassengerNotifierImpl(Logger logger) {
        this.logger = logger;
    }

    public PassengerNotifierImpl(String passengerName) {
        this.logger = LoggerFactory.getLogger(passengerName);
    }


    @Override
    public void notifySuccessfulRegistration(String flightCode, String destination) throws RemoteException {
        logger.info("You are following Flight {} with destination {}.", flightCode, destination);
    }

    @Override
    public void notifySeatAssignment(String flightCode, String destination, Ticket.SeatLocation seat, SeatCategory category) throws RemoteException {
        logger.info("Your seat is {} {} for Flight {} with destination {}.", category, seat, flightCode, destination);
    }

    @Override
    public void notifySeatChange(String flightCode, String destination, Ticket.SeatLocation oldSeat, SeatCategory oldCategory, Ticket.SeatLocation newSeat, SeatCategory newCategory) throws RemoteException {
        logger.info("Your seat changed to {} {} from {} {} for Flight {} with destination {}.", newCategory, newSeat, oldCategory, oldSeat, flightCode, destination);
    }

    @Override
    public void notifyFlightCancellation(String flightCode, String destination, Ticket.SeatLocation seat, SeatCategory category) throws RemoteException {
        String seatString = seat == null ? "" : String.format(" and your seat %s %s", category, seat);
        logger.info("Your Flight {} with destination {} was cancelled{}.", flightCode, destination, seatString);
    }

    @Override
    public void notifyFlightChange(String oldFlightCode, String newFlightCode, String destination) throws RemoteException {
        logger.info("Your ticket changed to Flight {} with destination {} from Flight {} with destination {}.", newFlightCode, destination, oldFlightCode, destination);
    }

    @Override
    public void notifyFlightConfirmation(String flightCode, String destination, Ticket.SeatLocation seat, SeatCategory category) throws RemoteException {
        String seatString = seat == null ? "" : String.format(" and your seat %s %s", category, seat);
        logger.info("Your Flight {} with destination {} was confirmed{}.", flightCode, destination, seatString);
        UnicastRemoteObject.unexportObject(this,true);
    }
}
