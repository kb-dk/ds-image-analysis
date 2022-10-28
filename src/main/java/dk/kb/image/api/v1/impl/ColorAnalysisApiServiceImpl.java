package dk.kb.image.api.v1.impl;

import dk.kb.image.Facade;
import dk.kb.image.api.v1.*;
import dk.kb.image.model.v1.InlineResponse200Dto;

import java.util.Collections;
import java.util.List;

import dk.kb.util.webservice.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import dk.kb.util.webservice.ImplBase;

import javax.imageio.ImageIO;

import org.apache.cxf.jaxrs.ext.multipart.*;
import java.awt.image.BufferedImage;

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
     * @param image:      The image to analyse
     * @param top-colors: Number of colors to return
     * @return <ul>
     * <li>code = 200, message = "The dominant color", response = String.class</li>
     * </ul>
     * @throws ServiceException when other http codes should be returned
     * @implNote return will always produce a HTTP 200 code. Throw ServiceException if you need to return other codes
     */
    @Override
    public List<Object> getMainOkLabColors(@Multipart(value = "image" ) Attachment imageDetail, @Multipart(value = "top-colors")  Integer topColors){
    // read image
        try {
            in = imageDetail.getDataHandler().getInputStream();;
            img = ImageIO.read(in);
            List<InlineResponse200Dto> response = Facade.getMostUsedOKLabColors(img, topColors);
            return Collections.singletonList(response);
        } catch (Exception f){
            throw handleException(f);
        }
    
    }


    @Override
    public List<Object> getMainRgbColors(Attachment imageDetail, Integer topColors) {
        // read image
        try {
            in = imageDetail.getDataHandler().getInputStream();;
            img = ImageIO.read(in);
            List<InlineResponse200Dto> response = Facade.getMostUsedRGBColors(img, topColors);
            return Collections.singletonList(response);
        } catch (Exception f){
            throw handleException(f);
        }
    }
}
