package cosc202.andie;

import java.awt.image.*;

import cosc202.andie.exceptions.NullFileException;

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
    public BufferedImage apply(BufferedImage input) throws NullFileException, Exception {
        try{
            if (direction.toLowerCase().equals("vertical")) {
                for (int y = 0; y < input.getHeight()/2; ++y) {
                    for (int x = 0; x < input.getWidth(); ++x) {

                        //first pixel
                        int argb = input.getRGB(x, y);

                        // opposite pixel
                        int yOpposite = input.getHeight() - (y + 1);
                        int argbOpposite = input.getRGB(x, yOpposite);

                        //swap pixels
                        input.setRGB(x, y, argbOpposite);
                        input.setRGB(x, yOpposite, argb);
                    }
                }
            }
            else if (direction.toLowerCase().equals("horizontal")) {
                for (int y = 0; y < input.getHeight(); ++y) {
                    for (int x = 0; x < input.getWidth()/2; ++x) {

                        // first pixel
                        int argb = input.getRGB(x, y);

                        // opposite pixel
                        int xOpposite = input.getWidth() - (x + 1);
                        int argbOpposite = input.getRGB(xOpposite, y);

                        //swap pixels
                        input.setRGB(x, y, argbOpposite);
                        input.setRGB(xOpposite, y, argb);
                    }
                }
            }
            else {
                throw new IllegalArgumentException("Direction provided in EditActions.java is invalid");
            }
        }catch(NullPointerException ex){
            throw new NullFileException(ex);
        }
        
        return input;
    }
    
}
