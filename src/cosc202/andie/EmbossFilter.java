package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply an Emboss effect to an image.
 * </p>
 * 
 * <p>
 * The Embossed effect of this filter gives a raised effect on the
 * specified side of the image.
 * </p>
 * 
 * @author Anthony Deng
 * @author Abby Fernandes
 * @version 1.0
 */
public class EmbossFilter implements ImageOperation, java.io.Serializable {
    public static final int NONE = 0, EAST = 1, NORTH_EAST = 2, NORTH = 3, NORTH_WEST = 4, WEST = 5, SOUTH_EAST = 6, SOUTH = 7, SOUTH_WEST = 8;

    private int direction;

    private  float[] east = {0, 0, 0, -1, 0, 1, 0, 0, 0};
    private  float[] northEast = {0, 0, 1, 0, 0, 0, -1, 0, 0};
    private  float[] north = {0, 1, 0, 0, 0, 0, 0, -1, 0};;
    private  float[] northWest = {1, 0, 0, 0, 0, 0, 0, 0, -1};
    private  float[] west = {0, 0, 0, 1, 0, -1, 0, 0, 0};
    private  float[] southEast = {-1, 0, 0, 0, 0, 0, 0, 0, 1};
    private  float[] south = {0, -1, 0, 0, 0, 0, 0, 1, 0};
    private  float[] southWest = {0, 0, -1, 0, 0, 0, 1, 0, 0};
    
    /**
     * <p>
     * Construct a new EmbossFilter with a default direction of zero.
     * </p>
     */
    EmbossFilter(){
        this.direction = 0;
    }

    /**
     * <p>
     * Construct a new EmbossFilter.
     * </p>
     * 
     * @param direction The direction to emboss. 
     */
    EmbossFilter(int direction){
        this.direction = direction;
    }

    /**
     * <p>
     * Apply the Emboss effect to an image.
     * </p>
     * 
     * <p>
     * The Emboss filter is implemented as a convolve operation.
     * There is no size to this filter as it is applied to the whole
     * image.
     * </p>
     * 
     * @param image The image to apply the Emboss effect to.
     * @return The image with the Emboss effect applied.
     * @throws Exception Raised if an unexpected {@code Exception} occurs.
     */
    public BufferedImage apply(BufferedImage input) throws Exception {
        BufferedImage output = null;
        float[] array;

        if(direction == 1) array = east;
        else if(direction == 2) array = northEast;
        else if(direction == 3) array = north;
        else if(direction == 4) array = northWest;
        else if(direction == 5) array = west;
        else if(direction == 6) array = southEast;
        else if(direction == 7) array = south;
        else if(direction == 8) array = southWest;
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