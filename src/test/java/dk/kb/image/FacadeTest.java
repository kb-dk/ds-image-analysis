package dk.kb.image;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import dk.kb.util.Resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacadeTest {
    private Logger log = LoggerFactory.getLogger(this.toString());

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
        BufferedImage testImg = createTestImg();
        assertEquals(1, Facade.getColorCount(testImg));
        log.info("Colors get counted"); 
    }

    @Test
    public void testMostUsedColor() throws IOException{
        BufferedImage img;
        img = ImageIO.read(Resolver.resolveStream("flower.jpg"));
        assertFalse(Facade.getMostUsedColor(img).isEmpty());
        log.info("The most used color has been calculated");
    }

    @Test
    public void testMostUsedColor2() throws IOException{
        BufferedImage img;
        img = ImageIO.read(Resolver.resolveStream("blue-bricks.jpg"));
        assertFalse(Facade.getMostUsedColor2(img).isEmpty());
        log.info("The most used color has been calculated");
    }
    
    @Test 
    public void testDeltaE() throws IOException{
        int pixelRGB1 = Color.red.getRGB();
        int pixelRGB2 = Color.green.getRGB();
        float oklabFloat1 = Facade.convertRGBtoOKlab(pixelRGB1);
        float oklabFloat2 = Facade.convertRGBtoOKlab(pixelRGB2);
        float[] fa1 = Facade.convertOKlabFloatToFloatArray(oklabFloat1);
        float[] fa2 = Facade.convertOKlabFloatToFloatArray(oklabFloat2);
        log.info("Testing with colors red and green from java.awt.Color");
        
        double result = Facade.calculateDeltaE(fa1, fa2);
        assertTrue(result <= 1 & result >= 0);
        log.info("DeltaE gets calculated and returns value between 0 and 1.");
    }

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

    // Create simple testImage with 1 color
    public static BufferedImage createTestImg(){
        BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setPaint (new Color(255, 0, 0));
        graphics.fillRect ( 0, 0, img.getWidth(), img.getHeight() );
        return img;
    }
}
