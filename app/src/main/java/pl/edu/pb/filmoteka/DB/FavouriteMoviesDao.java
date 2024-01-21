package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FavouriteMoviesDao {
    @Query("SELECT * FROM movies WHERE movieDbId = :movieDbId")
    Movie getFavouriteMovieById(long movieDbId);
    @Insert
    void insert(FavouriteMovies favouriteMovies);

}
