package ar.edu.itba.pod.client.seatmap;

import ar.edu.itba.pod.client.seatmap.actions.ActionType;
import ar.edu.itba.pod.models.SeatCategory;
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
        var args = new String[]{"-DserverAddress=" + serverAddress, "Dflight=AR1235", "DoutPath= \"out.csv\" "};


        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }

    @ParameterizedTest()
    @ValueSource(strings = {"","1..1.1:9999","11.11.11.11","11.1234.11.11:9999","hola","11.11.11.11:98900",
            "11:99","11.11:999","11.11.11.11:10.10"
    })
    public void missingOptionsTest_shouldFail(String serverAddress){
        // Arrange
        var args = new String[]{"-DserverAddress=" + serverAddress};

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
        var args = new String[]{"-DserverAddress=" + serverAddress, "-DoutPath=out.csv", "-Dflight=AA123"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();
        assertThat(cli.get().getHost()).isEqualTo(serverAddress.split(":")[0]);
        assertThat(cli.get().getPort()).isEqualTo(Integer.parseInt(serverAddress.split(":")[1]));
    }

    @Test
    public void validIdentityExecution_ShouldSucceed(){
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-DoutPath=out.csv", "-Dflight=AA123"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();

        assertThat(cli.get().getOutputPath().isPresent()).isTrue();
        assertThat(cli.get().getOutputPath().get()).isEqualTo("out.csv");

        assertThat(cli.get().getFlightCode().isPresent()).isTrue();
        assertThat(cli.get().getFlightCode().get()).isEqualTo("AA123");

        assertThat(cli.get().getAction()).isEqualTo(ActionType.IDENTITY);
    }

    @ParameterizedTest()
    @ValueSource(strings = {
            "BUSINESS", "PREMIUM_ECONOMY", "ECONOMY"
    })
    public void validCategoryExecution_ShouldSucceed(String category){
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-DoutPath=out.csv", "-Dflight=AA123", "-Dcategory=" +category};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();

        assertThat(cli.get().getOutputPath().isPresent()).isTrue();
        assertThat(cli.get().getOutputPath().get()).isEqualTo("out.csv");

        assertThat(cli.get().getFlightCode().isPresent()).isTrue();
        assertThat(cli.get().getFlightCode().get()).isEqualTo("AA123");

        assertThat(cli.get().getAction()).isEqualTo(ActionType.CATEGORY);
        assertThat(cli.get().getCategory().isPresent()).isTrue();
        assertThat(cli.get().getCategory().get()).isEqualTo(SeatCategory.valueOf(category));
    }


    @ParameterizedTest()
    @ValueSource(strings = {
            "1", "2", "3"
    })
    public void validRowExecution_ShouldSucceed(String row){
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-DoutPath=out.csv", "-Dflight=AA123", "-Drow=" + row};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isPresent()).isTrue();

        assertThat(cli.get().getOutputPath().isPresent()).isTrue();
        assertThat(cli.get().getOutputPath().get()).isEqualTo("out.csv");

        assertThat(cli.get().getFlightCode().isPresent()).isTrue();
        assertThat(cli.get().getFlightCode().get()).isEqualTo("AA123");

        assertThat(cli.get().getAction()).isEqualTo(ActionType.ROW);
        assertThat(cli.get().getRow().isPresent()).isTrue();
        assertThat(cli.get().getRow().get()).isEqualTo(Integer.valueOf(row));
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
    public void invalidArgument_ShouldFail(String action) {
        // Arrange
        var args = new String[]{"-DserverAddress=10.23.34.55:9999", "-Daction=", "-DoutPath=out.csv", "-Dflight=AA123"};

        // Act
        var cli = cliParser.parse(args);

        // Assert
        assertThat(cli.isEmpty()).isTrue();
    }


}
