package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE userId = :userId")
    User getUserById(long userId);

    @Insert
    void insertUser(User user);
}