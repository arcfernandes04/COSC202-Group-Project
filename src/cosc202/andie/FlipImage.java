package cosc202.andie;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.*;


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
    private boolean opposite;

    /**
     * <p>
     * Create a new FlipImage operation.
     * </p>
     */
    FlipImage(String direction) {
        this.direction = direction;
    }

    FlipImage(String direction, boolean rotation) {
        this.direction = direction;
        this.opposite = opposite;
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
    public BufferedImage apply(BufferedImage input) throws Exception{
        try{
            if (direction.toLowerCase().equals("horizontal")) {
                if(opposite) {
                    input = flipVertical(input);
                } else {
                    input = flipHorizontal(input);
                }
            }
            else if (direction.toLowerCase().equals("vertical")) {
                if(opposite) {
                    input = flipHorizontal(input);
                } else {
                    input = flipVertical(input);
                }
            }
            else {
                throw new IllegalArgumentException("Direction provided in EditActions.java is invalid");
            }
        }catch(NullPointerException ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }
        
        return input;
    }


    /**
     * <p>
     * Flip the image horizontally
     * </p>
     * 
     * <p>
     * The image will be flipped horizontally by swapping pixels
     * </p>
     * 
     * @param input The image to be flipped
     * @return The resulting flipped image.
     */
    private BufferedImage flipHorizontal(BufferedImage input) throws Exception {
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
        return input;
    }

    /**
     * <p>
     * Flip the image vertically
     * </p>
     * 
     * <p>
     * The image will be flipped vertically by swapping pixels
     * </p>
     * 
     * @param input The image to be flipped
     * @return The resulting flipped image.
     */
    private BufferedImage flipVertical(BufferedImage input) throws Exception {
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
        return input;
    }

    public String getDirection() {
        return this.direction;
    }
    
}
