package exceptions;

// Checked exception (extends Exception)
public class DuplicateArtworkException extends Exception {
    public DuplicateArtworkException(String message) {
        super(message);
    }

    public DuplicateArtworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
