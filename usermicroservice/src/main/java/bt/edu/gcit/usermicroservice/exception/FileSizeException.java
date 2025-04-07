package bt.edu.gcit.usermicroservice.exception;

public class FileSizeException extends RuntimeException {

    // Constructor to accept custom error message
    public FileSizeException(String message) {
        super(message);
    }

    // Constructor to accept custom error message and cause
    public FileSizeException(String message, Throwable cause) {
        super(message, cause);
    }
}
