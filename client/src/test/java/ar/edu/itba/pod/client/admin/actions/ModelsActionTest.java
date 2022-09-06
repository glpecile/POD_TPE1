package ar.edu.itba.pod.client.admin.actions;

import ar.edu.itba.pod.client.admin.CliParser;
import ar.edu.itba.pod.services.AdminService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.rmi.RemoteException;
import java.util.Objects;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ModelsActionTest {

    private final AdminService adminService = mock(AdminService.class);
    private final Logger logger = mock(Logger.class);
    private final ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    @Test
    public void loadValidModels_ShouldSucceed() throws RemoteException {
        // Arrange
        var arguments = new CliParser.Arguments();

        arguments.setFilePath(GetPath("PlaneModels.csv"));

        // Act
        try {
            new ModelsAction(adminService, arguments,logger).run();

            // Assert
            verify(adminService, times(2)).addPlane(any(),any());
            verify(logger, times(1)).info("{} added models",2);
        }
        catch (Exception e){
            fail("");
        }



    }

    @Test
    public void loadPartiallyValidModels_ShouldSucceed() throws RemoteException {
        // Arrange
        var arguments = new CliParser.Arguments();
        arguments.setFilePath(GetPath("PlaneModels.csv"));

        doThrow(RemoteException.class).when(adminService).addPlane(eq("Boeing 787"),any());

        // Act
        try {
            new ModelsAction(adminService, arguments,logger).run();
        }
        catch (Exception e){
            fail("");
        }

        verify(adminService, times(2)).addPlane(any(),any());
        verify(logger, times(1)).info("{} added models",1);
        verify(logger, times(1)).error("Cannot add model {}", "Boeing 787");

    }


    private String GetPath(String fileName)
    {
        return Objects.requireNonNull(classloader.getResource(fileName)).getPath();
    }

}
