package dk.kb.image;

import dk.kb.image.model.v1.DominantColorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Implements the OK Lab colorspace for the MostUsedColors template.
 * The OK Lab colorspace works with floats.
 */
public class MostUsedOkLabColor extends TemplateMostUsedColors<Float> {
    private static final Logger log = LoggerFactory.getLogger(MostUsedOkLabColor.class);

    static final byte[] entriesForAllRgbColors;
    static {
        try {
            entriesForAllRgbColors = Thread.currentThread().getContextClassLoader().
                    getResource("OklabBucketEntriesForAllRgbColors").openStream().readAllBytes();
        } catch (IOException e) {
            log.error("Bucket entries for RGB colors are not loaded from file.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Defines which color palette to use. This palette consists of 256 hex colors used by SMK (National Gallery of Denmark) converted to OKLab colorspace.
     * It is not clear how this palette has been constructed in the first place.
     * @return the palette as a list of floats
     */
    @Override
    public List<Float> defineBuckets() {
        List<Float> buckets = PalettePicker.smkOkLabBuckets();
        return buckets;
    }

    @Override
    void updateBucketCounter(int pixel, List<Float> buckets, int[] bucketCounter) throws IOException {

        int pixelNoAlpha = pixel & 0xFFFFFF;

        int bestColor = Byte.toUnsignedInt(entriesForAllRgbColors[pixelNoAlpha]);

        bucketCounter[bestColor] ++;
    }


    /**
     * Calculate the colour difference value between two colours in a lab space.
     * This CIEDE2000 calculation fits the OKlab colorspace.
     * DeltaE function from: https://stackoverflow.com/a/61343807
     * The function has been proofread against the equation from:
     * M. R. Luo, G. Cui, B. Rigg: The Development of the CIE 2000 Colour-Difference Formula: CIEDE2000 in COLOR research and application Volume 26, Number 5, October 2001
     * @param pixel int containing RGB color of input pixel.
     * @param bucket float containing OKLab color of input bucket.
     * @return the CIE 2000 colour difference
     */
    @Override
    double calculateDistance(int pixel, Float bucket) throws IOException {

        int pixelNoAlpha = pixel & 0xFFFFFF;

        int bestColor = Byte.toUnsignedInt(entriesForAllRgbColors[pixelNoAlpha]);

        return bestColor;
    }

    /**
     * Convert an entry<Float, Integer> containing OKlab float value and number of pixels with that color into JSON object
     * containing the RGB hex value of the OKlab color  and the pixel value as percentage of the full picture.
     * @param entry input entry containing Oklab float key and an integer value of pixels with the color of the key.
     * @return a JSON object containing the String RGB hex value and a float with the percentage of pixels from the image with the given color.
     */
    @Override
    DominantColorDto entryToRGB(Map.Entry<Float, Integer> entry) {
        String key = ColorConversion.convertOKlabToHex(entry.getKey());
        float value = entry.getValue();
        float percentage = value/pixelCount*100;

        DominantColorDto response = new DominantColorDto();

        response.hexRGB(key);
        response.percent(percentage);

        return response;
    }
}
