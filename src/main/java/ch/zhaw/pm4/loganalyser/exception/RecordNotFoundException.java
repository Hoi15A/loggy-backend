package ch.zhaw.pm4.loganalyser.exception;

/**
 * Is thrown if a record could not be found in the database.
 * This exception will be processed in the {@link ApiExceptionHandler} for the frontend error messages.
 */
public class RecordNotFoundException extends RuntimeException {

    /**
     * Creates a new record not found exception instance.
     * @param message String
     */
    public RecordNotFoundException(String message) {
        super(message);
    }

}
