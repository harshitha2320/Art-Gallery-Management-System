package artgallery;

import model.*;
import exceptions.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class GalleryApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Artwork> artworks = new ArrayList<>();
    private static List<Exhibition> exhibitions = new ArrayList<>();
    
    public static void main(String[] args) {
        System.out.println("\n🎨 Welcome to Art Gallery Management System\n");
        
        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> addNewArtwork();
                case 2 -> viewAllArtworks();
                case 3 -> findArtworkByTitle();
                case 4 -> filterArtworksByStyle();
                case 5 -> createExhibition();
                case 6 -> viewArtworkDetails();
                case 0 -> {
                    System.out.println("\nThank you for using Art Gallery Management System!");
                    running = false;
                }
                default -> System.out.println("\nInvalid choice. Please try again.");
            }
        }
        scanner.close();
    }
    
    private static void printMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Add New Artwork");
        System.out.println("2. View All Artworks");
        System.out.println("3. Find Artwork by Title");
        System.out.println("4. Filter Artworks by Style");
        System.out.println("5. Create New Exhibition");
        System.out.println("6. View Artwork Details");
        System.out.println("0. Exit");
        System.out.println("====================");
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private static void addNewArtwork() {
        System.out.println("\n===== ADD NEW ARTWORK =====");
        System.out.println("1. Add Painting");
        System.out.println("2. Add Sculpture");
        System.out.print("Select artwork type: ");
        
        int typeChoice = getIntInput("");
        
        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("Enter artist name: ");
        String artist = scanner.nextLine().trim();
        
        int year = getIntInput("Enter year created: ");
        
        // Show available art styles
        System.out.println("\nAvailable Art Styles:");
        for (int i = 0; i < ArtStyle.values().length; i++) {
            System.out.printf("%d. %s%n", i + 1, ArtStyle.values()[i]);
        }
        int styleIndex = getIntInput("Select art style (number): ") - 1;
        ArtStyle style = ArtStyle.values()[styleIndex];
        
        double price = getDoubleInput("Enter price: $");
        
        try {
            if (typeChoice == 1) {
                System.out.print("Enter medium (e.g., Oil, Watercolor): ");
                String medium = scanner.nextLine().trim();
                boolean isFramed = getIntInput("Is it framed? (1 for Yes, 0 for No): ") == 1;
                
                Painting painting = new Painting(title, artist, year, style, medium);
                painting.setPrice(price);
                painting.setFramed(isFramed);
                artworks.add(painting);
                System.out.println("\n✅ Painting added successfully!");
                
            } else if (typeChoice == 2) {
                System.out.print("Enter material (e.g., Marble, Bronze): ");
                String material = scanner.nextLine().trim();
                double weight = getDoubleInput("Enter weight (kg): ");
                boolean isOutdoor = getIntInput("Is it for outdoor display? (1 for Yes, 0 for No): ") == 1;
                
                Sculpture sculpture = new Sculpture(title, artist, year, style, price, material, weight, isOutdoor);
                artworks.add(sculpture);
                System.out.println("\n✅ Sculpture added successfully!");
            } else {
                System.out.println("\n❌ Invalid artwork type!");
            }
        } catch (Exception e) {
            System.out.println("\n❌ Error adding artwork: " + e.getMessage());
        }
    }
    
    private static void viewAllArtworks() {
        if (artworks.isEmpty()) {
            System.out.println("\nNo artworks in the gallery yet!");
            return;
        }
        
        System.out.println("\n===== ALL ARTWORKS =====");
        for (int i = 0; i < artworks.size(); i++) {
            Artwork art = artworks.get(i);
            System.out.printf("%d. %s by %s (%d) - %s - $%,.2f%n",
                i + 1, art.getTitle(), art.getArtistName(), 
                art.getYearCreated(), art.getStyleName(), art.getPrice());
        }
    }
    
    private static void findArtworkByTitle() {
        System.out.print("\nEnter artwork title to search: ");
        String searchTitle = scanner.nextLine().trim().toLowerCase();
        
        List<Artwork> found = artworks.stream()
            .filter(a -> a.getTitle().toLowerCase().contains(searchTitle))
            .collect(Collectors.toList());
            
        if (found.isEmpty()) {
            System.out.println("\nNo artworks found with that title.");
        } else {
            System.out.println("\n===== SEARCH RESULTS =====");
            found.forEach(art -> System.out.println(
                "• " + art.getTitle() + " by " + art.getArtistName() + 
                " (" + art.getYearCreated() + ") - " + art.getStyleName()));
        }
    }
    private static void filterArtworksByStyle() {
        System.out.println("\n===== FILTER BY STYLE =====");
        System.out.println("Available Art Styles:");
        for (int i = 0; i < ArtStyle.values().length; i++) {
            System.out.printf("%d. %s%n", i + 1, ArtStyle.values()[i]);
        }
        System.out.println("Select art styles by numbers separated by spaces (e.g. 1 3 5):");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("No styles selected. Showing all artworks.");
            displayArtworks(artworks);
            return;
        }

        // Parse input numbers into a set of selected styles
        Set<ArtStyle> selectedStyles = new HashSet<>();
        String[] tokens = input.split("\\s+");
        for (String token : tokens) {
            try {
                int index = Integer.parseInt(token) - 1;
                if (index >= 0 && index < ArtStyle.values().length) {
                    selectedStyles.add(ArtStyle.values()[index]);
                } else {
                    System.out.println("Ignoring invalid style number: " + (index + 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("Ignoring invalid input: " + token);
            }
        }

        if (selectedStyles.isEmpty()) {
            System.out.println("No valid styles selected. Showing all artworks.");
            displayArtworks(artworks);
            return;
        }

        // Filter artworks that match ANY of the selected styles
        List<Artwork> filtered = artworks.stream()
            .filter(a -> selectedStyles.contains(a.getStyle()))
            .collect(Collectors.toList());

        // Show results
        System.out.printf("\n===== ARTWORKS WITH STYLES: %s =====%n",
            selectedStyles.stream().map(ArtStyle::toString).collect(Collectors.joining(", ")));

        if (filtered.isEmpty()) {
            System.out.println("No artworks found with selected styles.");
        } else {
            displayArtworks(filtered);
        }
    }

    // Helper method to display artworks nicely
    private static void displayArtworks(List<Artwork> list) {
        list.forEach(art -> System.out.println(
            "• " + art.getTitle() + " by " + art.getArtistName() + 
            " (" + art.getYearCreated() + ") - $" + art.getPrice()));
    }
   private static void createExhibition() {
    System.out.println("\n===== CREATE EXHIBITION =====");
    if (artworks.isEmpty()) {
        System.out.println("No artworks available to create an exhibition!");
        return;
    }

    System.out.print("Enter exhibition name: ");
    String name = scanner.nextLine().trim();

    if (exhibitions.stream().anyMatch(e -> e.name().equalsIgnoreCase(name))) {
        System.out.println("Exhibition name already exists. Please choose a unique name.");
        return;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate startDate, endDate;
    try {
        System.out.print("Enter start date (yyyy-MM-dd): ");
        startDate = LocalDate.parse(scanner.nextLine().trim(), formatter);

        System.out.print("Enter end date (yyyy-MM-dd): ");
        endDate = LocalDate.parse(scanner.nextLine().trim(), formatter);

        if (endDate.isBefore(startDate)) {
            System.out.println("End date cannot be before start date.");
            return;
        }
    } catch (DateTimeParseException e) {
        System.out.println("Invalid date format.");
        return;
    }

    List<Artwork> filteredArtworks = new ArrayList<>(artworks);

    // Optional simple search before selecting artworks
    System.out.print("Enter a keyword to search artworks by title/artist (or press Enter to skip): ");
    String keyword = scanner.nextLine().trim().toLowerCase();
    if (!keyword.isEmpty()) {
        filteredArtworks = filteredArtworks.stream()
            .filter(a -> a.getTitle().toLowerCase().contains(keyword) ||
                         a.getArtistName().toLowerCase().contains(keyword))
            .collect(Collectors.toList());
    }

    if (filteredArtworks.isEmpty()) {
        System.out.println("No artworks found for that search.");
        return;
    }

    Set<Artwork> selectedArtworks = new HashSet<>();

    while (true) {
        System.out.println("\nAvailable Artworks:");
        for (int i = 0; i < filteredArtworks.size(); i++) {
            Artwork art = filteredArtworks.get(i);
            String selectedMark = selectedArtworks.contains(art) ? " [selected]" : "";
            System.out.printf("%d. %s by %s (%d) - %s%s%n",
                i + 1, art.getTitle(), art.getArtistName(), art.getYearCreated(), art.getStyle(), selectedMark);
        }

        System.out.print("\nEnter artwork numbers to toggle selection (comma-separated), 'done' to finish: ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("done")) {
            break;
        }

        String[] tokens = input.split(",");
        for (String token : tokens) {
            try {
                int idx = Integer.parseInt(token.trim()) - 1;
                if (idx >= 0 && idx < filteredArtworks.size()) {
                    Artwork art = filteredArtworks.get(idx);
                    if (selectedArtworks.contains(art)) {
                        selectedArtworks.remove(art);
                        System.out.println("'" + art.getTitle() + "' removed from selection.");
                    } else {
                        selectedArtworks.add(art);
                        System.out.println("'" + art.getTitle() + "' added to selection.");
                    }
                } else {
                    System.out.println("Invalid artwork number: " + (idx + 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + token);
            }
        }
    }

    if (selectedArtworks.isEmpty()) {
        System.out.println("No artworks selected. Exhibition creation cancelled.");
        return;
    }

    try {
        Exhibition exhibition = new Exhibition(name, startDate, endDate, selectedArtworks);
        exhibitions.add(exhibition);

        System.out.println("\n🎉 Exhibition created successfully!");
        System.out.println("Exhibition: " + exhibition.name());
        System.out.println("Duration: " + exhibition.startDate() + " to " + exhibition.endDate());
        System.out.println("Number of artworks: " + exhibition.artworks().size());

    } catch (Exception e) {
        System.out.println("\n❌ Error creating exhibition: " + e.getMessage());
    }
}


    
//    private static void createExhibition() {
//        System.out.println("\n===== CREATE EXHIBITION =====");
//        if (artworks.isEmpty()) {
//            System.out.println("No artworks available to create an exhibition!");
//            return;
//        }
//        
//        System.out.print("Enter exhibition name: ");
//        String name = scanner.nextLine().trim();
//        
//        System.out.println("\nAvailable Artworks:");
//        viewAllArtworks();
//        
//        System.out.print("\nEnter artwork numbers to include (comma-separated): ");
//        String[] indices = scanner.nextLine().split(",");
//        
//        Set<Artwork> selectedArtworks = new HashSet<>();
//        for (String indexStr : indices) {
//            try {
//                int index = Integer.parseInt(indexStr.trim()) - 1;
//                if (index >= 0 && index < artworks.size()) {
//                    selectedArtworks.add(artworks.get(index));
//                }
//            } catch (NumberFormatException e) {
//                // Skip invalid entries
//            }
//        }
//        
//        if (selectedArtworks.isEmpty()) {
//            System.out.println("No valid artworks selected!");
//            return;
//        }
//        
//        try {
//            Exhibition exhibition = new Exhibition(name, LocalDate.now(), 
//                LocalDate.now().plusMonths(1), selectedArtworks);
//                
//            System.out.println("\n🎉 Exhibition created successfully!");
//            System.out.println("Exhibition: " + exhibition.name());
//            System.out.println("Duration: " + exhibition.startDate() + " to " + exhibition.endDate());
//            System.out.println("Number of artworks: " + exhibition.artworks().size());
//            
//        } catch (Exception e) {
//            System.out.println("\n❌ Error creating exhibition: " + e.getMessage());
//        }
//    }
    
    private static void viewArtworkDetails() {
        if (artworks.isEmpty()) {
            System.out.println("\nNo artworks in the gallery yet!");
            return;
        }
        
        viewAllArtworks();
        int index = getIntInput("\nEnter artwork number to view details: ") - 1;
        
        if (index >= 0 && index < artworks.size()) {
            Artwork artwork = artworks.get(index);
            System.out.println("\n===== ARTWORK DETAILS =====");
            System.out.println("Title: " + artwork.getTitle());
            System.out.println("Artist: " + artwork.getArtistName());
            System.out.println("Year: " + artwork.getYearCreated());
            System.out.println("Style: " + artwork.getStyleName());
            System.out.printf("Price: $%,.2f%n", artwork.getPrice());
            
            if (artwork instanceof Painting p) {
                System.out.println("Type: Painting");
                System.out.println("Medium: " + p.getMedium());
                System.out.println("Framed: " + (p.isFramed() ? "Yes" : "No"));
            } else if (artwork instanceof Sculpture s) {
                System.out.println("Type: Sculpture");
                System.out.println("Material: " + s.getMaterial());
                System.out.printf("Weight: %.2f kg%n", s.getWeightKg());
                System.out.println("Outdoor: " + (s.isOutdoor() ? "Yes" : "No"));
            }
            
            System.out.println("\nDescription: " + 
                (artwork instanceof Painting ? 
                    "A beautiful painting in the " + artwork.getStyleName() + " style." :
                    "An impressive sculpture made of " + ((Sculpture)artwork).getMaterial() + "."));
        } else {
            System.out.println("\n❌ Invalid artwork number!");
        }
    }
}