package model;

// import com.artgallery.exceptions.InvalidArtStyleException;

import java.time.Year;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

// Sealed class - only Painting and Sculpture can extend this
public sealed class Artwork implements ArtworkOperations permits Painting, Sculpture {
    // Private fields with proper encapsulation
    private final String title;  // final for immutability
    private final String artistName;
    private final Year yearCreated;  // Using java.time.Year for better date handling
    private final ArtStyle style;
    private double price;
    private final List<String> tags;  // Demonstrating List usage
    private final LocalDate creationDate;  // Using Java 8+ Date/Time API

    // Constructor using 'this' for field initialization
    // 'this' refers to the current instance of the class
    public Artwork(String title, String artistName, int yearCreated, ArtStyle style) {
        this(title, artistName, yearCreated, style, 0.0, LocalDate.now());  // Using this() to call another constructor
    }

    // Constructor with additional parameters
    // 'this.' is used to refer to instance variables
    public Artwork(String title, String artistName, int yearCreated, ArtStyle style, double price, LocalDate creationDate) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.artistName = Objects.requireNonNull(artistName, "Artist name cannot be null");
        this.yearCreated = Year.of(yearCreated);
        this.style = Objects.requireNonNull(style, "Art style cannot be null");
        this.price = price;
        this.creationDate = Objects.requireNonNull(creationDate, "Creation date cannot be null");
        this.tags = new ArrayList<>();
        
        // Validate year
        if (yearCreated < 1000 || yearCreated > Year.now().getValue() + 1) {
            throw new IllegalArgumentException("Invalid year created: " + yearCreated);
        }
    }

    // Method overloading examples
    public Artwork(String title, String artistName, int yearCreated, ArtStyle style, double price) {
        this(title, artistName, yearCreated, style, price, LocalDate.now());
    }
    
    // Another overloaded constructor with varargs for tags
    public Artwork(String title, String artistName, int yearCreated, ArtStyle style, String... tags) {
        this(title, artistName, yearCreated, style, 0.0, LocalDate.now());
        if (tags != null) {
            this.tags.addAll(Arrays.asList(tags));
        }
    }

    // Getters with defensive copying where needed
    // Using final for methods that shouldn't be overridden
    public final String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getYearCreated() {
        return yearCreated.getValue();
    }
    
    // Using Java 8+ Date/Time API
    public long getAgeInYears() {
        return ChronoUnit.YEARS.between(yearCreated, Year.now());
    }

    // Returning an immutable copy of the list for encapsulation
    public List<String> getTags() {
        return List.copyOf(tags);
    }
    
    // Method using varargs to add multiple tags
    public void addTags(String... newTags) {
        if (newTags != null) {
            for (String tag : newTags) {
                if (tag != null && !tag.trim().isEmpty()) {
                    this.tags.add(tag.trim());
                }
            }
        }
    }
    
    // Method using LVTI (Local Variable Type Inference with var)
    public String getFormattedInfo() {
        var info = new StringBuilder();  // Using var with StringBuilder
        info.append("Title: ").append(title)
           .append("\nArtist: ").append(artistName)
           .append("\nYear: ").append(yearCreated)
           .append("\nStyle: ").append(style);
            
        if (!tags.isEmpty()) {
            // Using Stream API with method reference
            var tagsString = tags.stream()
                               .map(String::toLowerCase)
                               .collect(Collectors.joining(", "));
            info.append("\nTags: ").append(tagsString);
        }
        
        return info.toString();
    }
    
    // Demonstrating method overloading (polymorphism)
    public String getFormattedInfo(boolean includePrice) {
        var info = getFormattedInfo();
        if (includePrice) {
            return info + "\nPrice: $" + String.format("%.2f", price);
        }
        return info;
    }

    public double getPrice() {
        return price;
    }

    // Setter with validation
    public final void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    // Method to be overridden by subclasses
    public String getArtType() {
        return "Generic Artwork";
    }

    // Overriding equals and hashCode for proper object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artwork artwork = (Artwork) o;
        return yearCreated == artwork.yearCreated && 
               Double.compare(artwork.price, price) == 0 &&
               title.equals(artwork.title) &&
               artistName.equals(artwork.artistName) &&
               style == artwork.style;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artistName, yearCreated, style, price);
    }

    @Override
    // Using text blocks (Java 15+)
    public String toString() {
        return """
            Artwork {
                title: %s,
                artist: %s,
                year: %s,
                style: %s,
                price: $%.2f,
                type: %s,
                created: %s
            }
            """.formatted(title, artistName, yearCreated, style, price, 
                         getArtType(), creationDate);
    }
    
    // New Java 22 feature: Record Patterns (Preview)
    public void processExhibitionRecord(Exhibition exhibition) {
        if (exhibition instanceof Exhibition(String name, LocalDate start, LocalDate end, var artworks)) {
            System.out.printf("Processing exhibition '%s' with %d artworks%n", 
                           name, artworks.size());
        }
    }
    
    // Factory method demonstrating static method in class
    public static Artwork createWithCurrentDate(String title, String artist, int year, ArtStyle style) {
        // We can't instantiate Artwork directly as it's abstract
        // This method should be overridden by subclasses
        throw new UnsupportedOperationException("Cannot create instance of abstract class Artwork");
    }
    
    // Method demonstrating defensive copying with a mutable object (LocalDate)
    public LocalDate getCreationDate() {
        return LocalDate.from(creationDate);  // Returns a copy to prevent modification
    }
    
    public ArtStyle getStyle() {
        return style;
    }
    
    // Add a method to get the style name
    public String getStyleName() {
        return style != null ? style.name() : "Unknown";
    }
    
    // Method demonstrating effectively final variable in lambda
    public void logArtworkInfo() {
        final String logPrefix = "[ARTWORK]";  // Effectively final variable
        
        // Using lambda with effectively final variable
        Runnable logger = () -> {
            System.out.println(logPrefix + " " + this);  // 'this' refers to the Artwork instance
        };
        
        logger.run();
    }
}

//// Painting
//Painting painting1 = new Painting("Starry Night", "Vincent van Gogh", 1889, ArtStyle.POST_IMPRESSIONISM, 100_000_000, "Oil", true);
//Painting painting2 = new Painting("The Persistence of Memory", "Salvador Dalí", 1931, ArtStyle.SURREALISM, 55_000_000, "Oil", false);
//Painting painting3 = new Painting("Composition VII", "Wassily Kandinsky", 1913, ArtStyle.ABSTRACT, 80_000_000, "Oil", true);

//// Sculptures
//Sculpture sculpture1 = new Sculpture("David", "Michelangelo", 1504, ArtStyle.RENAISSANCE, 200_000_000, "Marble", 6_000, false);
//Sculpture sculpture2 = new Sculpture("The Thinker", "Auguste Rodin", 1904, ArtStyle.REALISM, 15_000_000, "Bronze", 600, true);
//Sculpture sculpture3 = new Sculpture("Balloon Dog", "Jeff Koons", 1994, ArtStyle.CONTEMPORARY, 58_000_000, "Stainless Steel", 1_000, false);

//// Exhibition
//Exhibition exhibition = Exhibition.createWithArtworks("Masterpieces 2025", painting1, sculpture1, painting2);

