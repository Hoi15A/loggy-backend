package ch.zhaw.pm4.loganalyser.exception;

public class FileReadException extends RuntimeException {
    /**
     * Creates a new input output exception instance.
     * @param message String
     */
    public FileReadException(String message) {
        super(message);
    }
}
