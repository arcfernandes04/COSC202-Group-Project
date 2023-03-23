package cosc202.andie.exceptions;

/**
 * <p>
 * The class {@code InvalidOperationsFileException} extends {@code Exception}, and
 * occurs when the program attempts to read to an invalid operations file.
 * </p>
 * 
 * @author Joshua Carter
 * @see Exception
 * @see java.io.StreamCorruptedException
 */
public class InvalidOperationsFileException extends Exception {

    /**
     * The default message for instances of this class.
     */
    private static final String DEFAULT_MESSAGE = "The image's operation file has been corrupted, although the original image is still intact.";
    
    /**
     * Default constructor that creates a new instance of
     * {@code InvalidOperationsFileException} with
     * {@code null} set as its message and cause. The cause can be set thereafter
     * with {@link #initCause()}.
     */
    public InvalidOperationsFileException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructor that creates a new instance of
     * {@code InvalidOperationsFileException} with
     * cause set as {@code null} - which can be set thereafter
     * using {@link #initCause()} - and a message detailing the exception.
     *
     * @param message The message explaining the {@code InvalidOperationsFileException}
     *                instance,
     *                which can be retrieved later with the {@link #getMessage()}
     *                method.
     */
    public InvalidOperationsFileException(String message) {
        super(message);
    }

    /**
     * Constructor that creates a new instance of
     * {@code InvalidOperationsFileException} with
     * a cause, and the message is set to equal {@code cause.toString()}, or
     * {@code null} if {@code cause == null}.
     *
     * @param cause The cause for the creation of the
     *              {@code InvalidOperationsFileException}
     *              instance, which can be retrieved with the {@link #getCause()}
     *              cause.
     */
    public InvalidOperationsFileException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    /**
     * Constructor that creates a new instance of
     * {@code InvalidOperationsFileException} with
     * a cause and a message.
     *
     * @param cause   The cause for the creation of the
     *                {@code InvalidOperationsFileException}
     *                instance, which can be retrieved with the {@link #getCause()}
     *                cause.
     * 
     * @param message The message explaining the {@code InvalidOperationsFileException}
     *                instance,
     *                which can be retrieved later with the {@link #getMessage()}
     *                method.
     */
    public InvalidOperationsFileException(String message, Throwable cause) {
        super(message, cause);
    }

}
