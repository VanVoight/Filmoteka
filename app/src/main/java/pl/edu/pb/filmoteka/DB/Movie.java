package pl.edu.pb.filmoteka.DB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey
    public int movieDbId;
    @NonNull
    public int movieApiId;
    public float rate;
}
