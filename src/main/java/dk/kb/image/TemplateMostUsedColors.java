package dk.kb.image;

import dk.kb.image.model.v1.DominantColorDto;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

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

    // buckets
    abstract List<C> defineBuckets();

    /**
     * Loop through pixels of input image, get color in colorspace defined by subclass for pixel and +1 to bucket closest to pixel color.
     * @param buckets array of bucket colors, type is defined by subclass.
     * @return the integer array bucketCounter, which contains the count for each bucket for the input image,
     */
    int[] getBucketCount(BufferedImage img, List<C> buckets){
        // Create bucket counter array
        int[] bucketCounter = new int[buckets.size()];
        // Loop over all pixels in image and get RGB color'
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

    abstract void updateBucketCounter(int pixel, List<C> buckets, int[] bucketCounter);

    //buckets with count
    abstract Map<C, Integer> combineBucketsAndBucketCount(List<C> buckets, int[] bucketCount);

    //Sorted list
    abstract List<Map.Entry<C, Integer>> sortList(Map<C, Integer> bucketsWithCount );

    // return top X as hex
    abstract List<DominantColorDto> returnResult(List<Map.Entry<C, Integer>> sortedList, int x);
}
