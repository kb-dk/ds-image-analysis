package dk.kb.image;

// TODO: Maybe change name of class, so that it could be used as a general analysis tool
// Something like: DeltaECalculator
// Then expose it in the API as a place where people can check their color palettes if needed?
public class AnalyseSmkDeltaE {
    public static void AnalyseColorDistance(){
        float[] SmkColors = PalettePicker.smkOkLabBuckets();
        float[] SmkColorFloatArrayI;
        float[] SmkColorFloatArrayJ;

        for (int i = 0; i < SmkColors.length; i ++){
            SmkColorFloatArrayI = ColorConversion.convertOKlabFloatToFloatArray(SmkColors[i]);
            for(int j = i+1; j < SmkColors.length; j++){
                SmkColorFloatArrayJ = ColorConversion.convertOKlabFloatToFloatArray(SmkColors[j]);
                double deltaE = OkLabColor.calculateDeltaE(SmkColorFloatArrayI, SmkColorFloatArrayJ);

                // This articel has a table that makes it easy to understand the values of delta E: 
                // http://zschuessler.github.io/DeltaE/learn/ 
                // 100 in the article equals 1.00 in the code
                if (deltaE < 0.01) {
                    System.out.println("There is no perceptual difference between color " + i + " and color " + j + ". Delta E: " + deltaE);
                } else {}
            }
        }
    }
}
