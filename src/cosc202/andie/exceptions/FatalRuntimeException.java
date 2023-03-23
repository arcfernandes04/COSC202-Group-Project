package cosc202.andie.exceptions;

/**
 * <p>
 * The class {@code FatalRuntimeException} extends {@code Exception}, and
 * occurs when the program encounters an {@code Exception} or {@code Error}
 * from which it cannot recover.
 * </p>
 * 
 * @author Joshua Carter
 * @see Exception
 * @see Error
 */
public class FatalRuntimeException extends Exception {

    /**
     * The default message for instances of this class.
     */
    private static final String DEFAULT_MESSAGE = "A fatal error has occurred. Please try re-opening the program.";

    /**
     * Default constructor that creates a new instance of
     * {@code FatalRuntimeException} with
     * {@code null} set as its message and cause. The cause can be set thereafter
     * with {@link #initCause()}.
     */
    public FatalRuntimeException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructor that creates a new instance of
     * {@code FatalRuntimeException} with
     * cause set as {@code null} - which can be set thereafter
     * using {@link #initCause()} - and a message detailing the exception.
     *
     * @param message The message explaining the {@code FatalRuntimeException}
     *                instance,
     *                which can be retrieved later with the {@link #getMessage()}
     *                method.
     */
    public FatalRuntimeException(String message) {
        super(message);
    }

    /**
     * Constructor that creates a new instance of
     * {@code FatalRuntimeException} with
     * a cause, and the message is set to equal {@code cause.toString()}, or
     * {@code null} if {@code cause == null}.
     *
     * @param cause The cause for the creation of the
     *              {@code FatalRuntimeException}
     *              instance, which can be retrieved with the {@link #getCause()}
     *              cause.
     */
    public FatalRuntimeException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    /**
     * Constructor that creates a new instance of
     * {@code FatalRuntimeException} with
     * a cause and a message.
     *
     * @param cause   The cause for the creation of the
     *                {@code FatalRuntimeException}
     *                instance, which can be retrieved with the {@link #getCause()}
     *                cause.
     * 
     * @param message The message explaining the {@code FatalRuntimeException}
     *                instance,
     *                which can be retrieved later with the {@link #getMessage()}
     *                method.
     */
    public FatalRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
