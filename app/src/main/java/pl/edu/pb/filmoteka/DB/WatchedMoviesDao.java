package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WatchedMoviesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertWatchedMovie(WatchedMovies watchedMovies);

    @Query("SELECT COUNT(*) FROM watched_movies WHERE userId = :userId AND movieDbId = :movieDbId")
    int checkIfWatchedMovieExists(long userId, int movieDbId);

    @Query("SELECT * FROM watched_movies WHERE userId = :userId")
    List<WatchedMovies> getAllWatchedMovies(long userId);
    @Query("DELETE FROM watched_movies")
    void deleteAllWatchedMovies();

    @Query("DELETE FROM watched_movies WHERE userId = :userId AND movieDbId = :movieDbId")
    void deleteWatchedMovies(long userId, int movieDbId);
}