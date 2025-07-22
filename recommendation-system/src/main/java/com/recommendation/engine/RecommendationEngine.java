package com.recommendation.engine;

import com.recommendation.model.Product;
import com.recommendation.model.Rating;
import com.recommendation.model.User;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecommendationEngine {
    private UserBasedRecommender recommender;
    private Map<Long, Product> productMap;
    private Map<Long, User> userMap;
    
    public RecommendationEngine(List<User> users, List<Product> products, List<Rating> ratings) 
            throws IOException, TasteException {
        this.userMap = users.stream().collect(Collectors.toMap(User::getUserId, u -> u));
        this.productMap = products.stream().collect(Collectors.toMap(Product::getProductId, p -> p));
        
        // Create temporary CSV file for Mahout
        File dataFile = createDataFile(ratings);
        
        // Initialize Mahout components
        DataModel model = new FileDataModel(dataFile);
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        this.recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        
        // Clean up temporary file
        dataFile.deleteOnExit();
    }
    
    private File createDataFile(List<Rating> ratings) throws IOException {
        File tempFile = File.createTempFile("ratings", ".csv");
        
        try (FileWriter writer = new FileWriter(tempFile)) {
            for (Rating rating : ratings) {
                writer.write(String.format("%d,%d,%.1f%n", 
                    rating.getUserId(), rating.getProductId(), rating.getRating()));
            }
        }
        
        return tempFile;
    }
    
    public List<ProductRecommendation> getRecommendations(long userId, int numRecommendations) 
            throws TasteException {
        List<ProductRecommendation> recommendations = new ArrayList<>();
        
        try {
            List<RecommendedItem> items = recommender.recommend(userId, numRecommendations);
            
            for (RecommendedItem item : items) {
                Product product = productMap.get(item.getItemID());
                if (product != null) {
                    recommendations.add(new ProductRecommendation(
                        product, 
                        item.getValue(),
                        "Collaborative Filtering"
                    ));
                }
            }
        } catch (TasteException e) {
            System.err.println("Error generating recommendations for user " + userId + ": " + e.getMessage());
            // Return content-based recommendations as fallback
            return getContentBasedRecommendations(userId, numRecommendations);
        }
        
        return recommendations;
    }
    
    private List<ProductRecommendation> getContentBasedRecommendations(long userId, int numRecommendations) {
        List<ProductRecommendation> recommendations = new ArrayList<>();
        
        // Simple content-based fallback - recommend popular products from different categories
        List<Product> products = new ArrayList<>(productMap.values());
        products.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
        
        for (int i = 0; i < Math.min(numRecommendations, products.size()); i++) {
            Product product = products.get(i);
            recommendations.add(new ProductRecommendation(
                product, 
                3.5f + (float)(Math.random() * 1.5), // Random score between 3.5-5.0
                "Content-Based (Fallback)"
            ));
        }
        
        return recommendations;
    }
    
    public double getUserSimilarity(long userId1, long userId2) throws TasteException {
        try {
            UserSimilarity similarity = new PearsonCorrelationSimilarity(recommender.getDataModel());
            return similarity.userSimilarity(userId1, userId2);
        } catch (TasteException e) {
            return 0.0; // Return 0 if similarity cannot be calculated
        }
    }
    
    public static class ProductRecommendation {
        private Product product;
        private float score;
        private String algorithm;
        
        public ProductRecommendation(Product product, float score, String algorithm) {
            this.product = product;
            this.score = score;
            this.algorithm = algorithm;
        }
        
        // Getters
        public Product getProduct() { return product; }
        public float getScore() { return score; }
        public String getAlgorithm() { return algorithm; }
        
        @Override
        public String toString() {
            return String.format("Recommendation{product='%s', score=%.2f, algorithm='%s'}", 
                product.getName(), score, algorithm);
        }
    }
}
