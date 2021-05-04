package ch.zhaw.pm4.loganalyser.exception;

/**
 * This exception will be processed in the {@link ApiExceptionHandler} for the frontend error messages.
 */
public class FileReadException extends RuntimeException {

    /**
     * Creates a new input output exception instance.
     * @param message String
     */
    public FileReadException(String message) {
        super(message);
    }

}
