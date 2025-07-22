package com.recommendation.model;

public class Rating {
    private long userId;
    private long productId;
    private float rating;
    private long timestamp;
    
    public Rating(long userId, long productId, float rating, long timestamp) {
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.timestamp = timestamp;
    }
    
    // Getters and setters
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    
    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }
    
    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    @Override
    public String toString() {
        return "Rating{" +
                "userId=" + userId +
                ", productId=" + productId +
                ", rating=" + rating +
                ", timestamp=" + timestamp +
                '}';
    }
}
