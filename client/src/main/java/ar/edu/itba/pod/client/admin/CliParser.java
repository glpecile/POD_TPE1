package ar.edu.itba.pod.client.admin;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Pattern;

public class CliParser {

    private final Logger logger =LoggerFactory.getLogger(ar.edu.itba.pod.client.Client.class);
    private final CommandLineParser parser = new DefaultParser();
    private final static Pattern SERVER_ADDRESS_PATTERN = Pattern.compile("\\d\\d(\\.\\d{2}){3}:\\d{4}");
    private final Options options = new Options();


    public Optional<Arguments> parse(String[] args) {
        try {
            return GetArguments(parser.parse(options, args));
        } catch (ParseException e) {
            logger.error("Error parsing command line arguments");
            return Optional.empty();
        }
    }

    public CliParser(){
        options.addRequiredOption("DserverAddress","DserverAddress",true,"RMI Server address");
        options.addRequiredOption("Daction","Daction",true,"Action to perform");
        options.addOption(new Option("DinPath","DinPath",true,"Input file path"))
                .addOption(new Option("Dflight","Dflight",true,"Flight code"));

    }

    private Optional<Arguments> GetArguments(CommandLine cmd) {
        var args = new Arguments();

        // Set server address
        var serverAddress = cmd.getOptionValue("DserverAddress");
        if (SERVER_ADDRESS_PATTERN.matcher(serverAddress).matches())
            args.setServerAddress(serverAddress);
        else {
            logger.error("The server address is not valid!");
            return Optional.empty();
        }

        // Set action
        try {
            args.setAction(Actions.valueOf(cmd.getOptionValue("Daction").toUpperCase()));
        } catch (IllegalArgumentException e) {
            logger.error("The action is not valid!");
            return Optional.empty();
        }

        // Check action and other parameters



        return Optional.of(args);
    }

    public class Arguments{
        private String serverAddress;
        private Actions action;

        public Actions getAction() {
            return action;
        }

        public void setAction(Actions action) {
            this.action = action;
        }

        public String getServerAddress() {
            return serverAddress;
        }

        public void setServerAddress(String serverAddress) {
            this.serverAddress = serverAddress;
        }
    }


}
