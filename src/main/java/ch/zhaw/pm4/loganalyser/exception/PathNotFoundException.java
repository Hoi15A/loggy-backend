package ch.zhaw.pm4.loganalyser.exception;

public class PathNotFoundException extends RuntimeException {
    /**
     * Creates a new path not found exception instance.
     * @param message String
     */
    public PathNotFoundException(String message) {
        super(message);
    }
}
