package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyListMoviesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertMyListMovie(MyListMovies myListMovies);

    @Query("SELECT COUNT(*) FROM my_list_movies WHERE userId = :userId AND movieDbId = :movieDbId")
    int checkIfMyListMovieExists(long userId, int movieDbId);
    @Query("SELECT * FROM my_list_movies WHERE userId = :userId")
    List<MyListMovies> getAllMyListMovies(long userId);
    @Query("DELETE FROM my_list_movies")
    void deleteAllMyListMovies();
}