package cosc202.andie.exceptions;

/**
 * <p>
 * The class {@code NullFileException} extends {@code Exception}, and
 * occurs when the program attempts to operate on a {@code null} file.
 * </p>
 * 
 * @author Joshua Carter
 * @see Exception
 */
public class NullFileException extends Exception {

    /**
     * The default message for instances of this class.
     */
    private static final String DEFAULT_MESSAGE = "No file is currently open";

    /**
     * Default constructor that creates a new instance of
     * {@code NullFileException} with
     * {@code null} set as its message and cause. The cause can be set thereafter
     * with {@link #initCause()}.
     */
    public NullFileException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructor that creates a new instance of {@code NullFileException} with
     * cause set as {@code null} - which can be set thereafter
     * using {@link #initCause()} - and a message detailing the exception.
     *
     * @param message The message explaining the {@code NullFileException}
     * instance,
     * which can be retrieved later with the {@link #getMessage()}
     * method.
     */
    public NullFileException(String message) {
        super(message);
    }

    /**
     * Constructor that creates a new instance of {@code NullFileException} with
     * a cause, and the message is set to equal {@code cause.toString()}, or
     * {@code null} if {@code cause == null}.
     *
     * @param cause The cause for the creation of the {@code NullFileException}
     * instance, which can be retrieved with the {@link #getCause()}
     * cause.
     */
    public NullFileException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    /**
     * Constructor that creates a new instance of {@code NullFileException} with
     * a cause and a message.
     *
     * @param cause   The cause for the creation of the
     * {@code NullFileException}
     * instance, which can be retrieved with the {@link #getCause()}
     * cause.
     * 
     * @param message The message explaining the {@code NullFileException}
     * instance,
     * which can be retrieved later with the {@link #getMessage()}
     * method.
     */
    public NullFileException(String message, Throwable cause) {
        super(message, cause);
    }

}
