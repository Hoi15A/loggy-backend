package ch.zhaw.pm4.loganalyser.exception;

/**
 * Is thrown if a record already exists in the database
 */
public class RecordAlreadyExistsException extends RuntimeException {
    /**
     * Creates a new RecordAlreadyExistsException
     */
    public RecordAlreadyExistsException(String message) {
        super(message);
    }
}
