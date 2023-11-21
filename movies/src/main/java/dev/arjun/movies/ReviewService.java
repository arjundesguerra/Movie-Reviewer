package dev.arjun.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    public Review createReview(String reviewBody, String imdbId) {
        Review review = reviewRepository.insert(new Review(reviewBody));
        // This line creates a new Review object with the specified reviewBody and inserts it into the ReviewRepository.

        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review))
                .first();
        // This block of code updates a Movie object in the MongoDB database associated with the provided imdbId.
        // It uses the MongoTemplate to apply an update operation, specifically pushing the new review's ID into the "reviewIds" field of the Movie.

        return review;
    }
}
