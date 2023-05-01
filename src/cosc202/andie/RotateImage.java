package cosc202.andie;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

/**
 * <p>
 * ImageOperation to rotate an image.
 * </p>
 * 
 * <p>
 * Images will be rotated clockwise by the amount specified by the rotation datafield.
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

    private int rotation;

    /**
     * <p>
     * Create a new RotateImage operation.
     * </p>
     */
    RotateImage(int rotation) {
        this.rotation = rotation;
    }

    /**
     * <p>
     * Rotate the image
     * </p>
     * 
     * <p>
     * The image is rotated using AffineTransform.
     * </p>
     * 
     * @param input The image to be rotated
     * @return The resulting rotated image.
     */
    public BufferedImage apply(BufferedImage input) throws IllegalArgumentException, Exception {
        BufferedImage newImage = null;
        try{
            double rads = Math.toRadians(this. rotation);
            int width = (int) Math.floor(input.getWidth() * Math.abs(Math.cos(rads)) + input.getHeight() * Math.abs(Math.sin(rads)));
            int height = (int) Math.floor(input.getHeight() * Math.abs(Math.cos(rads)) + input.getWidth() * Math.abs(Math.sin(rads)));

            BufferedImage rotatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = rotatedImage.createGraphics();
            AffineTransform at = new AffineTransform();
            at.translate((width - input.getWidth()) / 2, (height - input.getHeight()) / 2);

            
            Color transparent = new Color(255, 0,0, 0);

            at.rotate(Math.toRadians(this.rotation), input.getWidth()/2, input.getHeight()/2);
            g.setTransform(at);
            g.drawImage(input, 0, 0, null);
            g.setColor(transparent);
            g.drawRect(0, 0, width - 1, height - 1);
            g.dispose();

            return rotatedImage;

        } catch(NullPointerException ex) {
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }
        return newImage;
    }

    public int getRotation() {
        return this.rotation;
    }
    
}