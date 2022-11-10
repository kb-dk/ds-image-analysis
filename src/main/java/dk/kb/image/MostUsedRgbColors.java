package dk.kb.image;

import dk.kb.image.model.v1.DominantColorDto;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

public class MostUsedRgbColors extends TemplateMostUsedColors<Integer> {
    static int pixelCount = 0;

    @Override
    public List<Integer> defineBuckets() {
        List<Integer> buckets = PalettePicker.smkRgbBuckets();
        return buckets;
    }

    /**
     * Loop through pixels of input image, get RGB color for pixel and +1 to bucket closest to pixel color.
     * @param buckets Integer array of bucket colors.
     * @return the integer array bucketCounter, which contains the count for each bucket for the input image,
     */
    @Override
    public int[] defineBucketCount(BufferedImage img, List<Integer> buckets) {
        // Create bucket counter array
        int[] bucketCounter = new int[buckets.size()];
        // get image's width and height
        int width = img.getWidth();
        int height = img.getHeight();

        // Loop over all pixels in image and get RGB color
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
     * Combine colorbuckets float[] and bucketCount int[] into a map. The float[] holds the keys and the int[] holds the values.
     * The arrays get combined by index number.
     * @param buckets float[] with keys of the map.
     * @param bucketCount int[] with the values for the map.
     * @return a map with key-value pairs from the input float[] and int[].
     */
    @Override
    public Map<Integer, Integer> combineBucketsAndBucketCount(List<Integer> buckets, int[] bucketCount) {
        Map<Integer, Integer> bucketsWithCount = new HashMap<>();
        for (int i=0; i<buckets.size(); i++) {
            bucketsWithCount.put(buckets.get(i), bucketCount[i]);
        }

        return bucketsWithCount;
    }

    /**
     * Method to sort a map<Float, Integer> and have the key-value pair with the biggest value at index 0.
     * @param bucketsWithCount of type map<Float, Integer> to be sorted
     * @return a list of entries of the type Entry<Float, Integer> sorted after biggest value.
     */
    @Override
    public List<Map.Entry<Integer, Integer>> sortList(Map<Integer, Integer> bucketsWithCount) {
        List<Map.Entry<Integer, Integer>> sortedList = new ArrayList<>(bucketsWithCount.entrySet());
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
    @Override
    public List<DominantColorDto> returnResult(List<Map.Entry<Integer, Integer>> sortedList, int x) {
        return sortedList.stream().
                map(entry-> Rgb2Hex(entry)).
                limit(x).
                collect(Collectors.toList());
    }
    /**
     * Method to update bucketCounter in countBucketsForImg().
     * @param pixel The current pixels RGB value as integer.
     * @param buckets Integer array of color buckets.
     * @param bucketCounter Integer array to store count of buckets.
     */
    public static void updateBucketCounter(int pixel, List<Integer> buckets, int[] bucketCounter){
        // Values for checking max
        int bestColor = 0;
        int minDistance = 2147483647;
        for (int i = 0; i < buckets.size(); i ++){
            int totalDistance = getEuclidianColorDistance(pixel, buckets.get(i));
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
     * Calculate Euclidean color distance between two RGB colors.
     * @param pixel1 RGB color of pixel1 as integer.
     * @param pixel2 RGB color of pixel2 as integer.
     * @return the total distance between pixel 1 and 2. Higher number = Bigger distance.
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
     * Convert an entry<Float, Integer> containing OKlab float value and number of pixels with that color into JSON object
     * containing the RGB hex value of the OKlab color  and the pixel value as percentage of the full picture.
     * @param rgbEntry input entry containing Oklab float key and an integer value of pixels with the color of the key.
     * @return a JSON object containing the String RGB hex value and a float with the percentage of pixels from the image with the given color.
     */
    public static DominantColorDto Rgb2Hex(Map.Entry<Integer, Integer> rgbEntry){
        String key = String.format(Locale.ROOT, "#%06X", (0xFFFFFF & rgbEntry.getKey()));
        float value = rgbEntry.getValue();
        float percentage = value/pixelCount*100;

        DominantColorDto response = new DominantColorDto();

        response.hexRGB(key);
        response.percent(percentage);

        return response;
    }

}
