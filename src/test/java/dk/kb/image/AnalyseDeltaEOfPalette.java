package dk.kb.image;

import java.util.ArrayList;
import java.util.List;

public class AnalyseDeltaEOfPalette {
    /**
     * Analyse distance between colors from input String[] in OKlab colorspace.
     * The method calculates deltaE between all colors on the palette and checks if they perceptually look the same.
     * @param hexPalette the input string[] of hex colors to analyse.
     * @return a string[] with information on colors that perceptually looks the same.
     */
    public static List<String> AnalyseHexPaletteDeltaE(String[] hexPalette){
        List<Float> colorPalette = ColorConversion.convertHexArrayToOKlabArray(hexPalette);
        String[] deltaEArray = new String[hexPalette.length];

        for (int i = 0; i < colorPalette.size(); i ++){
            for(int j = i+1; j < colorPalette.size(); j++){
                int pixel = ColorConversion.convertOKlabToRgbInt(colorPalette.get(i));
                double deltaE = MostUsedOkLabColor.calculateDeltaE(pixel, colorPalette.get(j));

                // This articel has a table that makes it easy to understand the values of delta E: 
                // http://zschuessler.github.io/DeltaE/learn/ 
                // 100 in the article equals 1.00 in the code
                if (deltaE < 0.01) {
                    System.out.println("There is no perceptual difference between color " + i + " and color " + j + ". Delta E: " + deltaE);
                    deltaEArray[j] = "There is no perceptual difference between color " + i + " and color " + j + ". Delta E: " + deltaE;
                } else {}
            }   
        }

        if (deltaEArray[0] == null){
            deltaEArray[0] = "All colors are distinguisable.";
        }

        List<String> result = new ArrayList<String>();
        for(String data: deltaEArray) {
            if(data != null) { 
                result.add(data);
            }
        }
        return result;
    }
}
