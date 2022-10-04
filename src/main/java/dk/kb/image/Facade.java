package dk.kb.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
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
    public static int getColorCount(BufferedImage img) throws IOException{
        // Hashset is a collection that only has one of each value
        Set<Integer> colors = new HashSet<Integer>();
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
        int result = colors.size();
        return result;
    }

    /**
     * Get primary RGB color from input image.
     * <p>
     * This method uses the standard RGB colorspace and a very limited amount of color bins. 
     * Currently it contains six colors (RED, GREEN, BLUE, YELLOW, CYAN and MAGENTA).
     * The method calculates the euclidian distance between the colors of the input img pixels and the colors of the buckets. 
     * </p>
     * @return the primary RGB color from input image as HEX value.
     */
    public static String getMostUsedRGBColor(BufferedImage img){
        // Define simple buckets
        // TODO: Change buckets - Maybe use SMK buckets for this method as well?
        int[] buckets = PalettePicker.defineSimpleBuckets();
        // Count pixels and add 1 to closest bucket
        int[] bucketCount = RgbColor.countBucketsForImg(img, buckets);
        // Get best bucket
        int largestBucket = getlargestBucket(bucketCount);
        // Returns result as HEX value
        String result = RgbColor.printResult(buckets, largestBucket); 
        return result;
    }

    /**
     * Get most used color from input image. 
     * <p>
     * This method uses the OKlab colorspace, which is a perceptual colorspace created from the CIELAB colorspace. 
     * Currently this method evaluates colors against 256 hex colors from SMK Open (National Gallery of Denmark) converted into OKlab colorspace. 
     * The distance between palette colors and image pixel colors is calculated using the delta E function adopted bye CIE in in 2000 known as CIEDE2000.
     * </p>
     * <p> Information on the OKlab colorspace can be found <a href="https://bottosson.github.io/posts/oklab/">here</a>.</p>
     * <p> Information on CIEDE2000 can be found <a href="https://www.researchgate.net/publication/229511830_The_development_of_the_CIE_2000_colour-difference_formula_CIEDE2000">here</a>.</p>
     * @param img
     * @return
     */
    public static String getMostUsedOKLabColor(BufferedImage img){
        // Define buckets
        float[] buckets = PalettePicker.smkOkLabBuckets();
        // Count pixels and add 1 to closest bucket
        int[] bucketCount = OkLabColor.countBucketsForImg(img, buckets);
        // Create map, where buckets and bucketCount has been combined
        Map<Float, Integer> bucketsWithCount = OkLabColor.bucketsAndBucketCountToMap(buckets, bucketCount);
        // Sorts and returns the combined map
        List<Entry<Float, Integer>> sortedList = OkLabColor.sortMap(bucketsWithCount);
        //  Returns top X from the sorted list
        // Currently set to 10
        // TODO: API implementation that allows users to set this value
        List<Entry<String, Float>> topX = OkLabColor.returnTopXAsHex(sortedList, 10);

        // TODO: Maybe this shouldn't return a string but a list array?
        String result = topX.toString();
        System.out.println(topX);
        return result;
    }

    /**
     * Gets the most used bucket from the input bucketCount.
     * @param bucketCount integer array containing the count of each color bucket.
     * @return the index of the most used bucket.
     */
    public static int getlargestBucket(int[] bucketCount){ 
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

    // TODO: Add capability of returning top X colors pr image
     
    // TODO: Implement OKlab method in the API service

    

}
