package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FavouriteFilmsDao {
    @Query("SELECT * FROM films WHERE filmDbId = :filmDbId")
    Film getFavouriteMovieById(long filmDbId);
    @Insert
    void insert(FavouriteFilms favouriteFilms);

}
