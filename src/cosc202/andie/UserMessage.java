package cosc202.andie;

import java.io.FileNotFoundException;
import java.nio.file.InvalidPathException;
import javax.swing.*;
import cosc202.andie.exceptions.*;

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
    private static JFrame PARENT;

    /**
     * The title for the pop-up box instance. Defaults to "Error Message Window".
     */
    public static String TITLE = "Error Message Window";

    public static final int OVERWRITE_DIALOG = 0;

    public static final int INVALID_OPTION = -1;
    public static final int YES_OPTION = 0;
    public static final int NO_OPTION = 1;
    public static final int CANCEL_OPTION = 2;
    public static final int CLOSED_OPTION = 3;


    public static final int GENERIC_WARN = 10;
    public static final int EMPTY_REDO_STACK_WARN = 11;
    public static final int EMPTY_UNDO_STACK_WARN = 12;
    public static final int INVALID_IMG_FILE_WARN = 13;
    public static final int INVALID_OPS_FILE_WARN = 14;
    public static final int NULL_FULL_WARN = 15;
    public static final int NON_IMG_FILE_WARN = 16;
    public static final int FATAL_ERROR_WARN = 17;

        /*//Exceptions from other libraries
        else if(exception instanceof FileNotFoundException) this.message = "That file could not be located.";
        else if(exception instanceof InvalidPathException) this.message = "The path provided is invalid.";
        else if(exception instanceof SecurityException) this.message = "The program does not have the required permissions to open that file.";*/
    
    
    /**
     * The {@code Exception} that caused the UserMessage instance to be created.
     */
    private Exception exception;

    /**
     * The message to be displayed in a pop-up box for users.
     */
    private String message;

    /**
     * Creates a new instance of UserMessage, using the default message "An error
     * has occurred."
     */
    public UserMessage() {
        this("An error has occurred.");
    }

    /**
     * Creates a new instance of UserMessage, in order to open a pop-up box.
     * 
     * @param message The message to be displayed to the user.
     */
    public UserMessage(String message) {
        this.message = message;
        this.displayMessage();
    }

    public int showDialog(int dialogOption){
        int result = UserMessage.INVALID_OPTION;
        String title = UserMessage.TITLE;

        if(dialogOption == UserMessage.OVERWRITE_DIALOG){
            title = "Overwrite file?";
            String message = "The file already exists. Would you like to overwrite it?";
            result = JOptionPane.showConfirmDialog(UserMessage.PARENT, message, title, JOptionPane.YES_NO_CANCEL_OPTION);
        }

        if(result == JOptionPane.YES_OPTION) return UserMessage.YES_OPTION;
        else if(result == JOptionPane.NO_OPTION) return UserMessage.NO_OPTION;
        else if(result == JOptionPane.CANCEL_OPTION) return UserMessage.CANCEL_OPTION;
        else if(result == JOptionPane.CLOSED_OPTION) return UserMessage.CLOSED_OPTION;
        else return UserMessage.INVALID_OPTION;
    }

    /**
     * Creates a new instance of UserMessage
     */
    public UserMessage(Exception exception) {
        this.exception = exception;

        //Get the message

        //  To be refactored! The messages will need to be stored in preference files later.
        if(exception instanceof EmptyRedoStackException) this.message = "There are currently no operations to redo.";
        else if (exception instanceof EmptyUndoStackException) this.message = "There are currently no operations to undo.";
        else if (exception instanceof InvalidImageFormatException) this.message = "The current image's format is invalid.";
        else if (exception instanceof InvalidOperationsFileException) this.message = "The image's operation file has been corrupted, although the original image is still intact.";
        else if (exception instanceof NonImageFileException) this.message = "The program cannot operate on non-image files.";
        else if (exception instanceof NullFileException) this.message = "No file is currently open.";
        else if (exception instanceof FatalRuntimeException) this.message = "A fatal error has occurred. Please try re-opening the program.";

        //Exceptions from other libraries
        else if(exception instanceof FileNotFoundException) this.message = "That file could not be located.";
        else if(exception instanceof InvalidPathException) this.message = "The path provided is invalid.";
        else if(exception instanceof SecurityException) this.message = "The program does not have the required permissions to open that file.";
        else this.message = "An unexpected error occurred.";

        this.displayMessage();
    }

    /**
     * Display the message stored in this instance of UserMessage.
     */
    public void displayMessage(){
        JOptionPane.showMessageDialog(UserMessage.PARENT, this.message, UserMessage.TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Accessor for the {@code 'exception'} datafield in an instance of {@code UserMessage}.
     * 
     * @return The {@code Exception} instance that is stored in this {@code UserMessage} object,
     * containing details about why the message was created, if an exception was the cause.
     */
    public Exception getException(){
        return this.exception;
    }

    /**
     * Set the title for all UserMessage windows.
     * 
     * @param title The desired title to use for UserMessage instances.
     */
    public static void setTitle(String title) {
        UserMessage.TITLE = title;
    }

    /**
     * Set the parent component for all UserMessage windows.
     * 
     * @param parent The parent JFrame instance
     */
    public static void setParent(JFrame parent) {
        UserMessage.PARENT = parent;
    }

}
