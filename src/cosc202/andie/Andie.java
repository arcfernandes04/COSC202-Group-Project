package cosc202.andie;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.GeneralPath;
import java.nio.file.InvalidPathException;

import javax.swing.*;

import javax.imageio.*;

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
        JFrame frame = new JFrame("ANDIE");
        Image image = ImageIO.read(Andie.class.getClassLoader().getResource("icon.png"));
        frame.setIconImage(image);

        //Add an event listener that checks when the frame is closed.
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                if (ImageAction.getTarget().getImage().hasUnsavedChanges() == false) System.exit(0); // If there aren't unsaved changes, just exit as usual.

                int result = UserMessage.showDialog(UserMessage.SAVE_AND_EXIT_DIALOG); //Does the user want to save before exiting?
                if (result == UserMessage.YES_OPTION) ImageAction.getTarget().getImage().save(); //Yes, save the changes
                else if (result == UserMessage.CANCEL_OPTION || result == UserMessage.CLOSED_OPTION) return; //Cancel or closed, do not save or exit.
                System.exit(0); //Exit
            }
        });

        // Set the parent frame for UserMessages
        UserMessage.setParent(frame);

        // The main content area is an ImagePanel
        ImagePanel imagePanel = new ImagePanel();
        ImageAction.setTarget(imagePanel);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        frame.add(scrollPane, BorderLayout.CENTER);
        
        // Add in menus for various types of action the user may perform.
        JMenuBar menuBar = new JMenuBar();

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
        
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
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
