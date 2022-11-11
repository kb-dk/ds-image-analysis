package dk.kb.image;

import dk.kb.image.model.v1.DominantColorDto;

import java.util.*;

/**
 * Implements the OK Lab colorspace for the MostUsedColors template.
 * The OK Lab colorspace works with floats.
 */
public class MostUsedOkLabColor extends TemplateMostUsedColors<Float> {
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
    double calculateDistance(int pixel, Float bucket) {
        //Convert input floats to float arrays
        float pixelOKlab = ColorConversion.convertRGBtoOKlab(pixel);
        float[] pixelFloatArray = ColorConversion.convertOKlabFloatToFloatArray(pixelOKlab);
        float[] bucketFloatArray = ColorConversion.convertOKlabFloatToFloatArray(bucket);
        // Extract L, A and B values from input arrays.
        // Furthermore, converts floats to doubles to make precise calculation
        double L1 = pixelFloatArray[0];
        double a1 = pixelFloatArray[1];
        double b1 = pixelFloatArray[2];
        double L2 = bucketFloatArray[0];
        double a2 = bucketFloatArray[1];
        double b2 = bucketFloatArray[2];
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
