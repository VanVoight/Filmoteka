package pl.edu.pb.filmoteka.DB;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "my_list_movies",
        primaryKeys = {"userId", "movieDbId"},
        indices = {@Index("userId"), @Index("movieDbId")}
)
public class MyListMovies {
    public long userId;
    public int movieDbId;
}