package com.recommendation;

import com.recommendation.data.DataGenerator;
import com.recommendation.engine.RecommendationEngine;
import com.recommendation.model.Product;
import com.recommendation.model.Rating;
import com.recommendation.model.User;
import org.apache.mahout.cf.taste.common.TasteException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class RecommendationEngineTest {
    private RecommendationEngine engine;
    private List<User> users;
    private List<Product> products;
    private List<Rating> ratings;
    
    @Before
    public void setUp() throws IOException, TasteException {
        users = DataGenerator.generateUsers(10);
        products = DataGenerator.generateProducts();
        ratings = DataGenerator.generateRatings(users, products);
        engine = new RecommendationEngine(users, products, ratings);
    }
    
    @Test
    public void testGetRecommendations() throws TasteException {
        List<RecommendationEngine.ProductRecommendation> recommendations = 
            engine.getRecommendations(1L, 5);
        
        assertNotNull("Recommendations should not be null", recommendations);
        assertTrue("Should have at least one recommendation", recommendations.size() > 0);
        assertTrue("Should not exceed requested number", recommendations.size() <= 5);
        
        // Check that all recommendations have valid scores
        for (RecommendationEngine.ProductRecommendation rec : recommendations) {
            assertNotNull("Product should not be null", rec.getProduct());
            assertTrue("Score should be positive", rec.getScore() > 0);
            assertNotNull("Algorithm should not be null", rec.getAlgorithm());
        }
    }
    
    @Test
    public void testUserSimilarity() throws TasteException {
        double similarity = engine.getUserSimilarity(1L, 2L);
        
        assertTrue("Similarity should be between -1 and 1", 
            similarity >= -1.0 && similarity <= 1.0);
    }
    
    @Test
    public void testDataGeneration() {
        assertNotNull("Users should not be null", users);
        assertNotNull("Products should not be null", products);
        assertNotNull("Ratings should not be null", ratings);
        
        assertEquals("Should have 10 users", 10, users.size());
        assertEquals("Should have 20 products", 20, products.size());
        assertTrue("Should have ratings", ratings.size() > 0);
        
        // Verify rating values are within valid range
        for (Rating rating : ratings) {
            assertTrue("Rating should be between 1 and 5", 
                rating.getRating() >= 1.0f && rating.getRating() <= 5.0f);
        }
    }
}
