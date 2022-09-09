package ar.edu.itba.pod.client.models;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;


public class Arguments {
    protected final Logger logger = LoggerFactory.getLogger(Arguments.class);

    private final static Pattern SERVER_ADDRESS_PATTERN = Pattern.
            compile("^(?<host>localhost|\\d?\\d?\\d(?:\\.\\d{1,3}){3}):(?<port>\\d{1,4})$");
    @Getter
    private String host = null;
    @Getter
    private int port = 0;

    public boolean isValid(){
        return host != null && port > 0;
    }

    public void setServerAddress(String serverAddress) {
        var matcher = SERVER_ADDRESS_PATTERN.matcher(serverAddress);
        if (matcher.matches()){
            host = matcher.group("host");
            port = Integer.parseInt(matcher.group("port"));
        }
        else{
            logger.error("The server address is not valid!");
        }
    }
}
