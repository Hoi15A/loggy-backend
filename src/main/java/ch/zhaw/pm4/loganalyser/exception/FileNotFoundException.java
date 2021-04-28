package ch.zhaw.pm4.loganalyser.exception;

/**
 * This exception will be processed in the {@link ApiExceptionHandler} for the frontend error messages.
 */
public class FileNotFoundException extends RuntimeException {

    /**
     * Creates a new file not found exception instance.
     * @param message String
     */
    public FileNotFoundException(String message) {
        super(message);
    }

}
