package cosc202.andie;

import javax.swing.*;

/**
 * <p>
 * Create a pop up box with {@code showWarning()} to notify the user when an error has occurred, instead of
 * crashing with no explanation. Or, to ask the user for input using the {@code showDialog()} method.
 * </p>
 * 
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @see javax.swing.JOptionPane
 * @author Joshua Carter
 */
public class UserMessage {

    /**
     * The parent frame that the UserMessage instance will appear over.
     * If left blank, the pop-up box will instead appear in the middle of the
     * screen.
     */
    private static JFrame parent;
    /**The result when the user choses the "yes" option for a dialog message. */
    public static final int YES_OPTION = 0;
    /** The result when the user choses the "no" option for a dialog message. */
    public static final int NO_OPTION = 1;
    /** The result when the user choses the "cancel" option for a dialog message. */
    public static final int CANCEL_OPTION = 2;
    /** The result when the user closes the dialog message windo4w. */
    public static final int CLOSED_OPTION = 3;

    /** The dialog option asking the user whether they would like to overwrite the existing file, or not. */
    public static final int OVERWRITE_EXISTING_FILE_DIALOG = 10;
    /** The dialog option asking the user whether they would like to save and exit, or exit without saving, or cancel. */
    public static final int SAVE_AND_EXIT_DIALOG = 11;
    /** The dialog option asking the user whether they would like to save the current file and option the selection,
     * or open the selection without saving the current file, or cancel. */
    public static final int SAVE_AND_OPEN_DIALOG = 12;

    /** A generic warning to tell the user that an error has occurred. */
    public static final int GENERIC_WARN = 20;
    /** A warning to tell the user that the redo stack is empty. */
    public static final int EMPTY_REDO_STACK_WARN = 21;
    /** A warning to tell the user that the undo stack is empty. */
    public static final int EMPTY_UNDO_STACK_WARN = 22;
    /** A warning to tell the user that the image file they are trying to open is in an unreadable format. */
    public static final int INVALID_IMG_FILE_WARN = 23;
    /** A warning to tell the user that the operations file they are trying to open is unreadable. */
    public static final int INVALID_OPS_FILE_WARN = 24;
    /** A warning to tell the user that no file is currently open. */
    public static final int NULL_FILE_WARN = 25;
    /** A warning to tell the user that the file the program can only open image files. */
    public static final int NON_IMG_FILE_WARN = 26;
    /** A warning to tell the user that a fatal error has occurred. */
    public static final int FATAL_ERROR_WARN = 27;
    /** A warning to tell the user that the desired file was not found. */
    public static final int FILE_NOT_FOUND_WARN = 28;
    /** A warning to tell the user that the entered file path is invalid. */
    public static final int INVALID_PATH_WARN = 29;
    /** A warning to tell the user that the program does not have permission to open the desired file. */
    public static final int SECURITY_WARN = 30;


    /**
     * Set the parent component for all UserMessage windows.
     * 
     * @param parent The parent JFrame instance
     */
    public static void setParent(JFrame parent) {
        UserMessage.parent = parent;
    }

    /**
     * <p>
     * Show the user a dialog window, asking them to choose between different options.
     * </p>
     * 
     * @param dialogOption The type of query in the dialog box - e.g. if they would like to overwrite
     * the existing file, or save before exiting with unsaved changes.
     * @return The user's answer in response to the dialog - e.g. they may choose {@code UserMessage.YES_OPTION}
     * when asked shown the dialog for {@code UserMessage.showDialog(UserMessage.SAVE_AND_EXIT_DIALOG)}
     */
    public static int showDialog(int dialogOption){
        return showDialog(dialogOption, UserMessage.parent);
    }

