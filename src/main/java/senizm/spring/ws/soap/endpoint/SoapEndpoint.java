package senizm.spring.ws.soap.endpoint;

import com.ws.sample.AddData;
import com.ws.sample.AddDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Slf4j
@Endpoint
@RequiredArgsConstructor
public class SoapEndpoint {

    private static final String NAMESPACE_URI = "http://tempuri.org/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddData")
    @ResponsePayload
    public AddDataResponse addData(@RequestPayload AddData addData) {

        log.info("Data received with id: {}, description: {}", addData.getDataId(), addData.getDataDescription());
        AddDataResponse addDataResponse = new AddDataResponse();
        addDataResponse.setAddDataResult("Success");
        return addDataResponse;
    }
}
