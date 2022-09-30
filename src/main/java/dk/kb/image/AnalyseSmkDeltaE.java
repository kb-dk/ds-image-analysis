package dk.kb.image;

public class AnalyseSmkDeltaE {
    public static void AnalyseColorDistance(){
        float[] SmkColors = PalettePicker.smkOkLabBuckets();
        float[] SmkColorFloatArrayI;
        float[] SmkColorFloatArrayJ;

        for (int i = 0; i < SmkColors.length; i ++){
            System.out.println("Calculating Delta E for color: "+SmkColors[i]+" located at index: " + i);
            SmkColorFloatArrayI = ColorConversion.convertOKlabFloatToFloatArray(SmkColors[i]);

            for(int j = i+1; j < SmkColors.length; j++){
                SmkColorFloatArrayJ = ColorConversion.convertOKlabFloatToFloatArray(SmkColors[j]);
                double deltaE = OkLabColor.calculateDeltaE(SmkColorFloatArrayI, SmkColorFloatArrayJ);

                // TODO: If i = j break loop
                // Add delta e to output
                if (deltaE < 0.01) {
                    System.out.println("There is no perceptual difference between color " + i + " with OKlab Value: " + SmkColors[i] + " and color " + j + " with OKlab value: " + SmkColors[j]);
                //} else if ( deltaE < 0.02 ){
                //    System.out.println("There is almost no perceptual difference between color " + i + " with OKlab Value: " + SmkColors[i] + " and color " + j + " with OKlab value: " + SmkColors[j]);
                } else {}
                    // Then do if-else statements for the three conditions
                // TODO: Hvis Delta E er mindre end 1% skal programmet give en advarsel
                // TODO: Hvis delta E er mellem 1-2% skal programmet give en mindre advarsel
                // TODO: Hvis delta E er 2-10 skal der gives en meddelelse
            }
        }
    }
}
