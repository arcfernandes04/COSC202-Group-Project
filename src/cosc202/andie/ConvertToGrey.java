package cosc202.andie;

import java.awt.image.*;
import cosc202.andie.exceptions.*;

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
     * Create a new CovertToGrey operation.
     * </p>
     */
    ConvertToGrey() {

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
     * @throws NullFileException           If a file is not currently open.
     * @throws InvalidImageFormatException If the current image is in an
     * unrecognised format. Probably due to erroneous manipulation.
     * @throws Exception If an unexpected {@code Exception} occurs.
     */
    public BufferedImage apply(BufferedImage input) throws NullFileException, InvalidImageFormatException, Exception {
        try{
            for (int y = 0; y < input.getHeight(); ++y) {
                for (int x = 0; x < input.getWidth(); ++x) {
                    int argb = input.getRGB(x, y);
                    int a = (argb & 0xFF000000) >> 24;
                    int r = (argb & 0x00FF0000) >> 16;
                    int g = (argb & 0x0000FF00) >> 8;
                    int b = (argb & 0x000000FF);

                    int grey = (int) Math.round(0.3*r + 0.6*g + 0.1*b);

                    argb = (a << 24) | (grey << 16) | (grey << 8) | grey;
                    input.setRGB(x, y, argb);
                }
            }
        }catch(NullPointerException ex){
            throw new NullFileException(ex);
        }catch (java.awt.image.RasterFormatException ex) {
            throw new InvalidImageFormatException(ex);
        }
        
        return input;
    }
    
}
