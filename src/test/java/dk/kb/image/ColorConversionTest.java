package dk.kb.image;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorConversionTest {
    private Logger log = LoggerFactory.getLogger(this.toString());
    
    @Test
    public void testConvertHexArrayToOKlabArray(){
        String[] testPalette = {"#ffffff", "#222222"};
        float[] oklabResult = ColorConversion.convertHexArrayToOKlabArray(testPalette);
        assertTrue(oklabResult[0] == -84904433160159100000000000000000000000.000000 && oklabResult[1] == -84903297345221300000000000000000000000.000000);
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
}
