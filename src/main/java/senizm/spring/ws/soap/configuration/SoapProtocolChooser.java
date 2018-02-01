package senizm.spring.ws.soap.configuration;

import org.springframework.ws.transport.TransportInputStream;

import java.io.IOException;

public interface SoapProtocolChooser {

    boolean useSoap11(TransportInputStream transportInputStream) throws IOException;
    boolean useSoap12(TransportInputStream transportInputStream) throws IOException;
}
