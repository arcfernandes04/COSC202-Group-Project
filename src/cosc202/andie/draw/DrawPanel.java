package cosc202.andie.draw;

import cosc202.andie.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>
 * Child of JPanel holding the GUI elements that allow for selecting the primary and secondary colours.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Josh Carter
 */
public class DrawPanel extends JPanel {

    /** The default colour of items inside of this DrawPanel such that they blend in with the rest of the GUI */
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    /** The colour to set GUI elements after being hovered over. */
    private static final Color HOVER = new Color(0, 0, 0, 25);
    /** Whether or not the current image has an alpha channel or not. */
    private static boolean isTransparencyEnabled = true;

    /** A tool allowing the user to make temporary selections of the current image. */
    public static final String SELECTION = "Selection";
    /** A tool allowing the user to draw shapes on the current image, as instances of {@code DrawShape}. */
    public static final String SHAPE = "Shapes";
    /** A tool allowing the user to draw curves on the current image, as instances of {@code DrawBrush}. */
    public static final String BRUSH = "Brush";

    /** Identifies the current shape tool as being a rectangle. */
    public static final String RECTANGLE = "Rectangle";
    /** Identifies the current shape tool as being an oval. */
    public static final String OVAL = "Oval";
    /** Identifies the current shape tool as being a straight line. */
    public static final String LINE = "Line";

    /** Identifies the current fill policy to fill only the inside of a shape. */
    public static final String FILL_ONLY = "Fill_only";
    /** Identifies the current fill policy to fill only the border of a shape. */
    public static final String BORDER_ONLY = "Border_only";
    /** Identifies the current fill policy to fill both the inside and border of a shape. */
    public static final String FILL_AND_BORDER = "Fill_and_border";

    /** The tool currently being used. By default, this is {@code SELECTION}. */
    private static String toolType = SELECTION;
    /** The shape currently being drawn. By default, this is {@code RECTANGLE}. */
    private static String shapeType = RECTANGLE;
    /** The brush size being used. By default, this is {@code 2}. */
    private static int strokeSize = 2;
    /** The fill policy currently being used. By default, this is {@code BORDER_ONLY}. */
    private static String fillType = BORDER_ONLY;
    /** Primary colour to be used for drawings. */
    private static Color primaryColour = Color.BLACK;
    /** Secondary colour to be used for drawings. */
    private static Color secondaryColour = Color.BLACK;

    /** The most recent instance of DrawPanel. This enables the class to be used statically. */
    private static DrawPanel currentInstance;
    /** The panel storing the primary and secondary colour selection options. */
    private ColourPanel colourPanel;
    /** The menu option which holds the possible tools. */
    private ToolActions toolActions;
    /** The menu option which holds the possible shapes. */
    private ShapeActions shapeActions;
    /** The menu option which holds the possible fill policies. */
    private FillActions fillActions;
    /** The menu option which enables selection of different brush sizes. */
    private BrushActions brushActions;

    /**
     * <p>
     * Instantiate all of the variables by looking at the config file,
     * using the Preferences class to access them.
     * </p>
     * 
     * Default values are used if the values cannot be resolved.
     */
    static {
        try{
            toolType = Preferences.getPreference("tool_type");
            shapeType = Preferences.getPreference("shape_type");
            fillType = Preferences.getPreference("fill_type");

            int pri = Integer.parseInt(Preferences.getPreference("colour_primary"));
            primaryColour = new Color(pri, true);
            int sec = Integer.parseInt(Preferences.getPreference("colour_secondary"));
            secondaryColour = new Color(sec, true);
            int stroke = Integer.parseInt(Preferences.getPreference("brush_size"));
            strokeSize = stroke;
        }catch(NumberFormatException e){}

        if(toolType == null) toolType = SELECTION;
        if(shapeType == null) shapeType = RECTANGLE;
        if(fillType == null) fillType = BORDER_ONLY;
        if(primaryColour == null) primaryColour = Color.BLACK;
        if(secondaryColour == null) secondaryColour = Color.BLACK;
        if(strokeSize < 2 || strokeSize > 20) strokeSize = 2;
    }
    
