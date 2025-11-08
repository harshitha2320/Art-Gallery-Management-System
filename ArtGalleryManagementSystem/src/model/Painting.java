package model;

import java.util.Objects;

// Final class as it's part of the sealed hierarchy
public final class Painting extends Artwork {
    private final String medium;  // e.g., Oil, Watercolor, Acrylic
    private boolean isFramed;

    public Painting(String title, String artistName, int yearCreated, ArtStyle style, String medium) {
        this(title, artistName, yearCreated, style, 0.0, medium, false);
    }

    // Constructor with all fields, using 'this.' for field access
    public Painting(String title, String artistName, int yearCreated, 
                   ArtStyle style, double price, String medium, boolean isFramed) {
        super(title, artistName, yearCreated, style, price);
        this.medium = Objects.requireNonNull(medium, "Medium cannot be null");
        this.isFramed = isFramed;
    }

    // Getter with defensive copying not needed as String is immutable
    public String getMedium() {
        return medium;
    }
    
    @Override
    public String getArtType() {
        return "Painting";
    }

    public boolean isFramed() {
        return isFramed;
    }

    // Method demonstrating varargs
    public static String createDescription(String... details) {
        return String.join(" | ", details);
    }

    // Example of method overloading
    public void updateArtwork(double newPrice) {
        setPrice(newPrice);
    }

    public void setFramed(boolean framed) {
        this.isFramed = framed;
    }
    
    public void updateArtwork(double newPrice, boolean newFramedStatus) {
        updateArtwork(newPrice);
        setFramed(newFramedStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Painting painting = (Painting) o;
        return isFramed == painting.isFramed && medium.equals(painting.medium);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), medium, isFramed);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" - %s%s", 
            medium, isFramed ? " (Framed)" : "");
    }
}
