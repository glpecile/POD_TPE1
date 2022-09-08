package ar.edu.itba.pod.client.admin.actions;

import ar.edu.itba.pod.client.admin.CliParser;
import ar.edu.itba.pod.models.SeatCategory;
import ar.edu.itba.pod.services.AdminService;
import ar.edu.itba.pod.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ModelsAction implements Runnable {
    private final Logger logger;
    private final AdminService service;
    private final CliParser.Arguments arguments;

    public ModelsAction(AdminService service, CliParser.Arguments arguments) {
        this.service = service;
        this.arguments = arguments;
        this.logger = LoggerFactory.getLogger(ModelsAction.class);
    }

    public ModelsAction(AdminService service, CliParser.Arguments arguments, Logger logger) {
        this.service = service;
        this.arguments = arguments;
        this.logger = logger;
    }

    @Override
    public void run() {
        List<PlaneModel> planeModels;
        try {
            planeModels = Files
                    .readAllLines(Paths.get(arguments.getFilePath().get()))
                    .stream().skip(1)
                    .map(t -> t.split(";"))
                    .map(t -> new PlaneModel(t[0], t[1]))
                    .collect(Collectors.toList());
        }
        catch (IOException e) {
            logger.error("Error reading file {}", arguments.getFilePath().get());
            return;
        }

        int addedPlaneModels = 0;
        for (var planeModel : planeModels) {
             try {
                 service.addPlane(planeModel.getPlaneModelName(), planeModel.getSeatsPerCategory());
                 addedPlaneModels++;
             }
             catch (RemoteException e) {
                    logger.error("Cannot add model {}", planeModel.getPlaneModelName());
             }

        }

        logger.info("{} added models", addedPlaneModels);

    }

    private static class PlaneModel{
        private final String planeModelName;
        private final TreeMap<SeatCategory, Pair<Integer, Integer>> seatsPerCategory;

        private PlaneModel(String planeModelName, String seats) {
            this.planeModelName = planeModelName;
            this.seatsPerCategory = new TreeMap<>();

            for (var section: seats.split(",")) {
                var a = section.split("#");
                this.seatsPerCategory.put(SeatCategory.valueOf(a[0].toUpperCase()),
                        new Pair<>(Integer.parseInt(a[1]), Integer.parseInt(a[2]))
                );
            }
        }

        public String getPlaneModelName() {
            return planeModelName;
        }

        public TreeMap<SeatCategory, Pair<Integer, Integer>> getSeatsPerCategory() {
            return seatsPerCategory;
        }

    }
}
