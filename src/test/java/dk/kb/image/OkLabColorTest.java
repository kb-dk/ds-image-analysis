package dk.kb.image;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.validation.constraints.AssertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kb.util.Resolver;


public class OkLabColorTest {
    private Logger log = LoggerFactory.getLogger(this.toString());

    @Test
    public void testCountBucketsForImg() throws IOException{
        BufferedImage img;
        float[] buckets = {1.1709022E-38f, -8.4903297E37f};
        img = ImageIO.read(Resolver.resolveStream("flower.jpg"));
        int[] result = OkLabColor.countBucketsForImg(img, buckets);
        assertTrue(result[0]<result[1]);
        log.info("Bucket counter divides pixels tp correct buckets.");
    }

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

    @Test
    public void testSortMap(){
        Map<Float, Integer>  testMap = OkLabColorTest.createTestMap();
        List<Entry<Float, Integer>> sortedList = OkLabColor.sortMap(testMap);
        assertEquals(testMap.get(1.2f), sortedList.get(0).getValue());
        log.info("Map gets sorted.");
    }

    public static Map<Float, Integer> createTestMap(){
        Map<Float, Integer> testMap = new LinkedHashMap<>();
        testMap.put(1.1f, 23);
        testMap.put(1.2f, 500);
        testMap.put(1.3f, 2);

        return testMap;
    }
}
