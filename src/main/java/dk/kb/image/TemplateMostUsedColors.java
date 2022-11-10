package dk.kb.image;

import dk.kb.image.model.v1.DominantColorDto;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

abstract class TemplateMostUsedColors<C> {
    final List<DominantColorDto> getMostUsedColors(BufferedImage img, int x){
        // Define buckets
        List<C> buckets = defineBuckets();
        // Load image size
        int height = img.getHeight();
        int width = img.getWidth();
        // Create bucket counter
        int[] bucketCount = defineBucketCount(img, buckets, height, width);
        // Create combined map of buckets and bucket count
        Map<C, Integer> bucketsWithCount = combineBucketsAndBucketCount(buckets, bucketCount);
        // Sort the map
        List<Map.Entry<C, Integer>> sortedList = sortList(bucketsWithCount);
        // Return results
        return returnResult(sortedList, x);
    }

    // buckets
    abstract List<C> defineBuckets();

    // bucketcounter
    abstract int[] defineBucketCount(BufferedImage img, List<C> buckets, int height, int width);

    //buckets with count
    abstract Map<C, Integer> combineBucketsAndBucketCount(List<C> buckets, int[] bucketCount);

    //Sorted list
    abstract List<Map.Entry<C, Integer>> sortList(Map<C, Integer> bucketsWithCount );

    // return top X as hex
    abstract List<DominantColorDto> returnResult(List<Map.Entry<C, Integer>> sortedList, int x);
}
