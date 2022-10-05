package dk.kb.image;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OkLabColorTest {
    private Logger log = LoggerFactory.getLogger(this.toString());

    @Test 
    public void testDeltaE() throws IOException{
        int pixelRGB1 = Color.red.getRGB();
        int pixelRGB2 = Color.green.getRGB();
        float oklabFloat1 = ColorConversion.convertRGBtoOKlab(pixelRGB1);
        float oklabFloat2 = ColorConversion.convertRGBtoOKlab(pixelRGB2);
        float[] fa1 = ColorConversion.convertOKlabFloatToFloatArray(oklabFloat1);
        float[] fa2 = ColorConversion.convertOKlabFloatToFloatArray(oklabFloat2);
        log.info("Testing with colors red and green from java.awt.Color");
        
        double result = OkLabColor.calculateDeltaE(fa1, fa2);
        assertTrue(result <= 1 & result >= 0);
        log.info("DeltaE gets calculated and returns value between 0 and 1.");
    }

    // TODO: Check which test are missing
    // TODO: Add missing tests
}
