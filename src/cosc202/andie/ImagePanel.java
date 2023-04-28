package cosc202.andie;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * <p>
 * UI display element for {@link EditableImage}s.
 * </p>
 * 
 * <p>
 * This class extends {@link JPanel} to allow for rendering of an image, as well as zooming
 * in and out. 
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class ImagePanel extends JPanel {

    int x1;
    int y1;
    int x2;
    int y2;

    boolean beenDragged = false;
    boolean mouseSelectionOn = false;
    
    /**
     * The image to display in the ImagePanel.
     */
    private EditableImage image;

    /**
     * <p>
     * The zoom-level of the current view.
     * A scale of 1.0 represents actual size; 0.5 is zoomed out to half size; 1.5 is zoomed in to one-and-a-half size; and so forth.
     * </p>
     * 
     * <p>
     * Note that the scale is internally represented as a multiplier, but externally as a percentage.
     * </p>
     */
    private double scale;

    /**
     * <p>
     * Create a new ImagePanel.
     * </p>
     * 
     * <p>
     * Newly created ImagePanels have a default zoom level of 100%
     * </p>
     */
    public ImagePanel() {
        image = new EditableImage();
        scale = 1.0;

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (image.hasImage()) {
                    mouseSelectionOn = true;
                }
                if (mouseSelectionOn) {
                    beenDragged = false;
                    x1 = e.getX();
                    y1 = e.getY();
                    if (x1 < 0) x1 = 0;
                    if (x1 > image.getImageDimensions()[0]) x1 = image.getImageDimensions()[0];
                    if (y1 < 0) y1 = 0;
                    if (y1 > image.getImageDimensions()[1]) y1 = image.getImageDimensions()[1];
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (mouseSelectionOn) {
                    beenDragged = true;
                    x2 = e.getX();
                    y2 = e.getY();
                    if (x2 < 0) x2 = 0;
                    if (x2 > image.getImageDimensions()[0]) x2 = image.getImageDimensions()[0];
                    if (y2 < 0) y2 = 0;
                    if (y2 > image.getImageDimensions()[1]) y2 = image.getImageDimensions()[1];
                    image.previewApply(new BrightnessContrastAdjustment(25, 0, x1, y1, x2, y2));
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if(mouseSelectionOn) {
                    if (!beenDragged) {
                        image.previewApply(new BrightnessContrastAdjustment(0, 0));
                        forgetSelectedArea();
                    }
                }
            }
        });

    }

    public void forgetSelectedArea() {
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
    }

    /**
     * <p>
     * set the mouse selection to be on or off.
     * </p>
     * 
     * <p>
     * mouse selection will decide whether a use can select an area of the screen with their mouse.
     * </p>
     * @param mouseSelectionOn mouse selection on, true or false.
     */
    public void setMouseSelectionOn(boolean mouseSelectionOn){
        this.mouseSelectionOn = mouseSelectionOn;
    }

    /**
     * <p>
     * Get the currently selected area of image.
     * </p>
     *
     * @return a 2D array of ints in form [x,y][x,y] where the first set of points
     *  is the top left corner and the second set is the bottom right corner.
     */
    public int[][] getSelectedArea() {
        int[][] points = new int[2][2];
        points[0][0] = Math.min(x1, x2);
        points[1][0] = Math.max(x1, x2);
        points[0][1] = Math.min(y1, y2);
        points[1][1] = Math.max(y1, y2);
        return points;
    }

    /**
     * <p>
     * Get the currently displayed image
     * </p>
     *
     * @return the image currently displayed.
     */
    public EditableImage getImage() {
        return image;
    }

    /**
     * <p>
     * Get the current zoom level as a percentage.
     * </p>
     * 
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the original size, 50% is half-size, etc. 
     * </p>
     * @return The current zoom level as a percentage.
     */
    public double getZoom() {
        return 100*scale;
    }

    /**
     * <p>
     * Set the current zoom level as a percentage.
     * </p>
     * 
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the original size, 50% is half-size, etc. 
     * The zoom level is restricted to the range [50, 200].
     * </p>
     * @param zoomPercent The new zoom level as a percentage.
     */
    public void setZoom(double zoomPercent) {
        if (zoomPercent < 50) {
            zoomPercent = 50;
        }
        if (zoomPercent > 200) {
            zoomPercent = 200;
        }
        scale = zoomPercent / 100;
    }


    /**
     * <p>
     * Gets the preferred size of this component for UI layout.
     * </p>
     * 
     * <p>
     * The preferred size is the size of the image (scaled by zoom level), or a default size if no image is present.
     * </p>
     * 
     * @return The preferred size of this component.
     */
    @Override
    public Dimension getPreferredSize() {
        if (image.hasImage()) {
            return new Dimension((int) Math.round(image.getCurrentImage().getWidth()*scale), 
                                 (int) Math.round(image.getCurrentImage().getHeight()*scale));
        } else {
            return new Dimension(450, 450);
        }
    }

    /**
     * <p>
     * (Re)draw the component in the GUI.
     * </p>
     * 
     * @param g The Graphics component to draw the image on.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image.hasImage()) {
            Graphics2D g2  = (Graphics2D) g.create();
            g2.scale(scale, scale);
            g2.drawImage(image.getCurrentImage(), null, 0, 0);
            g2.dispose();
        }
        repaint();
    }
}
