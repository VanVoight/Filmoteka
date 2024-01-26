package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertReview(Review review);

    @Query("SELECT COUNT(*) FROM watched_movies WHERE userId = :userId AND movieDbId = :movieDbId")
    int checkIfReviewExists(long userId, int movieDbId);

}