package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply a Sobel filter to an image.
 * </p>
 * 
 * <p>
 * The Sobel filter detects the vertical or horizontal gradient of an image.
 * </p>
 * 
 * @author Anthony Deng
 * @author Abby Fernandes
 * @version 1.0
 */
public class SobelFilter implements ImageOperation, java.io.Serializable {
    public static final int NONE = 0, HORIZONTAL = 1, VERTICAL = 2;

    private int direction;

    private float[] horizontal = {-1/2, 0, 1/2, -1, 0, 1, -1/2, 0, 1/2};
    private float[] vertical = {-1/2, -1, -1/2, 0, 0, 0, 1/2, 1, 1/2};

    /**
     * <p>
     * Construct a new SobelFilter with a default direction of zero.
     * </p>
     */
    public SobelFilter(){
        this.direction = 0;
    }


    /**
     * <p>
     * Construct a new SobelFilter.
     * </p>
     * 
     * @param direction The direction of Sobel to apply. 
     */
    public SobelFilter(int direction){
        this.direction = direction;
    }

    /**
     * <p>
     * Apply a Sobel filter to an image.
     * </p>
     * 
     * <p>
     * The Sobel filter is implemented as a convolve operation.
     * There is no size to this filter as it is applied to the whole image.
     * </p>
     * 
     * @param input The image to apply the Sobel filter to filter to.
     * @return The image with the Sobel filter applied to it.
     * @throws Exception Raised if an unexpected {@code Exception} occurs.
     */
    public BufferedImage apply(BufferedImage input) throws Exception {
        BufferedImage output = null;
        float[] array;

        if(direction == 1) array = horizontal;
        else if(direction == 2) array = vertical;
        else return input;

        try {

            Kernel kernel = new Kernel(3, 3, array);
            AndieConvolveOp convOp = new AndieConvolveOp(kernel, true);
            output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
            convOp.filter(input, output);
        } catch (Exception ex) {
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }

        return output;
    }

    
}
