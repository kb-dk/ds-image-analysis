package dk.kb.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Locale;

import com.badlogic.gdx.utils.FloatArray;
import com.github.tommyettinger.colorful.oklab.ColorTools;
import com.github.tommyettinger.colorful.oklab.Palette;

public class OkLabColor {

    public static int[] countBucketsForImg2(BufferedImage img, float[] buckets){
        // Create bucket counter array
        int[] bucketCounter = new int[buckets.length];
        // get image's width and height
        int width = img.getWidth();
        int height = img.getHeight();
        // Loop over all pixels in image and get RGB color
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get RGB for each pixel
                int pixelRGB = img.getRGB(x, y);
                float pixelOKlab = OkLabColor.convertRGBtoOKlab(pixelRGB);
                OkLabColor.updateBucketCounter2(pixelOKlab, buckets, bucketCounter);
            }
        }
        return bucketCounter;
    }

    public static void updateBucketCounter2(float pixel, float[] buckets, int[] bucketCounter){
        // Values for checking max
        int bestColor = 0;
        double minDistance = 2147483647;
        for (int i = 0; i < buckets.length; i ++){
            float[] pixelFloatArray = OkLabColor.convertOKlabFloatToFloatArray(pixel);
            float[] bucketFloatArray = OkLabColor.convertOKlabFloatToFloatArray(buckets[i]);
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

    public static String printResult2(float[] buckets, int largestBucket){
        String hexColor = OkLabColor.convertOKlabToHex(buckets[largestBucket]);
        return hexColor;
    }

    /**
     * Convert input RGB integer to OKlab float.   
     * @param pixelRGB Input RGB as integer.
     * @return an OKlab float containing the color from inbut RGB pixel in the OKlab colorspace.
     */
    public static float convertRGBtoOKlab(int RGB){
        float[] RGBfloatArray = new float[4];
        Color conversion = new Color(RGB, true);
        RGBfloatArray = conversion.getComponents(RGBfloatArray);
        float oklabFloat = ColorTools.fromRGBA(RGBfloatArray[0],RGBfloatArray[1],RGBfloatArray[2],RGBfloatArray[3]);
        return oklabFloat;
    }

    /**
     * Convert OKlab float to RGB color represented as an integer.
     * @return the integer RGB value of the input OKlab float.
     */
    public static int convertOKlabToRGB(float oklabFloat){
        // Get R, G and B values as integers
        int red = ColorTools.redInt(oklabFloat);
        int green = ColorTools.greenInt(oklabFloat);
        int blue = ColorTools.blueInt(oklabFloat);
        int rgb = (red << 16 | green << 8 | blue);
        return rgb;
    }

    /**
     * Convert OKlab float to HEX color string.
     * @return the hexcolor representation of the input OKlab float.
     */
    public static String convertOKlabToHex(float oklabFloat){
        // Get R, G and B values as integers
        int red = ColorTools.redInt(oklabFloat);
        int green = ColorTools.greenInt(oklabFloat);
        int blue = ColorTools.blueInt(oklabFloat);
    
        String hex = String.format(Locale.ROOT, "#%02X%02X%02X", red, green, blue);
        return hex; 
    }

    /**
     * Convert OKlab float to float array containing L value at index 0, A value at index 1 and B value at index 2.
     * @return a float array containing L, A and B values from input OKlab float.
     */
    public static float[] convertOKlabFloatToFloatArray(float oklabFloat){
        float[] oklabFloatArray = new float[3];
        // Get L, A and B values from oklabFloat
        oklabFloatArray[0] = ColorTools.channelL(oklabFloat);
        oklabFloatArray[1] = ColorTools.channelA(oklabFloat);
        oklabFloatArray[2] = ColorTools.channelB(oklabFloat);
        return oklabFloatArray;
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

    public static float[] arbitraryOKlabBuckets(){
        FloatArray array = Palette.LIST;
        float[] buckets = array.toArray();
        return buckets;
    }
    
}
