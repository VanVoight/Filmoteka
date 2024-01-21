package pl.edu.pb.filmoteka.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    public long movieDbId;
    @NonNull
    public long movieApiId;
    public double rate;
    public void setMovieDbId(long movieDbId) {
        this.movieDbId = movieDbId;
    }
    public void setMovieApiId(long movieApiId) {this.movieApiId = movieApiId;}

}