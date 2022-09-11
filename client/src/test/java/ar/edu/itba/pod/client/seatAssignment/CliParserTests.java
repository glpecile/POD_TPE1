package ar.edu.itba.pod.client.seatAssignment;

import ar.edu.itba.pod.client.seatAssignment.actions.ActionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class CliParserTests {
    private CliParser cliParser;

    @BeforeEach
    public void setUp() {
        cliParser = new CliParser();
    }

    @ParameterizedTest()
    @ValueSource(strings = {"", "1..1.1:9999", "11.11.11.11", "11.1234.11.11:9999", "ciao", "11.11.11.11:98900",
            "11:99", "11.11:999", "11.11.11.11:10.10"
    })
    public void invalidServerAddress_ShouldFail(String serverAddress) {
        // Arrange
        var args = new String[]{"-DserverAddress=" + serverAddress, "-Daction=reticketing"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest()
    @ValueSource(strings = {
            "10.23.34.55:9999", "1.2.3.5:9999", "10.23.34.55:999", "10.23.34.55:99", "10.23.34.55:9",
            "localhost:1099"
    })
    public void validServerAddress_ShouldSucceed(String serverAddress) {
        // Arrange
        var args = new String[]{"-DserverAddress=" + serverAddress, "-Daction=status", "-Dflight=AA123", "-Drow=1", "-Dcol=A"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getHost()).isEqualTo(serverAddress.split(":")[0]);
        assertThat(cli.get().getPort()).isEqualTo(Integer.parseInt(serverAddress.split(":")[1]));
    }

    @Test()
    public void noParameters_ShouldFail() {
        // Arrange
        var args = new String[]{};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "invalid"})
    public void invalidAction_ShouldFail(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"move", "Move"})
    public void parseMove_ShouldSucceed(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action, "-Dpassenger=John Doe", "-Dflight=AA123", "-Drow=1", "-Dcol=A"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getAction()).isEqualTo(ActionType.MOVE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"move", "Move"})
    public void parseMove_MissingPassengerParameter_ShouldFail(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action, "-Dflight=AA123", "-Drow=1", "-Dcol=A"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"status", "Status"})
    public void parseStatus_ShouldSucceed(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action, "-Dflight=AA123", "-Drow=1", "-Dcol=A"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getAction()).isEqualTo(ActionType.STATUS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"status", "Status"})
    public void parseStatus_MissingRowParameter_ShouldFail(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action, "-Dflight=AA123", "-Dcol=A"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"assign", "Assign"})
    public void parseAssign_ShouldSucceed(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action,"-Dpassenger=John Doe", "-Dflight=AA123", "-Drow=3", "-Dcol=D"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getAction()).isEqualTo(ActionType.ASSIGN);
    }

    @ParameterizedTest
    @ValueSource(strings = {"assign", "Assign"})
    public void parseAssign_MissingPassengerParameter_ShouldFail(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action, "-Dflight=AA123", "-Drow=3", "-Dcol=D"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"alternatives", "Alternatives"})
    public void parseAlternatives_ShouldSucceed(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action,"-Dpassenger=Jane Doe", "-Dflight=AA169"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getAction()).isEqualTo(ActionType.ALTERNATIVES);
    }

    @ParameterizedTest
    @ValueSource(strings = {"alternatives", "Alternatives"})
    public void parseAlternatives_MissingFlightCodeParameter_ShouldFail(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action,"-Dpassenger=Jane Doe", "-Dflight"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"changeTicket", "ChangeTicket"})
    public void parseChangeTicket_ShouldSucceed(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action,"-Dpassenger=Jaime Doe", "-Dflight=AA169", "-DoriginalFlight=AA123"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getAction()).isEqualTo(ActionType.CHANGETICKET);
    }

    @ParameterizedTest
    @ValueSource(strings = {"changeTicket", "ChangeTicket"})
    public void parseChangeTicket_MissingOriginalFlightCodeParameter_ShouldFail(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action,"-Dpassenger=Jaime Doe", "-Dflight=AA169", "-DoriginalFlight"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

}
