package util;

import model.Artwork;
import model.ArtStyle;
import model.Painting;
import model.Sculpture;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ArtGalleryUtils {
    // Private constructor to prevent instantiation
    private ArtGalleryUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    // Static factory method using var
    public static Artwork createArtwork(String type, String title, String artist, int year, ArtStyle style) {
        var artwork = switch (type.toLowerCase()) {
            case "painting" -> new Painting(title, artist, year, style, "Oil");
            case "sculpture" -> new Sculpture(title, artist, year, style, "Bronze");
            default -> throw new IllegalArgumentException("Unknown artwork type: " + type);
        };
        return artwork;
    }

    // Method using Predicate and method reference
    public static List<Artwork> filterArtworks(List<Artwork> artworks, Predicate<Artwork> filter) {
        return artworks.stream()
                     .filter(filter)
                     .collect(Collectors.toList());
    }

    // Method demonstrating static interface method (Java 8+)
    public interface ArtworkFormatter {
        String format(Artwork artwork);
        
        // Static method in interface
        static ArtworkFormatter getDefaultFormatter() {
            return artwork -> String.format("%s by %s (%d) - $%.2f",
                artwork.getTitle(),
                artwork.getArtistName(),
                artwork.getYearCreated(),
                artwork.getPrice());
        }
        
        // Default method in interface
        default String formatWithStyle(Artwork artwork) {
            return format(artwork) + " - " + artwork.getStyle();
        }
    }

    // Sealed interface example (Java 17+)
    public sealed interface ArtworkValidator permits PaintingValidator, SculptureValidator {
        boolean validate(Artwork artwork);
        
        // Private interface method (Java 9+)
        private static void logValidation(String message) {
            System.out.println("[Validation] " + message);
        }
        
        // Public static factory method
        static ArtworkValidator forType(Class<? extends Artwork> type) {
            if (type == Painting.class) {
                return new PaintingValidator();
            } else if (type == Sculpture.class) {
                return new SculptureValidator();
            }
            throw new IllegalArgumentException("No validator for type: " + type.getSimpleName());
        }
    }

    // Record for validation result (Java 16+)
    public record ValidationResult(boolean isValid, String message) {
        public static ValidationResult valid() {
            return new ValidationResult(true, "");
        }
        
        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }
    }

    // Final class implementing sealed interface
    public static final class PaintingValidator implements ArtworkValidator {
        @Override
        public boolean validate(Artwork artwork) {
            if (!(artwork instanceof Painting)) {
                return false;
            }
            Painting painting = (Painting) artwork;
            return !painting.getMedium().isBlank();
        }
    }

    // Final class implementing sealed interface
    public static final class SculptureValidator implements ArtworkValidator {
        @Override
        public boolean validate(Artwork artwork) {
            if (!(artwork instanceof Sculpture)) {
                return false;
            }
            Sculpture sculpture = (Sculpture) artwork;
            return sculpture.getWeightKg() > 0;
        }
    }

    // Method demonstrating switch expression with pattern matching (Java 21+)
    public static String describeArtwork(Artwork artwork) {
        if (artwork == null) {
            return "No artwork provided";
        }
        if (artwork instanceof Painting p) {
            return "Painting: " + p.getTitle() + " in " + p.getMedium();
        } else if (artwork instanceof Sculpture s) {
            if (s.getWeightKg() > 100) {
                return "Large sculpture: " + s.getTitle() + " (needs special handling)";
            } else {
                return "Sculpture: " + s.getTitle() + " made of " + s.getMaterial();
            }
        }
        return "Unknown artwork type";
    }

    // Method demonstrating text blocks (Java 15+)
    public static String generateArtworkReport(Artwork artwork) {
        return """
            ===== ARTWORK REPORT =====
            Title:      %s
            Artist:     %s
            Year:       %d
            Style:      %s
            Price:      $%.2f
            Type:       %s
            %s
            ==========================
            """.formatted(
                artwork.getTitle(),
                artwork.getArtistName(),
                artwork.getYearCreated(),
                artwork.getStyle(),
                artwork.getPrice(),
                artwork.getArtType(),
                getArtworkSpecificDetails(artwork)
            );
    }

    private static String getArtworkSpecificDetails(Artwork artwork) {
        return switch (artwork) {
            case Painting p -> "Medium: %s\nFramed: %s".formatted(p.getMedium(), p.isFramed());
            case Sculpture s -> "Material: %s\nWeight: %.1f kg\nOutdoor: %s"
                .formatted(s.getMaterial(), s.getWeightKg(), s.isOutdoor());
            default -> "";
        };
    }
}
