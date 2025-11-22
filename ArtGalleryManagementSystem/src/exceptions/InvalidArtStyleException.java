package exceptions;

// Checked exception with custom behavior
public class InvalidArtStyleException extends Exception {
    private final String invalidStyle;
    
    public InvalidArtStyleException(String message) {
        this(message, null, null);
    }
    
    public InvalidArtStyleException(String message, String invalidStyle) {
        this(message, invalidStyle, null);
    }
    
    public InvalidArtStyleException(String message, String invalidStyle, Throwable cause) {
        super(message, cause);
        this.invalidStyle = invalidStyle;
    }
    
    public String getInvalidStyle() {
        return invalidStyle;
    }
    
    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (invalidStyle != null) {
            message += " Invalid style: " + invalidStyle;
        }
        return message;
    }
}
