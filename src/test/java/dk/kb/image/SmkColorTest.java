package dk.kb.image;

import org.junit.jupiter.api.Test;

public class SmkColorTest {
    @Test
    public void testSMKColors(){
        AnalyseSmkDeltaE.AnalyseColorDistance();
    }

    @Test
    public void getHexColors(){
        String[] hexValues = PalettePicker.smkHexPalette();
        System.out.println(hexValues[0]);
        System.out.println(hexValues[2]);
    }
}

