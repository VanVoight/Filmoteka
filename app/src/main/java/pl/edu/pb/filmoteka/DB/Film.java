package pl.edu.pb.filmoteka.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "films")
public class Film {
    @PrimaryKey(autoGenerate = true)
    public long filmDbId;

    @NonNull
    public long filmApiId;

    public double rate;

    public void setFilmDbId(long filmDbId) {
        this.filmDbId = filmDbId;
    }

    public void setFilmApiId(long filmApiId) {
        this.filmApiId = filmApiId;
    }
}