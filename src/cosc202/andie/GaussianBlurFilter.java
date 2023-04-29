package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply a Gaussian Blur filter
 * </p>
 * 
 * <p>
 * A Gaussian Blur filter blurs an image by using the 2-dimensional Gaussian 
 * formula to simulate a blurring caused by an out-of-focus camera lenses and 
 * other natural blurring effects. This can be implemented using the convolution
 * kernel.
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author Anthony Deng
 * @version 1.0
 */
public class GaussianBlurFilter implements ImageOperation, java.io.Serializable{

    /**
     * The size of the filter to apply. A radius of 1 is 3x3 etc.
     */
    private int radius;

    /**
     * <p>
     * Construct a Gaussian Blur filter with the given size
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used.
     * A larger radius gives a stronger blurring effect.
     * </p>
     * @param radius The radius of the newly constructed GaussianBlurFilter
     */
    GaussianBlurFilter(int radius){
        this.radius = radius;
    }

    /**
     * <p>
     * Construct a Gaussian Blur filter with the default size.
     * </p>
     * 
     * <p>
     * By default, a Gaussian Filter has radius 1.
     * </p>
     * 
     * @see GaussianBlurFilter(int)
     */
    GaussianBlurFilter(){
        this(1);
    }

    /**
     * <p>
     * Apply a Gaussian Blur filter to an image.
     * </p>
     * 
     * <p>
     * The Gaussian Blur filter is implemented via convolution.
     * The size of the convolution kernel used is specified by the {@link radius}.
     * A larger radius leads to a stronger blurring effect.
     * </p>
     * 
     * @param input The image to apply the Gaussian Blur filter to.
     * @return The resulting blurred image
     * @throws Exception Raised if an unexpected {@code Exception} occurs.
     */
    public BufferedImage apply(BufferedImage input) throws Exception {
        BufferedImage output = null;
        try{
            // if radius is 0 then return original image;
            if (radius == 0) {
                return input;
            }
            int size = (2*radius+1) * (2*radius+1);
            float[] array = new float[size];
            float sigma = ((float) radius) / 3; 
            float twoSigmaSq = 2 * sigma * sigma; 
            float sum = 0;
            int index = 0;

            for (int y = -radius; y <= radius; y++) {
                for (int x = -radius; x <= radius; x++) {
                    float value = (float) Math.exp(-(x * x + y * y) / twoSigmaSq);
                    value /= twoSigmaSq * Math.PI; 
                    array[index] = value;
                    sum += value;
                    index++;
                }
            }

            for (int i = 0; i < array.length; i++) {
                array[i] /= sum;
            }

            Kernel kernel = new Kernel(2*radius+1, 2*radius+1, array);
            AndieConvolveOp convOp = new AndieConvolveOp(kernel);
            output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
            convOp.filter(input, output);

        }catch(NullPointerException ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }
        return output;
    }
}
