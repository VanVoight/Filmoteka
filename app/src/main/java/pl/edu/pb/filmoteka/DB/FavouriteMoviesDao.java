package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouriteMoviesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertFavouriteMovie(FavouriteMovies favouriteMovies);

    @Query("SELECT COUNT(*) FROM favourite_movies WHERE userId = :userId AND movieDbId = :movieDbId")
    int checkIfFavouriteMovieExists(long userId, int movieDbId);

    @Query("SELECT * FROM favourite_movies")
    List<FavouriteMovies> getAllFavouriteMovies();
    @Query("DELETE FROM favourite_movies")
    void deleteAllFavouriteMovies();
}