package ar.edu.itba.pod.client.seatmap;

import ar.edu.itba.pod.client.seatmap.actions.ActionType;
import ar.edu.itba.pod.models.SeatCategory;
import ar.edu.itba.pod.models.criteria.CategoryCriteria;
import ar.edu.itba.pod.models.criteria.Criteria;
import ar.edu.itba.pod.models.criteria.IdentityCriteria;
import ar.edu.itba.pod.models.criteria.RowCriteria;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CliParser {
    private final CommandLineParser parser = new DefaultParser();
    private final Logger logger = LoggerFactory.getLogger(ar.edu.itba.pod.client.notifications.CliParser.class);
    private final Options options = new Options();

    public CliParser() {
        options.addRequiredOption("DserverAddress", "DserverAddress", true, "RMI Server address");
        options.addRequiredOption("Dflight", "Dflight", true, "Flight code");
        options.addRequiredOption("DoutPath", "DoutPath", true, "File name to export results.");
        options.addOption("Drow", "Drow", true, "Number of row the client is asking to query.");
        options.addOption("Dcategory", "Dcategory", true, "Name of the category the client is asking to query.");
    }

    public Optional<ar.edu.itba.pod.client.seatmap.CliParser.Arguments> parse(String[] args) {
        try {
            return getArguments(parser.parse(options, args));
        } catch (ParseException e) {
            logger.error("Error parsing command line arguments");
            return Optional.empty();
        }
    }

    private Optional<ar.edu.itba.pod.client.seatmap.CliParser.Arguments> getArguments(CommandLine cmd) throws ParseException {
        var args = new Arguments();

        args.setServerAddress(cmd.getOptionValue("DserverAddress"));
        args.setFlight(cmd.getOptionValue("Dflight"));
        args.setOutputPath(cmd.getOptionValue("DoutPath"));

        var category = cmd.getOptionValue("Dcategory");
        var row = cmd.getOptionValue("Drow");
        if(category != null) {
            args.setCategory(SeatCategory.valueOf(category));
            args.setAction(ActionType.CATEGORY);
        }else if(row != null) {
            args.setRow(Integer.valueOf(row));
            args.setAction(ActionType.ROW);
        }else{
            args.setAction(ActionType.IDENTITY);
        }


        return args.isValid() ? Optional.of(args) : Optional.empty();
    }

    public static class Arguments extends ar.edu.itba.pod.client.models.Arguments {

        @Getter
        @Setter
        private String flight = null;

        @Setter
        private SeatCategory category = null;

        @Setter
        private Integer row = null;

        @Setter
        private String outputPath = null;

        @Getter
        @Setter
        private ActionType action;

        @Override
        public boolean isValid() {
            if(!super.isValid())
                return false;

            var outputPath = getOutputPath();
            if (outputPath.isPresent()){
                if(!outputPath.get().endsWith(".csv")){
                    logger.error("The file is not a csv file!");
                    return false;
                }
            }

            var flightCode = getFlightCode();

            if(flightCode.isPresent() && flightCode.get().isEmpty()){
                logger.error("The flight code is not valid!");
                return false;
            }

            var row = getRow();
            var category = getCategory();

            if(row.isPresent() && category.isPresent()){
                logger.error("Invalid amount of query arguments!");
                return false;
            }

            if(row.isPresent() && row.get() < 0) {
                logger.info("The row is not valid");
                return false;
            }

            return true;
        }

        public Optional<String> getFlightCode() {
            return Optional.ofNullable(flight);
        }

        public Optional<String> getOutputPath() {
            return Optional.ofNullable(outputPath);
        }

        public Optional<SeatCategory> getCategory(){
            return Optional.ofNullable(category);
        }

        public Optional<Integer> getRow(){
            return Optional.ofNullable(row);
        }

        public Criteria getCriteria(){
            if (!isValid()){
                logger.error("Invalid amount of arguments");
                return null;
            }
            var row = getRow();
            var category = getCategory();
            if(row.isPresent()){
                return new RowCriteria(row.get());
            }else if(category.isPresent()){
                return  new CategoryCriteria(category.get());
            }
            return new IdentityCriteria();
        }


    }


}
