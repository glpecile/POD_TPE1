package ar.edu.itba.pod.client.admin;

import static org.assertj.core.api.Assertions.*;

import ar.edu.itba.pod.client.admin.actions.ActionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Objects;

public class CliParserTests {

    private CliParser cliParser;

    @BeforeEach
    public void setUp(){
        cliParser = new CliParser();
    }

    @ParameterizedTest()
    @ValueSource(strings = {"","1..1.1:9999","11.11.11.11","11.1234.11.11:9999","hola","11.11.11.11:98900",
            "11:99","11.11:999","11.11.11.11:10.10"
    })
    public void invalidServerAddress_ShouldFail(String serverAddress){
        // Arrange
        var args = new String[]{"-DserverAddress=" + serverAddress, "-Daction=reticketing"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest()
    @ValueSource(strings = {
            "10.23.34.55:9999","1.2.3.5:9999", "10.23.34.55:999", "10.23.34.55:99", "10.23.34.55:9",
            "localhost:1099"
    })
    public void validServerAddress_ShouldSucceed(String serverAddress){
        // Arrange
        var args = new String[]{"-DserverAddress=" + serverAddress, "-Daction=confirm", "-Dflight=AA123"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getHost()).isEqualTo(serverAddress.split(":")[0]);
        assertThat(cli.get().getPort()).isEqualTo(Integer.parseInt(serverAddress.split(":")[1]));
    }

    @Test()
    public void noParameters_ShouldFail(){
        // Arrange
        var args = new String[]{};

        // Act
        var cli = cliParser.parse(args);
         // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"","invalid"})
    public void invalidAction_ShouldFail(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "reticketing", "Reticketing"
    })
    public void parseReticketing_ShouldSucceed(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getAction()).isEqualTo(ActionType.RETICKETING);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Models","models"})
    public void parseModels_ShouldSucceed(String action){
        // Arrange
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var file = Objects.requireNonNull(classloader.getResource("test.csv")).getPath();
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action,"-DinPath=" + file};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getAction()).isEqualTo(ActionType.MODELS);
        assertThat(cli.get().getFilePath().isPresent()).isTrue();
        assertThat(cli.get().getFilePath().get()).isEqualTo(file);
    }

    @Test
    public void parseModels_InvalidExtension_ShouldFail(){
        // Arrange
        var file = "../../file.exe";
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=models","-DinPath=" + file};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @Test
    public void parseModels_FileDoesNotExist_ShouldFail(){
        // Arrange
        var file = "../../file.csv";
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=models","-DinPath=" + file};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"status","Status"})
    public void parseStatus_ShouldSucceed(String action){
        // Arrange
        var flightCode = "AA123";
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action,"-Dflight=" + flightCode};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getAction()).isEqualTo(ActionType.STATUS);
        assertThat(cli.get().getFlightCode().isPresent()).isTrue();
        assertThat(cli.get().getFlightCode().get()).isEqualTo(flightCode);
    }

    @Test
    public void parseStatus_EmptyFlightCode_ShouldFail(){
        // Arrange
        var flightCode = "";
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=status","-Dflight=" + flightCode};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"confirm","Confirm"})
    public void parseConfirm_ShouldSucceed(String action){
        // Arrange
        var flightCode = "AA123";
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action,"-Dflight=" + flightCode};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getAction()).isEqualTo(ActionType.CONFIRM);
        assertThat(cli.get().getFlightCode().isPresent()).isTrue();
        assertThat(cli.get().getFlightCode().get()).isEqualTo(flightCode);
    }

    @Test
    public void parseConfirm_EmptyFlightCode_ShouldFail(){
        // Arrange
        var flightCode = "";
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=confirm","-Dflight=" + flightCode};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"cancel","Cancel"})
    public void parseCancel_ShouldSucceed(String action){
        // Arrange
        var flightCode = "AA123";
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action,"-Dflight=" + flightCode};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getAction()).isEqualTo(ActionType.CANCEL);
        assertThat(cli.get().getFlightCode().isPresent()).isTrue();
        assertThat(cli.get().getFlightCode().get()).isEqualTo(flightCode);
    }

    @Test
    public void parseCancel_EmptyFlightCode_ShouldFail(){
        // Arrange
        var flightCode = "";
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=confirm","-Dflight=" + flightCode};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Flights","flights"})
    public void parseFlights_ShouldSucceed(String action){
        // Arrange
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var file = Objects.requireNonNull(classloader.getResource("test.csv")).getPath();
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action,"-DinPath=" + file};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getAction()).isEqualTo(ActionType.FLIGHTS);
        assertThat(cli.get().getFilePath().isPresent()).isTrue();
        assertThat(cli.get().getFilePath().get()).isEqualTo(file);
    }

    @Test
    public void parseFlights_InvalidExtension_ShouldFail(){
        // Arrange
        var file = "../../file.exe";
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=flights","-DinPath=" + file};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @Test
    public void parseFlights_FileDoesNotExist_ShouldFail(){
        // Arrange
        var file = "../../file.csv";
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=flights","-DinPath=" + file};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }


}
