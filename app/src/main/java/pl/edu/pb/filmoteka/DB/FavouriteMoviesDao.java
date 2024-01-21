package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface FavouriteMoviesDao {
    @Insert
    void insertFavouriteMovie(FavouriteMovies favouriteMovies);
}