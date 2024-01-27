package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Update
    void updateUser(User user);
    @Query("UPDATE users SET firstName = :firstName, lastName = :lastName, email = :email WHERE userId = :userId")
    void updateUserDetails(long userId, String firstName, String lastName, String email);

    @Query("UPDATE users SET profileImage = :newImageBytes WHERE userId = :userId")
    void updateProfileImage(long userId, byte[] newImageBytes);

    @Query("SELECT profileImage FROM users WHERE userId = :userId")
    byte[] getUserProfileImage(long userId);
}
