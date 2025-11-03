package exceptions;

// Unchecked exception (extends RuntimeException)
public class ArtworkNotFoundException extends RuntimeException {
    public ArtworkNotFoundException(String message) {
        super(message);
    }

    public ArtworkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
