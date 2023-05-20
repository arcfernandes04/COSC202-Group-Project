package cosc202.andie;

import java.awt.Point;
import java.awt.image.*;

/**
 * <p>
 * ImageOperation to adjust the brightness/contrast levels of an image.
 * </p>
 * 
 * <p> 
 * The image adjustments are done by taking the red, green, and blue values of each
 * pixel and using the brightness and contrast percentage changes to calculate the 
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
     * <p>
     * The coordinates of the corners of the selected area. If there is no selected area, these will be equal to -1.
     * </p>
     */
    private int x1, x2, y1, y2 = -1;
    
    /**
     * Construct BrightnessContrastAdjustment with given values.
     * 
     * @param brightness The percentage of brightness change (0 - 100 inclusive)
     * @param contrast The percentage of contrast change (0 - 100 inclusive)
     */
    public BrightnessContrastAdjustment(int brightness, int contrast){
        this.brightness = brightness;
        this.contrast = contrast;
    }

    /**
     * Construct BrightnessContrastAdjustment with given values to be applied from p1 to p2.
     * 
     * @param brightness The percentage of brightness change (0 - 100 inclusive)
     * @param contrast The percentage of contrast change (0 - 100 inclusive)
     * @param p1 The point at the top corner of the selection
     * @param p2 The point at the bottom corner of the selection
     */
    public BrightnessContrastAdjustment(int brightness, int contrast, Point p1, Point p2) {
        this.brightness = brightness;
        this.contrast = contrast;
        this.x1 = (int) p1.getX();
        this.x2 = (int) p2.getX();
        this.y1 = (int) p1.getY();
        this.y2 = (int) p2.getY();
    }

    /**
     * <p> 
     * Construct BrightnessContrastAdjustment with the default values.
     * </p>
     * 
     * <p>
     * By default, brightness and contrast percentage changes are 0.
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
     * The adjustments are applied to the red, green, and blue values using a 
     * simple model that uses the brightness and contrast percentage changes, 
     * and the current value of the pixel.
     * 
     * </p>
     * 
     * @param input The image to be adjusted
     * @return The resulting adjusted image.
     */
    public BufferedImage apply(BufferedImage input) {
        try{
            for (int y = 0; y < input.getHeight(); ++y) {
                for (int x = 0; x < input.getWidth(); ++x) {
                    int argb = input.getRGB(x, y);
                    int a = (argb & 0xFF000000) >> 24;
                    int r = (argb & 0x00FF0000) >> 16;
                    int g = (argb & 0x0000FF00) >> 8;
                    int b = (argb & 0x000000FF);
                    if (x1 != -1 && x2 != -1 && y1 != -1 && y2 != -1) { // i.e. there is a selected area
                        if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
                            r = (calculateAdjustment(r));
                            g = (calculateAdjustment(g));
                            b = (calculateAdjustment(b));
                        }
                    }
                    else { // i.e. there is no selected area
                        r = (calculateAdjustment(r));
                        g = (calculateAdjustment(g));
                        b = (calculateAdjustment(b));
                    }

                    argb = (a << 24) | (r << 16) | (g << 8) | b;
                    input.setRGB(x, y, argb);
                }
            }
        }catch(NullPointerException ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
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
     * @return The adjusted value of the pixel <p> Note: if adjusted value 
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
