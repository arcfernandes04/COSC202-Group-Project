package cosc202.andie;

import java.awt.image.*;

import org.w3c.dom.stylesheets.StyleSheetList;

/**
 * <p>
 * ImageOperation to flip an image.
 * </p>
 * 
 * <p>
 * Images will be flipped either vertically or horizontally based on the direction data-field.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Corban Surtees
 * @version 1.0
 */
public class FlipImage implements ImageOperation, java.io.Serializable {

    private String direction;

    /**
     * <p>
     * Create a new FlipImage operation.
     * </p>
     */
    FlipImage(String direction) {
        this.direction = direction;
    }

    /**
     * <p>
     * Flip the image
     * </p>
     * 
     * <p>
     * The image is flipped by swapping pixels either vertically or horizontally.
     * </p>
     * 
     * @param input The image to be flipped
     * @return The resulting flipped image.
     */
    public BufferedImage apply(BufferedImage input) {
        if (direction.toLowerCase().equals("vertical")) {
            for (int y = 0; y < input.getHeight()/2; ++y) {
                for (int x = 0; x < input.getWidth(); ++x) {

                    //first pixel
                    int argb = input.getRGB(x, y);
                    int a = (argb & 0xFF000000) >> 24;
                    int r = (argb & 0x00FF0000) >> 16;
                    int g = (argb & 0x0000FF00) >> 8;
                    int b = (argb & 0x000000FF);

                    // opposite pixel
                    int yOpposite = input.getHeight() - (y + 1);
                    int argbOpposite = input.getRGB(x, yOpposite);
                    int aOpposite = (argbOpposite & 0xFF000000) >> 24;
                    int rOpposite = (argbOpposite & 0x00FF0000) >> 16;
                    int gOpposite = (argbOpposite & 0x0000FF00) >> 8;
                    int bOpposite = (argbOpposite & 0x000000FF);

                    argb = (aOpposite << 24) | (rOpposite << 16) | (gOpposite << 8) | bOpposite;
                    argbOpposite = (a << 24) | (r << 16) | (g << 8) | b;
                    input.setRGB(x, y, argb);
                    input.setRGB(x, yOpposite, argbOpposite);
                }
            }
        }
        else if (direction.toLowerCase().equals("horizontal")) {
            for (int y = 0; y < input.getHeight(); ++y) {
                for (int x = 0; x < input.getWidth()/2; ++x) {

                    // first pixel
                    int argb = input.getRGB(x, y);
                    int a = (argb & 0xFF000000) >> 24;
                    int r = (argb & 0x00FF0000) >> 16;
                    int g = (argb & 0x0000FF00) >> 8;
                    int b = (argb & 0x000000FF);

                    // opposite pixel
                    int xOpposite = input.getWidth() - (x + 1);
                    int argbOpposite = input.getRGB(xOpposite, y);
                    int aOpposite = (argbOpposite & 0xFF000000) >> 24;
                    int rOpposite = (argbOpposite & 0x00FF0000) >> 16;
                    int gOpposite = (argbOpposite & 0x0000FF00) >> 8;
                    int bOpposite = (argbOpposite & 0x000000FF);

                    argb = (aOpposite << 24) | (rOpposite << 16) | (gOpposite << 8) | bOpposite;
                    argbOpposite = (a << 24) | (r << 16) | (g << 8) | b;
                    input.setRGB(x, y, argb);
                    input.setRGB(xOpposite, y, argbOpposite);
                }
            }
        }
        else {
            throw new IllegalArgumentException("Direction provided in EditActions.java is invalid");
        }
        
        return input;
    }
    
}
