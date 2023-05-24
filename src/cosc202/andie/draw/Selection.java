package cosc202.andie.draw;

import cosc202.andie.*;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * <p>
 * Allows selection of the current open image, enabling the functionality of crop,
 * brush, and shapes tools.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Corban Surtees
 */
public class Selection {

    /** A marker object used to indicate an unset field. */
    private static final Point EMPTY_POINT = new Point(-2, -2);
    /** The {@code ImagePanel} that this selection refers to. */
    private ImagePanel target;
    /** The corner in the selection that was first to be recorded. */
    private Point p1;
    /** The corner in the selection that was most recently recorded. */
    private Point p2;
    /** Whether or not the selection has been dragged after initially clicking. */
    private boolean beenDragged;
    /** Whether or not mouse selection is currently happening. */
    private boolean mouseSelectionOn;
    /** If the {@code BRUSH} tool is enabled, stores all of the points 
     * visited during the current selection so that they can be drawn together as one line. */
    private ArrayList<Point> points;

    /** 
     * Instantiate a new, empty selection.
     * 
     * @param target The ImagePanel this instance of {@code Selection} should refer to.
     */
    public Selection(ImagePanel target){
        this.target = target;
        this.p1 = EMPTY_POINT;
        this.p2 = EMPTY_POINT;
        this.beenDragged = false;
        this.mouseSelectionOn = false;
        points = new ArrayList<>();
    }

    /**
     * Reset the mouse selection, as well as any previewed operations
     * on the current {@code EditableImage}.
     */
    public void reset() {
        mouseSelectionOn = false;
        p1 = EMPTY_POINT;
        p2 = EMPTY_POINT;
        points.clear();
        target.getImage().previewApply(new BrightnessContrastAdjustment(0, 0)); //Reset the selection effect
    }

    /**
     * Get the currently selected corners of the image selection.
     *
     * @return An array of the two points defining the selection;
     * first is the top left corner, second is the bottom right corner.
     */
    public Point[] getCorners() {
        int minX = (int) Math.min(p1.getX(), p2.getX());
        int maxX = (int) Math.max(p1.getX(), p2.getX());
        int minY = (int) Math.min(p1.getY(), p2.getY());
        int maxY = (int) Math.max(p1.getY(), p2.getY());
        Point firstCorner = new Point(minX, minY);
        Point lastCorner = new Point(maxX, maxY);
        return new Point[] {firstCorner, lastCorner};
    }

    /**
     * Check if there is currently a non-zero area selection,
     * and that the 'Selection' mode is active.
     * 
     * @return True if there is a non-zero selection, false if
     * there is no selection or a different tool is being used.
     */
    public boolean isEmpty(){
        return p1.equals(EMPTY_POINT) && p2.equals(EMPTY_POINT) || !DrawPanel.getTool().equals(DrawPanel.SELECTION);
    }

    /**
     * <p>
     * Turns the inputted x and y values into a valid coordinate
     * found on the current image.
     * </p>
     * 
     * @param x The x coordinate to validate
     * @param y The y coordinate to validate
     * @return A valid point on the image of the inputted values
     */
    private Point validPoint(int x, int y, double scale){
        int xOffset = (int) target.getCenteredImageLocation().getWidth();
        int yOffset = (int) target.getCenteredImageLocation().getHeight();
        x = (int) ((x/scale - xOffset));
        y = (int) ((y/scale - yOffset));
        if(scale < 1) scale = 1 / scale; //Need to find inverse of 'scale' if it's less than one.
        Dimension size = target.getImage().getDimensions();
        if (x < 0) x = 0;
        else if (x >= size.width*scale) x = (int) (size.width*scale - 1);
        if (y < 0) y = 0;
        else if (y >= size.height*scale) y = (int) (size.height*scale - 1);

        return new Point((int) (x/(target.getImage().getResizeScaleTesting()*1)), (int) (y/(target.getImage().getResizeScaleTesting()*1)));
    }

    /**
     * <p>
     * Callback to notify the selection that the mouse was pressed.
     * </p>
     * 
     * @param e The event that triggered this.
     */
    public void mousePressed(MouseEvent e, double scale) {
        if (target.getImage().hasImage() == false) return;

        mouseSelectionOn = true;
        beenDragged = false;
        p1 = validPoint(e.getX(), e.getY(), scale);
        p2 = validPoint(e.getX(), e.getY(), scale);
        if(DrawPanel.getTool().equals(DrawPanel.BRUSH)) points.add(p1);
    }

    /**
     * <p>
     * Callback to notify the selection that the mouse was dragged.
     * </p>
     * 
     * @param e The event that triggered this.
     */
    public void mouseDragged(MouseEvent e, double scale) {
        if (target.getImage().hasImage() == false) return;
        if (mouseSelectionOn == false) return;

        beenDragged = true;
        p2 = validPoint(e.getX(), e.getY(), scale);
        points.add(p2);

        target.getImage().previewApply(getOperation());
    }

    /**
     * <p>
     * Callback to notify the selection that the mouse was released.
     * </p>
     * 
     * @param e The event that triggered this.
     */
    public void mouseReleased(MouseEvent e) {
        EditableImage img = target.getImage();
        if (img.hasImage() == false) return;
        
        if (DrawPanel.getTool().equals(DrawPanel.SELECTION)){
            if (mouseSelectionOn == false || beenDragged == true) return;            
            img.previewApply(new BrightnessContrastAdjustment(0, 0));
        }
        else{
            img.apply(getOperation());
        }
        reset();
    }

    /**
     * <p>
     * Determines which {@code ImageOperation} next needs to be applied
     * to the current image in order to correctly display the selection.
     * </p>
     * 
     * @return The next appropriate operation for the image, based on the tools
     * and other attributes the user has selected.
     */
    private ImageOperation getOperation(){
        Point[] corners = getCorners();
        Point topLeftCorner = new Point((int) (corners[0].x*(target.getImage().getResizeScaleTesting()*1)), (int) (corners[0].y*(target.getImage().getResizeScaleTesting()*1)));
        Point bottomRightCorner = new Point((int) (corners[1].x*(target.getImage().getResizeScaleTesting()*1)), (int) (corners[1].y*(target.getImage().getResizeScaleTesting()*1)));
        if (DrawPanel.getTool().equals(DrawPanel.SELECTION)) return new BrightnessContrastAdjustment(25, 0, topLeftCorner, bottomRightCorner);
        
        if (DrawPanel.getTool().equals(DrawPanel.BRUSH)) return new DrawBrush(DrawPanel.getStrokeSize(), points, DrawPanel.getPrimary());
        
        //Lines are a special case of "shape"; their corners need to be p1 and p2, not the top-left and bottom-right corners.
        if (DrawPanel.getShapeType().equals(DrawPanel.LINE)) return new DrawShape(DrawPanel.getShapeType(), DrawPanel.getFillType(), DrawPanel.getStrokeSize(), p1, p2, DrawPanel.getPrimary(), DrawPanel.getSecondary());        
        
        return new DrawShape(DrawPanel.getShapeType(), DrawPanel.getFillType(), DrawPanel.getStrokeSize(), corners[0], corners[1], DrawPanel.getPrimary(), DrawPanel.getSecondary());
    }

}
