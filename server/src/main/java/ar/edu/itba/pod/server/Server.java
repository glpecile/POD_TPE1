package ar.edu.itba.pod.server;

import ar.edu.itba.pod.models.Flight;
import ar.edu.itba.pod.models.Plane;
import ar.edu.itba.pod.server.services.AdminServiceImpl;
import ar.edu.itba.pod.server.services.SeatMapServiceImpl;
import ar.edu.itba.pod.services.AdminService;
import ar.edu.itba.pod.services.SeatMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        logger.info("tpe1-g3-parent Server Starting ...");
        List<Plane> planes = Collections.synchronizedList(new ArrayList<>());
        List<Flight> flights = Collections.synchronizedList(new ArrayList<>());

        var serviceAdmin = new AdminServiceImpl(planes, flights);
        var serviceSeatMap = new SeatMapServiceImpl(flights);
        var remoteAdmin = UnicastRemoteObject.exportObject(serviceAdmin,0);
        var remoteSeatMap = UnicastRemoteObject.exportObject(serviceSeatMap,1);

        final Registry registry = LocateRegistry.getRegistry();
        registry.rebind("AdminService", remoteAdmin); // bind, rebind, unbind
        registry.rebind("SeatMapService", remoteSeatMap);
    }
}
