package pl.edu.pb.filmoteka.DB;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "watched_movies",
        primaryKeys = {"userId", "movieDbId"},
        indices = {@Index("userId"), @Index("movieDbId")}
)
public class WatchedMovies {
    public long userId;
    public int movieDbId;
}