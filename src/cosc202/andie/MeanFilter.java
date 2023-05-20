package cosc202.andie;

import java.awt.Point;
import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to apply a Mean (simple blur) filter.
 * </p>
 * 
 * <p>
 * A Mean filter blurs an image by replacing each pixel by the average of the
 * pixels in a surrounding neighbourhood, and can be implemented by a convoloution.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author Steven Mills
 * @version 1.0
 */
public class MeanFilter implements ImageOperation, java.io.Serializable {
    
    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2 a 5x5 filter, and so forth.
     */
    private int radius;

    /**
     * <p>
     * The coordinates of the corners of the selected area. If there is no selected area, these will be equal to -1.
     * </p>
     */
    private int x1, x2, y1, y2 = -1;


    /**
     * <p>
     * Construct a Mean filter with the given size.
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger blurring effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed MeanFilter
     */
    MeanFilter(int radius) {
        this.radius = radius;    
    }

    /**
     * <p>
     * Construct a Mean filter with the given size to be applied from p1 to p2
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger blurring effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed MeanFilter
     * @param p1 The point at the top corner of the selection
     * @param p2 The point at the bottom corner of the selection
     */
    MeanFilter(int radius, Point p1, Point p2) {
        this.radius = radius;   
        this.x1 = (int) p1.getX();
        this.x2 = (int) p2.getX();
        this.y1 = (int) p1.getY();
        this.y2 = (int) p2.getY(); 
    }

    /**
     * <p>
     * Construct a Mean filter with the default size.
     * </p
     * >
     * <p>
     * By default, a Mean filter has radius 1.
     * </p>
     * 
     * @see MeanFilter(int)
     */
    MeanFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Mean filter to an image.
     * </p>
     * 
     * <p>
     * As with many filters, the Mean filter is implemented via convolution.
     * The size of the convolution kernel is specified by the {@link radius}.
     * Larger radii lead to stronger blurring.
     * </p>
     * 
     * @param input The image to apply the Mean filter to.
     * @return The resulting (blurred)) image.
     * @throws Exception Raised if an unexpected {@code Exception} occurs.
     * 
     */
    public BufferedImage apply(BufferedImage input) throws Exception {
        BufferedImage output = null;
        try{
            int size = (2*radius+1) * (2*radius+1);
            float [] array = new float[size];
            Arrays.fill(array, 1.0f/size);

            Kernel kernel = new Kernel(2*radius+1, 2*radius+1, array);
            AndieConvolveOp convOp = new AndieConvolveOp(kernel);
            output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
            if (x1 != -1 && x2 != -1 && y1 != -1 && y2 != -1) convOp.filter(input, output, x1, y1, x2, y2);
            else convOp.filter(input, output);

        }catch(NullPointerException ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }catch(java.awt.image.RasterFormatException ex){
            UserMessage.showWarning(UserMessage.INVALID_IMG_FILE_WARN);
        }
        return output;
    }


}
