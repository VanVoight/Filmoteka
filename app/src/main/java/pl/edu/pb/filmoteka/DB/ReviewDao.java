package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertReview(Review review);

    @Query("SELECT COUNT(*) FROM reviews WHERE userId = :userId AND movieId = :movieDbId")
    int checkIfReviewExists(long userId, int movieDbId);

    @Query("SELECT reviews.*, users.userName AS authorUserName " +
            "FROM reviews " +
            "INNER JOIN users ON reviews.userId = users.userId " +
            "WHERE reviews.movieId = :movieId")
    List<ReviewWithAuthor> getReviewsWithAuthorByMovieId(int movieId);


    class ReviewWithAuthor {
        public long reviewId;
        public long userId;
        public int movieId;
        public String content;
        public String authorUserName;
    }

}