package dk.kb.image.api.v1.impl;

import dk.kb.image.Facade;
import dk.kb.image.api.v1.*;
import java.io.File;
import java.util.List;
import java.util.Map;

import dk.kb.util.webservice.exception.ServiceException;
import dk.kb.util.webservice.exception.InternalServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.io.File;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.kb.util.webservice.ImplBase;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.*;
import java.awt.image.BufferedImage;
import io.swagger.annotations.Api;

/**
 * ds-image-analysis
 *
 * <p>Image Analysis provides analytical tools for our image collections at KB.  
 *
 */
public class ImageManipulationApiServiceImpl extends ImplBase implements ImageManipulationApi {
    private Logger log = LoggerFactory.getLogger(this.toString());

    InputStream in;
    BufferedImage img;

    /**
     * Return image in grayscale
     * 
     * @param image: The image to analyse
     * 
     * @return <ul>
      *   <li>code = 200, message = "Greyscale version of image.", response = File.class</li>
      *   </ul>
      * @throws ServiceException when other http codes should be returned
      *
      * @implNote return will always produce a HTTP 200 code. Throw ServiceException if you need to return other codes
     */
    @Override
    public javax.ws.rs.core.StreamingOutput getGreyscale( Attachment imageDetail) throws ServiceException {
        // read image
        try {
            log.debug("getGreyscale(...) called with call details: {}", getCallDetails());
            in = imageDetail.getDataHandler().getInputStream();;
            img = ImageIO.read(in);
            // Show download link in Swagger UI, inline when opened directly in browser
            setFilename("output", false, false);
            return output -> output.write(Facade.getGreyscale(img)); 
        } catch (Exception e){
            throw handleException(e);
        }
    
    }


}
