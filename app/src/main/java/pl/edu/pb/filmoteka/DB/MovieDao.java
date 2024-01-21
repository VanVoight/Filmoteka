package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies WHERE movieDbId = :movieDbId")
    Movie getMovieById(long movieDbId);
    @Insert
    void insert(Movie movie);

}

