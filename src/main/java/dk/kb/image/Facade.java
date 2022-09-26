package dk.kb.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.plaf.ColorUIResource;

import com.ctc.wstx.cfg.OutputConfigFlags;

import java.awt.image.BufferedImage;
import java.awt.Color;

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
    public static String getMostUsedColor(BufferedImage img){
        // Define simple buckets
        int[] buckets = defineSimpleBuckets();
        // Count pixels and add 1 to closest bucket
        int[] bucketCount = countBucketsForImg(img, buckets);
        // Get best bucket
        int largestBucket = getlargestBucket(bucketCount);
        // Returns result as HEX value
        String result = printResult(buckets, largestBucket); 
        return result;
    }

    /**
     * Defines simple color buckets. 
     * Used in getMostUsedColor() to extract primary color.
     * @return an integer array of RGB colors.
     */
    public static int[] defineSimpleBuckets(){
         // Define colors as integers in array
        return new int[]{
            Color.RED.getRGB(),
            Color.GREEN.getRGB(),
            Color.BLUE.getRGB(),
            Color.YELLOW.getRGB(),
            Color.CYAN.getRGB(),
            Color.MAGENTA.getRGB()
            };
    }

    /**
     * Loop through pixels of input image, get RGB color for pixel and +1 to bucket closest to pixel color.
     * @param buckets Integer array of bucket colors.
     * @return the integer array bucketCounter, which contains the count for each bucket for the input image,
     */
    public static int[] countBucketsForImg(BufferedImage img, int[] buckets){
        // Create bucket counter array
        int[] bucketCounter = new int[buckets.length];
        // get image's width and height
        int width = img.getWidth();
        int height = img.getHeight();

        // Loop over all pixels in image and get RGB color
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get RGB for each pixel
                int pixelRGB = img.getRGB(x, y);
                    updateBucketCounter(pixelRGB, buckets, bucketCounter);
            }
        }
        return bucketCounter;
    }
    
    /**
     * Method to update bucketCounter in countBucketsForImg(). 
     * @param pixel The current pixels RGB value as integer.
     * @param buckets Integer array of color buckets.
     * @param bucketCounter Integer array to store count of buckets.
     */
    public static void updateBucketCounter(int pixel, int[] buckets, int[] bucketCounter){
        // Values for checking max
        int bestColor = 0;
        int minDistance = 2147483647;
        for (int i = 0; i < buckets.length; i ++){
            int totalDistance = getEuclidianColorDistance(pixel, buckets[i]);

            // Evaluates total distance against minimum distance for given bucket
            if (totalDistance < minDistance) {
                minDistance = totalDistance;
                bestColor = i;
            }
        }
         // Add 1 to the bucket closest to pixel color
         bucketCounter[bestColor] ++;
    }

    /**
     * Calculate Euclidian color distance between two RGB colors. 
     * @param pixel1 RGB color of pixel1 as integer.
     * @param pixel2 RGB color of pixel2 as integer.
     * @return the total distance between pixel 1 and 2. Higher number = Bigger distance.""
     */
    public static int getEuclidianColorDistance(int pixel1, int pixel2){
        // Divide pixel1 and pixel2 RGB into Red, Green and Blue integers
        int bucketRed = (pixel1 >> 16) & 0xFF;
        int bucketGreen = (pixel1 >> 8 ) & 0xFF;
        int bucketBlue = (pixel1) & 0xFF;

        int pixelRed = (pixel2 >> 16) & 0xFF;
        int pixelGreen = (pixel2 >> 8 ) & 0xFF;
        int pixelBlue = (pixel2) & 0xFF;

        // Calculate the difference between current pixels Red, Green and Blue values and current bucket colors values
        int distanceRed = (pixelRed - bucketRed)*(pixelRed - bucketRed);
        int distanceGreen = (pixelGreen - bucketGreen)*(pixelGreen - bucketGreen);
        int distanceBlue = (pixelBlue - bucketBlue)*(pixelBlue - bucketBlue);

        // Add distances together to a total distance as RGB distance
        int totalDistance = (distanceRed + distanceGreen + distanceBlue);
        return totalDistance;
    }

    /**
     * Gets the most used bucket from the input bucketCount.
     * @param bucketCount integer array containing the count of each bucket.
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

    /**
     * Get the hex color of the most used bucket.
     * @param buckets Integer array of color buckets.
     * @param largestBucket integer containing the index of the most used bucket.
     * @return the most used hex color as a string.
     */
    public static String printResult(int[] buckets, int largestBucket){
        String hexColor = String.format(Locale.ROOT, "#%06X", (0xFFFFFF & buckets[largestBucket]));
        return hexColor;
    }

    // Check this link: https://stackoverflow.com/questions/470690/how-to-automatically-generate-n-distinct-colors
}
