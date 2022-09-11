package ar.edu.itba.pod.client.seatmap.actions;

import ar.edu.itba.pod.client.seatmap.CliParser;
import ar.edu.itba.pod.services.SeatMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;

public class SeatMapAction implements Runnable{

    private final SeatMapService service;
    private final CliParser.Arguments arguments;
    private final Logger logger;

    public SeatMapAction(SeatMapService service, CliParser.Arguments arguments, Logger logger) {
        this.service = service;
        this.arguments = arguments;
        this.logger = logger;
    }

    public SeatMapAction(SeatMapService service, CliParser.Arguments arguments) {
        this.service = service;
        this.arguments = arguments;
        this.logger = LoggerFactory.getLogger(SeatMapAction.class);
    }

    @Override
    public void run() {
        var flightCode = arguments.getFlightCode().get();
        var criteria = arguments.getCriteria();

        try {
            var seatRows = service.getSeatMap(flightCode,criteria);
            var outputFile = new FileWriter(arguments.getOutputPath().get());
            var printWriter = new PrintWriter(outputFile);
            seatRows.forEach( s -> printWriter.println(s.toString()));
            printWriter.close();
            logger.info("Seat map exported...");
        }
        catch (RemoteException e) {
            logger.error("Cannot request seat map for flight {}", flightCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
