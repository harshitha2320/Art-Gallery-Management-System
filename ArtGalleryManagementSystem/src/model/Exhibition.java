package model;

import exceptions.ArtworkNotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// Using records (Java 16+ feature)
public record Exhibition(String name, LocalDate startDate, LocalDate endDate, Set<Artwork> artworks) {
    
    // Compact constructor for validation
    public Exhibition {
        Objects.requireNonNull(name, "Exhibition name cannot be null");
        Objects.requireNonNull(startDate, "Start date cannot be null");
        Objects.requireNonNull(endDate, "End date cannot be null");
        
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        
        // Defensive copy of the mutable set
        artworks = new LinkedHashSet<>(artworks);
    }
    
    // Additional constructor with just name (uses current date range)
    public Exhibition(String name) {
        this(name, LocalDate.now(), LocalDate.now().plusMonths(1), new HashSet<>());
    }
    
    // Factory method using varargs
    public static Exhibition createWithArtworks(String name, Artwork... artworks) {
        return new Exhibition(
            name, 
            LocalDate.now(), 
            LocalDate.now().plusMonths(1), 
            new HashSet<>(Arrays.asList(artworks))
        );
    }
    
    // Method using streams and method references
    public List<Artwork> findArtworksByStyle(ArtStyle style) {
        return artworks.stream()
                      .filter(art -> art.getStyle() == style)
                      .collect(Collectors.toList());
    }
    
    // Method using Optional for null safety
    public Optional<Artwork> findArtworkByTitle(String title) {
        return artworks.stream()
                      .filter(art -> art.getTitle().equalsIgnoreCase(title))
                      .findFirst();
    }
    
    // Method demonstrating pattern matching (Java 16+)
    public String getArtworkDescription(Artwork artwork) {
        return switch (artwork) {
            case Painting p -> String.format("Painting: %s (%s) in %s style", 
                p.getTitle(), p.getMedium(), p.getStyleName());
            case Sculpture s -> String.format("Sculpture: %s made of %s (%.1f kg)", 
                s.getTitle(), s.getMaterial(), s.getWeightKg());
            default -> "Artwork: " + artwork.getTitle();
        };
    }
    
    // Method using lambda with Predicate
    public void processArtworks(java.util.function.Predicate<Artwork> filter, 
                               java.util.function.Consumer<Artwork> action) {
        artworks.stream()
               .filter(filter)
               .forEach(action);
    }
    
    // Method demonstrating effectively final concept
    public void updateArtworkPrices(double percentageIncrease) {
        // percentageIncrease is effectively final when used in lambda
        if (percentageIncrease < 0) {
            throw new IllegalArgumentException("Percentage increase cannot be negative");
        }
        
        artworks.forEach(art -> {
            double newPrice = art.getPrice() * (1 + percentageIncrease / 100);
            art.setPrice(newPrice);
        });
    }
    
    // Method demonstrating defensive copying
    public Set<Artwork> getArtworks() {
        return Collections.unmodifiableSet(artworks);
    }
    
    public boolean addArtwork(Artwork artwork) {
        return artworks.add(Objects.requireNonNull(artwork, "Artwork cannot be null"));
    }
    
    public boolean removeArtwork(Artwork artwork) {
        boolean removed = artworks.remove(artwork);
        if (!removed) {
            throw new ArtworkNotFoundException("Artwork not found in exhibition: " + artwork.getTitle());
        }
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("Exhibition: %s (%s to %s) - %d artworks", 
            name, startDate, endDate, artworks.size());
    }
}
