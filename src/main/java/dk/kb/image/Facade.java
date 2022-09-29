package dk.kb.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.ArrayUtils;

import java.awt.image.BufferedImage;
import java.awt.color.ColorSpace;

import com.github.tommyettinger.colorful.oklab.*;

public class Facade {
    /**
     * Get the input image as greyscale.
     * @return a JPG file as bytes.
     */
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

    /**
     * Get the amount of unique colors from input image.
     * @return a string with the count of unique colors form the input image.
     */
    public static int getColorCount(BufferedImage img) throws IOException{
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
        int result = colors.size();
        return result;
    }

    /**
     * Get primary color from input image.
     * @return the primary color from input image as HEX value.
     */
    public static String getMostUsedRGBColor(BufferedImage img){
        // Define simple buckets
        int[] buckets = RgbColor.defineSimpleBuckets();
        // Count pixels and add 1 to closest bucket
        int[] bucketCount = RgbColor.countBucketsForImg(img, buckets);
        // Get best bucket
        int largestBucket = getlargestBucket(bucketCount);
        // Returns result as HEX value
        String result = RgbColor.printResult(buckets, largestBucket); 
        return result;
    }

    // TODO: Write javadoc
    public static String getMostUsedOKLabColor(BufferedImage img){
        // Define simple buckets
        float[] buckets = OkLabColor.arbitraryOKlabBuckets();
        // Count pixels and add 1 to closest bucket
        int[] bucketCount = OkLabColor.countBucketsForImg(img, buckets);
        // Get best bucket
        int largestBucket = getlargestBucket(bucketCount);
        // Returns result as HEX value
        // Method convertOKlabToHex() might be useful here.
        String result = OkLabColor.printResult(buckets, largestBucket); 
        System.out.println(result);
        return result;
    }

    /**
     * Gets the most used bucket from the input bucketCount.
     * @param bucketCount integer array containing the count of each color bucket.
     * @return the index of the most used bucket.
     */
    public static int getlargestBucket(int[] bucketCount){ 
        // Values for getting most used Bucket
        int largestBucket = 0;
        int maxCount = 0;
        // Finds the most used bucket
        for (int i = 0; i < bucketCount.length; i++){
            if (bucketCount[i] > maxCount){
                maxCount = bucketCount[i];
                largestBucket = i;
            }
        } 
        return largestBucket;
    }

    // TODO: Define which color-palette to use?
    // Something about a web-safe 256 color palette?
    // SMK - How have they created their color palette?
    // DawnBringer Aurora palette is used in the colorful.oklab.palette and is primarily used for game design.
}
