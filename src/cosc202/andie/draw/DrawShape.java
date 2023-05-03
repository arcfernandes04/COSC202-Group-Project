package cosc202.andie.draw;

import cosc202.andie.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.*;

/**
 * <p>
 * A class which facilitates drawing shapes on images,
 * as well as allowing for extra options, such as choosing
 * colour, brush size, fill policy, etc.
 * </p>
 * 
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Joshua Carter
 * @version 1.0
 */
public class DrawShape implements ImageOperation, java.io.Serializable {

    /** The points representing the two corners of the shape. */
    private int x1, x2, y1, y2;
    /** The type of shape to draw. */
    private String shapeType;
    /** The way in which the shape should be filled (i.e. border only, inside only, or both) */
    private String fillType;
    /** The size of the brush */
    private int strokeSize;
    /** The primary colour */
    private Color primary;
    /** The secondary colour */
    private Color secondary;


    /**
     * <p>
     * Construct a new draw shape operation.
     * </p>
     * 
     * <p>
     * If {@code LINE} is the inputted {@code shapeType}, then the primary colour is always used.
     * Otherwise, the fill policy determines which part of the shape corresponds to primary and secondary;
     * i.e. {@code FILL_ONLY} and {@code BORDER_ONLY} use primary colour exclusively, and
     * {@code FILL_AND_BORDER} uses primary for the border and secondary for the inside of the shape.
     * </p>
     * 
     * @param shapeType The type of shape to be drawn
     * @param fillType The fill policy to use (e.g. only border, only inside, ...)
     * @param strokeSize The size of the brush
     * @param p1 The first point (top left corner for most shapes - needs to be the first
     * point that was selected temporally for lines)
     * @param p2 The second point to draw towards
     * @param primary The primary colour
     * @param secondary The secondary colour
     */
    DrawShape(String shapeType, String fillType, int strokeSize, Point p1, Point p2, Color primary, Color secondary) {
        this.shapeType = shapeType;
        this.fillType = fillType;
        this.strokeSize = strokeSize;

        this.x1 = (int) p1.getX();
        this.x2 = (int) p2.getX();
        this.y1 = (int) p1.getY();
        this.y2 = (int) p2.getY();

        this.primary = primary;
        this.secondary = secondary;
    }

    /**
     * <p>
     * Apply this filter to the specified {@code BufferedImage}
     * </p>
     * 
     * <p>
     * Uses the fields specified in the constructor to determine the shape,
     * fill policy, brush size, colours, and points of the shape.
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
            g.setColor(primary);

            if (shapeType.equals(DrawPanel.LINE)) { //Lines aren't the same as other shapes, since they are just a border effecively
                g.drawLine(x1, y1, x2, y2);
                g.dispose();
                return output;
            }
            
            if(fillType == DrawPanel.FILL_ONLY || fillType == DrawPanel.FILL_AND_BORDER){
                if (fillType == DrawPanel.FILL_AND_BORDER) g.setColor(secondary); //Only use secondary if we have both fill and border being drawn
                if (shapeType == DrawPanel.RECTANGLE) g.fillRect(x1, y1, x2 - x1, y2 - y1);
                else if (shapeType == DrawPanel.OVAL) g.fillOval(x1, y1, x2 - x1, y2 - y1);
            }

            if(fillType == DrawPanel.FILL_AND_BORDER || fillType == DrawPanel.BORDER_ONLY){
                g.setColor(primary);
                if (shapeType == DrawPanel.RECTANGLE) g.drawRect(x1, y1, x2 - x1, y2 - y1);
                else if (shapeType == DrawPanel.OVAL) g.drawOval(x1, y1, x2 - x1, y2 - y1);
            }

            g.dispose();
        }catch(NullPointerException ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }catch(java.awt.image.RasterFormatException ex){
            UserMessage.showWarning(UserMessage.INVALID_IMG_FILE_WARN);
        }
        return output;
    }


}
