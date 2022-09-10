package ar.edu.itba.pod.server.notifications;

import ar.edu.itba.pod.interfaces.PassengerNotifier;
import lombok.Getter;
import lombok.Setter;

public class PassengerSubscriber {
    @Getter
    private final String passengerName;
    @Getter @Setter
    private String flightCode;
    @Getter
    private final PassengerNotifier passengerNotifier;

    public PassengerSubscriber(String passengerName, String flightCode, PassengerNotifier passengerNotifier) {
        this.passengerName = passengerName;
        this.flightCode = flightCode;
        this.passengerNotifier = passengerNotifier;
    }
}
