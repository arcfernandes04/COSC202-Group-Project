package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    
    private static String lastDirectory = null;

    /**
     * <p>
     * Create a set of File menu actions.
     * </p>
     */
    public FileActions() {
        actions = new ArrayList<Action>();
        actions.add(new FileOpenAction("Open", null, "Open a file", Integer.valueOf(KeyEvent.VK_O)));
        actions.add(new FileSaveAction("Save", null, "Save the file", Integer.valueOf(KeyEvent.VK_S)));
        actions.add(new FileSaveAsAction("Save As", null, "Save a copy", Integer.valueOf(KeyEvent.VK_A)));
        actions.add(new FileExportAction("Export", null, "Export the image", Integer.valueOf(KeyEvent.VK_E)));
        actions.add(new FileExitAction("Exit", null, "Exit the program", Integer.valueOf(0)));
    }

    /**
     * <p>
     * Create a menu contianing the list of File actions.
     * </p>
     * 
     * @return The File menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu("File");

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action));
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
            try {
                JFileChooser fileChooser;
                try{
                    fileChooser = new AndieFileChooser(lastDirectory);
                }catch (Exception ex){
                    System.out.println(ex);
                    System.out.println("Here");
                    fileChooser = new AndieFileChooser();
                }

                int result = fileChooser.showOpenDialog(target);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().open(imageFilepath);
                    lastDirectory = fileChooser.getSelectedFile().getParent();
                }
            }catch(Exception ex){
                UserMessage.showWarning(UserMessage.GENERIC_WARN);
            }
            target.repaint();
            target.getParent().revalidate();
        }

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
            target.getImage().save();           
        }

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
            JFileChooser fileChooser;
            try {
                fileChooser = new AndieFileChooser(lastDirectory);
            } catch (Exception ex) {
                // System.out.println(ex);
                fileChooser = new AndieFileChooser();
            }
            int result = fileChooser.showSaveDialog(target);
            try{
                if (result == JFileChooser.APPROVE_OPTION) {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().saveAs(imageFilepath);
                    lastDirectory = fileChooser.getSelectedFile().getParent();
                }
            }catch(IOException ex){
                UserMessage.showWarning(UserMessage.GENERIC_WARN);
            }
        }

    }

    public class FileExportAction extends ImageAction
    {
        FileExportAction(String name, ImageIcon icon, String desc, Integer mnemonic)
        {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser;
            try {
                fileChooser = new AndieFileChooser(lastDirectory);
            } catch (Exception ex) {
                // System.out.println(ex);
                fileChooser = new AndieFileChooser();
            }
            int result = fileChooser.showSaveDialog(target);
                
            try{
                if(result == JFileChooser.APPROVE_OPTION){
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().export(imageFilepath);
                    lastDirectory = fileChooser.getSelectedFile().getParent();
                }
            }catch(IOException ex){
                UserMessage.showWarning(UserMessage.GENERIC_WARN);
            }
        }
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
            if(ImageAction.getTarget().getImage().hasUnsavedChanges() == false) System.exit(0); //If there aren't unsaved changes, just exit as usual.

            int result = UserMessage.showDialog(UserMessage.SAVE_AND_EXIT_DIALOG); //Show the user a pop-up dialog.
            if(result == UserMessage.YES_OPTION) ImageAction.getTarget().getImage().save();
            else if(result == UserMessage.CLOSED_OPTION || result == UserMessage.CANCEL_OPTION) return; //Don't save OR exit

            System.exit(0); //Only exit if they chose "Save and exit" or "Don't save"
        }

    }

    /**
     * <p>
     * A safer version of {@code JFileChooser}, asking the user whether they would
     * like to overwrite their chosen filename if it already exists, or save changes
     * to the current file before opening another file.
     * </p>
     * 
     * @author Joshua Carter
     * @see javax.swing.JFileChooser
     **/
    public class AndieFileChooser extends JFileChooser{

        /**
        * List of the file extensions that are displayed within the {@code JFileChooser} window.
         */
        protected static FileNameExtensionFilter fileExtensionFilter = new FileNameExtensionFilter("JPEG, PNG, GIF, TIFF", EditableImage.allowedExtensions);

        /**
         * Default constructor which also initialises the file extensions that will be visible in the dialog box.
         */
        public AndieFileChooser(){
            super();
            setFileFilter(fileExtensionFilter);
        }

        /**
         * Constructor which initialises the file extensions that will be visible in the dialog box.
         * Takes a directory and initialises the {@code JFileChooser} box at that location.
         */
        public AndieFileChooser(String directory){
            super(directory);
            setFileFilter(fileExtensionFilter);
        }

        /**
         * <p>
         * Safer version of JFileChooser that asks before overwriting files, or opening
         * a new file without saving the current file's changes.
         * </p>
         * 
         * <p>
         * Code adapted from
         * https://stackoverflow.com/questions/3651494/jfilechooser-with-confirmation-dialog
         * </p>
         * 
         */
        @Override
        public void approveSelection(){

            //Get the image and the desired filename
            EditableImage currImg = ImageAction.target.getImage();
            String filename;
            try{
                filename = getSelectedFile().getCanonicalPath();
            }catch(IOException ex){
                UserMessage.showWarning(UserMessage.GENERIC_WARN);
                return;
            }

            /// Dialog box for OPENING files
            if(getDialogType() == OPEN_DIALOG){
                // If the file doesn't even exist, deny selection.
                if(!getSelectedFile().exists()){
                    UserMessage.showWarning(UserMessage.FILE_NOT_FOUND_WARN);
                    return;
                }

                //Does the file end with one of the allowed extensions?
                String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
                boolean allowed = false;
                for (String ext : EditableImage.allowedExtensions) {
                    if (ext.equalsIgnoreCase(extension)) {
                        allowed = true;
                        break;
                    }
                }
                if (!allowed) { // If not in the allowed list, tell the user off and deny the selection.
                    UserMessage.showWarning(UserMessage.NON_IMG_FILE_WARN);
                    return;
                }
                
                //If it is indeed an allowed and existing file, but there are unsaved changes, then ask if they want to save first.
                if(currImg.hasUnsavedChanges() == true){
                    int result = UserMessage.showDialog(UserMessage.SAVE_AND_OPEN_DIALOG);
                    if(result == UserMessage.YES_OPTION) ImageAction.getTarget().getImage().save(); //Save then open if the user wants to do this
                    else if(result != UserMessage.NO_OPTION) return; //If not "NO", then they have closed or cancelled. Do not save or open in this case.
                } //The other possible case is they said "Don't save", in which case don't do anything - just approve the selection.

            //Dialog for SAVING files (either save as or export)
            }else if(getDialogType() == SAVE_DIALOG){
                // If there is no current file open, tell the user off and cancel the selection.
                if(ImageAction.getTarget().getImage().hasImage() == false){
                    UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
                    cancelSelection();
                    return;
                }

                // Run the current selected file through the syntax rules to see what its actual name is.
                // Check out EditableImage.makeSensible() - it just makes sure that there is always an appropriate extension
                String sensibleFilename = currImg.makeSensible(filename);
                setSelectedFile(new File(sensibleFilename));
                
                // If the file exists, ask if they want to overwrite.
                if(getSelectedFile().exists()){
                    int result = UserMessage.showDialog(UserMessage.OVERWRITE_EXISTING_FILE_DIALOG);
                    if(result != UserMessage.YES_OPTION) return; //If they didn't say yes, then they don't want to overwrite it.
                }
            }
            super.approveSelection();
        }
    }

}
