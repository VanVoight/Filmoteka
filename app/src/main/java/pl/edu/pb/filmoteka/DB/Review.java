package pl.edu.pb.filmoteka.DB;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "reviews")

public class Review {
    @PrimaryKey(autoGenerate = true)
    public long reviewId;
    public long userId;
    public int movieId;
    public String content;

    public Review() {
    }

    public Review(long reviewId, long userId, int movieId, String content) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.movieId = movieId;
        this.content = content;
    }

    public long getReviewId() {
        return reviewId;
    }

    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
