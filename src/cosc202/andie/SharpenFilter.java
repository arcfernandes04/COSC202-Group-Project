package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply a Sharpen filter.
 * </p>
 * 
 * <p>
 * A Sharpen filter enchances the differences between neighbouring values, 
 * and can be implemented by convolution with the kernel.
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author Anthony Deng
 * @version 1.0
 */
public class SharpenFilter implements ImageOperation, java.io.Serializable {

    /**
     * <p>
     * Construct a Median filter
     * </p>
     */
    SharpenFilter(){
    }

    /**
     * <p>
     * Apply a Median filter to an image
     * </p>
     * 
     * <p>
     * The Sharpen filter is implemented via convolution. There is no size
     * to the filter as it is applied to the whole image. 
     * </p>
     * 
     * @param input The image to apply the Sharpen filter to.
     * @return The resulting (sharpened) image.
     * @throws Exception Raised if an unexpected {@code Exception} occurs.
     */
    public BufferedImage apply(BufferedImage input) throws Exception {
        BufferedImage output = null;
        try{
            float[] array = {0, -1/2.0f, 0, -1/2.0f, 3, -1/2.0f, 0, -1/2.0f, 0};

            Kernel kernel = new Kernel(3, 3, array);
            AndieConvolveOp convOp = new AndieConvolveOp(kernel);
            output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
            convOp.filter(input, output);
        }catch(Exception ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }
        return output;
    }
}
