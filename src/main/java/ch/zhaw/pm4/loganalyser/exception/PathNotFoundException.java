package ch.zhaw.pm4.loganalyser.exception;

/**
 * This exception will be processed in the {@link ApiExceptionHandler} for the frontend error messages.
 */
public class PathNotFoundException extends RuntimeException {

    /**
     * Creates a new path not found exception instance.
     * @param message String
     */
    public PathNotFoundException(String message) {
        super(message);
    }

}
