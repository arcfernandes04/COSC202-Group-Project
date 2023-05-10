package cosc202.andie;

import java.util.*;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    protected static FileNameExtensionFilter fileExtensionFilter = new FileNameExtensionFilter("JPEG, PNG, GIF, ...", EditableImage.getAllowedExtensions());

    /**
     * The most recently opened directory. Defaults to {@code null}, in which case the user's default directory is used.
     */
    private static String lastDirectory = Preferences.getPreference("lastDirectory");

    /**
     * Whether or not the most recent attempt to select a file path was successful;
     * i.e. true if a valid directory was selected and confirmed,
     * false if cancelled or the directory did not exist.
     */
    private boolean success = true;

    /**
     * The fields to be renamed in each language.
     */
    private static final String[] elementsToRename = new String[]{"lookInLabelText", "filesOfTypeLabelText", "upFolderToolTipText", "fileNameLabelText", "homeFolderToolTipText", "newFolderToolTipText", "listViewButtonToolTipText", 
    "detailsViewButtonToolTipText", "saveButtonText", "directoryOpenButtonText", "openButtonText","cancelButtonText", "updateButtonText", "helpButtonText", "saveButtonToolTipText", 
    "openButtonToolTipText", "directoryOpenButtonToolTipText", "cancelButtonToolTipText", "updateButtonToolTipText", "helpButtonToolTipText"};

    /**
     * Default constructor that creates a new file chooser, starting from the most recently visited directory -
     * or the home directory if a recent directory cannot be located.
     */
    public AndieFileChooser(){
        super(lastDirectory);
        setFileFilter(fileExtensionFilter);
        this.setDialogTitle(Language.getWord("FileChooser.Title"));
    }

    /**
     * Whether or not the most recent attempt to select a file path was successful;
     * 
     * @return True if a valid directory was selected and confirmed,
     * false if cancelled or the directory did not exist.
     */
    public boolean isSuccessful(){
        return success;
    }

    /**
     * Get all of the named attributes in a JFileChooser so that they can be renamed by the UI Manager.
     * 
     * @return The elements to be renamed. Note that they are missing the prefix "JFileChooser." which must
     * be added before attempting to alter these attributes inside of the UI Manager.
     */
    public static String[] getElementsToRename(){
        return Arrays.copyOf(elementsToRename, elementsToRename.length);
    }

    /**
     * Tells the {@code AndieFileChooser} to update the 'lastDirectory' preference in the config file, by
     * calling the {@code Preferences} static class.
     */
    public static void updatePreferences(){
        if (lastDirectory == null) lastDirectory = "null"; //Need to stringify, otherwise it can't be put in config file.
        Preferences.setPreference("lastDirectory", lastDirectory);
    }

    /**
     * <p>
     * Access the canonical path of the currently selected AndieFileChooser as a String.
     * A more convenient way to access {@code AndieFileChooser.getSelectedFile().getCanonicalPath()}
     * as it is shorter and the error handling occurs inside of the method.
     * </p>
     * 
     * @return The canonical path of the current file after it has been approved.
     * Returns {@code null} if an error occurs.
     */
    public String getPath(){
        String imageFilepath = null;
        try {
            imageFilepath = this.getSelectedFile().getCanonicalPath();
        } catch (IOException ex) {
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
        return imageFilepath;
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

        success = false;

        //Get the image and the desired filename
        EditableImage currImg = ImageAction.target.getImage();
        String filename;
        String lastDirectory;
        try{
            filename = getSelectedFile().getCanonicalPath();
            lastDirectory = getSelectedFile().getParent();
        }catch(IOException ex){
            UserMessage.showWarning(UserMessage.INVALID_PATH_WARN);
            return;
        }

        AndieFileChooser.lastDirectory = lastDirectory;

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
            for (String ext : EditableImage.getAllowedExtensions()) {
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
                if(result == UserMessage.YES_OPTION){
                    boolean successful = FileActions.saveAction(); //Save then open if the user wants to do this
                    if(successful == false){ //BUT make sure that they don't hit cancel!
                        cancelSelection();
                        return;
                    }
                }
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

        success = true;
        super.approveSelection();
    }
}


