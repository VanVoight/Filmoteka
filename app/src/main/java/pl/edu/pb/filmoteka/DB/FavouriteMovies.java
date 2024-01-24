package pl.edu.pb.filmoteka.DB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "favourite_movies",
        primaryKeys = {"userId", "movieDbId"},
        indices = {@Index("userId"), @Index("movieDbId")}
)
public class FavouriteMovies {
    public long userId;
    public int movieDbId;
}