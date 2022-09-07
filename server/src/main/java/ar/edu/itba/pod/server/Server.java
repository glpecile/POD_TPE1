package ar.edu.itba.pod.server;

import ar.edu.itba.pod.models.Flight;
import ar.edu.itba.pod.models.Plane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("tpe1-g3-parent Server Starting ...");
        List<Plane> planes = Collections.synchronizedList(new ArrayList<>());
        List<Flight> flights = Collections.synchronizedList(new ArrayList<>());

    }
}
