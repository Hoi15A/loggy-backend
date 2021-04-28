package ch.zhaw.pm4.loganalyser.exception;

/**
 * Is thrown if a record already exists in the database.
 * This exception will be processed in the {@link ApiExceptionHandler} for the frontend error messages.
 */
public class RecordAlreadyExistsException extends RuntimeException {

    /**
     * Creates a new RecordAlreadyExistsException
     */
    public RecordAlreadyExistsException(String message) {
        super(message);
    }

}
