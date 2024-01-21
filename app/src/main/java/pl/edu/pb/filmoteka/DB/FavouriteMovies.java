package pl.edu.pb.filmoteka.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_movies")
public class FavouriteMovies {
    @PrimaryKey(autoGenerate = true)
    public long userId;
    @NonNull
    public long movieDbId;
    public void setUserId(long UserId) {
        this.userId = userId;
    }
    public void setMovieDbId(long MovieDbId) {this.movieDbId = movieDbId;}
}