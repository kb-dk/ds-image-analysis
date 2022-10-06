package dk.kb.image;

import java.awt.Color;
import java.util.Locale;

import com.github.tommyettinger.colorful.oklab.ColorTools;

public class ColorConversion {

    /**
     * Convert a String[] of hex colors into a float[] of OKlab colors.
     * @param inputPalette is a String[] of hex colors to be converted.
     * @return a float[] of colors defined in the OKlab colorspace.
     */
    public static float[] convertHexArrayToOKlabArray(String[] inputPalette){
        int length = inputPalette.length;
        int[] valuesRGB = new int[length];
        float[] valuesOKlab = new float[length]; 
        // Hex to RGB 
        for (int i = 0; i < inputPalette.length; i ++){
            Color color = Color.decode(inputPalette[i]);
            valuesRGB[i] = color.getRGB();
        }
        // RGB to OKlab
        for (int i = 0; i < inputPalette.length; i ++){
            float okLabFloat = ColorConversion.convertRGBtoOKlab(valuesRGB[i]);
            valuesOKlab[i] = okLabFloat;
        }
    
        return valuesOKlab;
    }

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
    public static int convertOKlabToRGB(float oklabFloat){
        // Get R, G and B values as integers
        int red = ColorTools.redInt(oklabFloat);
        int green = ColorTools.greenInt(oklabFloat);
        int blue = ColorTools.blueInt(oklabFloat);
        int rgb = (red << 16 | green << 8 | blue);
        return rgb;
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
    
}
