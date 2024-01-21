package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FilmDao {
    @Query("SELECT * FROM films WHERE filmDbId = :filmDbId")
    Film getMovieById(long filmDbId);
    @Insert
    void insert(Film film);

}

