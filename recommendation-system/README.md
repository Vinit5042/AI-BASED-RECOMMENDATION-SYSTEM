# Product Recommendation System

A Java-based recommendation system using Apache Mahout for collaborative filtering to suggest products based on user preferences and behavior patterns.

## Features

- **Collaborative Filtering**: Uses Apache Mahout's user-based collaborative filtering
- **Sample Data Generation**: Automatically generates realistic users, products, and ratings
- **Interactive Demo**: Command-line interface to explore the system
- **User Similarity**: Calculate similarity between users
- **Multiple Categories**: Supports Electronics, Books, Clothing, and Home products
- **Fallback Mechanism**: Content-based recommendations when collaborative filtering fails

## System Architecture

### Core Components

1. **Model Classes**
   - `User`: Represents system users with ID, name, and email
   - `Product`: Represents products with ID, name, category, price, and description
   - `Rating`: Represents user ratings for products with timestamps

2. **Data Generation**
   - `DataGenerator`: Creates realistic sample data for testing and demonstration

3. **Recommendation Engine**
   - `RecommendationEngine`: Core engine using Apache Mahout for collaborative filtering
   - Supports user-based recommendations with Pearson correlation similarity
   - Includes fallback content-based recommendations

4. **Demo Application**
   - `RecommendationSystemDemo`: Interactive command-line interface

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Installation and Setup

1. **Clone or download the project files**

2. **Build the project**
   \`\`\`bash
   mvn clean compile
   \`\`\`

3. **Run tests**
   \`\`\`bash
   mvn test
   \`\`\`

4. **Run the application**
   \`\`\`bash
   mvn exec:java -Dexec.mainClass="com.recommendation.RecommendationSystemDemo"
   \`\`\`

   Or build and run the JAR:
   \`\`\`bash
   mvn clean package
   java -jar target/recommendation-system-1.0.0.jar
   \`\`\`

## Usage

### Interactive Demo

The system provides an interactive menu with the following options:

1. **View all users**: Display all generated users
2. **View all products**: Show products organized by category
3. **View user ratings**: See what products a specific user has rated
4. **Get recommendations**: Generate personalized recommendations for a user
5. **Calculate user similarity**: Compare preferences between two users
6. **Show system statistics**: Display system overview and statistics
7. **Exit**: Close the application

### Sample Data

The system automatically generates:
- 20 users with realistic names and emails
- 20 products across 4 categories (Electronics, Books, Clothing, Home)
- 100-300 ratings with realistic patterns based on user preferences

### Recommendation Algorithm

The system uses:
- **Primary**: User-based collaborative filtering with Pearson correlation
- **Fallback**: Content-based recommendations when collaborative filtering fails
- **Similarity Threshold**: 0.1 for neighborhood formation

## Example Output

\`\`\`
=== Recommendations for Alice 01 ===
1. MacBook Pro (Score: 4.85)
   Category: Electronics, Price: $1999.99
   Algorithm: Collaborative Filtering

2. iPhone 15 (Score: 4.72)
   Category: Electronics, Price: $999.99
   Algorithm: Collaborative Filtering

3. AirPods Pro (Score: 4.45)
   Category: Electronics, Price: $249.99
   Algorithm: Collaborative Filtering
\`\`\`

## Technical Details

### Dependencies

- **Apache Mahout 0.13.0**: Machine learning library for collaborative filtering
- **SLF4J**: Logging framework
- **JUnit 4**: Unit testing framework

### Algorithm Details

1. **Data Model**: File-based data model from user-item-rating triplets
2. **Similarity Metric**: Pearson correlation coefficient
3. **Neighborhood**: Threshold-based user neighborhood (threshold: 0.1)
4. **Recommender**: Generic user-based recommender

### Performance Considerations

- Suitable for small to medium datasets (up to 10,000 users)
- For larger datasets, consider item-based collaborative filtering
- Memory usage scales with the number of users and items

## Extending the System

### Adding New Algorithms

\`\`\`java
// Example: Item-based collaborative filtering
ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(model);
ItemBasedRecommender itemRecommender = new GenericItemBasedRecommender(model, itemSimilarity);
\`\`\`

### Custom Similarity Metrics

\`\`\`java
// Example: Cosine similarity
UserSimilarity similarity = new UncenteredCosineSimilarity(model);
\`\`\`

### Integration with Databases

Replace the file-based data model with database connectivity:

\`\`\`java
DataSource dataSource = // your database connection
MySQLJDBCDataModel model = new MySQLJDBCDataModel(
    dataSource, "ratings_table", "user_id", "item_id", "rating", "timestamp");
\`\`\`

## Testing

Run the test suite:
\`\`\`bash
mvn test
\`\`\`

Tests cover:
- Recommendation generation
- User similarity calculation
- Data generation validation
- System initialization

## Troubleshooting

### Common Issues

1. **No recommendations generated**
   - Ensure users have sufficient ratings
   - Check if similarity threshold is too high
   - Verify data format (user_id, item_id, rating)

2. **Memory issues**
   - Reduce dataset size
   - Increase JVM heap size: `-Xmx2g`

3. **Similarity calculation errors**
   - Users need overlapping rated items
   - Check for sufficient data density

## Future Enhancements

- Web-based user interface
- Real-time recommendation updates
- Hybrid recommendation algorithms
- A/B testing framework
- Performance optimization for large datasets
- Integration with external data sources

## License

This project is for educational and demonstration purposes.
