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
     * The palette contains 256 colorsm chosen for game development.
     * @return a primitive float array with 256 colors in the OKlab colorspace
     */
    public static float[] arbitraryOKlabBuckets(){
        FloatArray array = Palette.LIST;
        float[] buckets = array.toArray();
        return buckets;
    }
    
    /* TODO: Define which color-palette to use
     * Something about a web-safe 256 color palette?
     * SMK - How have they created their color palette?
     * DawnBringer Aurora palette is used in the colorful.oklab.palette and is primarily used for game design.
     */
}
