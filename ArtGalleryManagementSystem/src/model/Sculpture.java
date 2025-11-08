package model;

import java.util.Objects;

// Final class as it's part of the sealed hierarchy

// Final class as it's part of the sealed hierarchy
public final class Sculpture extends Artwork {
    private final String material;  // e.g., Marble, Bronze, Wood
    private final double weightKg;
    private final boolean isOutdoor;

    public Sculpture(String title, String artistName, int yearCreated, 
                    ArtStyle style, String material) {
        this(title, artistName, yearCreated, style, 0.0, material, 0.0, false);
    }

    public Sculpture(String title, String artistName, int yearCreated, 
                    ArtStyle style, double price, String material, 
                    double weightKg, boolean isOutdoor) {
        super(title, artistName, yearCreated, style, price);
        this.material = Objects.requireNonNull(material, "Material cannot be null");
        this.weightKg = weightKg;
        this.isOutdoor = isOutdoor;
    }

    public String getMaterial() {
        return material;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public boolean isOutdoor() {
        return isOutdoor;
    }

    @Override
    public String getArtType() {
        return "Sculpture";
    }

    // Method demonstrating method reference usage
    public static double calculateShippingCost(Sculpture s, double pricePerKg) {
        return s.weightKg * pricePerKg;
    }

    // Example of using this. for clarity
    public void updateOutdoorStatus(boolean newStatus) {
        // Using this. to distinguish between parameter and field (though not strictly necessary here)
        if (this.isOutdoor != newStatus) {
            System.out.println("Updating outdoor status to: " + newStatus);
            // In a real application, we would update the status
            // But since isOutdoor is final, we can't change it after construction
            // This demonstrates the need for a new instance or builder pattern
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Sculpture sculpture = (Sculpture) o;
        return Double.compare(sculpture.weightKg, weightKg) == 0 && 
               isOutdoor == sculpture.isOutdoor && 
               material.equals(sculpture.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), material, weightKg, isOutdoor);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" - %s (%.1f kg)%s", 
            material, weightKg, isOutdoor ? " [Outdoor]" : "");
    }
}
