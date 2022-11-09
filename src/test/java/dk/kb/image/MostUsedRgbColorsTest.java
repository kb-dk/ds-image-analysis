package dk.kb.image;

import dk.kb.util.Resolver;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class MostUsedRgbColorsTest {
    private Logger log = LoggerFactory.getLogger(this.toString());

    @Test
    public void getMostUsedRgbColor() throws IOException {
        BufferedImage img;
        img = ImageIO.read(Resolver.resolveStream("flower.jpg"));
        MostUsedColors myColors = new MostUsedRgbColors();
        assertFalse(myColors.getMostUsedColors(img, 5).isEmpty());
        log.info("The most used color has been calculated");
    }
}
