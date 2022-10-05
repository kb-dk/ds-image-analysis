package dk.kb.image.api.v1.impl;

import dk.kb.image.AnalyseDeltaEOfPalette;
import dk.kb.image.Facade;
import dk.kb.image.api.v1.*;
import dk.kb.image.model.v1.InlineResponse200Dto;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import javax.validation.constraints.DecimalMin;
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
public class ColorAnalysisApiServiceImpl extends ImplBase implements ColorAnalysisApi {
    private Logger log = LoggerFactory.getLogger(this.toString());

    InputStream in;
    BufferedImage img;

    /**
     * Count the unique colors in the given image.
     * 
     * @param image: The image to analyse
     * 
     * @return <ul>
      *   <li>code = 200, message = "Number of unique colors in image:", response = Integer.class</li>
      *   </ul>
      * @throws ServiceException when other http codes should be returned
      *
      * @implNote return will always produce a HTTP 200 code. Throw ServiceException if you need to return other codes
     */
    @Override
    public Integer getColorCount( Attachment imageDetail) throws ServiceException {
        // read image
        try {
            in = imageDetail.getDataHandler().getInputStream();;
            img = ImageIO.read(in);
            int result = Facade.getColorCount(img);
            String response = "There are " + Integer.toString(result) + " unique colors in this picture.";
            return result;
        } catch (Exception f){
            throw handleException(f);
        }  
    
    }

    /**
     * Get the most dominant color from an image. Calculated in OKlab colorspace and messured with deltaE.
     * 
     * @param image: The image to analyse
     * 
     * @param top-colors: Number of colors to return
     * 
     * @return <ul>
      *   <li>code = 200, message = "The dominant color", response = String.class</li>
      *   </ul>
      * @throws ServiceException when other http codes should be returned
      *
      * @implNote return will always produce a HTTP 200 code. Throw ServiceException if you need to return other codes
     */
    @Override
    public List<InlineResponse200Dto> getMainOkLabColors( @Multipart(value = "image" ) Attachment imageDetail, @Multipart(value = "top-colors")  Integer topColors){
    // read image
        try {
            in = imageDetail.getDataHandler().getInputStream();;
            img = ImageIO.read(in);
            List<InlineResponse200Dto> response = Facade.getMostUsedOKLabColor(img, topColors);
            return response;
        } catch (Exception f){
            throw handleException(f);
        }
    
    }

    /**
     * Get the most dominant color from an image. Calculated in RGB space with euclidian distance.
     * 
     * @param image: The image to analyse
     * 
     * @return <ul>
      *   <li>code = 200, message = "The dominant color", response = String.class</li>
      *   </ul>
      * @throws ServiceException when other http codes should be returned
      *
      * @implNote return will always produce a HTTP 200 code. Throw ServiceException if you need to return other codes
     */
    @Override
    public String getMainRgbColor( Attachment imageDetail) throws ServiceException {
        // read image
        try {
            in = imageDetail.getDataHandler().getInputStream();;
            img = ImageIO.read(in);
            String response = Facade.getMostUsedRGBColor(img);
            return response;
        } catch (Exception f){
            throw handleException(f);
        }
    
    }

    @Override
    public List<String> calculateColorDistance(@NotNull @DecimalMin("2") List<String> colors) {
        // TODO Make it able to output the list through API. 
        // See this stackoverflow: https://stackoverflow.com/questions/9311396/no-message-body-writer-has-been-found-for-response-class-arraylist
        String[] array = new String[colors.size()];
        colors.toArray(array);
        List<String> response = AnalyseDeltaEOfPalette.AnalyseHexPaletteDeltaE(array);
        return response;
    }


}
