package cosc202.andie.draw;

import cosc202.andie.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.*;
import java.util.ArrayList;

/**
 * 
 * <p>
 * A class which facilitates drawing curved lines on images,
 * as well as allowing for control of the brush size and line colour.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Joshua Carter
 * @version 1.0
 */
public class DrawBrush implements ImageOperation, java.io.Serializable {

    /** The array of points to visit when drawing the curved line. */
    private int[] x, y;
    /** The brush size. */
    private int strokeSize;
    /** The colour of the line. */
    private Color fill;

    /**
     * <p>
     * Construct a new draw brush operation.
     * </p>
     * 
     * 
     * @param strokeSize The brush size of the line.
     * @param points The array list of points to draw the line over.
     * @param fill The colour of the line.
     */
    DrawBrush(int strokeSize, ArrayList<Point> points, Color fill) {
        this.strokeSize = strokeSize;

        this.x = new int[points.size()];
        this.y = new int[points.size()];
        for(int i = 0; i < points.size(); i++){
            Point p = points.get(i);
            x[i] = (int) p.getX();
            y[i] = (int) p.getY();
        }
        this.fill = fill;
    }

    /**
     * <p>
     * Apply this filter to the specified {@code BufferedImage}
     * </p>
     * 
     * <p>
     * Uses the fields specified in the constructor to determine the
     * points to draw over, the colour, and brush size.
     * </p>
     * 
     * @param input The image to draw to.
     */
    public BufferedImage apply(BufferedImage input) throws Exception {
        BufferedImage output = null;
        try{
            output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
            Graphics2D g = output.createGraphics();

            g.setStroke(new BasicStroke(strokeSize));
            g.setColor(fill);

            if(x.length <= 1){
                x = new int[]{x[0] - strokeSize/2, x[0] + strokeSize/2};
                y = new int[]{y[0] - strokeSize/2, y[0] + strokeSize/2};
            }
            g.drawPolyline(x, y, x.length);

            g.dispose();
        }catch(NullPointerException ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }catch(java.awt.image.RasterFormatException ex){
            UserMessage.showWarning(UserMessage.INVALID_IMG_FILE_WARN);
        }
        return output;
    }


}
