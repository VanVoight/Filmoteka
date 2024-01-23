package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import kotlinx.coroutines.*;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE userId = :userId")
    User getUserById(long userId);

    @Insert
    void insert(User user);
    @Query("SELECT * FROM users WHERE userName = (:userName) AND password = (:password)")
    User login(String userName,String password);

    @Query("SELECT * FROM users WHERE userName = (:userName) AND password = (:password)")
    User getUserByUsernameAndPassword(String userName, String password);

    @Query("SELECT * FROM users WHERE userName = (:userName)")
    User getUserNamesByName(String userName);

}
