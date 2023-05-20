package cosc202.andie;

import java.awt.*;
import java.awt.image.*;


/**
 * <p>
 * ImageOperation to resize an image.
 * </p>
 * 
 * <p>
 * Images will be resized based on the sizePercentageIncrease variable.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Corban Surtees
 * @version 1.0
 */
public class ResizeImage implements ImageOperation, java.io.Serializable {

    private int sizePercentageIncrease;


    /**
     * <p>
     * Create a new ResizeImage operation.
     * </p>
     */
    ResizeImage(int sizePercentageIncrease) {
        this.sizePercentageIncrease = sizePercentageIncrease;
    }

    /**
     * <p>
     * Resize the original image
     * </p>
     * 
     * <p>
     * The image is resized by calling the image class method getScaledInstance.
     * </p>
     * 
     * <p>
     * <a href="https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage">Convert Image to BufferedImage</a>
     * </p>
     * 
     * @param input The image to be resized
     * @return The resulting resized image.
     */
    public BufferedImage apply(BufferedImage input) {
        BufferedImage resizedBufferedImage = null;

        try{
            Image resizedImage = input.getScaledInstance((int)(input.getWidth() * ((double)this.sizePercentageIncrease/100)), (int)(input.getHeight() * ((double)this.sizePercentageIncrease/100)), 0);
            resizedBufferedImage = new BufferedImage(resizedImage.getWidth(null), resizedImage.getHeight(null), 2);

            Graphics2D graphics = resizedBufferedImage.createGraphics();
            graphics.drawImage(resizedImage, 0, 0, null);
            graphics.dispose();
        } catch(Exception ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }

        return resizedBufferedImage;
    }

    public double getResizeScale() {
        return this.sizePercentageIncrease/100.0;
    }
    
}