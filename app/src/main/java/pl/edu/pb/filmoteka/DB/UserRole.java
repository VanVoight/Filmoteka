package pl.edu.pb.filmoteka.DB;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_roles")

public class UserRole {
    @PrimaryKey
    private long userRoleId;
    private long userId;
    private long roleId;
    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }

    // Getter i Setter dla userId
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    // Getter i Setter dla roleId
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}

