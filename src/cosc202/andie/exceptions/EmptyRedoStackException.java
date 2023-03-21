package cosc202.andie.exceptions;

import java.util.EmptyStackException;

/**
 * <p>
 * The class {@code EmptyRedoStackException} extends {@code Exception}, and
 * occurs when the program attempts {@code pop()} from the stack of undone operations, when the stack is empty.
 * </p>
 * 
 * @author Joshua Carter
 * @see Exception
 * @see EmptyStackException
 */
public class EmptyRedoStackException extends Exception {

    /**
     * The default message for instances of this class.
     */
    private static final String DEFAULT_MESSAGE = "There are currently no operations to redo.";

    /**
     * Default constructor that creates a new instance of
     * {@code EmptyRedoStackException} with
     * {@code null} set as its message and cause. The cause can be set thereafter
     * with {@link #initCause()}.
     */
    public EmptyRedoStackException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructor that creates a new instance of
     * {@code EmptyRedoStackException} with
     * cause set as {@code null} - which can be set thereafter
     * using {@link #initCause()} - and a message detailing the exception.
     *
     * @param message The message explaining the {@code EmptyRedoStackException}
     *                instance,
     *                which can be retrieved later with the {@link #getMessage()}
     *                method.
     */
    public EmptyRedoStackException(String message) {
        super(message);
    }

    /**
     * Constructor that creates a new instance of
     * {@code EmptyRedoStackException} with
     * a cause, and the message is set to equal {@code cause.toString()}, or
     * {@code null} if {@code cause == null}.
     *
     * @param cause The cause for the creation of the
     *              {@code EmptyRedoStackException}
     *              instance, which can be retrieved with the {@link #getCause()}
     *              cause.
     */
    public EmptyRedoStackException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    /**
     * Constructor that creates a new instance of
     * {@code EmptyRedoStackException} with
     * a cause and a message.
     *
     * @param cause   The cause for the creation of the
     *                {@code EmptyRedoStackException}
     *                instance, which can be retrieved with the {@link #getCause()}
     *                cause.
     * 
     * @param message The message explaining the {@code EmptyRedoStackException}
     *                instance,
     *                which can be retrieved later with the {@link #getMessage()}
     *                method.
     */
    public EmptyRedoStackException(String message, Throwable cause) {
        super(message, cause);
    }

}
