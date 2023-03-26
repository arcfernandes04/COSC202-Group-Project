package cosc202.andie;

import javax.swing.*;

/**
 * <p>
 * Create a pop up box to notify the user when an error has occurred, instead of
 * crashing with no explanation.
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

    public static final int YES_OPTION = 0;
    public static final int NO_OPTION = 1;
    public static final int CANCEL_OPTION = 2;
    public static final int CLOSED_OPTION = 3;

    public static final int OVERWRITE_EXISTING_FILE_DIALOG = 10;
    public static final int EXIT_WITHOUT_SAVING_DIALOG = 11;

    public static final int GENERIC_WARN = 20;
    public static final int EMPTY_REDO_STACK_WARN = 21;
    public static final int EMPTY_UNDO_STACK_WARN = 22;
    public static final int INVALID_IMG_FILE_WARN = 23;
    public static final int INVALID_OPS_FILE_WARN = 24;
    public static final int NULL_FILE_WARN = 25;
    public static final int NON_IMG_FILE_WARN = 26;
    public static final int FATAL_ERROR_WARN = 27;
    public static final int FILE_NOT_FOUND_WARN = 28;
    public static final int INVALID_PATH_WARN = 29;
    public static final int SECURITY_WARN = 30;


    /**
     * Set the parent component for all UserMessage windows.
     * 
     * @param parent The parent JFrame instance
     */
    public static void setParent(JFrame parent) {
        UserMessage.parent = parent;
    }


    public static int showDialog(int dialogOption){
        return showDialog(dialogOption, UserMessage.parent);
    }

    public static int showDialog(int dialogOption, JFrame parent){
        int result = -1;
        String message = "Are you sure you would like to continue with this action?";
        String title = "ANDIE Dialog Window";

        if(dialogOption == UserMessage.OVERWRITE_EXISTING_FILE_DIALOG){
            title = "Overwrite file?";
            message = "The file already exists. Would you like to overwrite it?";
        }
        else if(dialogOption == UserMessage.EXIT_WITHOUT_SAVING_DIALOG){
            title = "Exit without saving?";
            message = "Unsaved changes have been made. Would you like to save these before exiting?";
        }
        else{ //If it's not one of the expected dialog options, then that is an illegal argument.
            throw new IllegalArgumentException("Invalid dialog option.");
        }

        result = JOptionPane.showConfirmDialog(UserMessage.parent, message, title, JOptionPane.YES_NO_CANCEL_OPTION);

        if(result == JOptionPane.YES_OPTION) return UserMessage.YES_OPTION;
        else if(result == JOptionPane.NO_OPTION) return UserMessage.NO_OPTION;
        else if(result == JOptionPane.CANCEL_OPTION) return UserMessage.CANCEL_OPTION;
        else if(result == JOptionPane.CLOSED_OPTION) return UserMessage.CLOSED_OPTION;
        else return -1; // If something goes wrong
    }


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
     * Display the message stored in this instance of UserMessage.
     */
    public static void showWarning(int warning){
        showWarning(warning, UserMessage.parent);
    }

}