    /**
     * Construct a new instance of {@code DrawPanel}, instantiating the
     * data fields for drawing options.
     */
    public DrawPanel(){
        currentInstance = this;
        setLayout(new FlowLayout(FlowLayout.LEFT));

        colourPanel = new ColourPanel();
        add(colourPanel);
        toolActions = (ToolActions) add(new ToolActions(TRANSPARENT));
        shapeActions = (ShapeActions) add(new ShapeActions(TRANSPARENT));
        fillActions = (FillActions) add(new FillActions(TRANSPARENT));
        brushActions = (BrushActions) add(new BrushActions(TRANSPARENT));
        update();
    }

    /**
     * Retrieves the user's current selection of tool.
     * @return The current tool being used by the user.
     */
    public static String getTool(){
        return toolType;
    }

    /**
     * Sets the user's current selection of tool, and reflects
     * this in the {@code DrawPanel} elements.
     * @param toolType The new tool to be used.
     */
    public static void setTool(String toolType){
        DrawPanel.toolType = toolType;
        currentInstance.update();
        Andie.getImagePanel().getSelection().reset(); //Need to reset selection in case a selection is already in progress.
    }

    /**
     * Retrieves the user's current shape type.
     * @return The current shape type being drawn.
     */
    public static String getShapeType(){
        return shapeType;
    }

    /**
     * Sets the user's current selection of shape, and reflects
     * this in the {@code DrawPanel} elements.
     * @param shapeType The new type of shape to be drawn.
     */
    public static void setShapeType(String shapeType) {
        DrawPanel.shapeType = shapeType;
        currentInstance.update();
    }

    /**
     * Retrieves the user's current brush/stroke size.
     * @return The current stroke size.
     */
    public static int getStrokeSize(){
        return strokeSize;
    }

    /**
     * Sets the user's brush/stroke size, and reflects
     * this in the {@code DrawPanel} elements.
     * @param shapeType The stroke size to be used.
     */
    public static void setStrokeSize(int strokeSize){
        DrawPanel.strokeSize = strokeSize;
        currentInstance.update();
    }

    /**
     * Retrieves the user's current fill policy for drawing shapes.
     * (i.e. fill only inside, fill only border, or fill border and inside).
     * @return The current fill policy.
     */
    public static String getFillType(){
        return fillType;
    }

    /**
     * Sets the user's fill polcy, and reflects
     * this in the {@code DrawPanel} elements.
     * @param shapeType The fill policy to use.
     */
    public static void setFillType(String fillType){
        DrawPanel.fillType = fillType;
        currentInstance.update();
    }

    /**
     * Gets the current primary colour selection.
     * @return Current primary colour.
     */
    public static Color getPrimary() {
        return primaryColour;
    }

    /**
     * Gets the current secondary colour selection.
     * @return Current secondary colour.
     */
    public static Color getSecondary() {
        return secondaryColour;
    }

    /**
     * <p>
     * Tell the {@code Preferences} class to update
     * its values, based on the current tools
     * selected by the user.
     * </p>
     */
    public static void updatePreferences(){
        Preferences.setPreference("tool_type", toolType);
        Preferences.setPreference("shape_type", shapeType);
        Preferences.setPreference("fill_type", fillType);

        Preferences.setPreference("colour_primary", Integer.toString(primaryColour.getRGB()));
        Preferences.setPreference("colour_secondary", Integer.toString(secondaryColour.getRGB()));
        Preferences.setPreference("brush_size", Integer.toString(strokeSize));
    }

    /**
     * <p>
     * Tell each of the {@code DrawPanel} elements to update themselves,
     * as the current selection of options has changed.
     * </p>
     * 
     * <p>
     * Calls the {@code update()} method on each of the JMenuBar children,
     * then hides or shows the elements that are or are not relevant
     * for the current configuration.
     * </p>
     */
    private void update() {
        toolActions.update();
        shapeActions.update();
        fillActions.update();
        brushActions.update();

        //The brush size option should not be visible in "selection" mode
        if(toolType.equals(SELECTION)) brushActions.setVisible(false);
        else brushActions.setVisible(true);

        if(toolType.equals(SHAPE)){ //Make sure the shape and fill options are visible when using the "shape" mode
            shapeActions.setVisible(true);
            fillActions.setVisible(true);
            if(shapeType.equals(LINE)) fillActions.setVisible(false);
        }
        else{ //Don't want to see these options outside of "shape" mode.
            shapeActions.setVisible(false);
            fillActions.setVisible(false);
        }
        revalidate();
    }

