package pl.edu.pb.filmoteka.DB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "favourite_movies",
        primaryKeys = {"userId", "movieDbId"},
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId"),
                @ForeignKey(entity = Movie.class, parentColumns = "movieDbId", childColumns = "movieDbId")
        },
        indices = {@Index("userId"), @Index("movieDbId")}
)
public class FavouriteMovies {
    public int userId;
    public int movieDbId;
}