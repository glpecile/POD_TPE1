package ar.edu.itba.pod.client.admin;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CliParserTests {

    private CliParser cliParser;

    @BeforeEach
    public void setUp(){
        cliParser = new CliParser();
    }

    @ParameterizedTest()
    @ValueSource(strings = {"","1.1.1.1:9999","11.11.11.11","11.1.11.11:9999","hola","11.11.11.11:9",
            "11.11.11.11:99","11.11.11.11:999","11.11.11.11:99999"
    })
    public void invalidServerAddress_ShouldFail(String serverAddress){
        // Arrange
        var args = new String[]{"-DserverAddress=" + serverAddress, "-Daction=models"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest()
    @ValueSource(strings = {"10.23.34.55:9999"})
    public void validServerAddress_ShouldSucceed(String serverAddress){
        // Arrange
        var args = new String[]{"-DserverAddress=" + serverAddress, "-Daction=models"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getServerAddress()).isEqualTo(serverAddress);
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
            "models","flights","status","confirm","cancel","reticketing",
            "Models","Flights","Status","Confirm","Cancel","Reticketing"
    })
    public void validAction_ShouldSucceed(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=" + action};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getAction()).isEqualTo(Actions.valueOf(action.toUpperCase()));
    }


}
