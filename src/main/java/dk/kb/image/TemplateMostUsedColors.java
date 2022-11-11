package dk.kb.image;

import dk.kb.image.model.v1.DominantColorDto;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Template for color analysis. The template defines how the algorithm runs and the subclasses/implementations defines which colorspace to use.
 * Implementations need to implement a colorspace to work with.
 * @param <C> represents the datatype used for calculating color difference in the specific implementations.
 */
abstract class TemplateMostUsedColors<C> {
    int pixelCount = 0;

    /**
     * Template for color analysis.
     * getMostUsedColors defines the template for the main method of the template class.
     * This method calculates the dominant colors for the input image.
     * @param x defines how many results that gets returned.
     * @return top x colors and their percentage of all pixels.
     */
    List<DominantColorDto> getMostUsedColors(BufferedImage img, int x){
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
     * Update count for the color bucket that is closest to the color of the input pixel, based on calculation from the method calculateDistance().
     * @param pixel The current pixels color value in RGB colorspace.
     * @param buckets List of color buckets of used type in the implementation.
     * @param bucketCounter Integer array to store count of buckets.
     */
    void updateBucketCounter(int pixel, List<C> buckets, int[] bucketCounter) {
        // Values for checking max
        int bestColor = 0;
        double minDistance = Double.MAX_VALUE;
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

    /**
     * Calculate distance between two colors. When implementing this method it is possible to calculate colors in different ways.
     * This can be achieved by implementing different calculations, for example euclidean distance or Delta E distance. <br/>
     * If implementing a colorspace that works with colors as something else than integers,
     * then implementations of this method has to convert the rgbPixel into the given colorspace before calculating distance.
     * @param rgbPixel color of the input pixel.
     * @param color in the implemented colorspace that we compare the input rgbPixel to.
     * @return the calculated color distance between the inputted rgbPixel and color.
     */
    abstract double calculateDistance(int rgbPixel, C color);

    /**
     * Combine list of color-buckets and bucketCount int[] into a map. The list holds the values of the palette colors and the int[] specifies how many times the given color has been used in the input image.
     * The arrays get combined by index number.
     * @param buckets list of palette colors in implemented colorspace.
     * @param bucketCount int[] of how many times the color with same index has been used.
     * @return a map consisting of color keys and number of uses as values.
     */
     protected Map<C, Integer> combineBucketsAndBucketCount(List<C> buckets, int[] bucketCount){
         Map<C, Integer> bucketsWithCount = new HashMap<>();
         for (int i=0; i<buckets.size(); i++) {
             bucketsWithCount.put(buckets.get(i), bucketCount[i]);
         }
         return bucketsWithCount;
    }

    /**
     * Method to sort a {@code map<C, Integer>} and have the key-value pair with the biggest value at index 0.
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
     * Return a JSON array with top X RGB colors and their percentage from input palette colors and their percentage.
     * @param sortedList input list holding palette colors and their percentage to extract top x from.
     * @param x integer to limit size of returned list.
     * @return a JSON array containing the first x RGB colors and their percentage from the input list.
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
