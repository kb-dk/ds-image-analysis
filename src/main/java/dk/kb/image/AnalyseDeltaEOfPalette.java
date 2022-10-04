package dk.kb.image;

public class AnalyseDeltaEOfPalette {
    // TODO: Write JavaDoc
    public static String[] AnalyseHexPaletteDeltaE(String[] hexPalette){
        float[] colorPalette = ColorConversion.convertHexToOKlab(hexPalette);
        float[] colorFloatArrayI;
        float[] colorFloatArrayJ;
        String[] result = new String[hexPalette.length];

        for (int i = 0; i < colorPalette.length; i ++){
            colorFloatArrayI = ColorConversion.convertOKlabFloatToFloatArray(colorPalette[i]);
            for(int j = i+1; j < colorPalette.length; j++){
                colorFloatArrayJ = ColorConversion.convertOKlabFloatToFloatArray(colorPalette[j]);
                double deltaE = OkLabColor.calculateDeltaE(colorFloatArrayI, colorFloatArrayJ);

                // This articel has a table that makes it easy to understand the values of delta E: 
                // http://zschuessler.github.io/DeltaE/learn/ 
                // 100 in the article equals 1.00 in the code
                if (deltaE < 0.01) {
                    System.out.println("There is no perceptual difference between color " + i + " and color " + j + ". Delta E: " + deltaE);
                    result[j] = "There is no perceptual difference between color " + i + " and color " + j + ". Delta E: " + deltaE;
                } else {}
            }   
        }

        if (result[0] == null){
            result[0] = "All colors are distinguisable.";
        }
        return result;
    }
}
