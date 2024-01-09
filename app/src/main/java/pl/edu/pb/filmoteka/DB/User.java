package pl.edu.pb.filmoteka.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users", indices = {@Index(value = "userName", unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    public long userId;
    @NonNull
    public String userName;
    @NonNull
    public String firstName;
    @NonNull
    public String lastName;
    @NonNull
    public String email;
    @NonNull
    public String password;
    @NonNull
    public long userRoleId;

    public long getUserRoleId() {
        return userRoleId;
    }
    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(@androidx.annotation.NonNull String email) {
        if (isValidEmail(email)){
            this.email=email;
        }
        else {
            throw new IllegalArgumentException("Invalid email address");
        }
    }
    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
