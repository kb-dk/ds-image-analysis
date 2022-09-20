package dk.kb.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Facade {
    public static byte[] getGreyscale(BufferedImage img) throws IOException{
        // get image's width and height
        int width = img.getWidth();
        int height = img.getHeight();
  
        // convert to grayscale
        // Nested for loop
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {               
                // Here (x,y)denotes the coordinate of image
                // for modifying the pixel value.
                // Get RGB value of single pixel
                int pixel = img.getRGB(x, y); 
                // Shifts colors bitwise right
                int a = (pixel >> 24) & 0xff;
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
  
                // calculate average
                int avg = (r + g + b) / 3;
  
                // replace RGB value with avg
                pixel = (a << 24) | (avg << 16) | (avg << 8)
                    | avg;
  
                img.setRGB(x, y, pixel);
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
        ImageIO.write(img, "jpg", baos);
        return baos.toByteArray();

    }

    public static String getColorCount(BufferedImage img) throws IOException{
        // Hashset is a collection that only has one of each value
        Set<Integer> colors = new HashSet<Integer>();
        // get image's width and height
        int w = img.getWidth();
        int h = img.getHeight();

        // Nested for loop that adds unique colors to the hashset colors
        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                int pixel = img.getRGB(x, y);     
                colors.add(pixel);
            }
        }

        String result = "There are " + Integer.toString(colors.size()) + " colors in this picture.";

        return result;
    }
}
