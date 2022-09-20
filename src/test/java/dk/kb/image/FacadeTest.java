package dk.kb.image;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import dk.kb.util.Resolver;

public class FacadeTest {

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

    BufferedImage img;

    @Test
    public void testGreyscale(){
        try {
            img = ImageIO.read(Resolver.resolveStream("grey_flower.jpeg"));
        } catch (IOException e) {}
        assertEquals(true, FacadeTest.isGreyscale(img));
        System.out.println("Image is greyscale");
    }
}
