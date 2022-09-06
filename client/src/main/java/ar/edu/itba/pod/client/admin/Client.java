package ar.edu.itba.pod.client.admin;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(ar.edu.itba.pod.client.Client.class);

    public static void main(String[] args) {
        logger.info("AdminClient Starting ...");

        var cli = new CliParser().parse(args);

        if (cli.isEmpty())
            return;

        var arguments = cli.get();


    }
}
