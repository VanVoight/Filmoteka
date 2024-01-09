package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

// UserRoleDao.java
@Dao
public interface UserRoleDao {
    @Query("SELECT * FROM user_roles WHERE userId = :userId")
    UserRole getUserRoleByUserId(long userId);

    @Insert
    void insertUserRole(UserRole userRole);
}

