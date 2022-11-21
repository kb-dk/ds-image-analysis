package dk.kb.image;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.reflections.Reflections.log;

public class ByteStreamSanityTest {
    private Logger log = LoggerFactory.getLogger(this.toString());


    // Create file containing byte for colors entry in buckets for OKlab method.
    @Test
    public void createTestByteFile(){
        Color red = new Color(204, 49, 49);
        Color blue = new Color(47, 87, 187);
        Color green = new Color(71, 180, 27);

        List<Float> buckets = PalettePicker.smkOkLabBuckets();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int[] colors = new int[]{red.getRGB(),blue.getRGB(),green.getRGB()};

        int bestColor;
        double minDistance;
        for (int color : colors) {
            bestColor = 0;
            minDistance = Double.MAX_VALUE;
            for (int i = 0; i < buckets.size(); i++) {
                double totalDistance = ColorConversion.calculateCiede2000Distance(color, buckets.get(i));
                if (totalDistance < minDistance) {
                    minDistance = totalDistance;
                    bestColor = i;
                }
            }
            byte bucketByte = (byte) bestColor;
            out.write(bucketByte);
        }

        // The three test colors map to the buckets at indexes 143,133 and 153. With the following hex values: #C6353D, #415E9E and #3FAA5C respectively.
        try (OutputStream outputStream = new FileOutputStream("src/test/resources/testBytes")) {
            out.writeTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File f = new File("src/test/resources/testBytes");
        assertTrue(f.isFile() && !f.isDirectory());
        log.info("testBytes file successfully created.");
    }

    // Tests that bytes are loaded correctly from file and correctly converted to unsigned ints.
    @Test
    public void testLoadBytesFile() throws IOException {
        byte[] correctValues = new byte[]{-113, -123, -103};
        byte[] testBytes = Thread.currentThread().getContextClassLoader().getResource("testBytes").openStream().readAllBytes();
        for (int i = 0; i < testBytes.length; i++){
            assertEquals(correctValues[i], testBytes[i]);
            assertEquals(Byte.toUnsignedInt(correctValues[i]), Byte.toUnsignedInt(testBytes[i]));
        }
        log.info("Bytes are loaded as expected");
    }

    @Test
    public void testOutputToInputByteStreams(){
        int[] testInts = new int[]{200,220,117,2};
        byte[] testBytes = new byte[]{-56, -36, 117, 2};

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for (int i = 0; i < testInts.length; i++){
            out.write((byte) testInts[i]);
        }

        ByteArrayInputStream in = new ByteArrayInputStream(testBytes);
        byte[] inArray = in.readAllBytes();

        // Tests converting int to byte and byte to int
        for (int i = 0; i < inArray.length; i++) {
            assertEquals((byte) testInts[i], inArray[i]);
            assertEquals(testInts[i], Byte.toUnsignedInt(inArray[i]));
        }
        log.info("Input and output byte from streams are alike.");
    }
}
