package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RatingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertRating(Rating rating);

    @Query("SELECT COUNT(*) FROM ratings WHERE userId = :userId AND movieId = :movieDbId")
    int checkIfRatingExists(long userId, int movieDbId);

    @Query("SELECT rating FROM ratings WHERE userId = :userId AND movieId = :movieId")
    float getRatingForMovie(long userId, int movieId);
    @Update
    void updateRating(Rating rating);
    @Query("UPDATE ratings SET rating = :newRating WHERE userId = :userId AND movieId = :movieDbId")
    void updateRatings(long userId, int movieDbId, float newRating);
    @Query("SELECT AVG(rating) AS averageRating FROM ratings WHERE movieId = :movieId")
    float getAverageRatingForMovie(long movieId);

}