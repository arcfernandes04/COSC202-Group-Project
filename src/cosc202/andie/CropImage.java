package cosc202.andie;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.*;


/**
 * <p>
 * ImageOperation to crop an image.
 * </p>
 * 
 * <p>
 * Images will be cropped to size of selected area
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Corban Surtees
 * @version 1.0
 */
public class CropImage implements ImageOperation, java.io.Serializable {

    private int x1, x2, y1, y2;

    /**
     * <p>
     * Create a new FlipImage operation.
     * </p>
     */
    CropImage(Point p1, Point p2) {
        this.x1 = (int) p1.getX();
        this.y1 = (int) p1.getY();
        this.x2 = (int) p2.getX();
        this.y2 = (int) p2.getY();
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
        BufferedImage newImage = input;
        BufferedImage img = input;
        try{
            int width = this.x2 - this.x1;
            int height = this.y2 - this.y1;
            if (width == 0 || height == 0) {
                UserMessage.showWarning(UserMessage.EMPTY_SELECTION_WARN);
                return input;
            } 
            img = input.getSubimage(x1, y1, width, height);
            newImage = new BufferedImage(width, height, 2);
        
            Graphics g = newImage.createGraphics();
            g.drawImage(img, 0, 0, null);
        }catch(NullPointerException ex){
            UserMessage.showWarning(UserMessage.EMPTY_SELECTION_WARN);
        }
        return newImage;
    }
    
}
