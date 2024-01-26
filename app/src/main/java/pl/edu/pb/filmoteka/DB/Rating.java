package pl.edu.pb.filmoteka.DB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ratings")

public class Rating {
    @PrimaryKey(autoGenerate = true)
    public long ratingId;
    public long userId;
    public int movieId;
    public float rating;

    public Rating() {
    }

    public Rating(long ratingId, long userId, int movieId, float rating) {
        this.ratingId = ratingId;
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public long getRatingId() {
        return ratingId;
    }

    public void setRatingId(long reviewId) {
        this.rating = reviewId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
