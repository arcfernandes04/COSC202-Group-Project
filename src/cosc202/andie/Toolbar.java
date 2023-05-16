package cosc202.andie;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import cosc202.andie.settings.ThemeActions;

/**
 * <p>
 * A toolbar containing essential/common actions.
 * </p>
 * 
 * <p>
 * A toolbar that contains essential/common actions that the user
 * can perform more efficiently.
 * </p>
 * 
 * @version 1.0
 */
public class Toolbar extends JPanel {

    /**
     * <p>
     * Create a toolbar that contains essential/common actions.
     * </p>
     */
    public Toolbar() {
        // Set up toolbar
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        // Aligns the toolbar to the left side
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.WEST);

        //Try to get all the toolbar icons
        String directory = "/resources/icons/";
        if(ThemeActions.isDark()) directory += "light/";
        else directory += "dark/";

        try{
            // Load in the icons
            ImageIcon openIcon = new ImageIcon(Toolbar.class.getResource(directory + "open_icon.png"));
            ImageIcon saveIcon = new ImageIcon(Toolbar.class.getResource(directory + "save_icon.png"));
            ImageIcon saveAsIcon = new ImageIcon(Toolbar.class.getResource(directory + "save_as_icon.png"));
            ImageIcon undoIcon = new ImageIcon(Toolbar.class.getResource(directory + "undo_icon.png"));
            ImageIcon redoIcon = new ImageIcon(Toolbar.class.getResource(directory + "redo_icon.png"));
            ImageIcon zoomInIcon = new ImageIcon(Toolbar.class.getResource(directory + "zoom_in_icon.png"));
            ImageIcon zoomOutIcon = new ImageIcon(Toolbar.class.getResource(directory + "zoom_out_icon.png"));

            // Create the action objects
            // Create the buttons and attach corresponding actions
            toolbar.add(new JButton(new OpenAction(openIcon)));
            toolbar.add(new JButton(new SaveAction(saveIcon)));
            toolbar.add(new JButton(new SaveAsAction(saveAsIcon)));
            toolbar.add(new JButton(new UndoAction(undoIcon)));
            toolbar.add(new JButton(new RedoAction(redoIcon)));
            toolbar.add(new JButton(new ZoomInAction(zoomInIcon)));
            toolbar.add(new JButton(new ZoomOutAction(zoomOutIcon)));
        }catch(Exception e){
            //If an error occurs, just continue without building the toolbar
        }
    }

    /**
     * <p>
     * Action that performs a save on the current image.
     * </p>
     * 
     * @see EditableImage#save()
     */
    private class SaveAction extends AbstractAction {
        
        /**
         * <p>
         * Create a new save action.
         * </p>
         */
        public SaveAction(ImageIcon icon) {
            super("", icon);
            putValue(Action.SHORT_DESCRIPTION, Language.getWord("Save"));
            // Resize the icon to a more appropriate size
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(img);
            putValue(Action.SMALL_ICON, resizedIcon);
        }

        /**
         * <p>
         * Callback for when the save action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the save action is triggered.
         * It saves the image to its original filepath.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            FileActions.FileSaveAction saveAction = new FileActions().new FileSaveAction(Language.getWord("Save"), null, Language.getWord("Save_desc"), Integer.valueOf(KeyEvent.VK_S));
            saveAction.actionPerformed(e);
        }
    }

    /**
     * <p>
     * Action to save an image to a new file location.
     * </p>
     * 
     * @see EditableImage#saveAs(String)
     */
    private class SaveAsAction extends AbstractAction {

        /**
         * <p>
         * Create a new save as action.
         * </p>
         */
        public SaveAsAction(ImageIcon icon) {
            super("", icon);
            putValue(Action.SHORT_DESCRIPTION, Language.getWord("SaveAs"));
            // Resize the icon to a more appropriate size
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(img);
            putValue(Action.SMALL_ICON, resizedIcon);
        }

        /**
         * <p>
         * Callback for when the save as action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the save as action is triggered.
         * It prompts the user to select a file and saves the image to it.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            FileActions.FileSaveAsAction saveAs = new FileActions().new FileSaveAsAction(Language.getWord("SaveAs"), null, Language.getWord("SaveAs_desc"), Integer.valueOf(KeyEvent.VK_A));
            saveAs.actionPerformed(e);
        }
    }

    /**
     * <p>
     * Action to open an image from file.
     * </p>
     * 
     * @see EditableImage#open(String)
     */
    private class OpenAction extends AbstractAction {
        
        /**
         * <p>
         * Create a new file-open action.
         * </p>
         */
        public OpenAction(ImageIcon icon) {
            super("", icon);
            putValue(Action.SHORT_DESCRIPTION, Language.getWord("Open"));
            // Resize the icon to a more appropriate size
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(img);
            putValue(Action.SMALL_ICON, resizedIcon);
        }

        /**
         * <p>
         * Callback for when the file-open action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the file-open action is triggered.
         * It prompts the user to select a file and opens it.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            FileActions.FileOpenAction open = new FileActions().new FileOpenAction(Language.getWord("Open"), null, Language.getWord("Open_desc"), Integer.valueOf(KeyEvent.VK_O));
            open.actionPerformed(e);
        }
    }

    /**
     * <p>
     * Action to undo the last action performed.
     * </p>
     * 
     * @see EditableImage#undo()
     */
    private class UndoAction extends AbstractAction {

        /**
         * <p>
         * Create a new undo action.
         * </p>
         */
        public UndoAction(ImageIcon icon) {
            super("", icon);
            putValue(Action.SHORT_DESCRIPTION, Language.getWord("Undo"));
            // Resize the icon to a more appropriate size
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(img);
            putValue(Action.SMALL_ICON, resizedIcon);
        }

        /**
         * <p>
         * Callback for when the undo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the undo action is triggered.
         * It undoes the most recently applied operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            EditActions.UndoAction undo = new EditActions().new UndoAction(Language.getWord("Undo"), null, Language.getWord("Undo_desc"), Integer.valueOf(KeyEvent.VK_Z));
            undo.actionPerformed(e);
        }
    }

    /**
     * <p>
     * Action to redo the last action performed.
     * </p>
     * 
     * @see EditableImage#redo()
     */
    private class RedoAction extends AbstractAction {

        /**
         * <p>
         * Create a new redo action.
         * </p>
         */
        public RedoAction(ImageIcon icon) {
            super("", icon);
            putValue(Action.SHORT_DESCRIPTION, Language.getWord("Redo"));
            // Resize the icon to a more appropriate size
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(img);
            putValue(Action.SMALL_ICON, resizedIcon);
        }

        /**
         * <p>
         * Callback for when the redo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the redo action is triggered.
         * It redoes the most recently undone operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            EditActions.RedoAction redo = new EditActions().new RedoAction(Language.getWord("Redo"), null, Language.getWord("Redo_desc"), Integer.valueOf(KeyEvent.VK_Y));
            redo.actionPerformed(e);
        }
    }

    /**
     * <p>
     * Action to zoom in on an image.
     * </p>
     */
    private class ZoomInAction extends AbstractAction {

        /**
         * <p>
         * Create a new zoom in action.
         * </p>
         */
        public ZoomInAction(ImageIcon icon) {
            super("", icon);
            putValue(Action.SHORT_DESCRIPTION, Language.getWord("ZoomIn"));
            // Resize the icon to a more appropriate size
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(img);
            putValue(Action.SMALL_ICON, resizedIcon);
        }

        /**
         * <p>
         * Callback for when the zoom in action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the zoom in action is triggered.
         * It increases the zoom level by 10%, to a maximum of 200%.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            ViewActions.ZoomInAction zoomIn = new ViewActions().new ZoomInAction(Language.getWord("ZoomIn"), null, Language.getWord("ZoomIn_desc"), Integer.valueOf(KeyEvent.VK_PLUS));
            zoomIn.actionPerformed(e);
        }
    }

    /**
     * <p>
     * Action to zoom out on an image.
     * </p>
     */
    private class ZoomOutAction extends AbstractAction {

        /**
         * <p>
         * Create a new zoom out action.
         * </p>
         */
        public ZoomOutAction(ImageIcon icon) {
            super("", icon);
            putValue(Action.SHORT_DESCRIPTION, Language.getWord("ZoomOut"));
            // Resize the icon to a more appropriate size
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(img);
            putValue(Action.SMALL_ICON, resizedIcon);
        }

        /**
         * <p>
         * A callback for when the zoom out action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the zoom out action is triggered.
         * It decreases the zoom level by 10%, to a minimum of 50%.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            ViewActions.ZoomOutAction zoomOut = new ViewActions().new ZoomOutAction(Language.getWord("ZoomOut"), null, Language.getWord("ZoomOut_desc"), Integer.valueOf(KeyEvent.VK_MINUS));
            zoomOut.actionPerformed(e);
        }
    }
}
