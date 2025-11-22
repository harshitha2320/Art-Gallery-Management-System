package model;

public enum ArtStyle {
    ABSTRACT,
    IMPRESSIONISM,
    EXPRESSIONISM,
    CUBISM,
    SURREALISM,
    REALISM,
    MINIMALISM,
    POP_ART,
    CONTEMPORARY,
    RENAISSANCE;

    // Method to get display name with spaces
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
    }
}
