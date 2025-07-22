package com.recommendation.data;

import com.recommendation.model.Product;
import com.recommendation.model.Rating;
import com.recommendation.model.User;

import java.util.*;

public class DataGenerator {
    private static final Random random = new Random();
    
    public static List<User> generateUsers(int count) {
        List<User> users = new ArrayList<>();
        String[] names = {"Alice", "Bob", "Charlie", "Diana", "Eve", "Frank", "Grace", "Henry", "Ivy", "Jack",
                         "Kate", "Liam", "Mia", "Noah", "Olivia", "Paul", "Quinn", "Ruby", "Sam", "Tina"};
        
        for (int i = 1; i <= count; i++) {
            String name = names[random.nextInt(names.length)] + " " + (i < 10 ? "0" + i : i);
            String email = name.toLowerCase().replace(" ", ".") + "@email.com";
            users.add(new User(i, name, email));
        }
        
        return users;
    }
    
    public static List<Product> generateProducts() {
        List<Product> products = new ArrayList<>();
        
        // Electronics
        products.add(new Product(1, "iPhone 15", "Electronics", 999.99, "Latest smartphone with advanced features"));
        products.add(new Product(2, "MacBook Pro", "Electronics", 1999.99, "High-performance laptop for professionals"));
        products.add(new Product(3, "AirPods Pro", "Electronics", 249.99, "Wireless earbuds with noise cancellation"));
        products.add(new Product(4, "iPad Air", "Electronics", 599.99, "Versatile tablet for work and entertainment"));
        products.add(new Product(5, "Samsung Galaxy S24", "Electronics", 899.99, "Android flagship smartphone"));
        
        // Books
        products.add(new Product(6, "The Great Gatsby", "Books", 12.99, "Classic American literature"));
        products.add(new Product(7, "To Kill a Mockingbird", "Books", 14.99, "Timeless story of justice and morality"));
        products.add(new Product(8, "1984", "Books", 13.99, "Dystopian novel by George Orwell"));
        products.add(new Product(9, "Pride and Prejudice", "Books", 11.99, "Jane Austen's romantic masterpiece"));
        products.add(new Product(10, "The Catcher in the Rye", "Books", 13.99, "Coming-of-age novel"));
        
        // Clothing
        products.add(new Product(11, "Nike Air Max", "Clothing", 129.99, "Comfortable running shoes"));
        products.add(new Product(12, "Levi's 501 Jeans", "Clothing", 79.99, "Classic denim jeans"));
        products.add(new Product(13, "Adidas Hoodie", "Clothing", 59.99, "Comfortable cotton hoodie"));
        products.add(new Product(14, "Ray-Ban Sunglasses", "Clothing", 149.99, "Stylish sunglasses"));
        products.add(new Product(15, "Patagonia Jacket", "Clothing", 199.99, "Outdoor adventure jacket"));
        
        // Home & Garden
        products.add(new Product(16, "Dyson Vacuum", "Home", 399.99, "Powerful cordless vacuum cleaner"));
        products.add(new Product(17, "KitchenAid Mixer", "Home", 299.99, "Professional stand mixer"));
        products.add(new Product(18, "Instant Pot", "Home", 89.99, "Multi-functional pressure cooker"));
        products.add(new Product(19, "Philips Air Fryer", "Home", 149.99, "Healthy cooking appliance"));
        products.add(new Product(20, "Roomba Robot Vacuum", "Home", 599.99, "Automated cleaning robot"));
        
        return products;
    }
    
    public static List<Rating> generateRatings(List<User> users, List<Product> products) {
        List<Rating> ratings = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        
        // Generate realistic ratings based on user preferences
        for (User user : users) {
            // Each user rates 5-15 products
            int numRatings = 5 + random.nextInt(11);
            Set<Long> ratedProducts = new HashSet<>();
            
            for (int i = 0; i < numRatings; i++) {
                Product product;
                do {
                    product = products.get(random.nextInt(products.size()));
                } while (ratedProducts.contains(product.getProductId()));
                
                ratedProducts.add(product.getProductId());
                
                // Generate rating based on product category preferences
                float rating = generateRealisticRating(user, product);
                long timestamp = currentTime - random.nextInt(365 * 24 * 60 * 60 * 1000); // Within last year
                
                ratings.add(new Rating(user.getUserId(), product.getProductId(), rating, timestamp));
            }
        }
        
        return ratings;
    }
    
    private static float generateRealisticRating(User user, Product product) {
        // Simulate user preferences based on user ID
        long userId = user.getUserId();
        String category = product.getCategory();
        
        float baseRating = 3.0f; // Neutral rating
        
        // Simulate category preferences
        switch (category) {
            case "Electronics":
                if (userId % 3 == 0) baseRating += 1.0f; // Tech enthusiasts
                break;
            case "Books":
                if (userId % 4 == 1) baseRating += 1.0f; // Book lovers
                break;
            case "Clothing":
                if (userId % 5 == 2) baseRating += 1.0f; // Fashion conscious
                break;
            case "Home":
                if (userId % 3 == 2) baseRating += 1.0f; // Home improvement enthusiasts
                break;
        }
        
        // Add some randomness
        baseRating += (random.nextFloat() - 0.5f) * 2.0f;
        
        // Ensure rating is between 1 and 5
        return Math.max(1.0f, Math.min(5.0f, baseRating));
    }
}
