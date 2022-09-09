package ar.edu.itba.pod.client.notifications;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CliParser {
    private final CommandLineParser parser = new DefaultParser();
    private final Logger logger = LoggerFactory.getLogger(CliParser.class);
    private final Options options = new Options();

    public CliParser(){
        options.addRequiredOption("DserverAddress","DserverAddress",true,"RMI Server address");
        options.addRequiredOption("Dflight","Dflight",true,"Flight code");
        options.addRequiredOption("Dpassenger","Dpassenger",true,"Passenger name");
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

        args.setServerAddress(cmd.getOptionValue("DserverAddress"));
        args.setFlight(cmd.getOptionValue("Dflight"));
        args.setPassenger(cmd.getOptionValue("Dpassenger"));

        return args.isValid() ? Optional.of(args) : Optional.empty();
    }

    public static class Arguments extends ar.edu.itba.pod.client.models.Arguments {
        @Getter @Setter
        private String flight;
        @Getter @Setter
        private String passenger;
    }
}
