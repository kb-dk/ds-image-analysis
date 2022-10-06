package dk.kb.image;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyseDeltaEOfPaletteTest {
    private Logger log = LoggerFactory.getLogger(this.toString());

    @Test
    public void testAnalyseHexPaletteDeltaE(){
        String[] testPalette = {"#ff1234", "#4321ff"};
        List<String> result = AnalyseDeltaEOfPalette.AnalyseHexPaletteDeltaE(testPalette);
        assertTrue(result.get(0) != null);
        log.info("Delta E gets calculated");

    }
}