    /**
     * <p>
     * Show the user a dialog window, asking them to choose between different options.
     * </p>
     * 
     * @param dialogOption The type of query in the dialog box - e.g. if they would like to overwrite
     * the existing file, or save before exiting with unsaved changes.
     * @param parent The parent component for the dialog box to appear over.
     * @return The user's answer in response to the dialog - e.g. they may choose {@code UserMessage.YES_OPTION}
     * when asked shown the dialog for {@code UserMessage.showDialog(UserMessage.SAVE_AND_EXIT_DIALOG)}
     */
    public static int showDialog(int dialogOption, JFrame parent){
        int result = -1;
        String message = "Are you sure you would like to continue with this action?";
        String title = "ANDIE Dialog Window";

        //Dialog for when the user tries to overwrite a file.
        if(dialogOption == UserMessage.OVERWRITE_EXISTING_FILE_DIALOG){
            title = "Overwrite File?";
            message = "Would you like to overwrite the existing file?";
            Object[] possibleValues = new Object[]{"Overwrite", "Cancel"};
            result = JOptionPane.showOptionDialog(UserMessage.parent, message, title,
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,possibleValues,possibleValues[1]);
        }//Dialog for when the user tries to exit without saving changes.
        else if(dialogOption == UserMessage.SAVE_AND_EXIT_DIALOG){
            title = "Save and Exit?";
            message = "Would you like to save changes before exiting?";
            Object[] possibleValues = new Object[]{"Save and Exit", "Don't Save", "Cancel"};
            result = JOptionPane.showOptionDialog(UserMessage.parent, message, title, JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, possibleValues, possibleValues[0]);
        }//Dialog for trying to open another file without saving changes.
        else if (dialogOption == UserMessage.SAVE_AND_OPEN_DIALOG) {
            title = "Save Changes?";
            message = "Would you like to save the changes to the current file?";
            Object[] possibleValues = new Object[] { "Save", "Don't Save", "Cancel" };
            result = JOptionPane.showOptionDialog(UserMessage.parent, message, title, JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, possibleValues, possibleValues[0]);
        }
        else{ //If it's not one of the expected dialog options, then that is an illegal argument.
            throw new IllegalArgumentException("Invalid dialog option.");
        }

        if(result == JOptionPane.YES_OPTION) return UserMessage.YES_OPTION;
        else if(result == JOptionPane.NO_OPTION) return UserMessage.NO_OPTION;
        else if(result == JOptionPane.CANCEL_OPTION) return UserMessage.CANCEL_OPTION;
        else if(result == JOptionPane.CLOSED_OPTION) return UserMessage.CLOSED_OPTION;
        else return -1; // If something goes wrong
    }

    /**
     * <p>
     * Show the user a warning window advising them that something has gone wrong, or that an action is disallowed.
     * </p>
     * 
     * @param warning The warning message to be displayed.
     * @param parent The parent component for the warning window to appear over.
     */
    public static void showWarning(int warning, JFrame parent) {
        String message;
        String title = "ANDIE Error Message";

        if(warning == EMPTY_REDO_STACK_WARN) message = "There are currently no operations to redo.";
        else if(warning == EMPTY_UNDO_STACK_WARN) message = "There are currently no operations to undo.";
        else if (warning == INVALID_IMG_FILE_WARN) message = "The current image's format is invalid.";
        else if (warning == INVALID_OPS_FILE_WARN) message = "The image's operation file has been corrupted, although the original image is still intact.";
        else if (warning == NON_IMG_FILE_WARN) message = "The program cannot operate on non-image files.";
        else if (warning == NULL_FILE_WARN) message = "No file is currently open.";
        else if (warning == FATAL_ERROR_WARN) message = "A fatal error has occurred. Please try re-opening the program.";
        else if(warning == FILE_NOT_FOUND_WARN) message = "That file could not be located.";
        else if(warning == INVALID_PATH_WARN) message = "The path provided is invalid.";
        else if(warning == SECURITY_WARN) message = "The program does not have the required permissions to open that file.";
        else if(warning == GENERIC_WARN) message = "An unexpected error occurred.";
        else throw new IllegalArgumentException("Invalid warning option.");

        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * <p>
     * Show the user a warning window advising them that something has gone wrong, or that an action is disallowed.
     * </p>
     * 
     * @param warning The warning message to be displayed.
     */
    public static void showWarning(int warning){
        showWarning(warning, UserMessage.parent);
    }

}
