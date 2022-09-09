package ar.edu.itba.pod.client.admin.actions;

import ar.edu.itba.pod.client.admin.CliParser;
import ar.edu.itba.pod.models.SeatCategory;
import ar.edu.itba.pod.models.Ticket;
import ar.edu.itba.pod.services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlightsAction implements Runnable {
    private final AdminService service;
    private final CliParser.Arguments arguments;
    private final Logger logger;
    public FlightsAction(AdminService service, CliParser.Arguments arguments) {
        logger = LoggerFactory.getLogger(FlightsAction.class);
        this.service = service;
        this.arguments = arguments;
    }

    public FlightsAction(AdminService service, CliParser.Arguments arguments, Logger logger) {
        this.service = service;
        this.arguments = arguments;
        this.logger = logger;
    }

    @Override
    public void run() {
        List<FlightModel> flights;
        try {
            flights = Files
                    .readAllLines(Paths.get(arguments.getFilePath().get()))
                    .stream().skip(1)
                    .map(t -> t.split(";"))
                    .map(t -> new FlightModel(t[0], t[1], t[2], t[3]))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Error reading file {}", arguments.getFilePath().get());
            return;
        }

        int flightsAdded = 0;
        for (var flight : flights) {
            try {
                service.addFlight(flight.getPlaneModelName(), flight.getFlightCode(),flight.getDestination(),flight.getTickets());
                flightsAdded++;
            }
            catch (RemoteException e) {
                logger.error("Cannot add flight {}.", flight.getFlightCode());
            }

        }

        logger.info("{} flights added", flightsAdded);
    }

    private static class FlightModel{
        private final String planeModelName;
        private final String flightCode;
        private final String destination;
        private final List<Ticket> Tickets;

        private FlightModel(String planeModelName, String flightCode, String destination, String tickets) {
            this.planeModelName = planeModelName;
            this.flightCode = flightCode;
            this.destination = destination;
            Tickets = new ArrayList<>();

            for (var ticket: tickets.split(",")) {
                var data = ticket.split("#");
                Tickets.add(new Ticket(data[1], SeatCategory.valueOf(data[0]), 1, 'a'));
            }
        }


        public String getPlaneModelName() {
            return planeModelName;
        }

        public String getFlightCode() {
            return flightCode;
        }

        public String getDestination() {
            return destination;
        }

        public List<Ticket> getTickets() {
            return Tickets;
        }
    }
}
