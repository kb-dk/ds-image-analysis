package dk.kb.image;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.*;
import dk.kb.image.model.v1.DominantColorDto;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import javax.imageio.ImageIO;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kb.util.Resolver;


public class OkLabColorTest {
    private  Logger log = LoggerFactory.getLogger(Logger.class);

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
        log.info("Height: " + height);
        log.info("Width: " + width);
        log.info("Number of pixels in img: " + numberOfPixels);
        log.info("Elapsed time in millis: " + elapsedTimeMillis);
        log.info("Milliseconds per pixel: " + (float)elapsedTimeMillis/numberOfPixels);
        log.info("Time to calculate 16 million colors: " + ((float)elapsedTimeMillis/numberOfPixels)*16000000);
    }

    @Test
    public void testSortMap() throws IOException {
        MostUsedOkLabColor myOkLabColor = new MostUsedOkLabColor();
        Map<Float, Integer>  testMap = OkLabColorTest.createTestMap();
        List<Entry<Float, Integer>> sortedList = myOkLabColor.sortList(testMap);
        assertEquals(testMap.get(1.2f), sortedList.get(0).getValue());
        log.info("Map gets sorted.");
    }


    @Test
    public void testOkLabResponse() throws IOException {
        MostUsedOkLabColor okLabAnalyser = new MostUsedOkLabColor();
        ObjectMapper mapper = new ObjectMapper();
        // These colors are the results from the OkLab calculation
        String[] trueColors = new String[]{
          "#153923", "#0E1C0D", "#081B0C", "#203A21", "#F6E670"
        };

        BufferedImage img;
        img = ImageIO.read(Resolver.resolveStream("flower.jpg"));

        List<DominantColorDto> mostUsedColors = okLabAnalyser.getMostUsedColors(img, 5);
        String jsonArray = mapper.writeValueAsString(mostUsedColors);
        JsonNode root = mapper.readTree(jsonArray);

        for (int i = 0; i< mostUsedColors.size(); i++){
            String hexValue = root.path(i).get("hexRGB").textValue();
            assertEquals(trueColors[i], hexValue);
        }
    }

    @Test
    public void testEntries() throws IOException {
        byte[] testBytes = Thread.currentThread().getContextClassLoader().getResource("testBytes").openStream().readAllBytes();
        byte[] rgbBytes = Thread.currentThread().getContextClassLoader().getResource("OklabBucketEntriesForAllRgbColors").openStream().readAllBytes();
        log.info("Testing that small testBytes file has same entries as big rgb color entries");

        Color red = new Color(204, 49, 49);
        int redInt = red.getRGB();
        int redNoAlpha = redInt & 0xFFFFFF;

        byte result = rgbBytes[redNoAlpha];

        log.info("Result: byte: " + result);
        log.info("Byte value from testBytes: " + testBytes[0]);
        log.info("Byte to unsigned int of result: " + Byte.toUnsignedInt(result));
        log.info("Byte to unsigned int of testBytes: " + Byte.toUnsignedInt(testBytes[0]));
        // Same red color located at index 0 in testBytes
        assertEquals(testBytes[0],rgbBytes[redNoAlpha]);
    }

    @Test
    public void testStream1024Colors() throws IOException {
        // Map 1024 colors to entries
        Color x;
        List<Float> buckets = PalettePicker.smkOkLabBuckets();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Loop to create 1024 colors and find their entry in the list of buckets
        for (int r = 0; r < 1; r++) {
            for (int g = 0; g < 4; g++) {
                for (int b = 0; b < 256; b++) {
                    // Create color x
                    x = new Color(r, g, b);
                    ColorConversion.writeEntryByteForColorToOutputStream(x, buckets, out);
                }
            }
        }
        // Stream entry bytes to file of bytes
        try (OutputStream outputStream = new FileOutputStream("src/test/resources/thousandBytes")) {
            // Write the byte output stream to file
            out.writeTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Load entries from bytes
        byte[] originalBytes = out.toByteArray();
        byte[] testBytes = Thread.currentThread().getContextClassLoader().getResource("thousandBytes").openStream().readAllBytes();
        URL OK = Thread.currentThread().getContextClassLoader().getResource("OklabBucketEntriesForAllRgbColors");
        log.info("Loading OKLabs mapping from '{}'", OK);
        byte[] bigFile = OK.openStream().readAllBytes();
        // Compare that bytes are loaded correctly and are equal to the original bytes
        for (int i = 0; i < out.size(); i++){
            assertEquals(originalBytes[i], testBytes[i]);
        }
        // Test that first thousand bytes of bigFile is equal to testBytes
        for (int i = 0; i < testBytes.length; i++){
            assertEquals(testBytes[i], bigFile[i]);
        }
    }

    // Test to ensure alpha channel gets removed correctly
    @Test
    public void testAlphaRemoval() throws IOException {
        // Create array of the byte values from the three test colors
        byte[] correctValues = new byte[]{-113, -123, -103};
        byte[] testBytes = Thread.currentThread().getContextClassLoader().getResource("testBytes").openStream().readAllBytes();

        for (int i = 0; i < testBytes.length; i++){
            int pixelNoAlpha = testBytes[i] & 0xFFFFFF;
            log.info(testBytes[i] + " : " + pixelNoAlpha + " : " + correctValues[i]);
            assertEquals(correctValues[i], testBytes[i]);
        }
    }

    /*
    // Create file of bytes. When mapping RGB colorspace it takes 24 minutes to run
    @Test
    public void createOKlabEntries() throws IOException {
        ColorConversion.allRgbColorsToOkLabBuckets();
    }
     */
    public static Map<Float, Integer> createTestMap(){
        Map<Float, Integer> testMap = new LinkedHashMap<>();
        testMap.put(1.1f, 23);
        testMap.put(1.2f, 500);
        testMap.put(1.3f, 2);

        return testMap;
    }

}
