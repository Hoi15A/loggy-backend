package ch.zhaw.pm4.loganalyser.exception;

public class FileNotFoundException extends RuntimeException {
    /**
     * Creates a new file not found exception instance.
     * @param message String
     */
    public FileNotFoundException(String message) {
        super(message);
    }
}
