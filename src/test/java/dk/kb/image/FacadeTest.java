package dk.kb.image;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import dk.kb.util.Resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacadeTest {
    private Logger log = LoggerFactory.getLogger(this.toString());


    // method to validate that image is greyscale
    public static boolean isGreyscale(BufferedImage image){
        // get width and height of image 
        int width = image.getWidth();
        int height = image.getHeight();
        // integers for pixel and colors
        int pixel,red, green, blue;
        // Nested loop to check pixels
        // Works vertically through pixels from bottom left 
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++) {
                // Get RGB value of single pixel
                pixel = image.getRGB(i, j);
                // Bitwise rightshifts in colors
                red = (pixel >> 16) & 0xff;
                green = (pixel >> 8) & 0xff;
                blue = (pixel) & 0xff;
                // Check that colors are equal to eachother
                // If not equal, the image contains color
                if (red != green || green != blue ) return false;
            }
        }
        return true;
    }

    public static boolean canCountColors(BufferedImage image){
        // Hashset is a collection that only has one of each value
        Set<Integer> colors = new HashSet<Integer>();
        // get image's width and height
        int width = image.getWidth();
        int height = image.getHeight();

        // Nested for loop that adds unique colors to the hashset colors
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);     
                colors.add(pixel);
            }
        }
        // Check if HashSet is empty
        if (colors.isEmpty()) return false;
        return true;
    }



    @Test
    public void testGreyscalePositive() throws IOException {
        BufferedImage img;
        img = ImageIO.read(Resolver.resolveStream("grey_flower.jpeg"));
        assertTrue(FacadeTest.isGreyscale(img));
        log.info("Image is greyscale");
    }

    @Test
    public void testGreyscaleNegative() throws IOException {
        BufferedImage img;
        img = ImageIO.read(Resolver.resolveStream("flower.jpg"));
        assertFalse(FacadeTest.isGreyscale(img));
        log.info("Image is not greyscale");
    }

    @Test
    public void testColorCount() throws IOException {
        BufferedImage img;
        img = ImageIO.read(Resolver.resolveStream("flower.jpg"));
        log.info(Facade.getColorCount(img));
        assertTrue(canCountColors(img));
        log.info("Colors get counted"); 
    }

    @Test
    public void testColorDistance() throws IOException{
        BufferedImage img;
        img = ImageIO.read(Resolver.resolveStream("flower.jpg"));
        Facade.colorDistance(img);
    }
}
