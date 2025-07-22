package com.recommendation;

import com.recommendation.data.DataGenerator;
import com.recommendation.engine.RecommendationEngine;
import com.recommendation.model.Product;
import com.recommendation.model.Rating;
import com.recommendation.model.User;
import org.apache.mahout.cf.taste.common.TasteException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RecommendationSystemDemo {
    private static RecommendationEngine engine;
    private static List<User> users;
    private static List<Product> products;
    private static List<Rating> ratings;
    private static Map<Long, User> userMap;
    private static Map<Long, Product> productMap;
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Product Recommendation System ===");
            System.out.println("Initializing system with sample data...\n");
            
            // Generate sample data
            users = DataGenerator.generateUsers(20);
            products = DataGenerator.generateProducts();
            ratings = DataGenerator.generateRatings(users, products);
            
            // Create lookup maps
            userMap = users.stream().collect(Collectors.toMap(User::getUserId, u -> u));
            productMap = products.stream().collect(Collectors.toMap(Product::getProductId, p -> p));
            
            // Initialize recommendation engine
            engine = new RecommendationEngine(users, products, ratings);
            
            System.out.println("System initialized successfully!");
            System.out.println("Generated " + users.size() + " users, " + products.size() + " products, " + ratings.size() + " ratings\n");
            
            // Interactive demo
            runInteractiveDemo();
            
        } catch (IOException | TasteException e) {
            System.err.println("Error initializing recommendation system: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void runInteractiveDemo() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== Recommendation System Menu ===");
            System.out.println("1. View all users");
            System.out.println("2. View all products");
            System.out.println("3. View user ratings");
            System.out.println("4. Get recommendations for user");
            System.out.println("5. Calculate user similarity");
            System.out.println("6. Show system statistics");
            System.out.println("7. Exit");
            System.out.print("Choose an option (1-7): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        showAllUsers();
                        break;
                    case 2:
                        showAllProducts();
                        break;
                    case 3:
                        showUserRatings(scanner);
                        break;
                    case 4:
                        getRecommendations(scanner);
                        break;
                    case 5:
                        calculateUserSimilarity(scanner);
                        break;
                    case 6:
                        showSystemStatistics();
                        break;
                    case 7:
                        System.out.println("Thank you for using the Recommendation System!");
                        return;
                    default:
                        System.out.println("Invalid option. Please choose 1-7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private static void showAllUsers() {
        System.out.println("\n=== All Users ===");
        for (User user : users) {
            System.out.printf("ID: %d, Name: %s, Email: %s%n", 
                user.getUserId(), user.getName(), user.getEmail());
        }
    }
    
    private static void showAllProducts() {
        System.out.println("\n=== All Products ===");
        String currentCategory = "";
        
        for (Product product : products) {
            if (!product.getCategory().equals(currentCategory)) {
                currentCategory = product.getCategory();
                System.out.println("\n--- " + currentCategory + " ---");
            }
            System.out.printf("ID: %d, Name: %s, Price: $%.2f%n", 
                product.getProductId(), product.getName(), product.getPrice());
        }
    }
    
    private static void showUserRatings(Scanner scanner) {
        System.out.print("Enter user ID (1-" + users.size() + "): ");
        try {
            long userId = Long.parseLong(scanner.nextLine());
            User user = userMap.get(userId);
            
            if (user == null) {
                System.out.println("User not found!");
                return;
            }
            
            System.out.println("\n=== Ratings for " + user.getName() + " ===");
            List<Rating> userRatings = ratings.stream()
                .filter(r -> r.getUserId() == userId)
                .collect(Collectors.toList());
            
            if (userRatings.isEmpty()) {
                System.out.println("No ratings found for this user.");
                return;
            }
            
            for (Rating rating : userRatings) {
                Product product = productMap.get(rating.getProductId());
                System.out.printf("Product: %s, Rating: %.1f/5.0%n", 
                    product.getName(), rating.getRating());
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID format.");
        }
    }
    
    private static void getRecommendations(Scanner scanner) {
        System.out.print("Enter user ID (1-" + users.size() + "): ");
        try {
            long userId = Long.parseLong(scanner.nextLine());
            User user = userMap.get(userId);
            
            if (user == null) {
                System.out.println("User not found!");
                return;
            }
            
            System.out.print("Number of recommendations (default 5): ");
            String numStr = scanner.nextLine().trim();
            int numRecommendations = numStr.isEmpty() ? 5 : Integer.parseInt(numStr);
            
            System.out.println("\n=== Recommendations for " + user.getName() + " ===");
            
            List<RecommendationEngine.ProductRecommendation> recommendations = 
                engine.getRecommendations(userId, numRecommendations);
            
            if (recommendations.isEmpty()) {
                System.out.println("No recommendations available for this user.");
                return;
            }
            
            for (int i = 0; i < recommendations.size(); i++) {
                RecommendationEngine.ProductRecommendation rec = recommendations.get(i);
                Product product = rec.getProduct();
                System.out.printf("%d. %s (Score: %.2f)%n", 
                    i + 1, product.getName(), rec.getScore());
                System.out.printf("   Category: %s, Price: $%.2f%n", 
                    product.getCategory(), product.getPrice());
                System.out.printf("   Algorithm: %s%n", rec.getAlgorithm());
                System.out.println();
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format.");
        } catch (TasteException e) {
            System.out.println("Error generating recommendations: " + e.getMessage());
        }
    }
    
    private static void calculateUserSimilarity(Scanner scanner) {
        System.out.print("Enter first user ID (1-" + users.size() + "): ");
        try {
            long userId1 = Long.parseLong(scanner.nextLine());
            System.out.print("Enter second user ID (1-" + users.size() + "): ");
            long userId2 = Long.parseLong(scanner.nextLine());
            
            User user1 = userMap.get(userId1);
            User user2 = userMap.get(userId2);
            
            if (user1 == null || user2 == null) {
                System.out.println("One or both users not found!");
                return;
            }
            
            double similarity = engine.getUserSimilarity(userId1, userId2);
            
            System.out.printf("\n=== User Similarity ===\n");
            System.out.printf("User 1: %s\n", user1.getName());
            System.out.printf("User 2: %s\n", user2.getName());
            System.out.printf("Similarity Score: %.3f\n", similarity);
            
            if (similarity > 0.7) {
                System.out.println("These users have very similar preferences!");
            } else if (similarity > 0.3) {
                System.out.println("These users have somewhat similar preferences.");
            } else {
                System.out.println("These users have different preferences.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID format.");
        } catch (TasteException e) {
            System.out.println("Error calculating similarity: " + e.getMessage());
        }
    }
    
    private static void showSystemStatistics() {
        System.out.println("\n=== System Statistics ===");
        System.out.println("Total Users: " + users.size());
        System.out.println("Total Products: " + products.size());
        System.out.println("Total Ratings: " + ratings.size());
        
        // Calculate average rating
        double avgRating = ratings.stream()
            .mapToDouble(Rating::getRating)
            .average()
            .orElse(0.0);
        System.out.printf("Average Rating: %.2f/5.0\n", avgRating);
        
        // Category distribution
        Map<String, Long> categoryCount = products.stream()
            .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));
        
        System.out.println("\nProducts by Category:");
        categoryCount.forEach((category, count) -> 
            System.out.printf("  %s: %d products\n", category, count));
        
        // Rating distribution
        Map<Integer, Long> ratingDistribution = ratings.stream()
            .collect(Collectors.groupingBy(r -> (int) Math.floor(r.getRating()), Collectors.counting()));
        
        System.out.println("\nRating Distribution:");
        for (int i = 1; i <= 5; i++) {
            long count = ratingDistribution.getOrDefault(i, 0L);
            System.out.printf("  %d stars: %d ratings\n", i, count);
        }
    }
}
