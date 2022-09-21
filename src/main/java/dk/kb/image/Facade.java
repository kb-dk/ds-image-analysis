package dk.kb.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.ctc.wstx.cfg.OutputConfigFlags;

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
                // set pixel to grayscale colors
                img.setRGB(x, y, pixel);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Write img to ByteArrayOutputStream as jpg
        ImageIO.write(img, "jpg", baos);
        return baos.toByteArray();

    }

    public static String getColorCount(BufferedImage img) throws IOException{
        // Hashset is a collection that only has one of each value
        Set<Integer> colors = new HashSet<Integer>();
        // get image's width and height
        int width = img.getWidth();
        int height = img.getHeight();

        // Nested for loop that adds unique colors to the hashset colors
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int pixel = img.getRGB(x, y);     
                colors.add(pixel);
            }
        }

        String result = "There are " + Integer.toString(colors.size()) + " colors in this picture.";

        return result;
    }


    public static void getDominantColor(BufferedImage img){
        // Create array long enough to hold all RGB colors
        int[] allPixelsArray = new int[16777216];


        // ArrayList is an array that can be added to.
        // get image's width and height
        int width = img.getWidth();
        int height = img.getHeight();

        // Loop over all pixels in image and get RGB color
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = img.getRGB(x, y);
                // add +1 to array at RGB index
                allPixelsArray[pixel & 0x00FFFFFF] += 1;
            }
        }

        int maxRGB = 0;
        int maxCount = 0;

        // Checks all posible RGB colors in array
        for (int rgb = 0; rgb < 16777215; rgb++){
            if (allPixelsArray[rgb] != 0) {
                if (allPixelsArray[rgb] > maxCount){
                    maxCount = allPixelsArray[rgb];
                    maxRGB = rgb;
                }
            }
        }

        System.out.println(Integer.toString(maxRGB, 16));
        System.out.println(maxCount);
    }
}
