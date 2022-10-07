package dk.kb.image;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kb.util.Resolver;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RgbColorTest {
    private Logger log = LoggerFactory.getLogger(this.toString());

    @Test
    public void testCountBucketsForImg() throws IOException{
        BufferedImage img;
        int[] buckets = PalettePicker.defineSimpleBuckets();
        img = ImageIO.read(Resolver.resolveStream("flower.jpg"));
        int[] result = RgbColor.countBucketsForImg(img, buckets);
        assertTrue(result[3] == 22089);
        log.info("Bucket counter divides pixels to correct buckets.");
    }

    @Test
    public void testGetEuclidianColorDistance(){
        int red = Color.RED.getRGB();
        int pink = Color.PINK.getRGB();
        int blue = Color.BLUE.getRGB();

        int redResult = RgbColor.getEuclidianColorDistance(red, pink);
        int blueResult = RgbColor.getEuclidianColorDistance(blue, pink);
        assertTrue(redResult<blueResult);
        log.info("Pink color is closer to red than to blue as expected.");
    }
}
