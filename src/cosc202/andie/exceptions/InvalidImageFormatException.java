package cosc202.andie.exceptions;

/**
 * <p>
 * The class {@code InvalidImageFormatException} extends {@code Exception}, and
 * occurs when the program attempts to operate on an image in a format that it does not recognise.
 * This is potentially due to a filter or other {@code ImageAction} malfunctioning.
 * </p>
 * 
 * @author Joshua Carter
 * @see Exception
 * @see java.awt.image.RasterFormatException
 */
public class InvalidImageFormatException extends Exception {

    /**
     * The default message for instances of this class.
     */
    private static final String DEFAULT_MESSAGE = "The current image's format is invalid.";

    /**
     * Default constructor that creates a new instance of
     * {@code InvalidImageFormatException} with
     * {@code null} set as its message and cause. The cause can be set thereafter
     * with {@link #initCause()}.
     */
    public InvalidImageFormatException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructor that creates a new instance of {@code InvalidImageFormatException} with
     * cause set as {@code null} - which can be set thereafter
     * using {@link #initCause()} - and a message detailing the exception.
     *
     * @param message The message explaining the {@code InvalidImageFormatException}
     *                instance,
     *                which can be retrieved later with the {@link #getMessage()}
     *                method.
     */
    public InvalidImageFormatException(String message) {
        super(message);
    }

    /**
     * Constructor that creates a new instance of {@code InvalidImageFormatException} with
     * a cause, and the message is set to equal {@code cause.toString()}, or
     * {@code null} if {@code cause == null}.
     *
     * @param cause The cause for the creation of the {@code InvalidImageFormatException}
     *              instance, which can be retrieved with the {@link #getCause()}
     *              cause.
     */
    public InvalidImageFormatException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    /**
     * Constructor that creates a new instance of {@code InvalidImageFormatException} with
     * a cause and a message.
     *
     * @param cause   The cause for the creation of the
     *                {@code InvalidImageFormatException}
     *                instance, which can be retrieved with the {@link #getCause()}
     *                cause.
     * 
     * @param message The message explaining the {@code InvalidImageFormatException}
     *                instance,
     *                which can be retrieved later with the {@link #getMessage()}
     *                method.
     */
    public InvalidImageFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
