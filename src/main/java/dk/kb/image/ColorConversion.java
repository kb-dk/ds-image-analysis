package dk.kb.image;

import java.awt.Color;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tommyettinger.colorful.oklab.ColorTools;

public class ColorConversion {

    /**
     * Convert a String[] of hex colors into a float[] of OKlab colors.
     * @param inputPalette is a String[] of hex colors to be converted.
     * @return a float[] of colors defined in the OKlab colorspace.
     */
    public static List<Float> convertHexArrayToOKlabArray(String[] inputPalette){
        int[] valuesRGB = new int[inputPalette.length];
        List<Float> valuesOKlab = new ArrayList<Float>(inputPalette.length);
        // Hex to RGB 
        for (int i = 0; i < inputPalette.length; i ++){
            Color color = Color.decode(inputPalette[i]);
            valuesRGB[i] = color.getRGB();
        }
        // RGB to OKlab
        for (int i = 0; i < inputPalette.length; i ++){
            float okLabFloat = ColorConversion.convertRGBtoOKlab(valuesRGB[i]);
            valuesOKlab.add(i, okLabFloat);
        }
    
        return valuesOKlab;
    }

    /**
     * Convert an RGB hex string to a float value in the OKLab colorspace.
     * @param hex input RGB hex value.
     * @return OKlab float value of the input RGB hex color.
     */
    public static float convertSingleHexToOklab(String hex){
        Color color = Color.decode(hex);
        int rgbValue = color.getRGB();
        float oklabValue = ColorConversion.convertRGBtoOKlab(rgbValue);
        return oklabValue;
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
     * Convert OKlab float to RGB color represented as an integer.
     * @return the integer RGB value of the input OKlab float.
     */
    public static int convertOKlabToRgbInt(float oklabFloat){
        // Get R, G and B values as integers
        int red = ColorTools.redInt(oklabFloat);
        int green = ColorTools.greenInt(oklabFloat);
        int blue = ColorTools.blueInt(oklabFloat);
        int rgb = (red << 16 | green << 8 | blue);
        return rgb;
    }

    /**
     * Convert input RGB integer to OKlab float.   
     * @param RGB Input RGB as integer.
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
     * Calculates which color from the chosen palette each color from the RGB color space is closest to.
     * Saves the calculated entries as raw bytes.
     */
    public static void rgbColorsToBuckets() throws IOException {
        MostUsedOkLabColor myColor = new MostUsedOkLabColor();
        Color x;
        List<Float> buckets = PalettePicker.smkOkLabBuckets();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for (int r = 0; r < 256; r++) {
            for (int g = 0; g < 256; g++) {
                for (int b = 0; b < 256; b++) {
                    x = new Color(r, g, b);

                    int colorInt = x.getRGB();
                    int bestColor = 0;
                    double minDistance = Double.MAX_VALUE;
                    for (int i = 0; i < buckets.size(); i++) {
                        double totalDistance = myColor.calculateDistance(colorInt, buckets.get(i));
                        // Evaluates total distance against minimum distance for given bucket
                        if (totalDistance < minDistance) {
                            minDistance = totalDistance;
                            bestColor = i;
                        }
                    }
                    byte bucketByte = (byte) bestColor;
                    out.write(bucketByte);
                }
            }
        }
        try (OutputStream outputStream = new FileOutputStream("/home/victor/Documents/OklabBucketEntriesForAllRgbColors")) {
            out.writeTo(outputStream);
        }
    }
}
