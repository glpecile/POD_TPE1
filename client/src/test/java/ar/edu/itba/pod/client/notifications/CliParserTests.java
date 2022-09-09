package ar.edu.itba.pod.client.notifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

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
        var args = new String[]{"-DserverAddress=" + serverAddress, "-Dpassenger=John", "-Dflight=AA123"};

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
        var passenger = "John_Doe";
        var flight = "AR1234";
        var args = new String[]{"-DserverAddress=" + serverAddress, "-Dpassenger="+ passenger, "-Dflight=" + flight};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getHost()).isEqualTo(serverAddress.split(":")[0]);
        assertThat(cli.get().getPort()).isEqualTo(Integer.parseInt(serverAddress.split(":")[1]));
        assertThat(cli.get().getPassenger()).isEqualTo(passenger);
        assertThat(cli.get().getFlight()).isEqualTo(flight);
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
}
