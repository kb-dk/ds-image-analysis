package dk.kb.image;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kb.util.Resolver;


public class OkLabColorTest {
    private Logger log = LoggerFactory.getLogger(this.toString());

    @Test 
    public void testDeltaE() throws IOException{
        MostUsedOkLabColor myColor = new MostUsedOkLabColor();
        int pixelRGB1 = Color.red.getRGB();
        int pixelRGB2 = Color.green.getRGB();
        float oklabFloat2 = ColorConversion.convertRGBtoOKlab(pixelRGB2);
        log.info("Testing with colors red and green from java.awt.Color");
        
        double result = ColorConversion.calculateCiede2000Distance(pixelRGB1, oklabFloat2);
        assertTrue(result <= 1 & result >= 0);
        log.info("DeltaE gets calculated and returns value between 0 and 1.");
    }

    @Test
    public void testSpeed() throws IOException{
        BufferedImage img;
        img = ImageIO.read(Resolver.resolveStream("flower.jpg"));
        long height = img.getHeight();
        long width = img.getWidth();
        long numberOfPixels = width*height;

        long startTime = System.nanoTime();
        Facade.getMostUsedOKLabColors(img, 10);
        long elapsedTime = System.nanoTime() - startTime;

        long elapsedTimeMillis = elapsedTime/1000000;

        System.out.println("Height: " + height);
        System.out.println("Width: " + width);
        System.out.println("Number of pixels in img: " + numberOfPixels);
        System.out.println("Elapsed time in millis: " + elapsedTimeMillis);
        System.out.println("Milliseconds per pixel: " + (float)elapsedTimeMillis/numberOfPixels);
        System.out.println("Time to calculate 16 million colors: " + ((float)elapsedTimeMillis/numberOfPixels)*16000000);
    }

    @Test
    public void testSortMap(){
        MostUsedOkLabColor myOkLabColor = new MostUsedOkLabColor();
        Map<Float, Integer>  testMap = OkLabColorTest.createTestMap();
        List<Entry<Float, Integer>> sortedList = myOkLabColor.sortList(testMap);
        assertEquals(testMap.get(1.2f), sortedList.get(0).getValue());
        log.info("Map gets sorted.");
    }

    /* Hvad skal testes?
     * ColorConversion.rgbColorsToBuckets() - laver fil med bytes.
     * MostUsedOkLabColor.getBucketCount - Her skal der være fokus på om den loader entriesForAllRgbColors korrekt.
     * MostUsedOkLabColor.updateBucketCounter - Sørg for at konverteringen mellem int og byte sker korrekt.
     *
     */
    @Test
    public void testCreateByteFile(){
        Color red = new Color(210, 27, 27);
        Color blue = new Color(35, 99, 196);
        Color green = new Color(71, 180, 27);

        List<Float> buckets = PalettePicker.smkOkLabBuckets();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int redInt = red.getRGB();
        int blueInt = blue.getRGB();
        int greenInt = green.getRGB();


        int bestColor = 0;
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < buckets.size(); i++) {
            double totalDistance = ColorConversion.calculateCiede2000Distance(redInt, buckets.get(i));
            // Evaluates total distance against minimum distance for given bucket
            if (totalDistance < minDistance) {
                minDistance = totalDistance;
                bestColor = i;
            }
        }
        byte bucketByte = (byte) bestColor;
        System.out.println(bucketByte);
        out.write(bucketByte);

    }

    public static Map<Float, Integer> createTestMap(){
        Map<Float, Integer> testMap = new LinkedHashMap<>();
        testMap.put(1.1f, 23);
        testMap.put(1.2f, 500);
        testMap.put(1.3f, 2);

        return testMap;
    }

}
