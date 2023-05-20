package cosc202.andie;

import java.awt.Point;
import java.awt.image.*;

/**
 * <p>
 * ImageOperation to convert an image from colour to greyscale.
 * </p>
 * 
 * <p>
 * The images produced by this operation are still technically colour images,
 * in that they have red, green, and blue values, but each pixel has equal
 * values for red, green, and blue giving a shade of grey.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class ConvertToGrey implements ImageOperation, java.io.Serializable {
    /**
     * <p>
     * The coordinates of the corners of the selected area. If there is no selected area, these will be equal to zero.
     * </p>
     */
    private int x1, x2, y1, y2 = -1;

    /**
     * <p>
     * Create a new CovertToGrey operation.
     * </p>
     */
    ConvertToGrey() {}

    /**
     * Create a new ConvertToGrey operation to be applied from p1 to p2
     * 
     * @param p1 The point at the top corner of the selection
     * @param p2 The point at the bottom corner of the selection
     */
    ConvertToGrey(Point p1, Point p2){
        this.x1 = (int) p1.getX();
        this.x2 = (int) p2.getX();
        this.y1 = (int) p1.getY();
        this.y2 = (int) p2.getY();
    }

    /**
     * <p>
     * Apply greyscale conversion to an image.
     * </p>
     * 
     * <p>
     * The conversion from red, green, and blue values to greyscale uses a
     * weighted average that reflects the human visual system's sensitivity
     * to different wavelengths -- we are most sensitive to green light and
     * least to blue.
     * </p>
     * 
     * @param input The image to be converted to greyscale
     * @return The resulting greyscale image.
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

                    if(x1 != -1 && x2 != -1 && y1 != -1 && y2 != -1){ // i.e. there is a selected area
                        if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
                            int grey = (int) Math.round(0.3*r + 0.6*g + 0.1*b);
                            argb = (a << 24) | (grey << 16) | (grey << 8) | grey;
                        }

                    }else{ // i.e. there is no selected area
                        int grey = (int) Math.round(0.3*r + 0.6*g + 0.1*b);
                        argb = (a << 24) | (grey << 16) | (grey << 8) | grey;
                    }

                    input.setRGB(x, y, argb);
                }
            }
        }catch(NullPointerException ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }catch (java.awt.image.RasterFormatException ex) {
            UserMessage.showWarning(UserMessage.INVALID_IMG_FILE_WARN);
        }
        
        return input;
    }
    
}
