package ar.edu.itba.pod.client.admin;

import ar.edu.itba.pod.client.admin.actions.ActionType;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;
import java.util.regex.Pattern;

public class CliParser {

    private final Logger logger =LoggerFactory.getLogger(ar.edu.itba.pod.client.Client.class);
    private final CommandLineParser parser = new DefaultParser();
    private final static Pattern SERVER_ADDRESS_PATTERN = Pattern.compile("^\\d?\\d(?:\\.\\d{1,2}){3}:\\d{1,4}$");
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
            args.setAction(ActionType.valueOf(cmd.getOptionValue("Daction").toUpperCase()));
        } catch (IllegalArgumentException e) {
            logger.error("The action is not valid!");
            return Optional.empty();
        }

        // Check action and other parameters

        switch (args.getAction()){
            case MODELS:
                if (cmd.hasOption("DinPath"))
                    args.setFilePath(cmd.getOptionValue("DinPath"));
                else {
                    logger.error("The input path is required for this action!");
                    return Optional.empty();
                }

                break;
            case FLIGHTS:
                break;
            case STATUS:
                break;
            case CONFIRM:
                break;
            case CANCEL:
                break;
            case RETICKETING:
                break;
        }

        return args.isValid() ? Optional.of(args) : Optional.empty();
    }

    public static class Arguments{
        private Logger logger = LoggerFactory.getLogger(ar.edu.itba.pod.client.admin.CliParser.class);
        private String serverAddress;
        private ActionType action;
        private Optional<String> filePath = Optional.empty();


        public boolean isValid(){
            if (filePath.isPresent()) {

                var file = new File(filePath.get());

                if (!file.getName().endsWith(".csv")) {
                    logger.error("The file is not a csv file!");
                    return false;
                }

                if (!file.exists()){
                    logger.error("The file does not exist!");
                    return false;
                }

            }


            return true;
        }




        public Optional<String> getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = Optional.of(filePath);
        }

        public ActionType getAction() {
            return action;
        }

        public void setAction(ActionType action) {
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
