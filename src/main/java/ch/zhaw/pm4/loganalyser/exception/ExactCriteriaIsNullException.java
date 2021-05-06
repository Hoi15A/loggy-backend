package ch.zhaw.pm4.loganalyser.exception;

/**
 * This exception will be processed in the {@link ApiExceptionHandler} for the frontend error messages.
 */
public class ExactCriteriaIsNullException extends RuntimeException {

    /**
     * Creates a new ExactCriteria is null exception instance.
     * @param message String
     */
    public ExactCriteriaIsNullException(String message) {
        super(message);
    }
}
