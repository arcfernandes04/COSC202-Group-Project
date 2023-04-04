package cosc202.andie;

import java.awt.Color;
import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to apply a Median filter.
 * </p>
 * 
 * <p>
 * A Median filter blurs an image by taking the median of a radius of pixels.
 * This is implemented by accessing each and every pixel needed to get the 
 * median value.
 */
public class MedianFilter implements ImageOperation, java.io.Serializable {

    /**
     * The size of the filter to apply. A radius of 1 is a 3x3 filter, a radius of 2 a 5x5 filter, and so forth.
     */
    private int radius;

    /**
     * <p>
     * Construct a Median filter with a given radius
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the pixels wanted to sort.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger median effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed MedianFilter
     */
    MedianFilter(int radius) {
        this.radius = radius;
    }

    /**
     * <p>
     * Construct a Median filter with the default size.
     * </p>
     * 
     * <p>
     * By default, a Median filter has radius 1.
     * </p>
     * 
     * @see MedianFilter(int)
     */
    MedianFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Median filter to an image.
     * </p>
     * 
     * <p>
     * The Median filter is implmented by finding the separate 
     * RBG and alpha channels and sorting these values inside
     * a given radius. 
     * A larger radius gives us a stronger blurring as pixels
     * are all set to the median value of the channels.
     * </p>
     * 
     * @param input The image to apply the Median filter to.
     * @return The resulting image.
     */
    public BufferedImage apply(BufferedImage input) throws Exception {
        BufferedImage output = null;
        try{
            int size = (2*radius+1) * (2*radius+1);
            Color[] pixel = new Color[size];
            int[] a = new int[size];
            int[] r = new int[size];
            int[] g = new int[size];
            int[] b = new int[size];
            int median = size/2;

            for (int x = 0; x < input.getWidth(); x++) {
                for (int y = 0; y < input.getHeight(); y++) {
                    int index = 0;
                    for (int i = x - radius; i <= x + radius && i < input.getWidth(); i++) {
                        for (int j = y - radius; j <= y + radius && j < input.getHeight(); j++) {
                            if(i >= 0 && j >= 0){ //Make sure it's in the frame
                                pixel[index] = new Color(input.getRGB(i, j), true);
                                a[index] = pixel[index].getAlpha();
                                r[index] = pixel[index].getRed();
                                g[index] = pixel[index].getGreen();
                                b[index] = pixel[index].getBlue();
                                index++;
                            }
                        }
                    }
                    Arrays.sort(a);
                    Arrays.sort(r);
                    Arrays.sort(g);
                    Arrays.sort(b);
                    input.setRGB(x, y, new Color(r[median], g[median], b[median], a[median]).getRGB());
                }
            }
            output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        }catch(NullPointerException ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }
        return output;
    }
}