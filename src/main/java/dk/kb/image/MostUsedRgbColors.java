package dk.kb.image;

import dk.kb.image.model.v1.DominantColorDto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements the RGB colorspace for the MostUsedColors template.
 * The RGB colorspace works with integers.
 */
public class MostUsedRgbColors extends TemplateMostUsedColors<Integer> {
    /**
     * Defines which color palette to use. This palette consists of 256 hex colors used by SMK (National Gallery of Denmark) converted to RGB colorspace.
     * It is not clear how this palette has been constructed in the first place.
     * @return the palette as a list of integers.
     */
    @Override
    public List<Integer> defineBuckets() {
        List<Integer> buckets = PalettePicker.smkRgbBuckets();
        return buckets;
    }

    @Override
    double calculateDistance(int pixel, Integer bucket) {
            // Divide pixel1 and pixel2 RGB into Red, Green and Blue integers
            int bucketRed = (pixel >> 16) & 0xFF;
            int bucketGreen = (pixel >> 8 ) & 0xFF;
            int bucketBlue = (pixel) & 0xFF;
            int pixelRed = (bucket >> 16) & 0xFF;
            int pixelGreen = (bucket >> 8 ) & 0xFF;
            int pixelBlue = (bucket) & 0xFF;
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
     * @param entry input entry containing Oklab float key and an integer value of pixels with the color of the key.
     * @return a JSON object containing the String RGB hex value and a float with the percentage of pixels from the image with the given color.
     */
    @Override
    DominantColorDto entryToRGB(Map.Entry<Integer, Integer> entry) {
        String key = String.format(Locale.ROOT, "#%06X", (0xFFFFFF & entry.getKey()));
        float value = entry.getValue();
        float percentage = value/pixelCount*100;

        DominantColorDto response = new DominantColorDto();

        response.hexRGB(key);
        response.percent(percentage);

        return response;
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
}
