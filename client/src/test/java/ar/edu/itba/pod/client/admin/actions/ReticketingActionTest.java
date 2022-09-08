package ar.edu.itba.pod.client.admin.actions;

import ar.edu.itba.pod.client.admin.CliParser;
import ar.edu.itba.pod.models.ReticketingReport;
import ar.edu.itba.pod.services.AdminService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ReticketingActionTest {
    private final AdminService adminService = mock(AdminService.class);
    private final Logger logger = mock(Logger.class);

    @Test
    public void WithoutAnyErrors_ShouldSucceed() throws RemoteException {
        // Arrange
        var success = 10;
        List<ReticketingReport.FailureTicket> errors = new ArrayList<>();
        when(adminService.rescheduleTickets()).thenReturn(new ReticketingReport(success,errors));
        var arguments = new CliParser.Arguments();

        // Act
        new ReticketingAction(adminService, arguments, logger).run();

        // Assert
        verify(adminService,times(1)).rescheduleTickets();
        verify(logger,times(1)).info("{} tickets where changed.", success);
    }

    @Test
    public void WithSomeErrors_ShouldPartiallySucceed() throws RemoteException {
        // Arrange
        var success = 10;
        List<ReticketingReport.FailureTicket> errors = new ArrayList<>();
        errors.add(new ReticketingReport.FailureTicket("Carlos","AA123"));
        errors.add(new ReticketingReport.FailureTicket("Carlitos","AA124"));
        errors.add(new ReticketingReport.FailureTicket("Carlota","AA125"));

        when(adminService.rescheduleTickets()).thenReturn(new ReticketingReport(success,errors));
        var arguments = new CliParser.Arguments();

        // Act
        new ReticketingAction(adminService, arguments, logger).run();

        // Assert
        verify(adminService,times(1)).rescheduleTickets();
        verify(logger,times(1)).info("{} tickets where changed.", success);
        errors.forEach(t->
                verify(logger,times(1))
                        .error("Cannot find alternative flight for {} with Ticket {}", t.getPassenger(), t.getFlightCode())
        );
    }
}
