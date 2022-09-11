package ar.edu.itba.pod.client.seatAssignment;

import ar.edu.itba.pod.client.seatAssignment.actions.ActionType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CliParser {

    private final Logger logger = LoggerFactory.getLogger(Client.class);
    private final CommandLineParser parser = new DefaultParser();
    private final Options options = new Options();

    public CliParser() {
        options.addRequiredOption("DserverAddress", "DserverAddress", true, "RMI Server address");
        options.addRequiredOption("Daction", "Daction", true, "Action to perform");
        options.addRequiredOption("Dflight", "Dflight", true, "Flight code");
        options.addOption(new Option("Dpassenger", "Dpassenger", true, "Passenger name"))
                .addOption(new Option("Drow", "Drow", true, "Seat row"))
                .addOption(new Option("Dcol", "Dcol", true, "Seat column"))
                .addOption(new Option("DoriginalFlight", "DoriginalFlight", true, "Original flight code"));
    }

    public Optional<Arguments> parse(String[] args) {
        try {
            return GetArguments(parser.parse(options, args));
        } catch (ParseException e) {
            logger.error("Error parsing command line arguments");
            return Optional.empty();
        }
    }

    private Optional<Arguments> GetArguments(CommandLine cmd) {
        var args = new Arguments();

        // set server address
        args.setServerAddress(cmd.getOptionValue("DserverAddress"));

        // set action
        try {
            args.setAction(ActionType.valueOf(cmd.getOptionValue("Daction").toUpperCase()));
        } catch (IllegalArgumentException e) {
            logger.error("The action is not valid!");
            return Optional.empty();
        }

        // Check action and other parameters
        switch (args.getAction()) {
            case STATUS -> {
                if (cmd.hasOption("Dflight") && cmd.hasOption("Drow") && cmd.hasOption("Dcol")) {
                    args.setFlightCode(cmd.getOptionValue("Dflight"));
                    args.setRow(Integer.parseInt(cmd.getOptionValue("Drow")));
                    args.setCol(cmd.getOptionValue("Dcol"));
                    return Optional.of(args);
                } else {
                    logger.error("Invalid parameters! Please include the correct flight code, row and column");
                    return Optional.empty();
                }
            }
            case ASSIGN -> {
                if (cmd.hasOption("Dflight") && cmd.hasOption("Dpassenger") && cmd.hasOption("Drow") && cmd.hasOption("Dcol")) {
                    args.setFlightCode(cmd.getOptionValue("Dflight"));
                    args.setPassengerName(cmd.getOptionValue("Dpassenger"));
                    args.setRow(Integer.parseInt(cmd.getOptionValue("Drow")));
                    args.setCol(cmd.getOptionValue("Dcol"));
                    return Optional.of(args);
                } else {
                    logger.error("Invalid parameters! Please include the correct flight code, passenger name, row and column");
                    return Optional.empty();
                }
            }
            case MOVE -> {
                if (cmd.hasOption("Dflight") && cmd.hasOption("Dpassenger") && cmd.hasOption("Dcol") && cmd.hasOption("Drow")) {
                    args.setFlightCode(cmd.getOptionValue("Dflight"));
                    args.setPassengerName(cmd.getOptionValue("Dpassenger"));
                    args.setRow(Integer.parseInt(cmd.getOptionValue("Drow")));
                    args.setCol(cmd.getOptionValue("Dcol"));
                    return Optional.of(args);
                } else {
                    logger.error("Invalid parameters! Please include the correct flight code, passenger name, row and column");
                    return Optional.empty();
                }
            }
            case ALTERNATIVES -> {
                if (cmd.hasOption("Dflight") && cmd.hasOption("Dpassenger")) {
                    args.setFlightCode(cmd.getOptionValue("Dflight"));
                    args.setPassengerName(cmd.getOptionValue("Dpassenger"));
                    return Optional.of(args);
                } else {
                    logger.error("Invalid parameters! Please include the correct flight code and passenger name");
                    return Optional.empty();
                }
            }
            case CHANGETICKET -> {
                if (cmd.hasOption("Dflight") && cmd.hasOption("Dpassenger") && cmd.hasOption("DoriginalFlight")) {
                    args.setFlightCode(cmd.getOptionValue("Dflight"));
                    args.setPassengerName(cmd.getOptionValue("Dpassenger"));
                    args.setOriginalFlightCode(cmd.getOptionValue("DoriginalFlight"));
                    return Optional.of(args);
                } else {
                    logger.error("Invalid parameters! Please include the correct new flight code, passenger name and original flight code");
                    return Optional.empty();
                }
            }
        }

        return args.isValid() ? Optional.of(args) : Optional.empty();
    }

    public static class Arguments extends ar.edu.itba.pod.client.models.Arguments {
        @Setter
        @Getter
        private ActionType action;
        @Setter
        @Getter
        private String flightCode;
        @Setter
        @Getter
        private String passengerName;
        @Setter
        @Getter
        private Integer row;
        @Setter
        @Getter
        private String col;
        @Setter
        @Getter
        private String originalFlightCode;
    }

}
