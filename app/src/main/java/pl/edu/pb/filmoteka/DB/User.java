package pl.edu.pb.filmoteka.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import pl.edu.pb.filmoteka.Movie;

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

    @NonNull
    public String getUserFirstName() {
        return firstName;
    }
    public String getUserLastName() {
        return lastName;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }
    public String getEmail(){
        return email;
    }
    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }
    public void setPassword(@NonNull String password) {
        this.password = password;
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
