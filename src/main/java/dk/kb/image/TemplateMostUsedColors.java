package dk.kb.image;

import dk.kb.image.model.v1.DominantColorDto;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Template to extract dominant colors from image.
 * Implementations need to implement a colorspace to work with.
 * @param <C> represents the datatype that the specific implementation implements.
 */
abstract class TemplateMostUsedColors<C> {
    int pixelCount = 0;

    /**
     * Template for color analysis. The template defines how the algorithm runs and the subclasses/implementations defines which colorspace to use.
     * @param x defines how many results that gets returned.
     * @return top x colors and their percentage of all pixels.
     */
    final List<DominantColorDto> getMostUsedColors(BufferedImage img, int x){
        // Define buckets
        List<C> buckets = defineBuckets();
        // Create bucket counter
        int[] bucketCount = getBucketCount(img, buckets);
        // Create combined map of buckets and bucket count
        Map<C, Integer> bucketsWithCount = combineBucketsAndBucketCount(buckets, bucketCount);
        // Sort the map
        List<Map.Entry<C, Integer>> sortedList = sortList(bucketsWithCount);
        // Return results
        return returnResult(sortedList, x);
    }

    /**
     * Defines which color palette to use and in which colorspace it should be represented.
     * @return the palette as a list of given type.
     */
    abstract List<C> defineBuckets();

    /**
     * Loop through pixels of input image, get color in colorspace defined by subclass for pixel and +1 to bucket closest to pixel color.
     * @param buckets list of bucket colors, type is defined by subclass.
     * @return the integer array bucketCounter, which contains the count for each bucket for the input image,
     */
    int[] getBucketCount(BufferedImage img, List<C> buckets){
        // Create bucket counter array
        int[] bucketCounter = new int[buckets.size()];
        // Loop over all pixels in image and get RGB color
        int height = img.getHeight();
        int width = img.getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get RGB for each pixel
                pixelCount ++;
                int pixelRGB = img.getRGB(x, y);
                updateBucketCounter(pixelRGB, buckets, bucketCounter);
            }
        }
        return bucketCounter;
    }

    /**
     * Method to update bucketCounter in getBucketCount().
     * @param pixel The current pixels color value in RGB colorspace.
     * @param buckets List of color buckets of used type in the implementation.
     * @param bucketCounter Integer array to store count of buckets.
     */
    void updateBucketCounter(int pixel, List<C> buckets, int[] bucketCounter) {
        // Convert RGB pixel value to OK Lab float
        // Values for checking max
        int bestColor = 0;
        double minDistance = 2147483647;
        for (int i = 0; i < buckets.size(); i ++){
            double totalDistance = calculateDistance(pixel, buckets.get(i));
            // Evaluates total distance against minimum distance for given bucket
            if (totalDistance < minDistance) {
                minDistance = totalDistance;
                bestColor = i;
            }
        }
        // Add 1 to the bucket closest to pixel color
        bucketCounter[bestColor] ++;
    }

    abstract double calculateDistance(int pixel, C bucket);

    /**
     * Combine list of color-buckets and bucketCount int[] into a map. The list holds the keys and the int[] holds the values.
     * The arrays get combined by index number.
     * @param buckets list of implemented type with keys of the map.
     * @param bucketCount int[] with the values for the map.
     * @return a map with key-value pairs from the input list and int[].
     */
     protected Map<C, Integer> combineBucketsAndBucketCount(List<C> buckets, int[] bucketCount){
         Map<C, Integer> bucketsWithCount = new HashMap<>();
         for (int i=0; i<buckets.size(); i++) {
             bucketsWithCount.put(buckets.get(i), bucketCount[i]);
         }
         return bucketsWithCount;
    }

    /**
     * Method to sort a map<C, Integer> and have the key-value pair with the biggest value at index 0.
     * @param bucketsWithCount of type map<C, Integer> to be sorted
     * @return a list of entries of the type Entry<C, Integer> sorted after biggest value.
     */
    List<Map.Entry<C, Integer>> sortList(Map<C, Integer> bucketsWithCount){
        List<Map.Entry<C, Integer>> sortedList = new ArrayList<>(bucketsWithCount.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());
        Collections.reverse(sortedList);

        return sortedList;
    }

    /**
     * Return a JSON array with top X entries from input list.
     * @param sortedList input list to extract top x from.
     * @param x integer to limit size of returned list.
     * @return a JSON array containing the first x entries from the input list.
     */
    List<DominantColorDto> returnResult(List<Map.Entry<C, Integer>> sortedList, int x){
        return sortedList.stream().
                map(entry-> entryToRGB(entry)).
                limit(x).
                collect(Collectors.toList());
    }

    /**
     * Convert an entry<C,Integer> containing value and number of pixels with that color in the implemented colorspace into JSON object
     * containing the RGB hex value of the color and the pixel value as percentage of the full picture.
     * @param entry input entry containing key of implemented type and an integer value of pixels with the color of the key.
     * @return a JSON object containing the String RGB hex value and a float with the percentage of pixels from the image with the given color.
     */
    abstract DominantColorDto entryToRGB(Map.Entry<C, Integer> entry);
}
