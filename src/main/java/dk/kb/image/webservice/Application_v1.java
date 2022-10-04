package dk.kb.image.webservice;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;

import dk.kb.image.api.v1.impl.ColorAnalysisApiServiceImpl;
import dk.kb.image.api.v1.impl.ImageManipulationApiServiceImpl;
import dk.kb.image.api.v1.impl.ServiceApiServiceImpl;


public class Application_v1 extends javax.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(
                JacksonJsonProvider.class,
                JacksonXMLProvider.class,
                ColorAnalysisApiServiceImpl.class,
                ImageManipulationApiServiceImpl.class,
                ServiceApiServiceImpl.class,
                dk.kb.util.webservice.exception.ServiceExceptionMapper.class
        ));
    }


}
