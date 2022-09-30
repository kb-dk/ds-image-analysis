package dk.kb.image;

import java.awt.image.BufferedImage;
import java.util.Locale;

public class RgbColor {

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
                RgbColor.updateBucketCounter(pixelRGB, buckets, bucketCounter);
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
            int totalDistance = RgbColor.getEuclidianColorDistance(pixel, buckets[i]);
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
     * Get the hex color of the most used bucket.
     * @param buckets Integer array of color buckets.
     * @param largestBucket integer containing the index of the most used bucket.
     * @return the most used hex color as a string.
     */
    public static String printResult(int[] buckets, int largestBucket){
        String hexColor = String.format(Locale.ROOT, "#%06X", (0xFFFFFF & buckets[largestBucket]));
        return hexColor;
    }
    
}
