package cosc202.andie;

import java.util.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * <p>
 * Actions provided by the File menu.
 * </p>
 * 
 * <p>
 * The File menu is very common across applications, 
 * and there are several items that the user will expect to find here.
 * Opening and saving files is an obvious one, but also exiting the program.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class FileActions {
    
    /** A list of actions for the File menu. */
    protected ArrayList<Action> actions;

    /** The target {@code ImagePanel}, which is used to interact with the {@code EditableImage} instance */
    private static ImagePanel target = ImageAction.getTarget();

    /**
     * <p>
     * Create a set of File menu actions.
     * </p>
     */
    public FileActions() {
        actions = new ArrayList<Action>();
        actions.add(new FileOpenAction(Language.getWord("Open"), null, Language.getWord("Open_desc"), Integer.valueOf(KeyEvent.VK_O)));
        actions.add(new FileSaveAction(Language.getWord("Save"), null, Language.getWord("Save_desc"), Integer.valueOf(KeyEvent.VK_S)));
        actions.add(new FileSaveAsAction(Language.getWord("SaveAs"), null, Language.getWord("SaveAs_desc"), Integer.valueOf(KeyEvent.VK_A)));
        actions.add(new FileExportAction(Language.getWord("Export"), null, Language.getWord("Export_desc"), Integer.valueOf(KeyEvent.VK_E)));
        actions.add(new FileExitAction(Language.getWord("Exit"), null, Language.getWord("Exit_desc"), Integer.valueOf(KeyEvent.VK_ESCAPE)));
    }

    /**
     * <p>
     * Create a menu contianing the list of File actions.
     * </p>
     * 
     * @return The File menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu(Language.getWord("File"));

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action)).setAccelerator(KeyStroke.getKeyStroke((Integer) action.getValue("MnemonicKey"), InputEvent.CTRL_DOWN_MASK)); //SHIFT_DOWN_MASK for shift
        }

        return fileMenu;
    }

    /**
     * <p>
     * Action to open an image from file.
     * </p>
     * 
     * @see EditableImage#open(String)
     */
    public class FileOpenAction extends ImageAction {

        /**
         * <p>
         * Create a new file-open action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileOpenAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-open action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileOpenAction is triggered.
         * It prompts the user to select a file and opens it as an image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            openAction();
        }
    }

    /**
     * Prompt the user to select a file. If the user successfully chooses and selects a
     * valid file, this file is then opened.
     * 
     * @return Whether or not the operation succeeds; true if the file is opened,
     * false if the file is not opened for whatever reason.
     */
    public static boolean openAction(){
        AndieFileChooser fileChooser = new AndieFileChooser();
        int result = fileChooser.showOpenDialog(target);
        if (result != JFileChooser.APPROVE_OPTION) return false;

        String imageFilepath = fileChooser.getPath();
        if(fileChooser.isSuccessful() == false) return false; //The selection was not successful, so don't continue.
        
        target.getImage().open(imageFilepath);
        target.repaint();
        target.getParent().revalidate();
        return true;
    }

    /**
     * <p>
     * Action to save an image to its current file location.
     * </p>
     * 
     * @see EditableImage#save()
     */
    public class FileSaveAction extends ImageAction {

        /**
         * <p>
         * Create a new file-save action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileSaveAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-save action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileSaveAction is triggered.
         * It saves the image to its original filepath.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            saveAction();
        }

    }

    /**
     * Saves the currently loaded file.
     * If no file is open, it warns the user.
     * If the file is only open locally (has no corresponding filename), then
     * the {@code saveAsAction()} function is called.
     * 
     * @return Whether this operation succeeds or not. True if the file
     * ends up being saved, false if the file fails to be saved.
     */
    public static boolean saveAction(){
        // If there is no image, cannot save.
        if (!target.getImage().hasImage()) {
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
            return false;
        }
        // Non-local image, save normally
        if (target.getImage().hasLocalImage() == false) {
            target.getImage().save();
            return true;
        }
        // The image is only local; need to call saveAs()
        return saveAsAction();
    }

    /**
     * <p>
     * Action to save an image to a new file location.
     * </p>
     * 
     * @see EditableImage#saveAs(String)
     */
    public class FileSaveAsAction extends ImageAction {

        /**
         * <p>
         * Create a new file-save-as action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileSaveAsAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

         /**
         * <p>
         * Callback for when the file-save-as action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileSaveAsAction is triggered.
         * It prompts the user to select a file and saves the image to it.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            saveAsAction();
        }
    }

    /**
     * Saves the current file under a new filename, which the user is
     * asked to provide. If no file is open, the user is warned.
     * 
     * @return Whether this operation succeeds or not. True if the file
     * ends up being saved, false if the file fails to be saved.
     */
    public static boolean saveAsAction(){
        if (!target.getImage().hasImage()) {
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
            return false;
        }
        AndieFileChooser fileChooser = new AndieFileChooser();
        int result = fileChooser.showSaveDialog(target);

        if (result == JFileChooser.APPROVE_OPTION) {
            String imageFilepath = fileChooser.getPath();
            if(fileChooser.isSuccessful() == false) return false; //Just in case the selection was not successful, don't continue.
            target.getImage().saveAs(imageFilepath);
            return true;
        }
        return false;
    }

    /**
     * <p>
     * Action to export the current image to a new file location.
     * </p>
     * 
     * @see EditableImage#export(String)
     */
    public class FileExportAction extends ImageAction
    {
        /**
         * <p>
         * Create a new file-export action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        FileExportAction(String name, ImageIcon icon, String desc, Integer mnemonic)
        {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-export action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileExportAction is triggered.
         * It prompts the user to select a path where the current file will be saved to.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            exportAction();
        }
    }

    /**
     * Exports the current file under a new filename, which the user is
     * asked to provide. If no file is open, the user is warned.
     * 
     * @return Whether this operation succeeds or not. True if the file
     * ends up being exported, false if the file fails to be exported.
     */
    public static boolean exportAction(){
        if (!target.getImage().hasImage()) {
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
            return false;
        }
        AndieFileChooser fileChooser = new AndieFileChooser();
        int result = fileChooser.showSaveDialog(target);

        if (result == JFileChooser.APPROVE_OPTION) {
            String imageFilepath = fileChooser.getPath();
            if(fileChooser.isSuccessful() == false) return false; //Just in case the selection was not successful, don't continue.
            target.getImage().export(imageFilepath);
            return true;
        }
        return false;
    }

    /**
     * <p>
     * Action to quit the ANDIE application.
     * </p>
     */
    public class FileExitAction extends AbstractAction {

        /**
         * <p>
         * Create a new file-exit action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileExitAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

         /**
         * <p>
         * Callback for when the file-exit action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileExitAction is triggered.
         * It quits the program.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            exitAction();
        }
    }

    /**
     * <p>
     * Exits the application, while also making sure that any unsaved changes are not lost.
     * </p>
     * 
     * <p>
     * This operation can fail: if the user has unsaved changes and wishes to cancel;
     * or they have unsaved changes choose to save, but {@code saveAction()} fails.
     * </p>
     */
    public static void exitAction(){
        if(ImageAction.getTarget().getImage().hasUnsavedChanges() == false) System.exit(0); //If there aren't unsaved changes, just exit as usual.

        int result = UserMessage.showDialog(UserMessage.SAVE_AND_EXIT_DIALOG); //Show the user a pop-up dialog to ask if they want to save.
        if(result == UserMessage.CLOSED_OPTION || result == UserMessage.CANCEL_OPTION) return; //Don't save, but also don't exit
        if(result == UserMessage.NO_OPTION) System.exit(0); //Exit without saving

        //Otherwise, they want to save and exit.
        boolean success = saveAction();
        if(success) System.exit(0);
    }

}
