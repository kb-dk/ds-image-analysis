package dk.kb.image;

import java.awt.Color;

import com.badlogic.gdx.utils.FloatArray;
import com.github.tommyettinger.colorful.oklab.Palette;

public class PalettePicker {

    /**
     * Defines simple color buckets. 
     * Used in getMostUsedRGBColor() to extract primary color.
     * @return an integer array of RGB colors.
     */
    public static int[] defineSimpleBuckets(){
         // Define colors as integers in array
        return new int[]{
            Color.RED.getRGB(),
            Color.GREEN.getRGB(),
            Color.BLUE.getRGB(),
            Color.YELLOW.getRGB(),
            Color.CYAN.getRGB(),
            Color.MAGENTA.getRGB()
            };
    }

    /**
     * Create a float array of 256 colors in the OKlab colorspace
     * This color palette is called the Aurora Palette and has originally been created by DawnBringer. 
     * The palette contains 256 colors chosen for game development.
     * @return a primitive float array with 256 colors in the OKlab colorspace
     */
    public static float[] arbitraryOKlabBuckets(){
        FloatArray array = Palette.LIST;
        float[] buckets = array.toArray();
        return buckets;
    }
    
    /**
     * Convert a string[] of hex colors to a float[] of colors in the OKlab Colorspace.
     * @return a float[] of OKlab colors.
     */
    public static float[] smkOkLabBuckets(){
        float[] smkOkLabBuckets = ColorConversion.convertHexArrayToOKlabArray(smkHexPalette());
        return smkOkLabBuckets;
    }

    public static int[] smkRgbBuckets(){
        int[] smgRgbBuckets = new int[PalettePicker.smkHexPalette().length];
        String[] hexPalette = PalettePicker.smkHexPalette();
        for (int i=0; i<smgRgbBuckets.length; i++){
            Color color = Color.decode(hexPalette[i]);
            smgRgbBuckets[i] = color.getRGB();
        }

        return smgRgbBuckets;
    }
    /**
     * Create a string[] of 256 hex colors used by SMK (National Gallery of Denmark).
     * It is not clear how this palette has been constructed in the first place.
     * @return a string[] of 256 hex values.
     */
    public static String[] smkHexPalette(){
        // 256 color palette from SMK Open (National Gallery of Denmark).
        // Used in their image discovery at: https://open.smk.dk/art/advanced/colors?q=*&page=0 
        // It is unclear how they have come to use this exact colorpalette 
        return new String[]{
            "#FDDCE5", "#FADCEC", "#F6DAED", "#EEDCED", "#E4DAEE", "#DFE1F1", "#DFEEFA", "#E1F5FA",
            "#E1F3F0", "#E2F1DE", "#EAF3D9", "#F8F9DC", "#FEFCDF", "#FFF1DC", "#FEE1DC", "#FCD4D7",
            "#FAC7D2", "#F5C7DA", "#EDC7DC", "#DFC7DF", "#D0C4E3", "#CBCBE7", "#C9E1F6", "#CDECF5",
            "#CEEAE5", "#CEE6C9", "#D7EAC3", "#F0F3C7", "#FCF6C9", "#FFE7C9", "#FDCEC7", "#F9B8BE",
            "#F5A3B6", "#E7A6C1", "#D3A7C4", "#BFA5C4", "#ADA3C7", "#A7AAD0", "#A9C9ED", "#AED7EA",
            "#AFD6D0", "#ADD1AA", "#BED69F", "#DFE8A4", "#FCF1A6", "#FFCFA5", "#F9ACA5", "#F397A0",
            "#EE839B", "#D689AE", "#BF8BB2", "#A387B2", "#8A82B2", "#848BBA", "#8AB7E1", "#92CDE1",
            "#95C9BE", "#95C18D", "#A9C97F", "#D0DA83", "#F9EA87", "#FEBD84", "#F38D84", "#EE7B82", "#EA6A81",
            "#CE709E", "#B176A2", "#8C6EA1", "#716DA1", "#6B73A7", "#73A1D2", "#7FC1D9", "#83BFB4", "#83BA72",
            "#9ABE5E", "#C3D268", "#F8E36C", "#FCAA6B", "#EE796B", "#EB626D", "#E84B6E", "#CB6092", "#A5689B",
            "#81659B", "#67669D", "#596A9F", "#638FC3", "#73BDD5", "#7DBCA9", "#7CB45E", "#8ABA49", "#BCCD40",
            "#F6DA46", "#F99C47", "#EC6449", "#E94E56", "#E73863", "#CB4E8D", "#A26095", "#7E6199", "#5D629B", 
            "#4B669E", "#5486BD", "#6CBDD4",
            "#75BAA1", "#73B15A", "#80B74C", "#B4C93D", "#F6D727", "#F68E2B", "#E84B33", "#E73843", "#E72653",
            "#CD498C", "#A05E92", "#7B5F98", "#53609A", "#46639C", "#4E80B8", "#6ABBD3", "#71B99E", "#6FB058",
            "#7EB44D", "#ADC43F", "#F1D41C", "#F18828", "#E7402D", "#DE323C", "#D6254B", "#C24188", "#9D5990",
            "#765A95", "#4E5D98", "#445E9A", "#437EB1", "#55B4C7", "#69B498", "#68AC59", "#70AF50", "#9FC043",
            "#D5C427", "#D9802D", "#D73B2E", "#C83239", "#BA2944", "#B13381", "#914C8E", "#6F4D8F", "#494E8F",
            "#405191", "#3071A6", "#29A5B4", "#44AB8C", "#48A85B", "#61AA54", "#8BB449", "#B7AC3E", "#B97438",
            "#B93530", "#A72D36", "#95253C", "#912A76", "#863C89", "#693E88", "#403F85", "#3A4286", "#19668F",
            "#008C92", "#00957D", "#0C9556", "#429553", "#77974B", "#908D41", "#956830", "#952B29", "#87212A",
            "#79172A", "#792464", "#6E2E76", "#513074", "#382E71", "#303870", "#214E77", "#017079", "#007A67",
            "#107B48", "#3B7B45", "#657B42", "#77733D", "#795025", "#77221D", "#651C21", "#541724", "#591044",
            "#511F59", "#3C1D59", "#231D57", "#212257", "#0E3B59", "#01535A", "#015C45", "#205C31", "#335C2D",
            "#475C2B", "#5A5527", "#593D1C", "#541F0E", "#431716", "#33101D", "#331728", "#2E1435", "#241033",
            "#1C1733", "#101933", "#082335", "#023035", "#033829", "#1A3921", "#233A21", "#2C3B20", "#3A351F",
            "#382510", "#33140C", "#230B09", "#140306", "#14040C", "#140817", "#0C0614", "#080614", "#060814",
            "#040A14", "#031014", "#03140E", "#081A0C", "#0E1C0A", "#141C0A", "#191909", "#170E06", "#140604",
            "#140604", "#FFFFFF", "#EEEEEE", "#DDDDDD", "#CCCCCC", "#BBBBBB", "#AAAAAA", "#999999", "#888888",
            "#777777", "#666666", "#555555", "#444444", "#333333", "#222222", "#111111", "#000000",
        };
    }
}
