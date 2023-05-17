package cosc202.andie;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.nio.file.InvalidPathException;

import javax.swing.*;

import javax.imageio.*;

import cosc202.andie.draw.DrawPanel;
import cosc202.andie.settings.SettingsActions;
import cosc202.andie.settings.ThemeActions;


/**
 * <p>
 * Main class for A Non-Destructive Image Editor (ANDIE).
 * </p>
 * 
 * <p>
 * This class is the entry point for the program.
 * It creates a Graphical User Interface (GUI) that provides access to various image editing and processing operations.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class Andie {

    /**
     * The JFrame that contains all of the GUI elements in the program.
     */
    private static JFrame frame;


    /**
     * The ImagePanel that displays the image.
     */
    private static ImagePanel imagePanel;

    /**
     * The JMenuBar that holds all of the user actions for the program.
     */
    private static JMenuBar menuBar;

    /**
     * The JPanel displayed at the bottom of the screen, holding information such as colour selection and current tool.
     */
    private static DrawPanel drawPanel;

    private static Toolbar toolbar;

    /**
     * ANDIE's main icon used throughout the program.
     */
    private static ImageIcon icon;

    /**
     * How much to scale the program's icon by.
     */
    private static final double ICON_SCALE_FACTOR = 0.1;

    /**
     * An accessor for ANDIE's icon data field.
     */
    public static ImageIcon getIcon(){
        return Andie.icon;
    }

    /**
     * An accessor for the main window frame - useful for any pop up windows which need to access 
     * this data to know where to place themselves on the screen.
     */
    public static JFrame getFrame(){
        return frame;
    }

    /**
     * Accessor for the current {@code ImagePanel} instance.
     * 
     * @return The current {@code ImagePanel} being used.
     */
    public static ImagePanel getImagePanel(){
        return imagePanel;
    }

    /**
     * Accessor for the current {@code DrawPanel} instance.
     * 
     * @return The current {@code DrawPanel} being used.
     */
    public static DrawPanel getDrawPanel(){
        return drawPanel;
    }

    /**
     * <p>
     * Attempt to update ANDIE's logo when the theme is changed,
     * or the program is initially opened.
     * </p>
     * 
     * <p>
     * If the logo fails to be found, then no logo is used,
     * or the previous one is reused if applicable.
     * </p>
     */
    public static void updateIcon(){
        try {
            if(icon == null) icon = new ImageIcon();
            String logoPath = "/resources/icons/logo/" + ThemeActions.getThemeName() + ".png";
            BufferedImage image = ImageIO.read(Andie.class.getResource(logoPath));
            frame.setIconImage(image);
            icon.setImage(image.getScaledInstance((int) (image.getWidth() * ICON_SCALE_FACTOR), (int) (image.getHeight() * ICON_SCALE_FACTOR), 0));
        } catch (Exception e) {
            // If something goes wrong, will either continue without the logo, or keep using the previous one.
        }
    }

    /**
     * Tells this class to update all of the preferences it stores
     */
    public static void updatePreferences(){
        Preferences.setPreference("Frame_width", Integer.toString(frame.getWidth()));
        Preferences.setPreference("Frame_height", Integer.toString(frame.getHeight()));
        Preferences.setPreference("initial_x", Integer.toString(frame.getX()));
        Preferences.setPreference("initial_y", Integer.toString(frame.getY()));
    }

    /**
     * <p>
     * Launches the main GUI for the ANDIE program.
     * </p>
     * 
     * <p>
     * This method sets up an interface consisting of an active image (an {@code ImagePanel})
     * and various menus which can be used to trigger operations to load, save, edit, etc. 
     * These operations are implemented {@link ImageOperation}s and triggerd via 
     * {@code ImageAction}s grouped by their general purpose into menus.
     * </p>
     * 
     * @see ImagePanel
     * @see ImageAction
     * @see ImageOperation
     * @see FileActions
     * @see EditActions
     * @see ViewActions
     * @see FilterActions
     * @see ColourActions
     * 
     * @throws Exception if something goes wrong.
     */
    private static void createAndShowGUI() throws Exception {

        // Need to set up the themes so that the correct one is taken from the preferences file.
        ThemeActions.setup();

        // We need to catch exceptions that are thrown in other threads
        // Easiest way to do this is by setting the default UncaughtExceptionHandler
        // https://stackoverflow.com/questions/12008662/swing-uncaughtexceptionhandler
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                if(e instanceof InvalidPathException){
                    UserMessage.showWarning(UserMessage.INVALID_PATH_WARN);
                }else{
                    UserMessage.showWarning(UserMessage.GENERIC_WARN);
                }
            }
        });

        // Set up the main GUI frame
        frame = new JFrame("ANDIE");
        updateIcon();

        //Add an event listener that checks when the frame is closed.
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                FileActions.exitAction();
            }
        });

        // The main content area is an ImagePanel
        imagePanel = new ImagePanel();
        ImageAction.setTarget(imagePanel);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        //Load preferences relating to the frame
        try{
            int x = Integer.parseInt(Preferences.getPreference("initial_x"));
            int y = Integer.parseInt(Preferences.getPreference("initial_y"));
            frame.setLocation(x, y);
            int width = Integer.parseInt(Preferences.getPreference("Frame_width"));
            int height = Integer.parseInt(Preferences.getPreference("Frame_height"));
            frame.setPreferredSize(new Dimension(width, height));
        }catch(NumberFormatException e){
            Dimension paneSize = imagePanel.getPreferredSize();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (int) ((screenSize.getWidth() - paneSize.getWidth()) / 2);
            int y = (int) ((screenSize.getHeight() - paneSize.getHeight()) / 2);

            frame.setLocation(x, y);
            frame.setPreferredSize(paneSize);
        }

        // Add the menu bar to hold all of the different user actions.
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        redrawMenuBar();

        redrawDrawPanel();

        redrawToolbar();

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * <p>
     * Adds all of user actions to the JMenuBar. Called initially by {@code createAndShowGUI()} when the program opens.
     * Called again by {@code redrawMenuBar()} in order to refresh the menu bar when the language changes.
     * </p>
     */
    public static void redrawMenuBar(){

        menuBar.removeAll();

        // Changing languages
        SettingsActions settingsActions = new SettingsActions();
        menuBar.add(settingsActions.createMenu());
        
        // File menus are pretty standard, so things that usually go in File menus go here.
        FileActions fileActions = new FileActions();
        menuBar.add(fileActions.createMenu());

        // Likewise Edit menus are very common, so should be clear what might go here.
        EditActions editActions = new EditActions();
        menuBar.add(editActions.createMenu());

        // View actions control how the image is displayed, but do not alter its actual content
        ViewActions viewActions = new ViewActions();
        menuBar.add(viewActions.createMenu());

        // Transform actions allow for rotation and scaling
        TransformActions transformActions = new TransformActions();
        menuBar.add(transformActions.createMenu());

        // Filters apply a per-pixel operation to the image, generally based on a local window
        FilterActions filterActions = new FilterActions();
        menuBar.add(filterActions.createMenu());

        // Actions that affect the representation of colour in the image
        ColourActions colourActions = new ColourActions();
        menuBar.add(colourActions.createMenu());

        menuBar.repaint();
        menuBar.revalidate();
    }

    /**
     * <p>
     * Delete the old instance of {@code DrawPanel} and create a new one.
     * </p>
     */
    public static void redrawDrawPanel(){
        if(drawPanel != null) frame.remove(drawPanel);
        drawPanel = new DrawPanel();
        frame.add(drawPanel, BorderLayout.SOUTH);
    }

    /**
     * <p>
     * Delete the old instance of {@code Toolbar} and create a new one.
     * </p>
     */
    public static void redrawToolbar() {
        if (toolbar != null) frame.remove(toolbar);
        // Add a toolbar to hold essential/common actions.
        toolbar = new Toolbar();
        // Sets the area where the toolbar is placed.
        frame.add(toolbar, BorderLayout.PAGE_START);
    }

    /**
     * <p>
     * Redraws all of the items inside the JMenuBar and lowerPanel. Calls {@code drawMenuBar()} and {@code drawLowerPanel()}.
     * </p>
     * 
     * This function is called when the language is changed and the buttons need to be redrawn in the correct language.
     */
    public static void redrawPanels(){
        redrawMenuBar();
        redrawDrawPanel();
        redrawToolbar();
    }

    /**
     * <p>
     * Main entry point to the ANDIE program.
     * </p>
     * 
     * <p>
     * Creates and launches the main GUI in a separate thread.
     * As a result, this is essentially a wrapper around {@code createAndShowGUI()}.
     * </p>
     * 
     * @param args Command line arguments, not currently used
     * @throws Exception If something goes awry
     * @see #createAndShowGUI()
     */
    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    UserMessage.showWarning(UserMessage.FATAL_ERROR_WARN);
                    System.exit(1);
                }
            }
        });
    }
}
