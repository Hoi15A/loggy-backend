package ch.zhaw.pm4.loganalyser.exception;

/**
 * Is thrown if a record already exists in the database
 */
public class RecordAlreadyExsistsException extends RuntimeException {
    /**
     * Creates a new RecordAlreadyExistsException
     */
    public RecordAlreadyExsistsException(String message) {
        super(message);
    }
}
