package dk.kb.image;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import java.util.List;

public class ColorConversionTest {
    private Logger log = LoggerFactory.getLogger(this.toString());
    
    @Test
    public void testConvertHexArrayToOKlabArray(){
        String[] testPalette = {"#ffffff", "#222222"};
        List<Float> oklabResult = ColorConversion.convertHexArrayToOKlabArray(testPalette);
        assertTrue(oklabResult.get(0) == -84904433160159100000000000000000000000.000000 && oklabResult.get(1) == -84903297345221300000000000000000000000.000000);
        log.info("Hex values get corretly converted to the OKlab colorspace");
    }

    @Test
    public void testConvertSingleHexToOklab(){
        String hex = "#ffffff";
        float oklabResult = ColorConversion.convertSingleHexToOklab(hex);
        assertTrue(oklabResult == -84904433160159100000000000000000000000.000000);
        log.info("Hex value gets correctly converted to OKlab colorspace.");
    }

    @Test 
    public void testConvertOKlabFloatToFloatArray(){
        float oklabTestFloat = 84904433160159100000000000000000000000.000000f;
        float[] splitFloatResult = ColorConversion.convertOKlabFloatToFloatArray(oklabTestFloat);
        assertTrue(splitFloatResult[0] == 1 && splitFloatResult[1] == 0.49803922f && splitFloatResult[2] == 0.49803922f);
        log.info("Input float gets split into correct L, A and B values.");
    }

    @Test
    public void testConvertOKlabToHex(){
        float oklabTestFloat = 84904433160159100000000000000000000000.000000f;
        String hexValue = ColorConversion.convertOKlabToHex(oklabTestFloat);
        assertEquals("#FBFFFF", hexValue);
        log.info("OKLab float converts to RGB Hex");
    }

    @Test
    public void testConvertOKlabToRgbInt(){
        float oklabTestFloat = 84904433160159100000000000000000000000.000000f;
        int resultRgb = ColorConversion.convertOKlabToRgbInt(oklabTestFloat);
        assertEquals(16515071, resultRgb);
        log.info("OKLab float converts to RGB integer");

    }

    @Test 
    public void testConvertRGBtoOKlab(){
        int testRgb = 16515071;
        float resultOkLab = ColorConversion.convertRGBtoOKlab(testRgb);
        assertEquals(1.1709022E-38f, resultOkLab);
        log.info("RGB int converts to OKlab float.");
    }

    @Test
    public void testByteFile() throws IOException {
        // Load OldOklabBucketEntriesForAllRgbColors
        // The first color has RGB value 0,0,0. Then followed by 0,0,1.
        byte[] test = Thread.currentThread().getContextClassLoader().getResource("OklabBucketEntriesForAllRgbColors").openStream().readAllBytes();
        //System.out.println(Arrays.toString(test));
        System.out.println(test.length);
    }

    @Test
    public void testTripleLoop() throws IOException {
        Color in = new Color(240,12,140);
        byte[] entriesForAllRgbColors = Thread.currentThread().getContextClassLoader().getResource("OklabBucketEntriesForAllRgbColors").openStream().readAllBytes();

        int i = in.getRGB() & 0xFFFFFF;

        int out = Byte.toUnsignedInt(entriesForAllRgbColors[i]);
        System.out.println(out);

        // How do I convert RGB values list of numbers
        // RGB -> index i liste
        // 001 = 0, 002 = 1, 0 1 0 = 256
    }
}
