package ar.edu.itba.pod.client.admin.actions;

import ar.edu.itba.pod.client.admin.CliParser;
import ar.edu.itba.pod.services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReticketingAction implements Runnable {
    private final AdminService service;
    private final CliParser.Arguments arguments;
    private final Logger logger;

    public ReticketingAction(AdminService service, CliParser.Arguments arguments, Logger logger) {
        this.service = service;
        this.arguments = arguments;
        this.logger = logger;
    }

    public ReticketingAction(AdminService service, CliParser.Arguments arguments) {
        this.service = service;
        this.arguments = arguments;
        logger = LoggerFactory.getLogger(ReticketingAction.class);
    }


    @Override
    public void run() {
        try {
            var result = service.rescheduleTickets();

            logger.info("{} tickets where changed.", result.getSuccess());
            result.getFailure().forEach((k) ->
                    logger.error("Cannot find alternative flight for {} with Ticket {}", k.getPassenger(), k.getFlightCode())
            );
        } catch (Exception e) {
            logger.error("Error while reticketing");
        }
    }
}
