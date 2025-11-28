package model;

import java.util.function.Predicate;

/**
 * Interface demonstrating default, private, and static interface methods
 */
public interface ArtworkOperations {
    
    // Static interface method (Java 8+)
    static String getArtworkTypeDescription(Artwork artwork) {
        return "Artwork type: " + artwork.getArtType();
    }
    
    // Default method with private helper method (Java 9+)
    default String getArtworkInfo(Artwork artwork) {
        return formatArtworkInfo(artwork) + " | " + getArtworkTypeDescription(artwork);
    }
    
    // Private interface method (Java 9+)
    private String formatArtworkInfo(Artwork artwork) {
        return String.format("%s by %s (%d)", 
            artwork.getTitle(), 
            artwork.getArtistName(), 
            artwork.getYearCreated());
    }
    
    // Default method using Predicate (Java 8+)
    default boolean isValuable(Artwork artwork, double minValue) {
        return createValuePredicate(minValue).test(artwork);
    }
    
    // Private static method (Java 9+)
    private static Predicate<Artwork> createValuePredicate(double minValue) {
        return artwork -> artwork.getPrice() >= minValue;
    }
    
    // New Java 22 feature: Unnamed Patterns and Variables (Preview)
    default void processArtworkRecord(Artwork artwork) {
        if (artwork instanceof Painting p) {
            // Using pattern matching for instanceof (Java 16+)
            System.out.println("Processing painting: " + p.getMedium());
        } else if (artwork instanceof Sculpture s) {
            // Using pattern matching with unnamed variable (Java 22+)
        	if (s instanceof Sculpture) {
        	    System.out.println("Processing sculpture");
        	}
        }
    }
}
