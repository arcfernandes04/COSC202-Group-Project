package cosc202.andie;

import java.awt.image.*;

import org.w3c.dom.stylesheets.StyleSheetList;

/**
 * <p>
 * ImageOperation to rotate an image.
 * </p>
 * 
 * <p>
 * Images will be rotated either vertically or horizontally based on the rotation data-field.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Corban Surtees
 * @version 1.0
 */
public class RotateImage implements ImageOperation, java.io.Serializable {

    private String rotation;

    /**
     * <p>
     * Create a new RotateImage operation.
     * </p>
     */
    RotateImage(String rotation) {
        this.rotation = rotation;
    }

    /**
     * <p>
     * Rotate the image
     * </p>
     * 
     * <p>
     * The image is rotated by swapping pixels either vertically or horizontally.
     * </p>
     * 
     * @param input The image to be retated
     * @return The resulting rotated image.
     */
    public BufferedImage apply(BufferedImage input) {
        if (this.rotation.toLowerCase().equals("180")) {
            for (int y = 0; y < input.getHeight(); ++y) {
                for (int x = 0; x < input.getWidth()/2; ++x) {

                    //first pixel
                    int argb = input.getRGB(x, y);

                    // opposite pixel
                    int xOpposite = input.getWidth() - (x + 1);
                    int yOpposite = input.getHeight() - (y + 1);
                    int argbOpposite = input.getRGB(xOpposite, yOpposite);

                    //swap pixels
                    input.setRGB(x, y, argbOpposite);
                    input.setRGB(xOpposite, yOpposite, argb);
                }
            }
            if (input.getWidth()%2 != 0){
                int x = input.getWidth()/2;
                for (int y = 0; y < input.getHeight()/2; ++y){

                    //first pixel
                    int argb = input.getRGB(x, y);

                    //opposite pixel
                    int yOpposite = input.getHeight() - (y + 1);
                    int argbOpposite = input.getRGB(x, yOpposite);
                    
                    //swap pixels
                    input.setRGB(x, y, argbOpposite);
                    input.setRGB(x, yOpposite, argb);
                }
            }
            return input;
        }
        else if (this.rotation.toLowerCase().equals("90 right") || this.rotation.toLowerCase().equals("90 left")) {
            BufferedImage newImage = new BufferedImage(input.getHeight(), input.getWidth(), 2);
            for (int y = 0; y < input.getHeight(); ++y) {
                for (int x = 0; x < input.getWidth(); ++x) {

                    //top left
                    int argb = input.getRGB(x, y);

                    //swap pixels
                    if(this.rotation.toLowerCase().equals("90 right")){
                        newImage.setRGB((input.getHeight() - (y + 1)), x, argb);
                    }
                    else{
                        newImage.setRGB(y, (input.getWidth() - (x + 1)), argb);
                    }
                    
                }
            }
            return newImage;
        }

        else {
            throw new IllegalArgumentException("Rotation provided in EditActions.java is invalid");
        }
    }
    
}