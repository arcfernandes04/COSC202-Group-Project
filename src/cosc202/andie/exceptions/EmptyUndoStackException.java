package cosc202.andie.exceptions;

import java.util.EmptyStackException;

/**
 * <p>
 * The class {@code EmptyUndoStackException} extends {@code Exception}, and
 * occurs when the program attempts {@code pop()} from the stack of currently applied operations, but the stack is empty.
 * </p>
 * 
 * @author Joshua Carter
 * @see Exception
 * @see EmptyStackException
 */
public class EmptyUndoStackException extends Exception {

    /**
     * The default message for instances of this class.
     */
    private static final String DEFAULT_MESSAGE = "There are currently no operations to undo.";

    /**
     * Default constructor that creates a new instance of
     * {@code EmptyUndoStackException} with
     * {@code null} set as its message and cause. The cause can be set thereafter
     * with {@link #initCause()}.
     */
    public EmptyUndoStackException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructor that creates a new instance of
     * {@code EmptyUndoStackException} with
     * cause set as {@code null} - which can be set thereafter
     * using {@link #initCause()} - and a message detailing the exception.
     *
     * @param message The message explaining the {@code EmptyUndoStackException}
     *                instance,
     *                which can be retrieved later with the {@link #getMessage()}
     *                method.
     */
    public EmptyUndoStackException(String message) {
        super(message);
    }

    /**
     * Constructor that creates a new instance of
     * {@code EmptyUndoStackException} with
     * a cause, and the message is set to equal {@code cause.toString()}, or
     * {@code null} if {@code cause == null}.
     *
     * @param cause The cause for the creation of the
     *              {@code EmptyUndoStackException}
     *              instance, which can be retrieved with the {@link #getCause()}
     *              cause.
     */
    public EmptyUndoStackException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    /**
     * Constructor that creates a new instance of
     * {@code EmptyUndoStackException} with
     * a cause and a message.
     *
     * @param cause   The cause for the creation of the
     *                {@code EmptyUndoStackException}
     *                instance, which can be retrieved with the {@link #getCause()}
     *                cause.
     * 
     * @param message The message explaining the {@code EmptyUndoStackException}
     *                instance,
     *                which can be retrieved later with the {@link #getMessage()}
     *                method.
     */
    public EmptyUndoStackException(String message, Throwable cause) {
        super(message, cause);
    }

}
