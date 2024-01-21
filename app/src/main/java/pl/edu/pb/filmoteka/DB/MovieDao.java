package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface MovieDao {
    @Insert
    void insertMovie(Movie movie);
}