    /**
     * Let the {@code DrawPanel} know whether or not the current image has an alpha channel.
     *
     * @param flag If {@code true}, then no actions need to be taken. If {@code false}, then the current
     * selection of primary and secondary colours is updated to remove the alpha component.
     */
    public static void setTransparencyEnabled(boolean flag) {
        isTransparencyEnabled = flag;
        if (flag) return; // Transparency is enabled so we don't need to care about there being an alpha channel
        // Otherwise, transparency is not enabled and we need to make sure that it is not present in the data fields.
        if (primaryColour.getAlpha() != 255) primaryColour = new Color(primaryColour.getRGB(), false);
        if (secondaryColour.getAlpha() != 255) secondaryColour = new Color(secondaryColour.getRGB(), false);

        currentInstance.colourPanel.primaryPreview.setBackground(primaryColour);
        currentInstance.colourPanel.secondaryPreview.setBackground(secondaryColour);
    }

    /**
     * <p>
     * A child of JPanel which holds the GUI elements for primary and secondary colour selection.
     * </p>
     * 
     */
    private class ColourPanel extends JPanel {

        /** The size of the square colour preview buttons. */
        private static final Dimension COLOUR_LABEL_DIMENSION = new Dimension(25, 25);

        /** The button with text to indicate that this for selecting the primary colour. */
        private JButton primaryButton;
        /** The button which shows a preview of the current primary colour. */
        private JButton primaryPreview;
        /** The button with text to indicate that this for selecting the secondary colour. */
        private JButton secondaryButton;
        /** The button which shows a preview of the current secondary colour. */
        private JButton secondaryPreview;

        /** 
         * Default constructor of the panel, instantiating the buttons,
         * setting their colours and sizes, and adding listeners so that
         * whenever the buttons are clicked, they call {@code UserMessage.showColourChooser()}
         */
        public ColourPanel(){
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            //Initialise buttons
            primaryButton = new JButton(Language.getWord("Colour_primary"));
            primaryPreview = new JButton();
            secondaryButton = new JButton(Language.getWord("Colour_secondary"));
            secondaryPreview = new JButton();

            //Make sure the text buttons have the same background color as the rest of the GUI, and do not have a border
            primaryButton.setBorderPainted(false); primaryButton.setBackground(TRANSPARENT);
            secondaryButton.setBorderPainted(false); secondaryButton.setBackground(TRANSPARENT);

            ChangeListener hoverListener = new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    JButton b = (JButton) e.getSource();
                    if (b.getModel().isRollover()) b.setBackground(HOVER);
                    else b.setBackground(TRANSPARENT);
                }
            };
            primaryButton.addChangeListener(hoverListener); secondaryButton.addChangeListener(hoverListener);

            //Add a listener for changing the primary colour
            ActionListener primaryListener = new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    primaryColour = UserMessage.showColourChooser(primaryColour, isTransparencyEnabled);
                    primaryPreview.setBackground(primaryColour);
                }
            };
            primaryButton.addActionListener(primaryListener); primaryPreview.addActionListener(primaryListener);

            //Listener for changing the secondary colour
            ActionListener secondaryListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    secondaryColour = UserMessage.showColourChooser(secondaryColour, isTransparencyEnabled);
                    secondaryPreview.setBackground(secondaryColour);
                }
            };
            secondaryButton.addActionListener(secondaryListener); secondaryPreview.addActionListener(secondaryListener);

            //Set the dimensions, background colour, and border for the 'preview' buttons
            primaryPreview.setMinimumSize(COLOUR_LABEL_DIMENSION); primaryPreview.setSize(COLOUR_LABEL_DIMENSION);
            primaryPreview.setMaximumSize(COLOUR_LABEL_DIMENSION); primaryPreview.setPreferredSize(COLOUR_LABEL_DIMENSION);
            primaryPreview.setBackground(primaryColour); primaryPreview.setBorderPainted(false);

            secondaryPreview.setMinimumSize(COLOUR_LABEL_DIMENSION); secondaryPreview.setSize(COLOUR_LABEL_DIMENSION);
            secondaryPreview.setMaximumSize(COLOUR_LABEL_DIMENSION); secondaryPreview.setPreferredSize(COLOUR_LABEL_DIMENSION);
            secondaryPreview.setBackground(secondaryColour); secondaryPreview.setBorderPainted(false);

            add(primaryPreview); add(primaryButton); add(secondaryPreview); add(secondaryButton);
        }

    }

}
