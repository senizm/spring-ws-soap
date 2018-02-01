package senizm.spring.ws.soap.configuration;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.TransportInputStream;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

public class GenericSoapMessageFactory implements SoapMessageFactory, InitializingBean {

    private static final String REQUEST_CONTEXT_ATTRIBUTE = "GenericSoapMessageFactory";

    private SaajSoapMessageFactory soap11;
    private SaajSoapMessageFactory soap12;
    private SoapProtocolChooser soapProtocolChooser;

    @PostConstruct
    public void init() {
        this.soap11 = new SaajSoapMessageFactory();
        this.soap12 = new SaajSoapMessageFactory();
        this.soapProtocolChooser = new SimpleSoapProtocolChooser();
    }

    private void setMessageFactoryForRequestContext(SaajSoapMessageFactory factory) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        attributes.setAttribute(REQUEST_CONTEXT_ATTRIBUTE, factory, RequestAttributes.SCOPE_REQUEST);
    }

    private SaajSoapMessageFactory getMessageFactoryForRequestContext() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (SaajSoapMessageFactory) attributes.getAttribute(REQUEST_CONTEXT_ATTRIBUTE,
            RequestAttributes.SCOPE_REQUEST);
    }

    public void setSoapVersion(SoapVersion version) {
        // ignore this, it will be set automatically
    }

    public void setSoapProtocolChooser(SoapProtocolChooser soapProtocolChooser) {
        this.soapProtocolChooser = soapProtocolChooser;
    }

    private void configureFactory(SaajSoapMessageFactory factory, SoapVersion version) {
        factory.setSoapVersion(version);
        factory.afterPropertiesSet();
    }

    @Override
    public void afterPropertiesSet() {
        configureFactory(soap11, SoapVersion.SOAP_11);
        configureFactory(soap12, SoapVersion.SOAP_12);
    }

    @Override
    public SoapMessage createWebServiceMessage() {
        return getMessageFactoryForRequestContext().createWebServiceMessage();
    }

    @Override
    public SoapMessage createWebServiceMessage(InputStream inputStream) throws IOException {
        setMessageFactoryForRequestContext(soap11);
        if (inputStream instanceof TransportInputStream) {
            TransportInputStream transportInputStream = (TransportInputStream) inputStream;
            if (soapProtocolChooser.useSoap12(transportInputStream)) {
                setMessageFactoryForRequestContext(soap12);
            }
        }
        SaajSoapMessageFactory mf = getMessageFactoryForRequestContext();
        return mf.createWebServiceMessage(inputStream);
    }
}
