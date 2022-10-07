package dk.kb.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import dk.kb.image.model.v1.InlineResponse200Dto;

public class OkLabColor {
    static int pixelCount = 0;

    /**
     * Loop through pixels of input image, get OKlab color for pixel and +1 to bucket closest to pixel color.
     * @param buckets float array of bucket colors.
     * @return the integer array bucketCounter, which contains the count for each bucket for the input image,
     */
    public static int[] countBucketsForImg(BufferedImage img, float[] buckets){
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
                float pixelOKlab = ColorConversion.convertRGBtoOKlab(pixelRGB);
                OkLabColor.updateBucketCounter(pixelOKlab, buckets, bucketCounter);
            }
        }
        return bucketCounter;
    }

    /**
     * Method to update bucketCounter in countBucketsForImg(). 
     * @param pixel The current pixels OKlab color value as float.
     * @param buckets float array of color buckets.
     * @param bucketCounter Integer array to store count of buckets.
     */
    public static void updateBucketCounter(float pixel, float[] buckets, int[] bucketCounter){
        // Values for checking max
        int bestColor = 0;
        double minDistance = 2147483647;
        for (int i = 0; i < buckets.length; i ++){
            float[] pixelFloatArray = ColorConversion.convertOKlabFloatToFloatArray(pixel);
            float[] bucketFloatArray = ColorConversion.convertOKlabFloatToFloatArray(buckets[i]);
            double totalDistance = OkLabColor.calculateDeltaE(pixelFloatArray, bucketFloatArray);
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
     * Get the hex color of the most used bucket.
     * @param buckets float array of color buckets.
     * @param largestBucket integer containing the index of the most used bucket.
     * @return the most used hex color as a string.
     */
    public static String printResult(float[] buckets, int largestBucket){
        String hexColor = ColorConversion.convertOKlabToHex(buckets[largestBucket]);
        return hexColor;
    }

    /**
     * Calculate the colour difference value between two colours in a lab space.
     * This CIEDE2000 calculation fits the OKlab colorspace. 
     * DeltaE function from: https://stackoverflow.com/a/61343807
     * The function has been proof read against the equation from:
     * M. R. Luo, G. Cui, B. Rigg: The Development of the CIE 2000 Colour-Difference Formula: CIEDE2000 in COLOR research and application Volume 26, Number 5, October 2001
     * @param lab1 double array containing values L, A and B of first color. 
     * @param lab2 double array containing values L, A and B of second color.
     * @return the CIE 2000 colour difference
     */
    public static double calculateDeltaE(float[] lab1, float[] lab2) {
        // Extract L, A and B values from input arrays.
        // Furthermore converts floats to doubles to make precise calculation
        double L1 = lab1[0];
        double a1 = lab1[1]; 
        double b1 = lab1[2];
        double L2 = lab2[0];
        double a2 = lab2[1];
        double b2 = lab2[2];
    
        // Calculates CIEDE2000
        double Lmean = (L1 + L2) / 2.0;
        double C1 =  Math.sqrt(a1*a1 + b1*b1);
        double C2 =  Math.sqrt(a2*a2 + b2*b2);
        double Cmean = (C1 + C2) / 2.0;
    
        double G =  ( 1 - Math.sqrt( Math.pow(Cmean, 7) / (Math.pow(Cmean, 7) + Math.pow(25, 7)) ) ) / 2;
        double a1prime = a1 * (1 + G);
        double a2prime = a2 * (1 + G);
    
        double C1prime =  Math.sqrt(a1prime*a1prime + b1*b1);
        double C2prime =  Math.sqrt(a2prime*a2prime + b2*b2);
        double Cmeanprime = (C1prime + C2prime) / 2;
    
        double h1prime =  Math.atan2(b1, a1prime) + 2*Math.PI * (Math.atan2(b1, a1prime)<0 ? 1 : 0);
        double h2prime =  Math.atan2(b2, a2prime) + 2*Math.PI * (Math.atan2(b2, a2prime)<0 ? 1 : 0);
        double Hmeanprime =  ((Math.abs(h1prime - h2prime) > Math.PI) ? (h1prime + h2prime + 2*Math.PI) / 2 : (h1prime + h2prime) / 2);
    
        double T =  1.0 - 0.17 * Math.cos(Hmeanprime - Math.PI/6.0) + 0.24 * Math.cos(2*Hmeanprime) + 0.32 * Math.cos(3*Hmeanprime + Math.PI/30) - 0.2 * Math.cos(4*Hmeanprime - 21*Math.PI/60);
    
        double deltahprime =  ((Math.abs(h1prime - h2prime) <= Math.PI) ? h2prime - h1prime : (h2prime <= h1prime) ? h2prime - h1prime + 2*Math.PI : h2prime - h1prime - 2*Math.PI);
    
        double deltaLprime = L2 - L1;
        double deltaCprime = C2prime - C1prime;
        double deltaHprime =  2.0 * Math.sqrt(C1prime*C2prime) * Math.sin(deltahprime / 2.0);
        double SL =  1.0 + ( (0.015*(Lmean - 50)*(Lmean - 50)) / (Math.sqrt( 20 + (Lmean - 50)*(Lmean - 50) )) );
        double SC =  1.0 + 0.045 * Cmeanprime;
        double SH =  1.0 + 0.015 * Cmeanprime * T;
    
        double deltaTheta =  (30 * Math.PI / 180) * Math.exp(-((180/Math.PI*Hmeanprime-275)/25)*((180/Math.PI*Hmeanprime-275)/25));
        double RC =  (2 * Math.sqrt(Math.pow(Cmeanprime, 7) / (Math.pow(Cmeanprime, 7) + Math.pow(25, 7))));
        double RT =  (-RC * Math.sin(2 * deltaTheta));
    
        double KL = 1;
        double KC = 1;
        double KH = 1;
    
        double deltaE = Math.sqrt(
                ((deltaLprime/(KL*SL)) * (deltaLprime/(KL*SL))) +
                ((deltaCprime/(KC*SC)) * (deltaCprime/(KC*SC))) +
                ((deltaHprime/(KH*SH)) * (deltaHprime/(KH*SH))) +
                (RT * (deltaCprime/(KC*SC)) * (deltaHprime/(KH*SH)))
                );
    
        return deltaE;
    }

    /**
     * Combine colorbuckets float[] and bucketCount int[] into a map. The float[] holds the keys and the int[] holds the values.
     * The arrays get combined by index number.
     * @param buckets float[] with keys of the map.
     * @param bucketCount int[] with the values for the map.
     * @return a map with key-value pairs from the input float[] and int[].
     */
    public static Map<Float, Integer> bucketsAndBucketCountToMap(float[] buckets, int[] bucketCount){
    
        Map<Float, Integer> map = new HashMap<>();
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
    public static List<Entry<Float, Integer>> sortMap(Map<Float, Integer> map){
        List<Entry<Float, Integer>> sortedList = new ArrayList<>(map.entrySet());
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
    public static List<InlineResponse200Dto> returnTopXAsHex(List<Entry<Float, Integer>> list, int x){
        return list.stream().
                map(entry-> OkLabColor.okEntry2RGBHex(entry)).
                limit(x).
                collect(Collectors.toList());
    }

    /**
     * Convert an entry<Float, Integer> containing OKlab float value and number of pixels with that color into JSON object
     * containing the RGB hex value of the OKlab color  and the pixel value as percentage of the full picture.
     * @param okEntry input entry containing Oklab float key and an integer value of pixels with the color of the key.
     * @return a JSON object containing the String RGB hex value and a float with the percentage of pixels from the image with the given color. 
     */
    public static InlineResponse200Dto okEntry2RGBHex(Entry<Float, Integer> okEntry){ 
        String key = ColorConversion.convertOKlabToHex(okEntry.getKey());
        float value = okEntry.getValue();
        float percentage = value/pixelCount*100;

        InlineResponse200Dto response = new InlineResponse200Dto();

        response.hexRGB(key);
        response.percent(percentage);

        return response;
    }
    
}
