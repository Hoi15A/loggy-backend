package ch.zhaw.pm4.loganalyser.exception;

/**
 * This exception will be processed in the {@link ApiExceptionHandler} for the frontend error messages.
 */
public class ArgumentMismatchException extends RuntimeException {

    /**
     * Creates a new argument mismatch exception instance.
     * @param message String
     */
    public ArgumentMismatchException(String message) {
        super(message);
    }

}
