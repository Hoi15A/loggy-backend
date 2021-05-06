package ch.zhaw.pm4.loganalyser.exception;

/**
 * This exception will be processed in the {@link ApiExceptionHandler} for the frontend error messages.
 */
public class InvalidInputException extends RuntimeException {
    /**
     * Creates a new InvalidInputException
     * @param message Error Message
     */
    public InvalidInputException(String message) {
        super(message);
    }
}
