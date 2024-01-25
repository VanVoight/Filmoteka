package pl.edu.pb.filmoteka.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RoleDao {
    @Query("SELECT * FROM roles WHERE roleId = :roleId")
    Role getRoleById(long roleId);

    @Insert
    void insertRole(Role role);
    @Insert
    void insertRoles(List<Role> roles);
    @Query("SELECT * FROM roles")
    List<Role> getAllRoles();
}
