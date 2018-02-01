package senizm.spring.ws.soap.configuration;

import org.springframework.ws.transport.TransportInputStream;

import java.io.IOException;
import java.util.Iterator;

public class SimpleSoapProtocolChooser implements SoapProtocolChooser {

    private static final String CONTENT_TYPE_HEADER_NAME = "content-type";
    private static final String CONTENT_TYPE_HEADER_CONTENT_SOAP_11 = "text/xml";

    @Override
    public boolean useSoap11(TransportInputStream transportInputStream) throws IOException {
        for (Iterator headerNames = transportInputStream.getHeaderNames(); headerNames.hasNext(); ) {
            String headerName = (String) headerNames.next();
            for (Iterator headerValues = transportInputStream.getHeaders(headerName); headerValues.hasNext(); ) {
                String headerValue = (String) headerValues.next();
                if (headerName.toLowerCase().contains(CONTENT_TYPE_HEADER_NAME)) {
                    if (headerValue.trim().toLowerCase().contains(CONTENT_TYPE_HEADER_CONTENT_SOAP_11)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean useSoap12(TransportInputStream transportInputStream) throws IOException {
        return useSoap11(transportInputStream) == false;
    }
}