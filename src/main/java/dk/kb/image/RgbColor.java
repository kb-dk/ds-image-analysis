package dk.kb.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import dk.kb.image.model.v1.InlineResponse200Dto;

public class RgbColor {
    static int pixelCount = 0;

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
                pixelCount ++;
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

    /**
     * Combine colorbuckets float[] and bucketCount int[] into a map. The float[] holds the keys and the int[] holds the values.
     * The arrays get combined by index number.
     * @param buckets float[] with keys of the map.
     * @param bucketCount int[] with the values for the map.
     * @return a map with key-value pairs from the input float[] and int[].
     */
    public static Map<Integer, Integer> bucketsAndBucketCountToMap(int[] buckets, int[] bucketCount){
    
        Map<Integer, Integer> map = new HashMap<>();
        for (int i=0; i<buckets.length; i++) {
            map.put(buckets[i], bucketCount[i]);
        }
    
        return map;
    }

    /**
     * Method to sort a map<Float, Integer> and have the key-value pair with the biggest value at index 0.
     * @param map of type map<Float, Integer> to be sorted
     * @return a list of entries of the type Entry<Float, Integer> sorted after biggest value.
     */
    public static List<Entry<Integer, Integer>> sortMap(Map<Integer, Integer> map){
        List<Entry<Integer, Integer>> sortedList = new ArrayList<>(map.entrySet());
    	sortedList.sort(Entry.comparingByValue());
        Collections.reverse(sortedList);
    
        return sortedList;
    }

    /**
     * Return a JSON array with top X entries from input list.
     * @param list input list to extract top x from.
     * @param x integer to limit size of returned list.
     * @return a JSON array containing the first x entries from the input list.
     */
    public static List<InlineResponse200Dto> returnTopXAsHex(List<Entry<Integer, Integer>> list, int x){
        return list.stream().
                map(entry-> RgbColor.Rgb2Hex(entry)).
                limit(x).
                collect(Collectors.toList());
    }

    /**
     * Convert an entry<Float, Integer> containing OKlab float value and number of pixels with that color into JSON object
     * containing the RGB hex value of the OKlab color  and the pixel value as percentage of the full picture.
     * @param okEntry input entry containing Oklab float key and an integer value of pixels with the color of the key.
     * @return a JSON object containing the String RGB hex value and a float with the percentage of pixels from the image with the given color. 
     */
    public static InlineResponse200Dto Rgb2Hex(Entry<Integer, Integer> rgbEntry){ 
        String key = String.format(Locale.ROOT, "#%06X", (0xFFFFFF & rgbEntry.getKey()));
        float value = rgbEntry.getValue();
        float percentage = value/pixelCount*100;

        InlineResponse200Dto response = new InlineResponse200Dto();

        response.hexRGB(key);
        response.percent(percentage);

        return response;
    }
    
}
