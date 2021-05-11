package ch.zhaw.pm4.loganalyser.exception;

/**
 * Is thrown when an invalid column type is used
 */
public class InvalidColumnTypeException extends RuntimeException {
    /**
     * Creates a new InvalidColumnTypeException
     * @param message Error Message
     */
    public InvalidColumnTypeException(String message) {
        super(message);
    }
}
