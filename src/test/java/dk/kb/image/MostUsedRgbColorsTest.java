package dk.kb.image;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kb.util.Resolver;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class MostUsedRgbColorsTest {
    private Logger log = LoggerFactory.getLogger(this.toString());

    @Test
    public void testCountBucketsForImg() throws IOException{
        BufferedImage img;
        List<Integer> buckets = PalettePicker.defineSimpleBuckets();
        MostUsedRgbColors myRgbColors = new MostUsedRgbColors();

        img = ImageIO.read(Resolver.resolveStream("flower.jpg"));
        int[] result = myRgbColors.defineBucketCount(img, buckets);
        assertTrue(result[3] == 22089);
        log.info("Bucket counter divides pixels to correct buckets.");
    }

    @Test
    public void testGetEuclidianColorDistance(){
        int red = Color.RED.getRGB();
        int pink = Color.PINK.getRGB();
        int blue = Color.BLUE.getRGB();

        int redResult = MostUsedRgbColors.getEuclidianColorDistance(red, pink);
        int blueResult = MostUsedRgbColors.getEuclidianColorDistance(blue, pink);
        assertTrue(redResult<blueResult);
        log.info("Pink color is closer to red than to blue as expected.");
    }
}
