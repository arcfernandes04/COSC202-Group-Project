package cosc202.andie;

import java.awt.image.*;

import cosc202.andie.exceptions.NullFileException;

/**
 * <p>
 * ImageOperation to adjust the brightness/contrast levels of an image.
 * </p>
 * 
 * <p> 
 * The image asjustments are done by taking the red, green, and blue values of each
 * pixel and using the brigtness and contrast percentage changes to calculate the 
 * new pixel value.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Abby Fernandes
 * @version 1.0
 */
public class BrightnessContrastAdjustment implements ImageOperation, java.io.Serializable {

    /**
     * Percentage of change for the brightness and contrast.
     */
    private int brightness;
    private int contrast;
    
    /**
     * Construct BrightnessContrastAdjustment with given values.
     * 
     * @param brightness The percentage of brightness change
     * @param contrast The percentage of contrast change
     */
    public BrightnessContrastAdjustment(int brightness, int contrast){
        this.brightness = brightness;
        this.contrast = contrast;
    }

    /**
     * <p> 
     * Construct BrightnessContrastAdjustment with default the values.
     * </p>
     * 
     * <p>
     * By default, has brightness and a contrast percentage changes of 0.
     * </p>
     */
    public BrightnessContrastAdjustment(){
        this(0,0);
    }


    /**
     * <p>
     * Apply brightness/contrast adjustments to an image.
     * </p>
     * 
     * <p>
     * The adjustments are applied to the red, gree, and blue values usig a 
     * simple model that uses the brightness and contrast percentage changes, 
     * and the current value of the pixel.
     * 
     * </p>
     * 
     * @param input The image to be adjusted
     * @return The resulting adjusted image.
     */
    public BufferedImage apply(BufferedImage input) throws NullFileException, Exception {
        try{
                for (int y = 0; y < input.getHeight(); ++y) {
                for (int x = 0; x < input.getWidth(); ++x) {
                    int argb = input.getRGB(x, y);
                    int a = (argb & 0xFF000000) >> 24;
                    int r = (argb & 0x00FF0000) >> 16;
                    int g = (argb & 0x0000FF00) >> 8;
                    int b = (argb & 0x000000FF);


                    r = (calculateAdjustment(r));
                    g = (calculateAdjustment(g));
                    b = (calculateAdjustment(b));

                    argb = (a << 24) | (r << 16) | (g << 8) | b;
                    input.setRGB(x, y, argb);
                }
            }
        }catch(NullPointerException ex){
            throw new NullFileException();
        }

        return input;
    }

    /**
     * <p>
     * Calculate the new value of the pixel after applying the brightness/contrast 
     * adjustments.
     * </p>
     * 
     * 
     * @param input The current value of the pixel
     * @return The adjusted value of the pixel <p> Note that, if adjusted value 
     *         falls outside the range 0 - 255, return the closer bound instead </p>
     */
    private int calculateAdjustment(int pixelValue){
        double brightnessConstant = (1 + (brightness / 100.0)) * 127.5;
        double contrastConstant = 1 + (contrast / 100.0);

        pixelValue = (int) Math.round(brightnessConstant + (contrastConstant * (((double) pixelValue) - 127.5)));

        if(pixelValue < 0) return 0;
        else if(pixelValue > 255) return 255;
        
        return pixelValue;
    }
}
