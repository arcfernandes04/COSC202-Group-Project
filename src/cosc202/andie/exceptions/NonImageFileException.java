package cosc202.andie.exceptions;


/**
 * <p>
 * The class {@code NonImageFileException} extends {@code Exception}, and indicates the case
 * where the program attempts to open a non-image file as if it is an image.
 * </p>
 * 
 * @author Joshua Carter
 * @see Exception
 */
public class NonImageFileException extends Exception {

    /**
     * The default message for instances of this class.
     */
    private static final String DEFAULT_MESSAGE = "The program cannot operate on non-image files";

    /**
     * Default constructor that creates a new instance of {@code NonImageFileException} with
     * {@code null} set as its message and cause. The cause can be set thereafter with {@link #initCause()}.
     */
    public NonImageFileException(){
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructor that creates a new instance of {@code NonImageFileException} with
     * cause set as {@code null} - which can be set thereafter
     * using {@link #initCause()} - and a message detailing the exception.
     *
     * @param message The message explaining the {@code NonImageFileException} instance,
     * which can be retrieved later with the {@link #getMessage()} method.
     */
    public NonImageFileException(String message){
        super(message);
    }

    /**
     * Constructor that creates a new instance of {@code NonImageFileException} with
     * a cause, and the message is set to equal {@code cause.toString()}, or {@code null} if {@code cause == null}.
     *
     * @param cause The cause for the creation of the {@code NonImageFileException}
     * instance, which can be retrieved with the {@link #getCause()} cause.
     */
    public NonImageFileException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    /**
     * Constructor that creates a new instance of {@code NonImageFileException} with
     * a cause and a message.
     *
     * @param cause The cause for the creation of the {@code NonImageFileException}
     * instance, which can be retrieved with the {@link #getCause()} cause.
     * 
     * @param message The message explaining the {@code NonImageFileException} instance,
     * which can be retrieved later with the {@link #getMessage()} method.
     */
    public NonImageFileException(String message, Throwable cause){
        super(message, cause);
    }
   
}
