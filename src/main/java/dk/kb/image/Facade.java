package dk.kb.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import dk.kb.image.model.v1.DominantColorDto;

import java.awt.image.BufferedImage;

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
    public static int getColorCount(BufferedImage img) {
        // Hashset is a collection that only has one of each value
        Set<Integer> colors = new HashSet<>();
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
        return colors.size();
    }

    /**
     * Get dominant RGB colors from input image.
     * <p>
     * This method uses the standard RGB colorspace and uses SMKs color palette.
     * The method calculates the euclidean distance between the colors of the input img pixels and the colors of the buckets.
     * </p>
     * @return the dominant RGB colors from input image as HEX value.
     */
    public static List<DominantColorDto> getMostUsedRGBColors(BufferedImage img, int x) {
        MostUsedRgbColors findMostUsedRgbColors = new MostUsedRgbColors();
        return findMostUsedRgbColors.getMostUsedColors(img, x);
    }

    /**
     * Get top X most used colors from input image. 
     * <p>
     * This method uses the OKlab colorspace, which is a perceptual colorspace created from the CIELAB colorspace. 
     * Currently this method evaluates colors against 256 hex colors from SMK Open (National Gallery of Denmark) converted into OKlab colorspace. 
     * The distance between palette colors and image pixel colors is calculated using the delta E function adopted bye CIE in 2000 known as CIEDE2000.
     * </p>
     * <p> Information on the OKlab colorspace can be found <a href="https://bottosson.github.io/posts/oklab/">here</a>.</p>
     * <p> Information on CIEDE2000 can be found <a href="https://www.researchgate.net/publication/229511830_The_development_of_the_CIE_2000_colour-difference_formula_CIEDE2000">here</a>.</p>
     * @param img
     * @return a JSON array of top X colors RGB hex value and percentage of pixels in given color.
     */
    public static List<DominantColorDto> getMostUsedOKLabColors(BufferedImage img, int x){
        MostUsedOkLabColor findMostUsedOkLabColors = new MostUsedOkLabColor();
        return findMostUsedOkLabColors.getMostUsedColors(img, x);
    }

    /**
     * Gets the most used bucket from the input bucketCount.
     * @param bucketCount integer array containing the count of each color bucket.
     * @return the index of the most used bucket.
     */
    public static int getlargestBucket(int[] bucketCount){
        // TODO: This method is never used.
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
}
